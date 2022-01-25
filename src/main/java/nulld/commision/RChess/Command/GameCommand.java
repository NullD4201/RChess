package nulld.commision.RChess.Command;

import nulld.commision.RChess.Main;
import nulld.commision.RChess.TEAM;
import nulld.commision.RChess.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.StringUtil;

import java.util.*;

public class GameCommand implements Listener, TabExecutor {
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard board = manager.getMainScoreboard();
    public Team white = board.getTeam("white");
    public Team black = board.getTeam("black");
    public Team spec = board.getTeam("spectator");

    public static Map<Player, String> setTeam = new HashMap<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){
        if (label.equalsIgnoreCase("cgame")){
            if (args.length == 1) return StringUtil.copyPartialMatches(args[0], Arrays.asList("start", "stop"), new ArrayList<>());
            else return StringUtil.copyPartialMatches(args[0], new ArrayList<>(), new ArrayList<>());
        }
        return null;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        FileConfiguration config = Main.getPlugin(Main.class).getConfig();
        BossBar bossBar = Bukkit.createBossBar("턴", BarColor.GREEN, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);
        bossBar.setVisible(config.getBoolean("bossbar"));
        for (Player player : Bukkit.getOnlinePlayers()) bossBar.addPlayer(player);

        if (label.equalsIgnoreCase("cgame") && args.length > 0){
            if (args[0].equalsIgnoreCase("start")){
                Main.booleanStatus.put("playing", true);
                Main.whichTeam.put(TEAM.WHITE, false);
                Main.whichTeam.put(TEAM.BLACK, false);
                notice_title("&aGAME START", "&a&l체스게임을 시작합니다.");
                notice_message("&7&l [ &f&lRChess &7&l] ");
                int playerCount = Bukkit.getOnlinePlayers().size()-config.getList("spectator").size();
                notice_message("&a 참가자 : &f"+playerCount+"명");
                notice_message("&f 백팀 : " + white.getEntries().size() + "명");
                notice_message("&f 흑팀 : " + black.getEntries().size() + "명");
                notice_message("&a 관전자 : &f"+config.getList("spectator").size()+"명");
                for (int i = 0; i < config.getList("spectator").size(); i++){
                    notice_message("&f - &e"+Bukkit.getPlayer(((List<UUID>) config.getList("spectator")).get(i)).getName());
                }
                List<String> whiteTeam = new ArrayList<>(white.getEntries());
                List<String> blackTeam = new ArrayList<>(black.getEntries());
                List<Player> whitePlayers = null;
                for (String string : whiteTeam) whitePlayers.add(Bukkit.getPlayer(string));
                List<Player> blackPlayers = null;
                for (String string : blackTeam) blackPlayers.add(Bukkit.getPlayer(string));
                Collections.shuffle(whitePlayers);
                Collections.shuffle(blackPlayers);

                if (config.get("commander.white") == null || config.get("commander.black") == null) {
                    new BukkitRunnable(){
                        @Override
                        public void run(){
                            notice_title("&cSTOP","&c커맨더가 모두 지정되지 않았습니다.");
                            ((Player) sender).performCommand("cgame stop");
                        }
                    }.runTaskLater(Main.getPlugin(Main.class), 10);
                    return false;
                }

                new BukkitRunnable(){
                    @Override
                    public void run(){
                        if (Main.booleanStatus.get("playing")){
                            notice_message(" ");
                            notice_message("&a이 플러그인의 저작권은 &7[&c유튜버 &l리스빈&7] &a에게 귀속됩니다.");
                            notice_message("&a자잘한 버그가 있을 수 있으며 개발자에게 문의 및 수정이 가능합니다.");
                        }
                    }
                }.runTaskLater(Main.getPlugin(Main.class), 60);
                new BukkitRunnable(){
                    @Override
                    public void run(){
                        if (Main.booleanStatus.get("playing")){
                            notice_message(" ");
                            notice_message("&a실제 체스와 다른 점이 있더라도 양해 바랍니다.");
                            notice_message("&a마인크래프트 내 구현이 불가능한 경우 및 특수룰은 적용되지 않았습니다.");
                            notice_message("&8&m개발자는 체스룰을 잘 모릅니다.");
                        }
                        Utils.fieldSet();
                        Utils.boardSet();
                    }
                }.runTaskLater(Main.getPlugin(Main.class), 60);
                for (int i = 0; i < config.getList("spectator").size(); i++){
                    spec.addEntry(Bukkit.getPlayer(((List<UUID>) config.getList("spectator")).get(i)).getName());
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (white.getEntries().contains(player.getName())) Utils.playerSetTeam(player, "white");
                    if (black.getEntries().contains(player.getName())) Utils.playerSetTeam(player, "black");
                }
                if (whitePlayers.size() > 1) {
                    setTeam.put(whitePlayers.get(0), "white_king");
                    setTeam.put(whitePlayers.get(1), "white_queen");
                    if (whitePlayers.size() > 2) {
                        setTeam.put(whitePlayers.get(2), "white_bishop_1");
                        if (whitePlayers.size() > 3) {
                            setTeam.put(whitePlayers.get(3), "white_bishop_2");
                            if (whitePlayers.size() > 4) {
                                setTeam.put(whitePlayers.get(4), "white_knight_1");
                                if (whitePlayers.size() > 5) {
                                    setTeam.put(whitePlayers.get(5), "white_knight_2");
                                    if (whitePlayers.size() > 6) {
                                        setTeam.put(whitePlayers.get(6), "white_rook_1");
                                        if (whitePlayers.size() > 7) {
                                            setTeam.put(whitePlayers.get(7), "white_rook_2");
                                            if (whitePlayers.size() > 8) {
                                                setTeam.put(whitePlayers.get(8), "white_pawn_1");
                                                if (whitePlayers.size() > 9) {
                                                    setTeam.put(whitePlayers.get(9), "white_pawn_2");
                                                    if (whitePlayers.size() > 10) {
                                                        setTeam.put(whitePlayers.get(10), "white_pawn_3");
                                                        if (whitePlayers.size() > 11) {
                                                            setTeam.put(whitePlayers.get(11), "white_pawn_4");
                                                            if (whitePlayers.size() > 12) {
                                                                setTeam.put(whitePlayers.get(12), "white_pawn_5");
                                                                if (whitePlayers.size() > 13) {
                                                                    setTeam.put(whitePlayers.get(13), "white_pawn_6");
                                                                    if (whitePlayers.size() > 14) {
                                                                        setTeam.put(whitePlayers.get(14), "white_pawn_7");
                                                                        if (whitePlayers.size() > 15) {
                                                                            setTeam.put(whitePlayers.get(15), "white_pawn_8");
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (blackPlayers.size() > 1) {
                    setTeam.put(blackPlayers.get(0), "black_king");
                    setTeam.put(blackPlayers.get(1), "black_queen");
                    if (blackPlayers.size() > 2) {
                        setTeam.put(blackPlayers.get(2), "black_bishop_1");
                        if (blackPlayers.size() > 3) {
                            setTeam.put(blackPlayers.get(3), "black_bishop_2");
                            if (blackPlayers.size() > 4) {
                                setTeam.put(blackPlayers.get(4), "black_knight_1");
                                if (blackPlayers.size() > 5) {
                                    setTeam.put(blackPlayers.get(5), "black_knight_2");
                                    if (blackPlayers.size() > 6) {
                                        setTeam.put(blackPlayers.get(6), "black_rook_1");
                                        if (blackPlayers.size() > 7) {
                                            setTeam.put(blackPlayers.get(7), "black_rook_2");
                                            if (blackPlayers.size() > 8) {
                                                setTeam.put(blackPlayers.get(8), "black_pawn_1");
                                                if (blackPlayers.size() > 9) {
                                                    setTeam.put(blackPlayers.get(9), "black_pawn_2");
                                                    if (blackPlayers.size() > 10) {
                                                        setTeam.put(blackPlayers.get(10), "black_pawn_3");
                                                        if (blackPlayers.size() > 11) {
                                                            setTeam.put(blackPlayers.get(11), "black_pawn_4");
                                                            if (blackPlayers.size() > 12) {
                                                                setTeam.put(blackPlayers.get(12), "black_pawn_5");
                                                                if (blackPlayers.size() > 13) {
                                                                    setTeam.put(blackPlayers.get(13), "black_pawn_6");
                                                                    if (blackPlayers.size() > 14) {
                                                                        setTeam.put(blackPlayers.get(14), "black_pawn_7");
                                                                        if (blackPlayers.size() > 15) {
                                                                            setTeam.put(blackPlayers.get(15), "black_pawn_8");
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                for (Player player : new ArrayList<>(setTeam.keySet())) {
                    for (String string : new ArrayList<>(Utils.playField.keySet())) {
                        if (setTeam.get(player).equals(string)) {
                            player.teleport(Utils.playField.get(string));
                        }
                    }
                }

                // 게임시작 (타이머반복)
                int tickTime = config.getInt("timer")*20;
                final int[] restTime = {0};

                new BukkitRunnable(){
                    @Override
                    public void run(){
                        notice_title("&aSTART", "&0흑팀 &b턴");
                    }
                }.runTaskLaterAsynchronously(Main.getPlugin(Main.class), 140);

                new BukkitRunnable(){
                    @Override
                    public void run(){
                        if (Main.booleanStatus.get("playing")){
                            if (!Main.whichTeam.get(TEAM.WHITE) && !Main.whichTeam.get(TEAM.BLACK)) {
                                Main.whichTeam.put(TEAM.BLACK, true);
                                bossBar.setTitle("흑팀 턴");
                            }

                            if (Main.whichTeam.get(TEAM.WHITE)){
                                if (white.getEntries().size() == 0) {
                                    notice_message("&c백팀에 사람이 없습니다.");
                                    notice_message("&a흑팀이 승리하였습니다.");
                                    Main.booleanStatus.put("playing", false);
                                    for (Player to : Bukkit.getOnlinePlayers()) {
                                        if (white.getEntries().contains(to.getName())) to.sendTitle(Main.colorText("&cLOSE"), Main.colorText("&7흑팀&f이 승리하였습니다."), 10, 20, 10);
                                        else if (black.getEntries().contains(to.getName())) to.sendTitle(Main.colorText("&aWIN"), Main.colorText("&7흑팀&f이 승리하였습니다."), 10, 20, 10);
                                    }
                                    notice_message("&c게임이 중지되었습니다. &b모든 데이터를 초기화합니다.");
                                    bossBar.removeAll();
                                    this.cancel();
                                }
                                else if (ChessCommand.isSkip) {
                                    restTime[0] = tickTime;
                                    ChessCommand.isSkip = false;
                                }
                                task(restTime[0], tickTime, "0흑팀", TEAM.WHITE, TEAM.BLACK, bossBar);
                            } else if (Main.whichTeam.get(TEAM.BLACK)){
                                if (black.getEntries().size() == 0) {
                                    notice_message("&c흑팀에 사람이 없습니다.");
                                    notice_message("&a백팀이 승리하였습니다.");
                                    Main.booleanStatus.put("playing", false);
                                    for (Player to : Bukkit.getOnlinePlayers()) {
                                        if (white.getEntries().contains(to.getName())) to.sendTitle(Main.colorText("&aWIN"), Main.colorText("&f백팀이 승리하였습니다."), 10, 20, 10);
                                        else if (black.getEntries().contains(to.getName())) to.sendTitle(Main.colorText("&cLOSE"), Main.colorText("&f백팀이 승리하였습니다."), 10, 20, 10);
                                    }
                                    notice_message("&c게임이 중지되었습니다. &b모든 데이터를 초기화합니다.");
                                    bossBar.removeAll();
                                    this.cancel();
                                }
                                else if (ChessCommand.isSkip) {
                                    restTime[0] = tickTime;
                                    ChessCommand.isSkip = false;
                                }
                                task(restTime[0], tickTime, "f백팀", TEAM.BLACK, TEAM.WHITE, bossBar);
                            }
                        } else {
                            bossBar.removeAll();
                            this.cancel();
                        }
                    }
                }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 140, 1);
            } else if (args[0].equalsIgnoreCase("stop")){
                if (Main.booleanStatus.get("playing") != null && Main.booleanStatus.get("playing")) {
                    Main.booleanStatus.put("playing", false);
                    bossBar.removeAll();
                    config.set("commander.white", null);
                    config.set("commander.black", null);
                    Main.getPlugin(Main.class).saveConfig();
                    notice_message("&c게임이 중지되었습니다. &b모든 데이터를 초기화합니다.");
                } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c게임이 진행중이 아닙니다."));
            }
        }
        return false;
    }

    public static void notice_message(String string) {
        for (Player player : Bukkit.getOnlinePlayers()) player.sendMessage(Main.colorText(string));
    }
    public static void notice_title(String title, String sub) {
        for (Player player : Bukkit.getOnlinePlayers()) player.sendTitle(Main.colorText(title), Main.colorText(sub), 10, 20, 10);
    }

    public static void task(int r, int t, String wt, TEAM ft, TEAM tt, BossBar bossBar) {
        if (r >= t) {
            notice_title("", "&"+wt+" &b턴");
            Main.whichTeam.put(ft, false);
            Main.whichTeam.put(tt, true);
            bossBar.setTitle(wt+" 턴");
            r = 0;
            bossBar.setProgress(1.0);
        } else {
            r += 1;
            double percentage = 1.0-r*1.0/t;
            bossBar.setProgress(percentage);
            if (percentage <= 1.0) bossBar.setColor(BarColor.GREEN);
            if (percentage <= 2.0/3) bossBar.setColor(BarColor.YELLOW);
            if (percentage <= 1.0/3) bossBar.setColor(BarColor.RED);
        }
    }
}
