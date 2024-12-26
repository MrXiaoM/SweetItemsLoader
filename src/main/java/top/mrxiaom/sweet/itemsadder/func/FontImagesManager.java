package top.mrxiaom.sweet.itemsadder.func;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.pluginbase.utils.Bytes;
import top.mrxiaom.sweet.itemsadder.SweetItemsLoader;
import top.mrxiaom.sweet.itemsadder.utils.Offset;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@AutoRegister
public class FontImagesManager extends AbstractModule {
    private static final Pattern offsetPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    long cooldown = 0;
    Map<String, String> images = new HashMap<>();
    File fontImagesFile;
    public FontImagesManager(SweetItemsLoader plugin) {
        super(plugin);
        registerBungee();
    }

    @Override
    public void reloadConfig(MemoryConfiguration config) {
        String s = config.getString("font-image-file", "./font_images.yml");
        fontImagesFile = s.startsWith("./") ? new File(plugin.getDataFolder(), s.substring(2)) : new File(s);
        if (!plugin.hasItemsAdder()) {
            if (!fontImagesFile.exists()) {
                warn("文件不存在: " + s);
                return;
            }
            images.clear();
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(fontImagesFile);
            ConfigurationSection section = cfg.getConfigurationSection("font_images");
            if (section != null) for (String key : section.getKeys(false)) {
                images.put(key, section.getString(key));
            }
            info("已加载 " + images.size() + " 个 font_images");
        } else if (!fontImagesFile.exists()) {
            overwriteCache();
        }
    }

    @Override
    public void receiveBungee(String subChannel, DataInputStream in) throws IOException {
        if (subChannel.equals("ItemsAdder")) {
            String operation = in.readUTF();
            if (operation.equals("Reload") && System.currentTimeMillis() > cooldown) {
                cooldown = System.currentTimeMillis() + 6000L;
                reloadConfig(plugin.getConfig());
            }
        }
    }

    public void sendReload() {
        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (player == null) return;
        ByteArrayDataOutput out = Bytes.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("All");
        out.writeUTF("ItemsAdder");
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             DataOutputStream msg = new DataOutputStream(bytes)) {
            msg.writeUTF("Reload");
            out.writeShort(bytes.toByteArray().length);
            out.write(bytes.toByteArray());
            player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        } catch (IOException e) {
            warn(e);
        }
    }

    public void overwriteCache() {
        File itemsAdderFolder = new File(plugin.getDataFolder().getParentFile(), "ItemsAdder");
        overwriteCache(itemsAdderFolder);
    }

    public void overwriteCache(File itemsAdderFolder) {
        images.clear();
        Map<String, String> cache = new HashMap<>();
        File cacheFile = new File(itemsAdderFolder, "storage/font_images_unicode_cache.yml");
        YamlConfiguration cacheData = YamlConfiguration.loadConfiguration(cacheFile);
        for (String key : cacheData.getKeys(false)) {
            cache.put(key, cacheData.getString(key));
        }
        load(cache, new File(itemsAdderFolder, "contents"));
        load(cache, new File(itemsAdderFolder, "data"));
        cache.clear();
        YamlConfiguration config = new YamlConfiguration();
        for (Map.Entry<String, String> entry : images.entrySet()) {
            config.set("font_images." + entry.getKey(), entry.getValue());
        }
        try {
            config.save(fontImagesFile);
        } catch (IOException e) {
            warn(e);
        }
        info("已保存 " + images.size() + " 个 font_images");
        sendReload();
    }

    private void load(Map<String, String> cache, File folder) {
        File[] files = folder.listFiles();
        if (files != null) for (File file : files) {
            if (file.isDirectory()) {
                load(cache, file);
                continue;
            }
            if (file.getName().endsWith(".yml")) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                String namespace = config.getString("info.namespace");
                ConfigurationSection section = config.getConfigurationSection("font_images");
                if (namespace == null || section == null) continue;
                for (String key : section.getKeys(false)) {
                    if (!section.getBoolean(key + ".enabled", true)) continue;
                    String symbol = cache.get(namespace + ":" + key);
                    if (symbol != null) {
                        images.put(key, symbol);
                    }
                }
            }
        }
    }

    public String requestOffset(String str) {
        String replace = str.replace(":", "");
        if (!offsetPattern.matcher(replace).matches()) {
            return "";
        }
        try {
            int parseInt = Integer.parseInt(replace.replace("offset_", ""));
            return Offset.get(parseInt);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Nullable
    public String requestImage(String key, boolean z) {
        String s = images.get(key);
        if (s != null && z) {
            return s + Offset.negative.get(1);
        }
        return s;
    }

    public static FontImagesManager inst() {
        return instanceOf(FontImagesManager.class);
    }
}
