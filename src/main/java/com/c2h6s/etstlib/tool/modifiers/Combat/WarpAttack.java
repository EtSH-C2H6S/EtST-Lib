package com.c2h6s.etstlib.tool.modifiers.Combat;

import com.c2h6s.etstlib.mixin.ProjectileInvoker;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.ProjectileTickModifierHook;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.EntityInRangeUtil;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.List;

public class WarpAttack extends EtSTBaseModifier implements ProjectileTickModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, EtSTLibHooks.PROJECTILE_TICK);
    }

    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {
        if (player!=null&&!level.isClientSide) {
            LivingEntity living = EntityInRangeUtil.getNearestLivingEntity(player, entry.getLevel() + 4, new IntOpenHashSet(), List.of());
            if (living!=null) ToolAttackUtil.attackEntity(tool,player,living);
        }
    }

    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        if (player!=null&&!level.isClientSide) {
            LivingEntity living = EntityInRangeUtil.getNearestLivingEntity(player, entry.getLevel() + 4, new IntOpenHashSet(), List.of());
            if (living!=null) ToolAttackUtil.attackEntity(tool,player,living);
        }
    }


    @Override
    public void onProjectileTick(ModifierNBT modifiers, ModifierEntry entry, Level level, @NotNull Projectile projectile, ModDataNBT persistentData, boolean hasBeenShot, boolean leftOwner) {
    }

    @Override
    public void onArrowTick(ModifierNBT modifiers, ModifierEntry entry, Level level, @NotNull AbstractArrow arrow, ModDataNBT persistentData, boolean hasBeenShot, boolean leftOwner, boolean inGround, @Nullable IntOpenHashSet piercingIgnoreEntityIds) {
        if (level.isClientSide) return;
        if (inGround||!hasBeenShot||!leftOwner){
            return;
        }
        if (piercingIgnoreEntityIds==null){
            piercingIgnoreEntityIds = new IntOpenHashSet();
        }
        if (arrow.getOwner()!=null){
            piercingIgnoreEntityIds.add(arrow.getOwner().getId());
        }
        LivingEntity living = EntityInRangeUtil.getNearestLivingEntity(arrow, entry.getLevel() + 2.5F,piercingIgnoreEntityIds, List.of(Player.class, ServerPlayer.class, FakePlayer.class));
        if (living!=null){
            EntityHitResult hitResult = new EntityHitResult(living);
            ((ProjectileInvoker)arrow).onHit(hitResult);
        }
    }
}
