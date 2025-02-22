package com.hypherionmc.craterlib.mixin.events;

import com.hypherionmc.craterlib.api.event.server.CraterAdvancementEvent;
import com.hypherionmc.craterlib.core.event.CraterEventBus;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {

    @Shadow private ServerPlayer player;

    @Inject(method = "award", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/AdvancementRewards;grant(Lnet/minecraft/server/level/ServerPlayer;)V", shift = At.Shift.AFTER))
    private void injectAdvancementEvent(Advancement advancement, String $$1, CallbackInfoReturnable<Boolean> cir) {
        CraterAdvancementEvent event = new CraterAdvancementEvent(this.player, advancement);

        if (advancement.getDisplay() != null && advancement.getDisplay().shouldAnnounceChat()) {
            CraterEventBus.INSTANCE.postEvent(event);
        }
    }
}
