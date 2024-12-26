package top.mrxiaom.sweet.itemsadder.commands;
        
import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.sweet.itemsadder.SweetItemsLoader;
import top.mrxiaom.sweet.itemsadder.func.AbstractModule;
import top.mrxiaom.sweet.itemsadder.func.FontImagesManager;

import java.io.File;
import java.util.*;

@AutoRegister
public class CommandMain extends AbstractModule implements CommandExecutor, TabCompleter, Listener {
    public CommandMain(SweetItemsLoader plugin) {
        super(plugin);
        registerCommand("sweetitemsloader", this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if (("gen".equalsIgnoreCase(args[0]) || "generate".equalsIgnoreCase(args[0])) && sender.isOp()) {
                if (!plugin.hasItemsAdder()) {
                    return t(sender, "&e该子服未安装 ItemsAdder");
                }
                File itemsAdderFolder = new File(plugin.getDataFolder().getParentFile(), "ItemsAdder");
                FontImagesManager.inst().overwriteCache(itemsAdderFolder);
                return t(sender, "&a已执行生成操作");
            }
            if ("reload".equalsIgnoreCase(args[0]) && sender.isOp()) {
                plugin.reloadConfig();
                return t(sender, "&a配置文件已重载");
            }
        }
        return true;
    }

    private static final List<String> emptyList = Lists.newArrayList();
    private static final List<String> listArg0 = Lists.newArrayList();
    private static final List<String> listOpArg0 = Lists.newArrayList(
            "generate", "reload");
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return startsWith(sender.isOp() ? listOpArg0 : listArg0, args[0]);
        }
        return emptyList;
    }

    public List<String> startsWith(Collection<String> list, String s) {
        return startsWith(null, list, s);
    }
    public List<String> startsWith(String[] addition, Collection<String> list, String s) {
        String s1 = s.toLowerCase();
        List<String> stringList = new ArrayList<>(list);
        if (addition != null) stringList.addAll(0, Lists.newArrayList(addition));
        stringList.removeIf(it -> !it.toLowerCase().startsWith(s1));
        return stringList;
    }
}
