package top.mrxiaom.sweet.itemsadder;
        
import top.mrxiaom.pluginbase.BukkitPlugin;
import top.mrxiaom.pluginbase.utils.Util;
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
    boolean hasItemsAdder;
    Placeholder placeholder;

    public boolean hasItemsAdder() {
        return hasItemsAdder;
    }

    @Override
    protected void beforeEnable() {
        Offset.init();
        hasItemsAdder = Util.isPresent("dev.lone.itemsadder.api.ItemsAdder");
        if (!hasItemsAdder) {
            placeholder = new Placeholder(this);
            placeholder.register();
        }
    }

    @Override
    protected void afterEnable() {
        getLogger().info("SweetItemsLoader 加载完毕");
    }

    @Override
    protected void afterDisable() {
        if (placeholder != null) {
            placeholder.unregister();
        }
    }
}
