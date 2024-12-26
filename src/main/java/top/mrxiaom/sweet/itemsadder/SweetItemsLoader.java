package top.mrxiaom.sweet.itemsadder;
        
import top.mrxiaom.pluginbase.BukkitPlugin;
import top.mrxiaom.sweet.itemsadder.utils.Offset;

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
    protected void beforeEnable() {
        Offset.init();
    }

    @Override
    protected void afterEnable() {
        getLogger().info("SweetItemsLoader 加载完毕");
    }
}
