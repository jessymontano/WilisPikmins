package net.wili.wilispikmins.sound;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wili.wilispikmins.WilisPikmins;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, WilisPikmins.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> PIKMIN_DEATH = registerSoundEvents("pikmin_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> PIKMIN_GREETING = registerSoundEvents("pikmin_greeting");
    public static final DeferredHolder<SoundEvent, SoundEvent> PIKMIN_JOIN_SQUAD = registerSoundEvents("pikmin_join_squad");
    public static final DeferredHolder<SoundEvent, SoundEvent> PIKMIN_SCREAM = registerSoundEvents("pikmin_scream");
    public static final DeferredHolder<SoundEvent, SoundEvent> PIKMIN_DROWNING = registerSoundEvents("pikmin_drowning");
    public static final DeferredHolder<SoundEvent, SoundEvent> PIKMIN_ENTER_IDLE = registerSoundEvents("pikmin_enter_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> PIKMIN_ATTACK = registerSoundEvents("pikmin_attack");
    public static final DeferredHolder<SoundEvent, SoundEvent> PIKMIN_DRINK_NECTAR = registerSoundEvents("pikmin_drink_nectar");
    public static final DeferredHolder<SoundEvent, SoundEvent> PIKMIN_PLUCK = registerSoundEvents("pikmin_pluck");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
