package dev.sefiraat.netheopoiesis.slimefun.tools;

import dev.sefiraat.netheopoiesis.slimefun.flora.seeds.NetherSeed;
import dev.sefiraat.netheopoiesis.utils.Keys;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.LimitedUseItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Optional;

public class HarvestingTool extends LimitedUseItem {

    private static final NamespacedKey key = Keys.newKey("uses");

    public HarvestingTool(ItemGroup group,
                          SlimefunItemStack item,
                          RecipeType recipeType,
                          ItemStack[] recipe,
                          int amount
    ) {
        super(group, item, recipeType, recipe);
        setMaxUseCount(amount);
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return this::onUse;
    }

    private void onUse(@Nonnull PlayerRightClickEvent e) {
        e.cancel();
        final Optional<Block> optional = e.getClickedBlock();
        if (optional.isPresent()) {
            final Block block = optional.get();
            final SlimefunItem slimefunItem = BlockStorage.check(block);
            if (slimefunItem instanceof NetherSeed seed && seed.isHarvestable() && seed.isMature(block)) {
                final ItemStack stackToDrop = seed.getHarvestingResult();
                seed.updateGrowthStage(block, 0);
                block.getWorld().dropItem(block.getLocation(), stackToDrop);
                damageItem(e.getPlayer(), e.getItem());
            }
        }
    }

    @Override
    protected @Nonnull
    NamespacedKey getStorageKey() {
        return key;
    }
}
