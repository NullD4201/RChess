package nulld.commision.RChess.Command;

import nulld.commision.RChess.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.util.*;

public class ChessCommand implements Listener, TabExecutor {
    public static Map<String, Boolean> setField = new HashMap<>();
    public static boolean isSkip = false;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){
        if (label.equalsIgnoreCase("chess")){
            if (args.length == 1) return StringUtil.copyPartialMatches(args[0], Arrays.asList("timer", "timerbar", "skip", "spec", "commander", "reset", "set", "role", "tpset", "info", "start", "stop"), new ArrayList<>());
            else if (args.length > 1){
                if (args.length == 2){
                    if (args[0].equalsIgnoreCase("timerbar")) return StringUtil.copyPartialMatches(args[1], Arrays.asList("on", "off"), new ArrayList<>());
                    else if (args[0].equalsIgnoreCase("commander")) return StringUtil.copyPartialMatches(args[1], Arrays.asList("black", "white"), new ArrayList<>());
                    else if (args[0].equalsIgnoreCase("role")) return StringUtil.copyPartialMatches(args[1], Arrays.asList("rook", "bishop", "knight", "pawn"), new ArrayList<>());
                    else if (args[0].equalsIgnoreCase("reset")) return StringUtil.copyPartialMatches(args[1], Collections.singletonList("all"), new ArrayList<>());
                    else if (args[0].equalsIgnoreCase("set")) return StringUtil.copyPartialMatches(args[1], Arrays.asList("field", "board"), new ArrayList<>());
                    else if (args[0].equalsIgnoreCase("spec")) return StringUtil.copyPartialMatches(args[1], Arrays.asList("add", "del"), new ArrayList<>());
                } else if (args[0].equalsIgnoreCase("commander") || args[0].equalsIgnoreCase("spec")) return null;
                else if (args[0].equalsIgnoreCase("role") && args.length == 3) return StringUtil.copyPartialMatches(args[2], Arrays.asList("true", "false"), new ArrayList<>());
                return StringUtil.copyPartialMatches(args[args.length-1], new ArrayList<>(), new ArrayList<>());
            }
        } else if (label.equalsIgnoreCase("체스판")) return StringUtil.copyPartialMatches(args[args.length-1], new ArrayList<>(), new ArrayList<>());
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player player = (Player) sender;
        FileConfiguration config = Main.getPlugin(Main.class).getConfig();
        if (label.equalsIgnoreCase("chess")){
            if (player.isOp()){
                if (args.length == 0){
                    player.sendMessage(" ");
                    player.sendMessage(Main.colorText("&d /chess timer <second> \n &e<second>&b초로 타이머를 설정하여 해당 시간 뒤에 자동으로 턴이 넘어갑니다.\n &a0인 경우 무제한으로 설정"));
                    player.sendMessage(Main.colorText("&d /chess timerbar <on/off> &b타이머를 보스바로 보일지 말지를 선택합니다."));
                    player.sendMessage(Main.colorText("&d /chess tpset &b명령자가 텔레포트할 장소를 설정합니다."));
                    player.sendMessage(Main.colorText("&d /chess skip &b강제로 턴을 넘깁니다. &c버그 발생시만 사용 권장"));
                    player.sendMessage(Main.colorText("&d /chess spec <add/del> <name> &b관전할 사람을 설정합니다."));
                    player.sendMessage(Main.colorText("&d /chess commander <white/black> <name> &b관전할 사람을 설정합니다."));
                    player.sendMessage(Main.colorText("&d /chess reset [all] &b팀 및 역할설정을 초기화합니다. \n &c/chess reset all&e을 입력하면 맵 정보를 비롯한 모든 정보를 초기화합니다."));
                    player.sendMessage(Main.colorText("&d /chess set field [x1] [z1] [x2] [z2] &b게임용 전체필드를 설정합니다. \n &c/chess info&b로 확인하며, 원하는 위치와 다를 경우만 입력하면 됩니다."));
                    player.sendMessage(Main.colorText("&d /chess set board [x1] [z1] [x2] [z2] &b명령용 필드를 설정합니다. \n &c/chess info&b로 확인하며, 원하는 위치와 다를 경우만 입력하면 됩니다."));
                    player.sendMessage(Main.colorText("&d /chess role <rook/bishop/knight/pawn> <true/false> \n &b활성화할 역할을 결정합니다. &atrue&b일시 활성화"));
                    player.sendMessage(Main.colorText("&d /chess fa <white/black> [x] [y] [z] \n &b싸움을 위한 장소를 지정합니다."));
                    player.sendMessage(Main.colorText("&d /chess stop \n &b진행 중인 게임을 종료하고 모든 데이터를 초기화합니다."));
                } else if (args.length == 1){
                    if (args[0].equalsIgnoreCase("skip")){
                        GameCommand.notice_message("&a턴을 강제로 스킵합니다.");
                        isSkip = true;
                    } else if (args[0].equalsIgnoreCase("reset")){
                        Main.playerRole.clear();
                        player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&a&l역할 초기화 완료"), 10, 20, 10);

                        config.set("commander", null);
                        Main.getPlugin(Main.class).saveConfig();
                    } else if (args[0].equalsIgnoreCase("tpset")){
                        config.set("teleport.world", player.getWorld().getName());
                        config.set("teleport.x", player.getLocation().getBlockX());
                        config.set("teleport.y", player.getLocation().getBlockY());
                        config.set("teleport.z", player.getLocation().getBlockZ());
                        config.set("teleport.p", player.getLocation().getPitch());
                        config.set("teleport.yaw", player.getLocation().getYaw());
                        Main.getPlugin(Main.class).saveConfig();
                        player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&a명령자 tp지점 설정완료"), 10, 20, 10);
                    } else if (args[0].equalsIgnoreCase("info")){
                        String fieldInfo1 = config.getString("field.world")+"@"+config.getInt("field.x1")+","+config.getInt("field.z1");
                        String fieldInfo2 = config.getString("field.world")+"@"+config.getInt("field.x2")+","+config.getInt("field.z2");
                        String chessPlate = config.getString("teleport.world")+"@"+config.getInt("teleport.x")+","+config.getInt("teleport.y")+","+config.getInt("teleport.z");
                        String boardPlate1 = config.getString("board.world")+"@"+config.getInt("board.x1")+","+config.getInt("board.z1");
                        String boardPlate2 = config.getString("board.world")+"@"+config.getInt("board.x2")+","+config.getInt("board.z2");
                        player.sendMessage(" ");
                        player.sendMessage(Main.colorText("&a 필드: &f"+fieldInfo1+"~"+fieldInfo2));
                        player.sendMessage(Main.colorText("&a 명령자 텔레포트 위치: &f"+chessPlate));
                        player.sendMessage(Main.colorText("&a 명령판 위치: &f"+boardPlate1+"~"+boardPlate2));
                        player.sendMessage(Main.colorText("&a 보스바 표시: &f"+config.getBoolean("bossbar")));
                        player.sendMessage(Main.colorText("&a 턴 타이머: &f"+config.getInt("timer")+"초"));
                        player.sendMessage(Main.colorText("&a 역할: "));
                        player.sendMessage(Main.colorText("&a   룩: &f"+config.getBoolean("role.rook")));
                        player.sendMessage(Main.colorText("&a   비숍: &f"+config.getBoolean("role.bishop")));
                        player.sendMessage(Main.colorText("&a   나이트: &f"+config.getBoolean("role.knight")));
                        player.sendMessage(Main.colorText("&a   폰: &f"+config.getBoolean("role.pawn")));
                        player.sendMessage(" ");
                    }
                } else if (args.length == 2){
                    if (args[0].equalsIgnoreCase("timerbar")) {
                        if (args[1].equalsIgnoreCase("on")) config.set("bossbar", true);
                        else if (args[1].equalsIgnoreCase("off")) config.set("bossbar", false);
                        else player.performCommand("chess");
                        Main.getPlugin(Main.class).saveConfig();
                        player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&a보스바 &e"+args[1]), 10, 30, 10);
                    } else if (args[0].equalsIgnoreCase("reset") && args[1].equalsIgnoreCase("all")){
                        new File(Main.getPlugin(Main.class).getDataFolder(), "config.yml").delete();
                        Main.getPlugin(Main.class).saveDefaultConfig();
                        player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&cconfig 초기화 완료"), 10, 30, 10);
                    } else if (args[0].equalsIgnoreCase("timer")){
                        if (args[1].chars().allMatch(Character::isDigit)) config.set("timer", Integer.parseInt(args[1]));
                        Main.getPlugin(Main.class).saveConfig();
                        player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&a턴 타이머 &e"+args[1]+"&a초로 설정"), 10, 30, 10);
                    } else if (args[0].equalsIgnoreCase("set")){
                        if (args[1].equalsIgnoreCase("field")) {
                            setField.put(player.getUniqueId() + "_field", true);
                            setField.put(player.getUniqueId() + "_board", false);
                        }
                        if (args[1].equalsIgnoreCase("board")) {
                            setField.put(player.getUniqueId() + "_field", false);
                            setField.put(player.getUniqueId() + "_board", true);
                        }
                        if (player.getInventory().contains(Material.NETHERITE_AXE)){
                            ItemStack mainItem = player.getInventory().getItemInMainHand();
                            for (int i = 0; i < player.getInventory().getContents().length; i++) {
                                if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType().equals(Material.NETHERITE_AXE)){
                                    ItemStack itemStack = player.getInventory().getItem(i);
                                    player.getInventory().setItem(i, mainItem);
                                    player.getInventory().setItemInMainHand(itemStack);
                                    break;
                                }
                            }
                        } else player.getInventory().addItem(new ItemStack(Material.NETHERITE_AXE, 1));
                        player.sendMessage(Main.colorText("&7[ &fRChess &7] &d좌클릭: pos1지점 선택, 우클릭: pos2지점 선택"));
                    }
                } else if (args.length == 3){
                    if (args[0].equalsIgnoreCase("spec")) {
                        List<UUID> specs = (List<UUID>) config.getList("spectator");
                        if (args[1].equalsIgnoreCase("add")){
                            if (specs.contains(Bukkit.getPlayer(args[2]).getUniqueId())) {
                                player.sendTitle(Main.colorText("&cERROR"), Main.colorText("&c이미 관전상태입니다."), 10, 20, 10);
                                return false;
                            }
                            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[2]))) {
                                specs.add(Bukkit.getPlayer(args[2]).getUniqueId());
                                player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&a관전자 &e"+args[2]+" &a추가완료"), 10, 20, 10);
                            }
                            else player.sendTitle(Main.colorText("&cERROR"), Main.colorText("&c서버에 접속한 사람이 아닙니다."), 10, 20, 10);
                        } else if (args[1].equalsIgnoreCase("del")){
                            if (!specs.contains(Bukkit.getPlayer(args[2]).getUniqueId()) && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[2])))player.sendTitle(Main.colorText("&cERROR"), Main.colorText("&c관전자가 아닙니다."), 10, 20, 10);
                            else if (specs.contains(Bukkit.getPlayer(args[2]).getUniqueId())) {
                                specs.remove(Bukkit.getPlayer(args[2]).getUniqueId());
                                player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&a관전자 &e"+args[2]+" &a삭제완료"), 10, 20, 10);
                            }
                            else if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[2]))) player.sendTitle(Main.colorText("&cERROR"), Main.colorText("&c서버에 접속한 사람이 아닙니다."), 10, 20, 10);
                        }
                        config.set("spectator", specs);
                        Main.getPlugin(Main.class).saveConfig();
                    } else if (args[0].equalsIgnoreCase("commander")){
                        if (Arrays.asList("black", "white").contains(args[1]) && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[2]))){
                            Main.playerRole.put(Bukkit.getPlayer(args[2]).getUniqueId().toString(), args[1] + ".commander");
                            if (Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(args[2]) != null && Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(args[2]).getName().equalsIgnoreCase(args[1])) {
                                player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&e"+args[2]+"&a를 &f"+args[1]+"&a팀의 커맨더로 지정합니다."), 10, 30, 10);
                                if (config.getString("commander."+args[1]) != null && config.getString("commander."+args[1]).equals(args[2])) config.set("commander."+args[1], null);
                                config.set("commander."+args[1], args[2]);
                                Main.getPlugin(Main.class).saveConfig();
                            } else {
                                player.sendTitle(Main.colorText("&cFAIL"), Main.colorText("&e"+args[2]+"&a는 &f"+args[1]+"&a팀이 아닙니다."), 10, 30, 10);
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("role")){
                        if (Arrays.asList("rook", "bishop", "knight", "pawn").contains(args[1]) && Arrays.asList("true", "false").contains(args[2])) config.set("role."+args[1], Boolean.valueOf(args[2]));
                        else player.performCommand("chess");
                        Main.getPlugin(Main.class).saveConfig();
                        player.sendMessage(Main.colorText("&7[ &fRChess &7] &e"+args[1]+"&f역할을 &b"+args[2]+"&f 상태로 설정했습니다."));
                    }
                } else if (args.length == 6){
                    if (args[0].equalsIgnoreCase("set") && (args[1].equalsIgnoreCase("field") || args[1].equalsIgnoreCase("board"))){
                        if (isDig(args[2]) && isDig(args[3]) && isDig(args[4]) && isDig(args[5])){
                            if ((Math.abs(Integer.parseInt(args[2])-Integer.parseInt(args[4]))+1)%8==0 && (Math.abs(Integer.parseInt(args[3])-Integer.parseInt(args[5]))+1)%8==0 ){
                                config.set(args[1] + ".world", player.getWorld().getName());
                                config.set(args[1] + ".x1", Integer.parseInt(args[2]));
                                config.set(args[1] + ".z1", Integer.parseInt(args[3]));
                                config.set(args[1] + ".x2", Integer.parseInt(args[4]));
                                config.set(args[1] + ".z2", Integer.parseInt(args[5]));
                                Main.getPlugin(Main.class).saveConfig();
                                player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&a필드 설정 완료"), 10, 30, 10);
                            } else {
                                player.sendMessage(Main.colorText("&7[ &fRChess &7] &c각 변의 블럭 개수가 8의 배수가 아닙니다."));
                                return false;
                            }
                        } else player.performCommand("chess");
                    }
                }
            } else {
                player.playSound(player.getEyeLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                player.sendTitle(Main.colorText("&c"), Main.colorText("&c&l오피만 사용가능합니다."), 10, 20, 10);
                return false;
            }
        } else if (label.equalsIgnoreCase("체스판")){
            if (role(player) != null){
                if (role(player).contains("commander")) {
                    Location destination = new Location(Bukkit.getWorld(config.getString("teleport.world")), config.getInt("teleport.x"), config.getInt("teleport.y")+1, config.getInt("teleport.z"), (float) config.get("teleport.yaw"), (float) config.get("teleport.p"));
                    player.teleport(destination);
                }
            } else {
                player.sendTitle(Main.colorText("&c"), Main.colorText("&c&l명령자만 사용가능합니다."), 10, 20, 10);
                return false;
            }
        }
        return false;
    }

    public static String role(Player player){ return Main.playerRole.get(player.getUniqueId().toString()); }
    public static Boolean isDig(String str) { return str.chars().allMatch(Character::isDigit); }
}
