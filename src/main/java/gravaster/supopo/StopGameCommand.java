package gravaster.supopo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StopGameCommand implements CommandExecutor {
    private final WerewolfGame plugin;

    public StopGameCommand(WerewolfGame plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!plugin.isGameRunning()) {
            sender.sendMessage(ChatColor.RED + "現在進行中のゲームはありません。");
            return false;
        }

        plugin.resetGame();
        Bukkit.broadcastMessage(ChatColor.RED + "ゲームが強制終了されました。");
        return true;
    }
}
