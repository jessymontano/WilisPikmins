package net.wili.wilispikmins.data;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.wili.wilispikmins.WilisPikmins;

import java.util.function.Supplier;

public class OnionComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, WilisPikmins.MOD_ID);

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, WilisPikmins.MOD_ID);

    public static final Supplier<DataComponentType<OnionData>> ONION_DATA =
            DATA_COMPONENT_TYPES.register("onion_data",
                    () -> DataComponentType.<OnionData>builder()
                            .persistent(OnionData.CODEC)
                            .networkSynchronized(OnionData.STREAM_CODEC)
                            .build());

    public static final Supplier<AttachmentType<OnionData>> PLAYER_ONION_DATA =
            ATTACHMENT_TYPES.register("player_onion_data",
                    () -> AttachmentType.builder(OnionData::new)
                            .serialize(OnionData.CODEC)
                            .copyOnDeath()
                            .build());
}
