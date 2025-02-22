package com.hypherionmc.craterlib.common.particles;

import net.minecraft.core.particles.SimpleParticleType;

/**
 * @author HypherionSA
 * Helper Class for exposing a hidden constructor in the vanilla particle type
 */
public class WrappedSimpleParticleType extends SimpleParticleType {

    public WrappedSimpleParticleType(boolean alwaysShow) {
        super(alwaysShow);
    }
}
