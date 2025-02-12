package net.Babychaosfloh.justvampires.datagen.loot;

import net.Babychaosfloh.justvampires.block.ModBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {

        this.dropSelf(ModBlocks.SAPPHIRE_BLOCK.get());
        this.dropSelf(ModBlocks.ALTAR_BLOCK.get());
        this.dropSelf(ModBlocks.TESTBLOCK.get());
        //this.dropSelf(ModBlocks.SOUND_BLOCK.get());

        //this.add(ModBlocks.SAPPHIRE_ORE.get(),
        //        block -> createCopperLikeOreDrops(ModBlocks.SAPPHIRE_ORE.get(), ModItems.RAW_SAPPHIRE.get()));
        //this.add(ModBlocks.DEEPSLATE_SAPPHIRE_ORE.get(),
        //        block -> createCopperLikeOreDrops(ModBlocks.DEEPSLATE_SAPPHIRE_ORE.get(), ModItems.RAW_SAPPHIRE.get()));
        //this.add(ModBlocks.NETHER_SAPPHIRE_ORE.get(),
        //        block -> createCopperLikeOreDrops(ModBlocks.NETHER_SAPPHIRE_ORE.get(), ModItems.RAW_SAPPHIRE.get()));
        //this.add(ModBlocks.END_STONE_SAPPHIRE_ORE.get(),
        //        block -> createCopperLikeOreDrops(ModBlocks.END_STONE_SAPPHIRE_ORE.get(), ModItems.RAW_SAPPHIRE.get()));
    }


    /*
    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }
     */


    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }


}
