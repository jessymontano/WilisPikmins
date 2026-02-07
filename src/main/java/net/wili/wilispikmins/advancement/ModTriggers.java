package net.wili.wilispikmins.advancement;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.advancement.custom.*;

import java.util.function.Supplier;

public class ModTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS =
            DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, WilisPikmins.MOD_ID);

    public static final Supplier<PikminObtainedTrigger> PIKMIN_OBTAINED =
            TRIGGERS.register("pikmin_obtained", PikminObtainedTrigger::new);
    public static final Supplier<PikminGrowTrigger> PIKMIN_GROW =
            TRIGGERS.register("pikmin_grow", PikminGrowTrigger::new);
    public static final Supplier<KillWithPikminTrigger> KILL_WITH_PIKMIN =
            TRIGGERS.register("kill_with_pikmin", KillWithPikminTrigger::new);
    public static final Supplier<TotalPikminTrigger> TOTAL_PIKMIN =
            TRIGGERS.register("total_pikmin", TotalPikminTrigger::new);
    public static final Supplier<OnionInteractTrigger> ONION_INTERACT =
            TRIGGERS.register("onion_interact", OnionInteractTrigger::new);

    public static void register(IEventBus modEventBus) {
        TRIGGERS.register(modEventBus);
    }
}
