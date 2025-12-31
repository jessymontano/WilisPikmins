package net.wili.wilispikmins.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wili.wilispikmins.WilisPikmins;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WilisPikmins.MOD_ID);

    public static final RegistryObject<SoundEvent> PIKMIN_DEATH = registerSoundEvents("pikmin_death");
    public static final RegistryObject<SoundEvent> PIKMIN_GREETING = registerSoundEvents("pikmin_greeting");
    public static final RegistryObject<SoundEvent> PIKMIN_JOIN_SQUAD = registerSoundEvents("pikmin_join_squad");
    public static final RegistryObject<SoundEvent> PIKMIN_SCREAM = registerSoundEvents("pikmin_scream");
    public static final RegistryObject<SoundEvent> PIKMIN_DROWNING = registerSoundEvents("pikmin_drowning");
    public static final RegistryObject<SoundEvent> PIKMIN_ENTER_IDLE = registerSoundEvents("pikmin_enter_idle");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WilisPikmins.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
