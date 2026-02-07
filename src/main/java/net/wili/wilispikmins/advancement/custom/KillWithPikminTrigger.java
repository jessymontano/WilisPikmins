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

public class KillWithPikminTrigger extends SimpleCriterionTrigger<KillWithPikminTrigger.TriggerInstance> {
    @Override
    public @NotNull Codec<KillWithPikminTrigger.TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, TriggerInstance::matches);
    }

    public record TriggerInstance(
            Optional<ContextAwarePredicate> player
    ) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
            triggerInstanceInstance -> triggerInstanceInstance.group(
                    EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player)
            ).apply(triggerInstanceInstance, TriggerInstance::new)
        );

        public boolean matches() {
            return true;
        }

        public static Criterion<TriggerInstance> killWithPikmin() {
            return ModTriggers.KILL_WITH_PIKMIN.get().createCriterion(
                    new TriggerInstance(Optional.empty())
            );
        }

        @Override
        public @NotNull Optional<ContextAwarePredicate> player() {
            return player;
        }
    }
}
