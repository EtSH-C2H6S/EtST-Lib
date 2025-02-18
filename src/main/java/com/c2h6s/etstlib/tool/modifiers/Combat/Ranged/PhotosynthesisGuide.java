package com.c2h6s.etstlib.tool.modifiers.Combat.Ranged;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.ProjectileTickModifierHook;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.ParticleChainUtil;
import com.c2h6s.etstlib.util.ProjectileUtil;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.List;

import static com.c2h6s.etstlib.util.EntityInRangeUtil.*;

public class PhotosynthesisGuide extends EtSTBaseModifier implements ProjectileTickModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, EtSTLibHooks.PROJECTILE_TICK);
    }

    @Override
    public void onProjectileTick(ModifierNBT modifiers, ModifierEntry entry, Level level, @NotNull Projectile projectile, ModDataNBT persistentData, boolean hasBeenShot, boolean leftOwner) {

    }

    @Override
    public void onArrowTick(ModifierNBT modifiers, ModifierEntry entry, Level level, @NotNull AbstractArrow arrow, ModDataNBT persistentData, boolean hasBeenShot, boolean leftOwner, boolean inGround, @Nullable IntOpenHashSet piercingIgnoreEntityIds) {
        if (inGround||!hasBeenShot||!leftOwner){
            return;
        }
        if (level instanceof ServerLevel serverLevel){
            IntOpenHashSet ignored = new IntOpenHashSet();
            if (arrow.getOwner()!=null) ignored.add(arrow.getOwner().getId());
            if (piercingIgnoreEntityIds!=null) ignored.addAll(piercingIgnoreEntityIds);
            LivingEntity target = getNearestLivingEntity(arrow,2.5f+entry.getLevel()*0.5f,ignored,List.of(Player.class, ServerPlayer.class, FakePlayer.class));
            if (target!=null) {
                ParticleChainUtil.summonParticleChain(arrow.position(),arrow.position().subtract(arrow.getDeltaMovement()), ParticleTypes.GLOW,0.1,128,serverLevel,1,0.01,0.01,0.01,0);
                ProjectileUtil.homingToward(arrow,target,entry.getLevel()*0.5f,2.5f+entry.getLevel()*0.5f);
            }
        }
    }
}
