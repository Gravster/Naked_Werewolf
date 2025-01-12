package gravaster.supopo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartGameCommand implements CommandExecutor {
    private final WerewolfGame plugin;
    private static final Random RANDOM = new Random();

    public StartGameCommand(WerewolfGame plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行できます。");
            return false;
        }

        if (plugin.isGameRunning()) {
            sender.sendMessage(ChatColor.RED + "ゲームはすでに進行中です。");
            return false;
        }

        List<Player> participants = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (participants.size() < 2) {
            sender.sendMessage(ChatColor.RED + "ゲームを開始するには少なくとも2人のプレイヤーが必要です。");
            return false;
        }

        Player werewolf = participants.get(RANDOM.nextInt(participants.size()));
        plugin.setWerewolf(werewolf);

        for (Player player : participants) {
            player.getInventory().clear();

            if (player.equals(werewolf)) {
                player.sendTitle(ChatColor.RED + "あなたは人狼です！", "村人を欺きましょう。", 10, 70, 20);

            } else {
                player.sendTitle(ChatColor.GREEN + "あなたは村人です！", "人狼を見つけ出しましょう。", 10, 70, 20);
                player.getInventory().setHelmet(plugin.getEquipment("helmet"));
                player.getInventory().setChestplate(plugin.getEquipment("chestplate"));
                player.getInventory().setLeggings(plugin.getEquipment("leggings"));
                player.getInventory().setBoots(plugin.getEquipment("boots"));

            }
        }

        plugin.showDiamondBossBar();
        plugin.setGameRunning(true);
        Bukkit.broadcastMessage(ChatColor.GREEN + "ゲームが開始されました！ 人狼を見つけてください！");
        return true;
    }
}
