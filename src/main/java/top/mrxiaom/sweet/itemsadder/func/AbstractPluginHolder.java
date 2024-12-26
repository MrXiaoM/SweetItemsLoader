package top.mrxiaom.sweet.itemsadder.func;
        
import top.mrxiaom.sweet.itemsadder.SweetItemsLoader;

@SuppressWarnings({"unused"})
public abstract class AbstractPluginHolder extends top.mrxiaom.pluginbase.func.AbstractPluginHolder<SweetItemsLoader> {
    public AbstractPluginHolder(SweetItemsLoader plugin) {
        super(plugin);
    }

    public AbstractPluginHolder(SweetItemsLoader plugin, boolean register) {
        super(plugin, register);
    }
}
