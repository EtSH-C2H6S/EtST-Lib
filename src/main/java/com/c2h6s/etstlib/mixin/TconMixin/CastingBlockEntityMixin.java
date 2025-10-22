package com.c2h6s.etstlib.mixin.TconMixin;

import com.c2h6s.etstlib.api.interfaces.IConditionalSpeedCastingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import slimeknights.tconstruct.library.recipe.casting.ICastingRecipe;
import slimeknights.tconstruct.smeltery.block.entity.CastingBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.tank.CastingFluidHandler;

@Mixin(value = CastingBlockEntity.class,remap = false)
public class CastingBlockEntityMixin {
    @Shadow private int coolingTime;

    @Shadow private int timer;

    @Shadow private ICastingRecipe currentRecipe;

    @Shadow @Final private CastingFluidHandler tank;

    @Inject(method = "lambda$loadRecipe$3",at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/recipe/casting/ICastingRecipe;getCoolingTime(Lslimeknights/tconstruct/library/recipe/casting/ICastingContainer;)I",shift = At.Shift.AFTER))
    public void modifyCoolingTime(FluidStack fluid, ICastingRecipe recipe, CallbackInfo ci){
        if ((CastingBlockEntity)(Object) this instanceof IConditionalSpeedCastingBlockEntity entity){
            coolingTime =Math.max(entity.modifyTotalCoolingTime(fluid,recipe,coolingTime),0);
        }
    }

    @ModifyArg(method = "onContentsChanged",at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(II)I"),index = 1)
    public int modifyCoolingTime(int a){
        if ((CastingBlockEntity)(Object) this instanceof IConditionalSpeedCastingBlockEntity entity){
            return entity.modifyTotalCoolingTime(tank.getFluid(),currentRecipe,a);
        }
        return a;
    }

    @Inject(method = "serverTick",at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/smeltery/block/entity/tank/CastingFluidHandler;getFluid()Lnet/minecraftforge/fluids/FluidStack;",shift = At.Shift.BY,by = 3))
    public void increaseProcessSpeed(Level level, BlockPos pos, CallbackInfo ci){
        if ((CastingBlockEntity)(Object) this instanceof IConditionalSpeedCastingBlockEntity entity){
            timer+=entity.getBoost(coolingTime);
        }
    }
}
