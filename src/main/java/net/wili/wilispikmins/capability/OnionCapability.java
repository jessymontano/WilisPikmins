package net.wili.wilispikmins.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class OnionCapability {
    public static final Capability<IOnionData> ONION_DATA =
            CapabilityManager.get(new CapabilityToken<>() {});
}
