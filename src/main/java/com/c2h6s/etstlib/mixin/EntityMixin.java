package com.c2h6s.etstlib.mixin;

import com.c2h6s.etstlib.content.misc.vibration.ToolVibrationAcceptor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(at = @At("HEAD"),method = "updateDynamicGameEventListener")
    public void addVibrationListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> pListenerConsumer, CallbackInfo ci){
        Entity entity = (Entity) (Object) this;
        if (entity instanceof ServerPlayer player&& ToolVibrationAcceptor.acceptorMap.get(player)!=null&&player.level() instanceof ServerLevel level){
            ToolVibrationAcceptor.acceptorMap.get(player).forEach((acceptor -> {
                pListenerConsumer.accept(new DynamicGameEventListener<>(acceptor.getListener()),level);
            }));
        }
    }
}
