package nulld.commision.RChess.Event;

import nulld.commision.RChess.Command.ChessCommand;
import nulld.commision.RChess.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ChessWand implements Listener {
    private static final Map<String, Boolean> field = new HashMap<>();
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        FileConfiguration config = Main.getPlugin(Main.class).getConfig();
        int ax=0, az=0, bx=0, bz=0;

        if (itemStack.getType().equals(Material.NETHERITE_AXE) && ChessCommand.setField.get(player.getUniqueId().toString())!= null){
            if (ChessCommand.setField.get(player.getUniqueId() + "_field") ^ ChessCommand.setField.get(player.getUniqueId() + "_board")) {
                if (action.equals(Action.LEFT_CLICK_BLOCK)) {
                    Location left_loc = event.getClickedBlock().getLocation();
                    if (ChessCommand.setField.get(player.getUniqueId() + "_field")) {
                        config.set("field.x1", left_loc.getBlockX());
                        config.set("field.z1", left_loc.getBlockZ());
                        config.set("field.y", left_loc.getBlockY());
                    }
                    if (ChessCommand.setField.get(player.getUniqueId() + "_board")) {
                        config.set("board.x1", left_loc.getBlockX());
                        config.set("board.z1", left_loc.getBlockZ());
                        config.set("board.y", left_loc.getBlockY());
                    }

                    field.put(player.getUniqueId() + ".leftSet", true);
                    ax = left_loc.getBlockX();
                    az = left_loc.getBlockZ();

                    player.sendMessage(Main.colorText("&7[ &fRChess &7] &dpos1지점 설정완료"));
                } else if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                    Location right_loc = event.getClickedBlock().getLocation();
                    if (ChessCommand.setField.get(player.getUniqueId() + "_field")) {
                        config.set("field.x2", right_loc.getBlockX());
                        config.set("field.z2", right_loc.getBlockZ());
                        config.set("field.y", right_loc.getBlockY());
                    }
                    if (ChessCommand.setField.get(player.getUniqueId() + "_board")) {
                        config.set("board.x2", right_loc.getBlockX());
                        config.set("board.z2", right_loc.getBlockZ());
                        config.set("board.y", right_loc.getBlockY());
                    }

                    field.put(player.getUniqueId() + ".rightSet", true);
                    bx = right_loc.getBlockX();
                    bz = right_loc.getBlockZ();

                    player.sendMessage(Main.colorText("&7[ &fRChess &7] &dpos2지점 설정완료"));
                }

                int ivx = Math.abs(ax-bx)+1;
                int ivz = Math.abs(az-bz)+1;

                if (ChessCommand.setField.get(player.getUniqueId() + "_board")) {
                    if (ivx != 8 || ivz != 8) {
                        player.sendMessage(Main.colorText("&7[ &fRChess &7] &e명령보드 크기가 64칸이 아닙니다. 편집을 취소합니다."));
                        return;
                    }
                }

                if (field.get(player.getUniqueId() + ".leftSet") && field.get(player.getUniqueId() + ".rightSet")){
                    ChessCommand.setField.remove(player.getUniqueId() + "_field");
                    ChessCommand.setField.remove(player.getUniqueId() + "_board");
                    field.remove(player.getUniqueId() + ".leftSet");
                    field.remove(player.getUniqueId() + ".rightSet");
                }

                if (ivx%8==0 && ivz%8==0) {
                    Main.getPlugin(Main.class).saveConfig();
                } else {
                    player.sendMessage(Main.colorText("&7[ &fRChess &7] &e블럭 단위의 구역을 설정할 수 없습니다. &c구역 편집이 취소됩니다."));
                }
            }
        }
    }
}
