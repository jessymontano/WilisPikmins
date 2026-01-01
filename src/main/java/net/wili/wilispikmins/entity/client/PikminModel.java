package net.wili.wilispikmins.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.GrowthStage;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class PikminModel extends GeoModel<PikminEntity> {
    @Override
    public ResourceLocation getModelResource(PikminEntity entity) {
        if (entity == null) {
            return new ResourceLocation(WilisPikmins.MOD_ID, "geo/pikmin.geo.json");
        }
        return switch (entity.getPikminType()) {
            case PURPLE -> new ResourceLocation(
                    WilisPikmins.MOD_ID,
                    "geo/purple_pikmin.geo.json"
            );
            case ROCK -> new ResourceLocation(
                    WilisPikmins.MOD_ID,
                    "geo/rock_pikmin.geo.json"
            );
            default -> new ResourceLocation(
                    WilisPikmins.MOD_ID,
                    "geo/pikmin.geo.json"
            );
        };
    }

    @Override
    public ResourceLocation getTextureResource(PikminEntity entity) {
        if (entity == null) return getDefaultTexture();

        PikminType type = entity.getPikminType();

        return new ResourceLocation(WilisPikmins.MOD_ID,
                "textures/entity/" + type.getName().toLowerCase() + "_pikmin.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PikminEntity pikmin) {
        return new ResourceLocation(WilisPikmins.MOD_ID,
                "animations/pikmin.animation.json");
    }

    private ResourceLocation getDefaultTexture() {
        return new ResourceLocation(WilisPikmins.MOD_ID,
                "textures/entity/red_pikmin.png");
    }

    @Override
    public void setCustomAnimations(PikminEntity animatable, long instanceId, AnimationState<PikminEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoModel model = (GeoModel) this;
        var animationProcessor = model.getAnimationProcessor();

        if (animationProcessor != null) {
            GrowthStage stage = animatable.getGrowthStage();

            var leafBone = animationProcessor.getBone("leaf");
            var budBone = animationProcessor.getBone("bud");
            var flowerBone = animationProcessor.getBone("flower");

            if (leafBone != null) {
                leafBone.setHidden(stage != GrowthStage.LEAF);
            }
            if (budBone != null) {
                budBone.setHidden(stage != GrowthStage.BUD);
            }
            if (flowerBone != null) {
                flowerBone.setHidden(stage != GrowthStage.FLOWER);
            }
        }
    }
}
