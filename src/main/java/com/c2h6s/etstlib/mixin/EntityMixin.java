package com.c2h6s.etstlib.mixin;

import com.c2h6s.etstlib.content.misc.entityTicker.EntityTickerManager;
import com.c2h6s.etstlib.content.misc.vibration.ToolVibrationListener;
import com.c2h6s.etstlib.entity.specialDamageSources.interfaces.IConditionalHurt;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.GameEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiConsumer;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(at = @At("HEAD"),method = "updateDynamicGameEventListener")
    public void addVibrationListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> pListenerConsumer, CallbackInfo ci){
        Entity entity = (Entity) (Object) this;
        if (entity instanceof ServerPlayer player&& player.level() instanceof ServerLevel level){
            ToolVibrationListener.validate(player);
            if(ToolVibrationListener.listenerMap.get(player)!=null){
                pListenerConsumer.accept(new DynamicGameEventListener<GameEventListener>(ToolVibrationListener.listenerMap.get(player).getListener()),level);
            }
        }
    }
    @Inject(method = "isInvulnerableTo",at = @At("HEAD"), cancellable = true)
    public void cancelHurt(DamageSource pSource, CallbackInfoReturnable<Boolean> cir){
        Entity entity = (Entity) (Object) this;
        if(pSource instanceof IConditionalHurt conditional&&conditional.canHurtEntity(entity)!=null){
            cir.setReturnValue(conditional.canHurtEntity(entity));
        }
    }
    @Inject(method = "setRemoved",at = @At("HEAD"))
    public void removeFromTickerMap(Entity.RemovalReason pRemovalReason, CallbackInfo ci){
        Entity entity = (Entity) (Object) this;
        EntityTickerManager.TICKER_MAP.remove(entity);
    }

}
