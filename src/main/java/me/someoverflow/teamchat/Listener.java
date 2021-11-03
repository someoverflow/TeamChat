package me.someoverflow.teamchat;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.event.EventHandler;

import java.util.logging.Level;

/**
 * @author SomeOverflow
 */
public class Listener implements net.md_5.bungee.api.plugin.Listener {

 @EventHandler
 public void handleChat(ChatEvent event) {
  if (!(event.getSender() instanceof ProxiedPlayer)) return;
  if (event.getMessage().startsWith("#reloadtc")) {
   if (((ProxiedPlayer) event.getSender()).hasPermission("tc.reload")) {
    ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent("[TeamChat] Reload config"));
    TeamChat.getInstance().reloadConfig();
    ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent("[TeamChat] Config reloaded"));
    event.setCancelled(true);
   }
   return;
  }
  for (String type :
          TeamChat.getInstance().getChatTypes()) {
   String[] strings = type.split(";");
   String permission = strings[1];
   if (event.getMessage().startsWith(strings[2])) {
    if (permission.equals("?")) permission = null;
    handle(event, (ProxiedPlayer) event.getSender(), permission, strings[2], strings[3], strings[0]);
    break;
   }
  }
 }

 private void handle(ChatEvent event, ProxiedPlayer player, String permission, String command, String prefix, String name) {
  String message = event.getMessage().replaceFirst(command + " ", "");
  if (permission == null) {
   for (ProxiedPlayer players : TeamChat.getInstance().getProxy().getPlayers()) {
    if (players.equals(player)) {
     player.sendMessage(new TextComponent(TeamChat.getInstance().getMessageSent()
             .replaceAll("%prefix%", prefix)
             .replaceAll("%sender%", player.getDisplayName())
             .replaceAll("%message%", message)
             .replaceAll("%server%", player.getServer().getInfo().getName())));
    } else players.sendMessage(new TextComponent(TeamChat.getInstance().getMessageReceived()
            .replaceAll("%prefix%", prefix)
            .replaceAll("%sender%", player.getDisplayName())
            .replaceAll("%message%", message)
            .replaceAll("%server%", player.getServer().getInfo().getName())));
   }
  } else {
   for (ProxiedPlayer players :
           TeamChat.getInstance().getProxy().getPlayers()) {
    if (players.hasPermission(permission)) {
     if (players.equals(player)) {
      player.sendMessage(new TextComponent(TeamChat.getInstance().getMessageSent()
              .replaceAll("%prefix%", prefix)
              .replaceAll("%sender%", player.getDisplayName())
              .replaceAll("%message%", message)
              .replaceAll("%server%", player.getServer().getInfo().getName())));
     } else players.sendMessage(new TextComponent(TeamChat.getInstance().getMessageReceived()
             .replaceAll("%prefix%", prefix)
             .replaceAll("%sender%", player.getDisplayName())
             .replaceAll("%message%", message)
             .replaceAll("%server%", player.getServer().getInfo().getName())));
    }
   }
  }
  event.setCancelled(true);
 }

}
