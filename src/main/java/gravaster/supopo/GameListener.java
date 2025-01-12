package gravaster.supopo;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GameListener implements Listener {
    private final WerewolfGame plugin;

    public GameListener(WerewolfGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!plugin.isGameRunning()) return;

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getItem().getItemStack().getType() == Material.DIAMOND) {
                plugin.updateDiamondBossBar(plugin.calculateTotalDiamonds());
                player.sendMessage("ダイヤモンドを拾いました！");
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!plugin.isGameRunning()) return;

        if (event.getWhoClicked() instanceof Player) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.updateDiamondBossBar(plugin.calculateTotalDiamonds());
            });
        }
    }
}
