package net.wili.wilispikmins.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wili.wilispikmins.WilisPikmins;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, WilisPikmins.MOD_ID);

    public static final Supplier<SimpleParticleType> SOUL_PARTICLES =
            PARTICLE_TYPES.register("soul_particles", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> ROCK_SOUL_PARTICLES =
            PARTICLE_TYPES.register("rock_soul_particles", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> WINGED_SOUL_PARTICLES =
            PARTICLE_TYPES.register("winged_soul_particles", () -> new SimpleParticleType(false));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
