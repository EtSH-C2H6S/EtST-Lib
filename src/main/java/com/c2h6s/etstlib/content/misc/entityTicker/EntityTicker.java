package com.c2h6s.etstlib.content.misc.entityTicker;

import com.c2h6s.etstlib.content.register.EtSTLibRegistries;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

//EntityTicker的基础类，你的Ticker需要继承这个
public abstract class EntityTicker {
    private ResourceLocation id = null;
    @Getter
    @Setter
    private boolean infinite = false;
    public ResourceLocation getId(){
        if (this.id==null) this.id = EtSTLibRegistries.ENTITY_TICKER_REGISTRY.getKey(this);
        return this.id;
    }
    public static EntityTicker fromId(ResourceLocation id){
        return EtSTLibRegistries.ENTITY_TICKER_REGISTRY.getValue(id);
    }
    //当实体即将被tick时被调用，返回值表示实体是否被tick（返回false会把实体停住）
    public boolean tick(int duration,int level,Entity entity){
        return true;
    }
}
