package top.mrxiaom.sweet.itemsadder;
        
import org.jetbrains.annotations.NotNull;
import top.mrxiaom.pluginbase.BukkitPlugin;
import top.mrxiaom.pluginbase.EconomyHolder;

public class SweetItemsLoader extends BukkitPlugin {
    public static SweetItemsLoader getInstance() {
        return (SweetItemsLoader) BukkitPlugin.getInstance();
    }

    public SweetItemsLoader() {
        super(options()
                .bungee(true)
                .adventure(false)
                .database(false)
                .reconnectDatabaseWhenReloadConfig(false)
                .vaultEconomy(false)
                .scanIgnore("top.mrxiaom.sweet.itemsadder.libs")
        );
    }


    @Override
    protected void afterEnable() {
        getLogger().info("SweetItemsLoader 加载完毕");
    }
}
