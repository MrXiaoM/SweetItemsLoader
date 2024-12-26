package top.mrxiaom.sweet.itemsadder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.sweet.itemsadder.func.FontImagesManager;

public class Placeholder extends PlaceholderExpansion {
    SweetItemsLoader plugin;
    protected Placeholder(SweetItemsLoader plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean register() {
        unregister();
        return super.register();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "img";
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
        String font = manager.requestImage(params, true);
        if (font == null) {
            return params + "_NOT_FOUND";
        }
        return font;
    }
}
