package nulld.commision.RChess;

import nulld.commision.RChess.Command.ChessCommand;
import nulld.commision.RChess.Command.GameCommand;
import nulld.commision.RChess.Event.InGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin {
    public static String colorText(String string) { return ChatColor.translateAlternateColorCodes('&', string); }
    public static ConsoleCommandSender console = Bukkit.getConsoleSender();
    public static Map<String, String> playerRole = new HashMap<>();
    public static Map<String, Boolean> booleanStatus = new HashMap<>();
    public static Map<TEAM, Boolean> whichTeam = new HashMap<>();

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("체스판").setExecutor(new ChessCommand());
        getCommand("cgame").setExecutor(new GameCommand());
        getCommand("chess").setExecutor(new ChessCommand());
        getCommand("체스판").setTabCompleter(new ChessCommand());

        getServer().getPluginManager().registerEvents(new ChessCommand(), this);
        getServer().getPluginManager().registerEvents(new InGame(), this);

        config.addDefault("timer", 60);
        config.addDefault("bossbar", true);
        config.addDefault("field.world", "world");
        config.addDefault("field.x1", 0);
        config.addDefault("field.z1", 0);
        config.addDefault("field.x2", 7);
        config.addDefault("field.z2", 7);
        config.addDefault("teleport.world", "world");
        config.addDefault("teleport.x", 0);
        config.addDefault("teleport.y", 0);
        config.addDefault("teleport.z", 0);
        config.addDefault("teleport.yaw", 0);
        config.addDefault("teleport.p", 0);
        config.addDefault("role.rook", true);
        config.addDefault("role.bishop", true);
        config.addDefault("role.knight", true);
        config.addDefault("role.pawn", true);
        config.addDefault("spectator", new ArrayList<>());
        config.options().copyDefaults(true);
        saveConfig();

        makeTeam("white", ChatColor.WHITE, "백팀");
        makeTeam("black", ChatColor.GRAY, "흑팀");
        makeTeam("spectator", ChatColor.YELLOW, "관전자");
    }
    private void makeTeam(String teamName, ChatColor teamColor, String displayName) {
        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName) != null) Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName).unregister();
        Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(teamName);
        Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName).setColor(teamColor);
        Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName).setDisplayName(displayName);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        config.set("commander.white", null);
        config.set("commander.black", null);
    }
}
