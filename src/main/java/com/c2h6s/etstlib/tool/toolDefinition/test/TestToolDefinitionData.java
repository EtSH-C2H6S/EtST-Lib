package com.c2h6s.etstlib.tool.toolDefinition.test;

import com.c2h6s.etstlib.EtSTLib;
import com.c2h6s.etstlib.tool.toolDefinition.ModifiableToolDefinition;
import com.c2h6s.etstlib.tool.toolDefinition.ModifiableToolDefinitionData;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.library.materials.RandomMaterial;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.definition.module.ToolHooks;
import slimeknights.tconstruct.library.tools.definition.module.material.DefaultMaterialsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.PartStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.mining.IsEffectiveToolHook;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerToolParts;

//测试用的一个ToolDefinitionData，可以提供部分例子
public class TestToolDefinitionData extends ModifiableToolDefinitionData implements IsEffectiveToolHook {

    @Override
    public void registerHook(ModuleHookMap.Builder builder) {
        super.registerHook(builder);
        //这两个Module分别是部件和默认材质的，用Module比实现Hook方便
        builder.addModule(PartStatsModule.parts()
                .part(TinkerToolParts.pickHead)
                .part(TinkerToolParts.toughHandle).
                primaryPart(0)
                .build()
        );
        builder.addModule(DefaultMaterialsModule.builder()
                .material(RandomMaterial.random().tier(1).build())
                .material(RandomMaterial.random().tier(1).build())
                .build()
        );
        //和Modifier类似的addHook
        builder.addHook(this, ToolHooks.IS_EFFECTIVE);
    }

    @Override
    public boolean isToolEffective(IToolStackView iToolStackView, BlockState blockState) {
        return true;
    }


    public static final DeferredRegister<Item> TEST_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, EtSTLib.MODID);

    public static final RegistryObject<ModifiableItem> TEST_TOOL = TEST_REGISTER.register("test_tool",()->new ModifiableItem(
            new Item.Properties().stacksTo(1),
            new ModifiableToolDefinition(
                    EtSTLib.getResourceLocation("test_definition"),
                    new TestToolDefinitionData()
            ))
    );
}
