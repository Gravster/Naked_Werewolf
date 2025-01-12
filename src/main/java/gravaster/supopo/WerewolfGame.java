package gravaster.supopo;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;


public class WerewolfGame extends JavaPlugin {
    private Player werewolfPlayer; // 人狼プレイヤーを保持
    private BossBar diamondBossBar; // ダイヤモンドカウント用ボスバー
    private boolean gameRunning = false; // ゲームが進行中かどうか
    private final int diamondGoal = 10; // ダイヤの目標個数

    @Override
    public void onEnable() {
        // コマンド登録
        getCommand("startgame").setExecutor(new StartGameCommand(this));
        getCommand("setarmor").setExecutor(new SetArmorCommand(this));

        // イベントリスナー登録
        getServer().getPluginManager().registerEvents(new ArmorSettingListener(this), this);
        getServer().getPluginManager().registerEvents(new GameListener(this), this);

        // デフォルト設定保存
        saveDefaultConfig();

        // ボスバーの初期化
        diamondBossBar = Bukkit.createBossBar(
                "ダイヤを集めよう: 0/" + diamondGoal,
                BarColor.BLUE,
                BarStyle.SEGMENTED_10
        );
        diamondBossBar.setProgress(0.0); // 最初は0%
    }

    public BossBar getDiamondBossBar() {
        return diamondBossBar;
    }

    public void setWerewolf(Player player) {
        this.werewolfPlayer = player;
    }

    public Player getWerewolf() {
        return this.werewolfPlayer;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean running) {
        this.gameRunning = running;
    }

    public void showDiamondBossBar() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            diamondBossBar.addPlayer(player);
        }
        diamondBossBar.setVisible(true);
    }

    public void updateDiamondBossBar(int currentCount) {
        diamondBossBar.setTitle("ダイヤモンド: " + currentCount + " / " + diamondGoal);
        double progress = Math.min(1.0, currentCount / (double) diamondGoal);
        diamondBossBar.setProgress(progress);
        if (currentCount >= diamondGoal) {
            Bukkit.broadcastMessage(ChatColor.GREEN + "ダイヤモンドの目標が達成されました！");
            resetGame(); // ゲーム終了ロジックを呼び出し
        }
    }

    public void hideDiamondBossBar() {
        diamondBossBar.setVisible(false);
        diamondBossBar.removeAll();
    }

    public ItemStack getEquipment(String slot) {
        return getConfig().contains("equipment." + slot)
                ? getConfig().getItemStack("equipment." + slot)
                : getDefaultEquipment(slot);
    }

    public ItemStack getDefaultEquipment(String slot) {
        switch (slot) {
            case "helmet":
                return new ItemStack(Material.DIAMOND_HELMET);
            case "chestplate":
                return new ItemStack(Material.DIAMOND_CHESTPLATE);
            case "leggings":
                return new ItemStack(Material.DIAMOND_LEGGINGS);
            case "boots":
                return new ItemStack(Material.DIAMOND_BOOTS);
            default:
                return null;
        }
    }

    public int calculateTotalDiamonds() {
        int total = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            total += player.getInventory().all(Material.DIAMOND).values().stream()
                    .mapToInt(ItemStack::getAmount)
                    .sum();
        }
        return total;
    }

    public void resetGame() {
        gameRunning = false;
        werewolfPlayer = null;
        diamondBossBar.removeAll();
        diamondBossBar.setProgress(0.0);
        Bukkit.getScheduler().cancelTasks(this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            // インベントリのクリア
            player.getInventory().clear();

            // タイトルとサブタイトルを表示
            player.sendTitle(
                    ChatColor.GOLD + "ゲーム終了",        // タイトル
                    ChatColor.LIGHT_PURPLE + "お疲れさまでした！",  // サブタイトル
                    10, 70, 20                             // フェードイン、表示、フェードアウトの時間
            );

            // 進捗達成時の音（紫色の音）を再生
            player.playSound(
                    player.getLocation(),
                    Sound.UI_TOAST_CHALLENGE_COMPLETE, // 進捗達成時の音
                    1.0f,                             // 音量
                    1.0f                              // 音程
            );
        }
    }



}