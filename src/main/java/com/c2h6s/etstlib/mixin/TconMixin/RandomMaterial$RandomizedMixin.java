package com.c2h6s.etstlib.mixin.TconMixin;

import com.c2h6s.etstlib.mixinUtil.MixinTemp;
import com.c2h6s.etstlib.util.EtSTLibTags;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.json.IntRange;
import slimeknights.tconstruct.library.materials.IMaterialRegistry;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;

import java.util.List;

import static com.c2h6s.etstlib.mixinUtil.MixinTemp.statType;
/*
@Mixin(remap = false,targets ="slimeknights.tconstruct.library.materials.RandomMaterial$Randomized" )
public class RandomMaterial$RandomizedMixin {
    @Final
    @Shadow private IntRange tier;
    @Final
    @Shadow private boolean allowHidden;
    @Final
    @Shadow private TagKey<IMaterial> tag;
    @Inject(method = "apply(Lslimeknights/tconstruct/library/materials/stats/MaterialStatsId;)Ljava/util/List;",at = @At("HEAD"))
    private void getStatType(MaterialStatsId statType, CallbackInfoReturnable<List<MaterialId>> cir){
        MixinTemp.statType = statType;
    }

    @ModifyVariable(method = "apply(Lslimeknights/tconstruct/library/materials/stats/MaterialStatsId;)Ljava/util/List;",at = @At("STORE"))
    private List<MaterialId> modifyChoices(List<MaterialId> list){
        IMaterialRegistry registry = MaterialRegistry.getInstance();
        return MaterialRegistry.getInstance().getAllMaterials().stream().filter((material) -> {
            MaterialId id = material.getIdentifier();
            boolean b = (registry.getTagValues(EtSTLibTags.WHITELISTED_RANDOM_MATERIAL).isEmpty()&&!registry.isInTag(id,EtSTLibTags.BLACKLISTED_RANDOM_MATERIAL))||registry.isInTag(id,EtSTLibTags.WHITELISTED_RANDOM_MATERIAL);
            return tier.test(material.getTier())&& b && (this.allowHidden || !material.isHidden()) && (this.tag == null || registry.isInTag(id, this.tag)) && registry.getMaterialStats(id, statType).isPresent();
        }).map(IMaterial::getIdentifier).toList();
    }
}
 */
