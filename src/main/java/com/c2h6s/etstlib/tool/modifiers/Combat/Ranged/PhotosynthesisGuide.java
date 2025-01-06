package com.c2h6s.etstlib.tool.modifiers.Combat.Ranged;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.ProjectileTickModifierHook;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.ParticleChainUtil;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import java.util.List;

import static com.c2h6s.etstlib.util.EntityInRangeUtil.*;

public class PhotosynthesisGuide extends EtSTBaseModifier implements ProjectileTickModifierHook {
    public static final ResourceLocation HOMING_TICK =EtSTLib.getResourceLocation("homing_tick");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, EtSTLibHooks.PROJECTILE_TICK);
    }

    @Override
    public void onProjectileTick(ModifierNBT modifiers, ModifierEntry entry, Level level, @NotNull Projectile projectile, NamespacedNBT persistentData, boolean hasBeenShot, boolean leftOwner) {

    }

    @Override
    public void onArrowTick(ModifierNBT modifiers, ModifierEntry entry, Level level, @NotNull AbstractArrow arrow, NamespacedNBT persistentData, boolean hasBeenShot, boolean leftOwner,boolean inGround, @Nullable IntOpenHashSet piercingIgnoreEntityIds) {
        if (hasBeenShot&&leftOwner&&level instanceof ServerLevel serverLevel&&!inGround){
            double velocity = arrow.getDeltaMovement().length();
            IntOpenHashSet ignored = new IntOpenHashSet();
            if (arrow.getOwner()!=null) ignored.add(arrow.getOwner().getId());
            if (piercingIgnoreEntityIds!=null) ignored.addAll(piercingIgnoreEntityIds);
            LivingEntity target = getNearestLivingEntity(arrow,2.5f+entry.getLevel()*0.5f,ignored,List.of());
            if (target!=null) {
                persistentData.putInt(HOMING_TICK,persistentData.getInt(HOMING_TICK)+1);
                Vec3 movementDirection = arrow.getDeltaMovement().normalize().scale(1.0f/(0.5+0.5*persistentData.getInt(HOMING_TICK)));
                ParticleChainUtil.summonParticleChain(arrow.position(),arrow.position().subtract(arrow.getDeltaMovement()),ParticleTypes.GLOW,0.1,128,serverLevel,1,0.01,0.01,0.01,0);
                Vec3 chasingAccelerate = new Vec3(target.getX() - arrow.getX(), target.getY()+target.getBbHeight()*0.5-arrow.getY()-arrow.getBbHeight()*0.5,target.getZ()-arrow.getZ()).normalize().scale(1+0.25*entry.getLevel());
                Vec3 newMovement = movementDirection.add(chasingAccelerate).normalize().scale(velocity);
                arrow.setDeltaMovement(newMovement);
            }
        }
    }
}
