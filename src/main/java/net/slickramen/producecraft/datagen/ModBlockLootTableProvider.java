package net.slickramen.producecraft.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.slickramen.producecraft.block.ModBlocks;
import net.slickramen.producecraft.block.custom.RiceCropBlock;
import net.slickramen.producecraft.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.function.Function;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider pRegistries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    @Override
    protected void generate() {

        LootItemCondition.Builder lootItemConditionBuilder =
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.RICE_CROP.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RiceCropBlock.AGE,
                                RiceCropBlock.FIRST_STAGE_MAX_AGE + RiceCropBlock.SECOND_STAGE_MAX_AGE));

        this.add(ModBlocks.RICE_CROP.get(),
                this.createCropDrops(ModBlocks.RICE_CROP.get(),
                        ModItems.RICE_SHOOT.get(), ModItems.RICE_SEEDS.get(), lootItemConditionBuilder));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}