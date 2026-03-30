package net.wili.wilispikmins.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoulParticles extends TextureSheetParticle {
    protected SoulParticles(ClientLevel level, double x, double y, double z, SpriteSet sprite) {
        super(level, x, y, z, 0, 0, 0);

        this.friction = 0.96F;
        this.speedUpWhenYMotionIsBlocked = true;

        this.xd = (this.random.nextDouble() * 2.0D - 1.0D) * 0.02D;
        this.yd = 0.1D + this.random.nextDouble() * 0.05D;
        this.zd = (this.random.nextDouble() * 2.0D - 1.0D) * 0.02D;

        this.quadSize *= 1.2F;
        this.lifetime = 60 + (this.random.nextInt(20));
        this.setSpriteFromAge(sprite);

        this.hasPhysics = false;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();

        this.alpha = 1.0F - ((float) this.age / (float) this.lifetime);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SoulParticles particles = new SoulParticles(clientLevel, x, y, z, this.spriteSet);
            particles.setColor((float) xSpeed, (float) ySpeed, (float) zSpeed);

            return particles;
        }
    }
}
