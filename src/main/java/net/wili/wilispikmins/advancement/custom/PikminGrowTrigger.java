package net.wili.wilispikmins.advancement.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.wili.wilispikmins.advancement.ModTriggers;
import net.wili.wilispikmins.entity.custom.enums.GrowthStage;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PikminGrowTrigger extends SimpleCriterionTrigger<PikminGrowTrigger.TriggerInstance> {
    @Override
    public @NotNull Codec<PikminGrowTrigger.TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, GrowthStage stage) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(stage));
    }


    public record TriggerInstance(
            Optional<ContextAwarePredicate> player,
            Optional<GrowthStage> stage
    ) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
            triggerInstanceInstance -> triggerInstanceInstance.group(
                    EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                    GrowthStage.CODEC.optionalFieldOf("stage").forGetter(TriggerInstance::stage)
            ).apply(triggerInstanceInstance, TriggerInstance::new)
        );

        public boolean matches(GrowthStage stage) {
            return this.stage.isEmpty() || this.stage.get() == stage;
        }

        public static Criterion<TriggerInstance> grewToStage(GrowthStage stage) {
            return ModTriggers.PIKMIN_GROW.get().createCriterion(
                    new TriggerInstance(Optional.empty(), Optional.of(stage))
            );
        }

        @Override
        public @NotNull Optional<ContextAwarePredicate> player() {
            return player;
        }
    }
}
