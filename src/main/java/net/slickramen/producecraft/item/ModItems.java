package net.slickramen.producecraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.slickramen.producecraft.ProduceCraft;
import net.slickramen.producecraft.block.ModBlocks;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ProduceCraft.MOD_ID);

    public static final RegistryObject<Item> RICE_SEEDS = ITEMS.register("rice_seeds",
            () -> new ItemNameBlockItem(ModBlocks.RICE_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> RICE_SHOOT = ITEMS.register("rice_shoot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RICE_BALL = ITEMS.register("rice_ball",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
