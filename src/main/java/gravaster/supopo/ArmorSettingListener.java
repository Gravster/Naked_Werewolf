package gravaster.supopo;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ArmorSettingListener implements Listener {
    private final WerewolfGame plugin;

    public ArmorSettingListener(WerewolfGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getView().getTitle().equals(ChatColor.BLUE + "装備設定")) {
            Inventory inventory = event.getInventory();

            plugin.getConfig().set("equipment.helmet", inventory.getItem(0));
            plugin.getConfig().set("equipment.chestplate", inventory.getItem(1));
            plugin.getConfig().set("equipment.leggings", inventory.getItem(2));
            plugin.getConfig().set("equipment.boots", inventory.getItem(3));
            plugin.saveConfig();

            player.sendMessage(ChatColor.GREEN + "装備設定が保存されました！");
        }
    }
}
