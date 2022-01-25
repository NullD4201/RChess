package nulld.commision.RChess;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static ItemStack armor(String team, EquipmentSlot slot){
        if (slot.equals(EquipmentSlot.CHEST)){
            ItemStack itemStack = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
            return getItemStack(team, itemStack);
        }
        if (slot.equals(EquipmentSlot.LEGS)){
            ItemStack itemStack = new ItemStack(Material.LEATHER_LEGGINGS, 1);
            return getItemStack(team, itemStack);
        }
        if (slot.equals(EquipmentSlot.FEET)){
            ItemStack itemStack = new ItemStack(Material.LEATHER_BOOTS, 1);
            return getItemStack(team, itemStack);
        }
        return null;
    }

    private static ItemStack getItemStack(String team, ItemStack itemStack) {
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        if (team.equalsIgnoreCase("white")){
            meta.setDisplayName(Main.colorText("&f&l화이트"));
            meta.setColor(Color.WHITE);
        }
        if (team.equalsIgnoreCase("black")){
            meta.setDisplayName(Main.colorText("&8&l블랙"));
            meta.setColor(Color.BLACK);
        }
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public static void playerSetTeam(Player player, String team){
        player.getInventory().setChestplate(armor(team, EquipmentSlot.CHEST));
        player.getInventory().setLeggings(armor(team, EquipmentSlot.LEGS));
        player.getInventory().setBoots(armor(team, EquipmentSlot.FEET));
    }

    public static Map<String, Location> playField = new HashMap<>();
    public static void fieldSet() {
        World world = Bukkit.getWorld(Main.getPlugin(Main.class).getConfig().getString("field.world"));
        int x1 = Main.getPlugin(Main.class).getConfig().getInt("field.x1");
        int x2 = Main.getPlugin(Main.class).getConfig().getInt("field.x2");
        int z1 = Main.getPlugin(Main.class).getConfig().getInt("field.z1");
        int z2 = Main.getPlugin(Main.class).getConfig().getInt("field.z2");
        int y = Main.getPlugin(Main.class).getConfig().getInt("field.y");

        int ivx = (Math.abs(x1-x2)+1)/8; if (x1 < x2) ivx *= -1;
        int ivz = (Math.abs(z1-z2)+1)/8; if (z1 < z2) ivz *= -1;

        mapSet(world, x1, z1, y, ivx, ivz, playField);
    }
    public static Map<String, Location> playBoard = new HashMap<>();
    public static void boardSet() {
        World world = Bukkit.getWorld(Main.getPlugin(Main.class).getConfig().getString("board.world"));
        int x1 = Main.getPlugin(Main.class).getConfig().getInt("board.x1");
        int x2 = Main.getPlugin(Main.class).getConfig().getInt("board.x2");
        int z1 = Main.getPlugin(Main.class).getConfig().getInt("board.z1");
        int z2 = Main.getPlugin(Main.class).getConfig().getInt("board.z2");
        int y = Main.getPlugin(Main.class).getConfig().getInt("board.y");

        int ivx = (Math.abs(x1-x2)+1)/8; if (x1 < x2) ivx = ivx-1;
        int ivz = (Math.abs(z1-z2)+1)/8; if (z1 < z2) ivz = ivz-1;

        mapSet(world, x1, z1, y, ivx, ivz, playBoard);
    }

    private static void mapSet(World world, int x1, int z1, int y, int ivx, int ivz, Map<String, Location> map) {
        map.put("white_pawn_1", new Location(world, x1+0.5, y, z1+ivz+0.5));
        map.put("white_pawn_2", new Location(world, x1+ivx+0.5, y, z1+ivz+0.5));
        map.put("white_pawn_3", new Location(world, x1+ivx*2+0.5, y, z1+ivz+0.5));
        map.put("white_pawn_4", new Location(world, x1+ivx*3+0.5, y, z1+ivz+0.5));
        map.put("white_pawn_5", new Location(world, x1+ivx*4+0.5, y, z1+ivz+0.5));
        map.put("white_pawn_6", new Location(world, x1+ivx*5+0.5, y, z1+ivz+0.5));
        map.put("white_pawn_7", new Location(world, x1+ivx*6+0.5, y, z1+ivz+0.5));
        map.put("white_pawn_8", new Location(world, x1+ivx*7+0.5, y, z1+ivz+0.5));
        map.put("white_rook_1", new Location(world, x1+0.5, y, z1+0.5));
        map.put("white_rook_2", new Location(world, x1+ivx*7+0.5, y, z1+0.5));
        map.put("white_knight_1", new Location(world, x1+ivx+0.5, y, z1+0.5));
        map.put("white_knight_2", new Location(world, x1+ivx*6+0.5, y, z1+0.5));
        map.put("white_bishop_1", new Location(world, x1+ivx*2+0.5, y, z1+0.5));
        map.put("white_bishop_2", new Location(world, x1+ivx*5+0.5, y, z1+0.5));
        map.put("white_queen", new Location(world, x1+ivx*3+0.5, y, z1+0.5));
        map.put("white_king", new Location(world, x1+ivx*4+0.5, y, z1+0.5));

        map.put("black_pawn_1", new Location(world, x1+0.5, y, z1+ivz*6+0.5));
        map.put("black_pawn_2", new Location(world, x1+ivx+0.5, y, z1+ivz*6+0.5));
        map.put("black_pawn_3", new Location(world, x1+ivx*2+0.5, y, z1+ivz*6+0.5));
        map.put("black_pawn_4", new Location(world, x1+ivx*3+0.5, y, z1+ivz*6+0.5));
        map.put("black_pawn_5", new Location(world, x1+ivx*4+0.5, y, z1+ivz*6+0.5));
        map.put("black_pawn_6", new Location(world, x1+ivx*5+0.5, y, z1+ivz*6+0.5));
        map.put("black_pawn_7", new Location(world, x1+ivx*6+0.5, y, z1+ivz*6+0.5));
        map.put("black_pawn_8", new Location(world, x1+ivx*7+0.5, y, z1+ivz*6+0.5));
        map.put("black_rook_1", new Location(world, x1+0.5, y, z1+ivz*7+0.5));
        map.put("black_rook_2", new Location(world, x1+ivx*7+0.5, y, z1+ivz*7+0.5));
        map.put("black_knight_1", new Location(world, x1+ivx+0.5, y, z1+ivz*7+0.5));
        map.put("black_knight_2", new Location(world, x1+ivx*6+0.5, y, z1+ivz*7+0.5));
        map.put("black_bishop_1", new Location(world, x1+ivx*2+0.5, y, z1+ivz*7+0.5));
        map.put("black_bishop_2", new Location(world, x1+ivx*5+0.5, y, z1+ivz*7+0.5));
        map.put("black_queen", new Location(world, x1+ivx*3+0.5, y, z1+ivz*7+0.5));
        map.put("black_king", new Location(world, x1+ivx*4+0.5, y, z1+ivz*7+0.5));
    }
}
