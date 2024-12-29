package com.c2h6s.etstlib.mixin.AEMixin;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.blockentity.misc.ChargerBlockEntity;
import appeng.util.inv.AppEngInternalInventory;
import com.c2h6s.etstlib.register.EtSTLibModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin(value = ChargerBlockEntity.class,remap = false)
public class ChargerBlockEntityMixin {
    @Final
    @Shadow private AppEngInternalInventory inv;
    @Shadow private boolean working;

    @Inject(at = @At(value = "TAIL"),method = "activate")
    public void insertTool(Player player, CallbackInfo ci){
        if (inv.getStackInSlot(0).isEmpty()){
            ItemStack held = player.getInventory().getSelected();
            if (held.getItem() instanceof IModifiable) {
                ToolStack tool = ToolStack.from(held);
                if (tool.getDamage()>0&&tool.getModifierLevel(EtSTLibModifier.applied_fixing.get())>0) {
                    held = player.getInventory().removeItem(player.getInventory().selected, 1);
                    this.inv.setItemDirect(0, held);
                }
            }
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lappeng/util/inv/AppEngInternalInventory;getStackInSlot(I)Lnet/minecraft/world/item/ItemStack;"),method = "doWork")
    public void repairTool(int ticksSinceLastCall, CallbackInfo ci){
        ChargerBlockEntity entity = (ChargerBlockEntity) (Object) this;
        ItemStack stack = inv.getStackInSlot(0);
        if (!stack.isEmpty()){
            if (stack.getItem() instanceof IModifiable&& ToolStack.from(stack).getModifierLevel(EtSTLibModifier.applied_fixing.get())>0){
                ToolStack tool = ToolStack.from(stack);
                int modifierLevel = tool.getModifierLevel(EtSTLibModifier.applied_fixing.get());
                int maxRepair = (int) Math.min(entity.getAEMaxPower()/100,tool.getDamage());
                maxRepair = Math.min(maxRepair,modifierLevel*4);
                int require = maxRepair*100;
                if (maxRepair>0){
                    tool.setDamage(tool.getDamage()-maxRepair);
                    entity.extractAEPower(require, Actionable.MODULATE, PowerMultiplier.CONFIG);
                    working=true;
                    entity.markForUpdate();
                }
            }
        }
    }


}
