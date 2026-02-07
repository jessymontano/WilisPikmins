package net.wili.wilispikmins.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.advancement.custom.*;
import net.wili.wilispikmins.block.ModBlocks;
import net.wili.wilispikmins.entity.custom.enums.GrowthStage;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.item.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementsProvider extends AdvancementProvider {
    public ModAdvancementsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new ModAdvancements()));
    }

    public static class ModAdvancements implements AdvancementGenerator {

        @Override
        public void generate(HolderLookup.@NotNull Provider provider, @NotNull Consumer<AdvancementHolder> consumer, @NotNull ExistingFileHelper existingFileHelper) {
            AdvancementHolder root = Advancement.Builder.advancement()
                    .display(
                            ModBlocks.BURIED_PIKMIN_BLOCK.get().asItem(),
                            Component.translatable("advancement.wilispikmins.root.title"),
                            Component.translatable("advancement.wilispikmins.root.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "root"), existingFileHelper);

            AdvancementHolder firstPikmin = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            ModBlocks.BURIED_PIKMIN_BLOCK.get().asItem(),
                            Component.translatable("advancement.wilispikmins.first_pikmin_title"),
                            Component.translatable("advancement.wilispikmins.first_pikmin.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("pluck_pikmin",
                            PikminObtainedTrigger.TriggerInstance.obtainedAnyPikmin())
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "first_pikmin"), existingFileHelper);


                    AdvancementHolder firstNectar = Advancement.Builder.advancement()
                            .parent(root)
                            .display(
                                    ModItems.NECTAR.get(),
                                    Component.translatable("advancement.wilispikmins.first_nectar.title"),
                                    Component.translatable("advancement.wilispikmins.first_nectar.description"),
                                    null,
                                    AdvancementType.TASK,
                                    true,
                                    true,
                                    false
                            )
                            .addCriterion("consume_nectar", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.NECTAR.get()))
                            .save(consumer, ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "first_nectar"),existingFileHelper);

            AdvancementHolder findOnion = Advancement.Builder.advancement()
                    .parent(firstPikmin)
                    .display(
                            ModBlocks.ONION_BLOCK.get().asItem(),
                            Component.translatable("advancement.wilispikmins.find_onion.title"),
                            Component.translatable("advancement.wilispikmins.first_onion.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("find_onion",
                            OnionInteractTrigger.TriggerInstance.interactWithOnion())
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "find_onion"), existingFileHelper);


            AdvancementHolder allPikminTypes = Advancement.Builder.advancement()
                    .parent(firstPikmin)
                    .display(
                            ModItems.MAIN_ONION.get(),
                            Component.translatable("advancement.wilispikmins.all_pikmin.title"),
                            Component.translatable("advancement.wilispikmins.all_pikmin.description"),
                            null,
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            false
                    )
                    .addCriterion("red_pikmin",
                            PikminObtainedTrigger.TriggerInstance.obtainedPikminOfType(PikminType.RED))
                    .addCriterion("yellow_pikmin",
                            PikminObtainedTrigger.TriggerInstance.obtainedPikminOfType(PikminType.YELLOW))
                    .addCriterion("blue_pikmin",
                            PikminObtainedTrigger.TriggerInstance.obtainedPikminOfType(PikminType.BLUE))
                    .addCriterion("purple_pikmin",
                            PikminObtainedTrigger.TriggerInstance.obtainedPikminOfType(PikminType.PURPLE))
                    .addCriterion("white_pikmin",
                            PikminObtainedTrigger.TriggerInstance.obtainedPikminOfType(PikminType.WHITE))
                    .addCriterion("winged_pikmin",
                            PikminObtainedTrigger.TriggerInstance.obtainedPikminOfType(PikminType.WINGED))
                    .addCriterion("rock_pikmin",
                            PikminObtainedTrigger.TriggerInstance.obtainedPikminOfType(PikminType.ROCK))
                    .requirements(AdvancementRequirements.Strategy.AND)
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "all_pikmin_types"), existingFileHelper);

            AdvancementHolder pikminFlower = Advancement.Builder.advancement()
                    .parent(firstPikmin)
                    .display(
                            ModBlocks.NECTAR_EGG_BLOCK.get().asItem(),
                            Component.translatable("advancement.wilispikmins.pikmin_flower.title"),
                            Component.translatable("advancement.wilispikmins.pikmin_flower.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("grow_to_flower",
                            PikminGrowTrigger.TriggerInstance.grewToStage(GrowthStage.FLOWER))
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "pikmin_flower"), existingFileHelper);

                    // derrotar un enemigo con pikmin
            AdvancementHolder defeatEnemy = Advancement.Builder.advancement()
                    .parent(firstPikmin)
                    .display(
                            Items.DIAMOND_SWORD,
                            Component.translatable("advancement.wilispikmins.defeat_enemy.title"),
                            Component.translatable("advancement.wilispikmins.defeat_enemy.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("defeat_enemy",
                            KillWithPikminTrigger.TriggerInstance.killWithPikmin())
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "pikmin_defeat_enemy"), existingFileHelper);

                    // recolectar 100 pikmin en total
            AdvancementHolder hundredPikmin = Advancement.Builder.advancement()
                    .parent(allPikminTypes)
                    .display(
                            ModItems.MAIN_ONION.get().asItem(),
                            Component.translatable("advancement.wilispikmins.hundred_pikmin.title"),
                            Component.translatable("advancement.wilispikmins.hundred_pikmin.description"),
                            null,
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            false
                    )
                    .addCriterion("hundred_pikmin",
                            TotalPikminTrigger.TriggerInstance.totalPikmin(100))
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "hundred_pikmin"), existingFileHelper);
        }
    }
}
