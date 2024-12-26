package top.mrxiaom.sweet.itemsadder.func;

import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.sweet.itemsadder.SweetItemsLoader;

import java.io.File;

@AutoRegister(requirePlugins = "ItemsAdder")
public class ItemsAdderHook extends AbstractModule implements Listener {
    public ItemsAdderHook(SweetItemsLoader plugin) {
        super(plugin);
        registerEvents();
    }

    @EventHandler
    public void onItemsAdderLoadData(ItemsAdderLoadDataEvent e) {
        File itemsAdderFolder = new File(plugin.getDataFolder().getParentFile(), "ItemsAdder");
        // TODO: ItemsAdder 重载时读取其 font_images 配置
        FontImagesManager.inst().overwriteCache(itemsAdderFolder);
    }
}
