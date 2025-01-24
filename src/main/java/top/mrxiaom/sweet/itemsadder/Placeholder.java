package top.mrxiaom.sweet.itemsadder;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.sweet.itemsadder.func.FontImagesManager;
import top.mrxiaom.sweet.itemsadder.utils.Offset;

public class Placeholder extends PlaceholderExpansion {
    SweetItemsLoader plugin;
    private final String id;
    protected Placeholder(SweetItemsLoader plugin, String id) {
        this.plugin = plugin;
        this.id = id;
    }

    @Override
    public boolean register() {
        unregister();
        return super.register();
    }

    @Override
    public @NotNull String getIdentifier() {
        return id;
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        FontImagesManager manager = FontImagesManager.inst();
        if (params.startsWith("offset_")) {
            return manager.requestOffset(params);
        }
        if (params.contains(",")) {
            String[] split = params.split(",");
            if (split.length >= 2) return requestForGui(manager, split);
        }
        String font = manager.requestImage(params, true);
        if (font == null) {
            return params + "_NOT_FOUND";
        }
        return font;
    }

    private String requestForGui(FontImagesManager manager, String[] split) {
        String name = split[0];
        try {
            int topOffset = split.length >= 3 ? -Integer.parseInt(split[2]) : 0;
            int bottomOffset = -Integer.parseInt(split[1]);
            int extraOffset = split.length >= 4 ? -Integer.parseInt(split[3]) : 0;
            int connectOffset = split.length >= 5 ? -Integer.parseInt(split[4]) : 0;
            String font1 = manager.requestImage(name + "_1", true);
            String font2 = manager.requestImage(name + "_2", true);
            String font3 = manager.requestImage(name + "_3", true);
            String font4 = manager.requestImage(name + "_4", true);
            StringBuilder sb = new StringBuilder();
            if (topOffset != 0) sb.append(Offset.get(topOffset));
            if (font1 != null) sb.append(font1);
            if (connectOffset != 0) sb.append(Offset.get(connectOffset));
            if (font2 != null) sb.append(font2);
            if (bottomOffset != 0) sb.append(Offset.get(bottomOffset));
            if (font3 != null) sb.append(font3);
            if (connectOffset != 0) sb.append(Offset.get(connectOffset));
            if (font4 != null) sb.append(font4);
            if (extraOffset != 0) sb.append(Offset.get(extraOffset));
            return sb.toString();
        } catch (NumberFormatException e) {
            return "WRONG NUMBER";
        }
    }
}
