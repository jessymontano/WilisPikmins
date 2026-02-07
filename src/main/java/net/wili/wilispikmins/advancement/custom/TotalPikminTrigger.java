package net.wili.wilispikmins.advancement.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.wili.wilispikmins.advancement.ModTriggers;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TotalPikminTrigger extends SimpleCriterionTrigger<TotalPikminTrigger.TriggerInstance> {

    @Override
    public @NotNull Codec<TotalPikminTrigger.TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, int count) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(count));
    }

    public record TriggerInstance(
            Optional<ContextAwarePredicate> player,
            Optional<Integer> count
    ) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                triggerInstanceInstance -> triggerInstanceInstance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                        Codec.INT.optionalFieldOf("count").forGetter(TriggerInstance::count)
                ).apply(triggerInstanceInstance, TriggerInstance::new)
        );

        public boolean matches(int currentCount) {
            return this.count.isEmpty() || currentCount >= this.count.get();
        }

        public static Criterion<TriggerInstance> totalPikmin(int count) {
            return ModTriggers.TOTAL_PIKMIN.get().createCriterion(
                    new TriggerInstance(Optional.empty(), Optional.of(count))
            );
        }

        @Override
        public @NotNull Optional<ContextAwarePredicate> player() {
            return player;
        }
    }
}
