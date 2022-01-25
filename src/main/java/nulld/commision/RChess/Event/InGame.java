package nulld.commision.RChess.Event;

import nulld.commision.RChess.Command.GameCommand;
import nulld.commision.RChess.Main;
import nulld.commision.RChess.TEAM;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scoreboard.Team;

public class InGame implements Listener {
    FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    @EventHandler
    public void thisTurn(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(player.getName());

        if (Main.booleanStatus.get("playing") != null && Main.booleanStatus.get("playing")) {
            player.sendMessage(Main.whichTeam.get(TEAM.BLACK) + "");
            player.sendMessage(team.getName().equalsIgnoreCase("black") + "");
            player.sendMessage(player.getName().equalsIgnoreCase(config.getString("commander.black")) + "");
            player.sendMessage(Main.whichTeam.get(TEAM.WHITE) + "");
            player.sendMessage(team.getName().equalsIgnoreCase("white") + "");
            player.sendMessage(player.getName().equalsIgnoreCase(config.getString("commander.white")) + "");
            if ((Main.whichTeam.get(TEAM.BLACK) && team.getName().equalsIgnoreCase("black") && player.getName().equalsIgnoreCase(config.getString("commander.black"))) || (Main.whichTeam.get(TEAM.WHITE) && team.getName().equalsIgnoreCase("white") && player.getName().equalsIgnoreCase(config.getString("commander.white")))) {
                if (!(event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_AIR))) {
                    if (Main.whichTeam.get(TEAM.BLACK)) {
                        if (player.getName().equalsIgnoreCase(config.getString("commader.black"))) GameCommand.notice_message("&7흑팀");
                        else if (player.getName().equalsIgnoreCase(config.getString("commader.white"))) event.setCancelled(true);
                    } else if (Main.whichTeam.get(TEAM.WHITE)) {
                        if (player.getName().equalsIgnoreCase(config.getString("commader.black"))) event.setCancelled(true);
                        else if (player.getName().equalsIgnoreCase(config.getString("commader.white"))) GameCommand.notice_message("&f백팀");
                    }
                }
            } else event.setCancelled(true);
        }
    }
}
