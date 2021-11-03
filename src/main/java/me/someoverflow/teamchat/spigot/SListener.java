package me.someoverflow.teamchat.spigot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author SomeOverflow
 */
public class SListener implements Listener {

    @EventHandler
    public void handleChat(AsyncPlayerChatEvent event) {
        if (event.getMessage().startsWith("#reloadtc")) {
            if (event.getPlayer().hasPermission(STeamChat.getInstance().getReloadPermission())) {
                event.getPlayer().sendMessage("[TeamChat] Reload config");
                STeamChat.getInstance().reloadConfig();
                event.getPlayer().sendMessage("[TeamChat] Config reloaded");
                event.setCancelled(true);
            }
            return;
        }
        for (String type :
                STeamChat.getInstance().getChatTypes()) {
            String[] strings = type.split(";");
            String permission = strings[1];
            if (event.getMessage().startsWith(strings[2])) {
                if (permission.equals("?")) permission = null;
                if (permission == null || event.getPlayer().hasPermission(permission))
                    handle(event, event.getPlayer(), permission, strings[2], strings[3], strings[0]);
                break;
            }
        }
    }

    private void handle(AsyncPlayerChatEvent event, Player player, String permission, String command, String prefix, String name) {
        String message = event.getMessage().replaceFirst(command + " ", "");
        // String serverName = STeamChat.getInstance().getServerName();

        String senderMessage = STeamChat.getInstance().getMessageSent()
                .replaceAll("%prefix%", prefix)
                .replaceAll("%sender%", player.getDisplayName())
                .replaceAll("%message%", message);
        String receiverMessage = STeamChat.getInstance().getMessageReceived()
                .replaceAll("%prefix%", prefix)
                .replaceAll("%sender%", player.getDisplayName())
                .replaceAll("%message%", message);

        /* TODO
        // player.getServer().getName() => paper
        if (!serverName.equalsIgnoreCase("!! serverName here !!")) {
            senderMessage = senderMessage.replaceAll("%server%", serverName);
            receiverMessage = receiverMessage.replaceAll("%server%", serverName);
        }
         */

        for (Player players : STeamChat.getInstance().getServer().getOnlinePlayers()) {
            if (permission == null || players.hasPermission(permission)) {
                if (players.equals(player)) {
                    player.sendMessage(senderMessage);
                } else player.sendMessage(receiverMessage);
            }
        }
        event.setCancelled(true);
    }

}
