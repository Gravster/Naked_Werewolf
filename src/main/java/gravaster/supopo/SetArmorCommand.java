package gravaster.supopo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SetArmorCommand implements CommandExecutor {
    private final WerewolfGame plugin;

    public SetArmorCommand(WerewolfGame plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行できます！");
            return true;
        }

        Player player = (Player) sender;
        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.BLUE + "装備設定");

        gui.setItem(0, plugin.getEquipment("helmet"));
        gui.setItem(1, plugin.getEquipment("chestplate"));
        gui.setItem(2, plugin.getEquipment("leggings"));
        gui.setItem(3, plugin.getEquipment("boots"));

        player.openInventory(gui);
        player.sendMessage(ChatColor.GREEN + "装備設定メニューを開きました！");
        return true;
    }
}