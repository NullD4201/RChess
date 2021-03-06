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
        } else if (label.equalsIgnoreCase("?????????")) return StringUtil.copyPartialMatches(args[args.length-1], new ArrayList<>(), new ArrayList<>());
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
                    player.sendMessage(Main.colorText("&d /chess timer <second> \n &e<second>&b?????? ???????????? ???????????? ?????? ?????? ?????? ???????????? ?????? ???????????????.\n &a0??? ?????? ??????????????? ??????"));
                    player.sendMessage(Main.colorText("&d /chess timerbar <on/off> &b???????????? ???????????? ????????? ????????? ???????????????."));
                    player.sendMessage(Main.colorText("&d /chess tpset &b???????????? ??????????????? ????????? ???????????????."));
                    player.sendMessage(Main.colorText("&d /chess skip &b????????? ?????? ????????????. &c?????? ???????????? ?????? ??????"));
                    player.sendMessage(Main.colorText("&d /chess spec <add/del> <name> &b????????? ????????? ???????????????."));
                    player.sendMessage(Main.colorText("&d /chess commander <white/black> <name> &b????????? ????????? ???????????????."));
                    player.sendMessage(Main.colorText("&d /chess reset [all] &b??? ??? ??????????????? ??????????????????. \n &c/chess reset all&e??? ???????????? ??? ????????? ????????? ?????? ????????? ??????????????????."));
                    player.sendMessage(Main.colorText("&d /chess set field [x1] [z1] [x2] [z2] &b????????? ??????????????? ???????????????. \n &c/chess info&b??? ????????????, ????????? ????????? ?????? ????????? ???????????? ?????????."));
                    player.sendMessage(Main.colorText("&d /chess set board [x1] [z1] [x2] [z2] &b????????? ????????? ???????????????. \n &c/chess info&b??? ????????????, ????????? ????????? ?????? ????????? ???????????? ?????????."));
                    player.sendMessage(Main.colorText("&d /chess role <rook/bishop/knight/pawn> <true/false> \n &b???????????? ????????? ???????????????. &atrue&b?????? ?????????"));
                    player.sendMessage(Main.colorText("&d /chess fa <white/black> [x] [y] [z] \n &b????????? ?????? ????????? ???????????????."));
                    player.sendMessage(Main.colorText("&d /chess stop \n &b?????? ?????? ????????? ???????????? ?????? ???????????? ??????????????????."));
                } else if (args.length == 1){
                    if (args[0].equalsIgnoreCase("skip")){
                        GameCommand.notice_message("&a?????? ????????? ???????????????.");
                        isSkip = true;
                    } else if (args[0].equalsIgnoreCase("reset")){
                        Main.playerRole.clear();
                        player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&a&l?????? ????????? ??????"), 10, 20, 10);

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
                        player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&a????????? tp?????? ????????????"), 10, 20, 10);
                    } else if (args[0].equalsIgnoreCase("info")){
                        String fieldInfo1 = config.getString("field.world")+"@"+config.getInt("field.x1")+","+config.getInt("field.z1");
                        String fieldInfo2 = config.getString("field.world")+"@"+config.getInt("field.x2")+","+config.getInt("field.z2");
                        String chessPlate = config.getString("teleport.world")+"@"+config.getInt("teleport.x")+","+config.getInt("teleport.y")+","+config.getInt("teleport.z");
                        String boardPlate1 = config.getString("board.world")+"@"+config.getInt("board.x1")+","+config.getInt("board.z1");
                        String boardPlate2 = config.getString("board.world")+"@"+config.getInt("board.x2")+","+config.getInt("board.z2");
                        player.sendMessage(" ");
                        player.sendMessage(Main.colorText("&a ??????: &f"+fieldInfo1+"~"+fieldInfo2));
                        player.sendMessage(Main.colorText("&a ????????? ???????????? ??????: &f"+chessPlate));
                        player.sendMessage(Main.colorText("&a ????????? ??????: &f"+boardPlate1+"~"+boardPlate2));
                        player.sendMessage(Main.colorText("&a ????????? ??????: &f"+config.getBoolean("bossbar")));
                        player.sendMessage(Main.colorText("&a ??? ?????????: &f"+config.getInt("timer")+"???"));
                        player.sendMessage(Main.colorText("&a ??????: "));
                        player.sendMessage(Main.colorText("&a   ???: &f"+config.getBoolean("role.rook")));
                        player.sendMessage(Main.colorText("&a   ??????: &f"+config.getBoolean("role.bishop")));
                        player.sendMessage(Main.colorText("&a   ?????????: &f"+config.getBoolean("role.knight")));
                        player.sendMessage(Main.colorText("&a   ???: &f"+config.getBoolean("role.pawn")));
                        player.sendMessage(" ");
                    }
                } else if (args.length == 2){
                    if (args[0].equalsIgnoreCase("timerbar")) {
                        if (args[1].equalsIgnoreCase("on")) config.set("bossbar", true);
                        else if (args[1].equalsIgnoreCase("off")) config.set("bossbar", false);
                        else player.performCommand("chess");
                        Main.getPlugin(Main.class).saveConfig();
                        player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&a????????? &e"+args[1]), 10, 30, 10);
                    } else if (args[0].equalsIgnoreCase("reset") && args[1].equalsIgnoreCase("all")){
                        new File(Main.getPlugin(Main.class).getDataFolder(), "config.yml").delete();
                        Main.getPlugin(Main.class).saveDefaultConfig();
                        player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&cconfig ????????? ??????"), 10, 30, 10);
                    } else if (args[0].equalsIgnoreCase("timer")){
                        if (args[1].chars().allMatch(Character::isDigit)) config.set("timer", Integer.parseInt(args[1]));
                        Main.getPlugin(Main.class).saveConfig();
                        player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&a??? ????????? &e"+args[1]+"&a?????? ??????"), 10, 30, 10);
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
                        player.sendMessage(Main.colorText("&7[ &fRChess &7] &d?????????: pos1?????? ??????, ?????????: pos2?????? ??????"));
                    }
                } else if (args.length == 3){
                    if (args[0].equalsIgnoreCase("spec")) {
                        List<UUID> specs = (List<UUID>) config.getList("spectator");
                        if (args[1].equalsIgnoreCase("add")){
                            if (specs.contains(Bukkit.getPlayer(args[2]).getUniqueId())) {
                                player.sendTitle(Main.colorText("&cERROR"), Main.colorText("&c?????? ?????????????????????."), 10, 20, 10);
                                return false;
                            }
                            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[2]))) {
                                specs.add(Bukkit.getPlayer(args[2]).getUniqueId());
                                player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&a????????? &e"+args[2]+" &a????????????"), 10, 20, 10);
                            }
                            else player.sendTitle(Main.colorText("&cERROR"), Main.colorText("&c????????? ????????? ????????? ????????????."), 10, 20, 10);
                        } else if (args[1].equalsIgnoreCase("del")){
                            if (!specs.contains(Bukkit.getPlayer(args[2]).getUniqueId()) && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[2])))player.sendTitle(Main.colorText("&cERROR"), Main.colorText("&c???????????? ????????????."), 10, 20, 10);
                            else if (specs.contains(Bukkit.getPlayer(args[2]).getUniqueId())) {
                                specs.remove(Bukkit.getPlayer(args[2]).getUniqueId());
                                player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&a????????? &e"+args[2]+" &a????????????"), 10, 20, 10);
                            }
                            else if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[2]))) player.sendTitle(Main.colorText("&cERROR"), Main.colorText("&c????????? ????????? ????????? ????????????."), 10, 20, 10);
                        }
                        config.set("spectator", specs);
                        Main.getPlugin(Main.class).saveConfig();
                    } else if (args[0].equalsIgnoreCase("commander")){
                        if (Arrays.asList("black", "white").contains(args[1]) && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[2]))){
                            Main.playerRole.put(Bukkit.getPlayer(args[2]).getUniqueId().toString(), args[1] + ".commander");
                            if (Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(args[2]) != null && Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(args[2]).getName().equalsIgnoreCase(args[1])) {
                                player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&e"+args[2]+"&a??? &f"+args[1]+"&a?????? ???????????? ???????????????."), 10, 30, 10);
                                if (config.getString("commander."+args[1]) != null && config.getString("commander."+args[1]).equals(args[2])) config.set("commander."+args[1], null);
                                config.set("commander."+args[1], args[2]);
                                Main.getPlugin(Main.class).saveConfig();
                            } else {
                                player.sendTitle(Main.colorText("&cFAIL"), Main.colorText("&e"+args[2]+"&a??? &f"+args[1]+"&a?????? ????????????."), 10, 30, 10);
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("role")){
                        if (Arrays.asList("rook", "bishop", "knight", "pawn").contains(args[1]) && Arrays.asList("true", "false").contains(args[2])) config.set("role."+args[1], Boolean.valueOf(args[2]));
                        else player.performCommand("chess");
                        Main.getPlugin(Main.class).saveConfig();
                        player.sendMessage(Main.colorText("&7[ &fRChess &7] &e"+args[1]+"&f????????? &b"+args[2]+"&f ????????? ??????????????????."));
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
                                player.sendTitle(Main.colorText("&aSUCCESS"), Main.colorText("&a?????? ?????? ??????"), 10, 30, 10);
                            } else {
                                player.sendMessage(Main.colorText("&7[ &fRChess &7] &c??? ?????? ?????? ????????? 8??? ????????? ????????????."));
                                return false;
                            }
                        } else player.performCommand("chess");
                    }
                }
            } else {
                player.playSound(player.getEyeLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                player.sendTitle(Main.colorText("&c"), Main.colorText("&c&l????????? ?????????????????????."), 10, 20, 10);
                return false;
            }
        } else if (label.equalsIgnoreCase("?????????")){
            if (role(player) != null){
                if (role(player).contains("commander")) {
                    Location destination = new Location(Bukkit.getWorld(config.getString("teleport.world")), config.getInt("teleport.x"), config.getInt("teleport.y")+1, config.getInt("teleport.z"), (float) config.get("teleport.yaw"), (float) config.get("teleport.p"));
                    player.teleport(destination);
                }
            } else {
                player.sendTitle(Main.colorText("&c"), Main.colorText("&c&l???????????? ?????????????????????."), 10, 20, 10);
                return false;
            }
        }
        return false;
    }

    public static String role(Player player){ return Main.playerRole.get(player.getUniqueId().toString()); }
    public static Boolean isDig(String str) { return str.chars().allMatch(Character::isDigit); }
}
