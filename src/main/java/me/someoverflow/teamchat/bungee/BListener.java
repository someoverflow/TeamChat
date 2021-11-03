package me.someoverflow.teamchat.bungee;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * @author SomeOverflow
 */
public class BListener implements Listener {

 @EventHandler
 public void handleChat(ChatEvent event) {
  if (!(event.getSender() instanceof ProxiedPlayer)) return;
  if (event.getMessage().startsWith("#reloadtc")) {
   if (BTeamChat.getInstance().getReloadPermission().equalsIgnoreCase("?") || ((ProxiedPlayer) event.getSender()).hasPermission(BTeamChat.getInstance().getReloadPermission())) {
    ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent("[TeamChat] Reload config"));
    BTeamChat.getInstance().reloadConfig();
    ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent("[TeamChat] Config reloaded"));
    event.setCancelled(true);
   }
   return;
  }
  for (String type :
          BTeamChat.getInstance().getChatTypes()) {
   String[] strings = type.split(";");
   String permission = strings[1];
   if (event.getMessage().startsWith(strings[2])) {
    if (permission.equals("?")) permission = null;
    if (permission == null || ((ProxiedPlayer) event.getSender()).hasPermission(permission))
     handle(event, (ProxiedPlayer) event.getSender(), permission, strings[2], strings[3], strings[0]);
    break;
   }
  }
 }

 private void handle(ChatEvent event, ProxiedPlayer player, String permission, String command, String prefix, String name) {
  String message = event.getMessage().replaceFirst(command + " ", "");
  for (ProxiedPlayer players : BTeamChat.getInstance().getProxy().getPlayers()) {
   if (permission == null || players.hasPermission(permission)) {
    if (players.equals(player)) {
     player.sendMessage(new TextComponent(BTeamChat.getInstance().getMessageSent()
             .replaceAll("%prefix%", prefix)
             .replaceAll("%sender%", player.getDisplayName())
             .replaceAll("%message%", message)
             .replaceAll("%server%", player.getServer().getInfo().getName())));
    } else players.sendMessage(new TextComponent(BTeamChat.getInstance().getMessageReceived()
            .replaceAll("%prefix%", prefix)
            .replaceAll("%sender%", player.getDisplayName())
            .replaceAll("%message%", message)
            .replaceAll("%server%", player.getServer().getInfo().getName())));
   }
  }
  event.setCancelled(true);
 }

}
