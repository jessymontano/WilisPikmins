package net.wili.wilispikmins.advancement.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.wili.wilispikmins.advancement.ModTriggers;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PikminObtainedTrigger extends SimpleCriterionTrigger<PikminObtainedTrigger.TriggerInstance> {
    @Override
    public @NotNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, PikminType type) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(type));
    }

    public record TriggerInstance(
            Optional<ContextAwarePredicate> player,
            Optional<PikminType> type
    ) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                triggerInstanceInstance -> triggerInstanceInstance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                        PikminType.CODEC.optionalFieldOf("type").forGetter(TriggerInstance::type)
                ).apply(triggerInstanceInstance, TriggerInstance::new)
        );

        public boolean matches(PikminType type) {
            return this.type.isEmpty() || this.type.get() == type;
        }

        public static Criterion<TriggerInstance> obtainedAnyPikmin() {
            return ModTriggers.PIKMIN_OBTAINED.get().createCriterion(
                    new TriggerInstance(Optional.empty(), Optional.empty())
            );
        }

        public static Criterion<TriggerInstance> obtainedPikminOfType(PikminType type) {
            return ModTriggers.PIKMIN_OBTAINED.get().createCriterion(
                    new TriggerInstance(Optional.empty(), Optional.of(type))
            );
        }

        @Override
        public @NotNull Optional<ContextAwarePredicate> player() {
            return player;
        }
    }
}
