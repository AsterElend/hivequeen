package aster.hivequeen.mixin;

import aster.hivequeen.api.ToggleableOvercastEnvironment;
import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static at.petrak.hexcasting.api.HexAPI.modLoc;


@Mixin (value = PlayerBasedCastEnv.class, remap = false)
public abstract class OvercastCancelMixin implements ToggleableOvercastEnvironment {


    @Shadow
    public abstract ServerPlayerEntity getCaster();

    @Unique
    @Override
    public void hivequeen$setSuppressed(Boolean bool){
        overcastingSuppressed = bool;
    }

    @Unique
    @Override
    public boolean hivequeen$querySuppressed() {return overcastingSuppressed;}


    @Unique boolean overcastingSuppressed = false;



    @Inject(method = "canOvercast", at = @At("HEAD"), cancellable = true)
    private void hivequeen$canOvercast(CallbackInfoReturnable<Boolean> callback) {
        if (overcastingSuppressed) {
            callback.setReturnValue(false);
            callback.cancel();
        }
    }

    @Inject(method = "postExecution", at = @At("TAIL"))
    private void hivequeen$resetOvercastStatus(CastResult result, CallbackInfo ci){
        overcastingSuppressed = false;
    }

}