package com.c2h6s.etstlib.content.misc.entityTicker;

import com.c2h6s.etstlib.EtSTLibConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

//EntityTicker的总控，可以很方便的来添加/减少Ticker。
public class EntityTickerManager {
    public static final ConcurrentHashMap<UUID, ConcurrentHashMap<EntityTicker,EntityTickerInstance>> TICKER_MAP = new ConcurrentHashMap<>();

    public static EntityTickerManagerInstance getInstance(Entity entity){
        return new EntityTickerManagerInstance(entity);
    }

    public static boolean tick(Entity entity){
        if (entity instanceof Player&&!EtSTLibConfig.ALLOW_TICKER_ON_PLAYER.get()) return true;
        if (entity.getPersistentData().contains("etstlib_tickers")){
            load(entity);
        }
        if (!TICKER_MAP.containsKey(entity.getUUID())) return true;
        if (TICKER_MAP.get(entity.getUUID())==null||TICKER_MAP.get(entity.getUUID()).isEmpty()){
            TICKER_MAP.remove(entity.getUUID());
            return true;
        }
        boolean doTick = true;
        EntityTickerManagerInstance managerInstance = new EntityTickerManagerInstance(entity);
        Map<EntityTicker,EntityTickerInstance> instances = managerInstance.instanceMap;
        List<EntityTickerInstance> instancesCopy = List.copyOf(managerInstance.instanceMap.values());
        for (EntityTickerInstance instance:instancesCopy){
            EntityTicker ticker = instance.ticker;
            if (!ticker.isInfinite()) instance.duration--;
            doTick = doTick && ticker.tick(instance.duration,instance.level,entity);
            if (instance.duration>0) managerInstance.setTicker(instance);
            else managerInstance.removeTicker(ticker);
        }
        return doTick;
    }
    public static void saveAll(MinecraftServer server){
        server.getAllLevels().forEach(serverLevel ->
                TICKER_MAP.keySet().forEach(uuid -> save(uuid,serverLevel)));

    }

    public static void checkInvalid(MinecraftServer server){
        var uuids = List.copyOf(TICKER_MAP.keySet());
        for (UUID uuid:uuids){
            boolean validEntity = false;
            for (ServerLevel serverLevel:server.getAllLevels()) {
                var entity = serverLevel.getEntity(uuid);
                if (entity != null && !entity.isRemoved()) {
                    validEntity=true;
                    break;
                }
            }
            if (!validEntity) TICKER_MAP.remove(uuid);
        }
    }

    public static void removeEntity(Entity entity){
        TICKER_MAP.remove(entity.getUUID());
    }

    public static void load(Entity entity){
        CompoundTag nbt = entity.getPersistentData().getCompound("etstlib_tickers");
        ConcurrentHashMap<EntityTicker,EntityTickerInstance> instances = new ConcurrentHashMap<>();
        if (!nbt.isEmpty()){
            nbt.getAllKeys().forEach(string -> {
                if (nbt.contains(string, CompoundTag.TAG_COMPOUND)){
                    EntityTickerInstance instance = EntityTickerInstance.readFromNbt(nbt.getCompound(string),new ResourceLocation(string));
                    if (instance!=null) instances.put(instance.ticker,instance);
                }
            });
        }
        TICKER_MAP.put(entity.getUUID(),instances);
        entity.getPersistentData().remove("etstlib_tickers");
    }
    public static void save(UUID uuid, ServerLevel level){
        if (uuid==null) return;
        var entity = level.getEntity(uuid);
        CompoundTag nbt = new CompoundTag();
        if (TICKER_MAP.get(uuid)!=null&&entity!=null) {
            TICKER_MAP.get(uuid).values().forEach(instance -> instance.writeToNbt(nbt));
            entity.getPersistentData().put("etstlib_tickers", nbt);
        }
    }

    public static class EntityTickerManagerInstance {
        public final @NotNull Map<EntityTicker,EntityTickerInstance> instanceMap;
        public final Entity entity;
        //为实体创建ManagerInstance，不需要从总的表去再获取。
        public EntityTickerManagerInstance(Entity entity){
            this.entity = entity;
            TICKER_MAP.computeIfAbsent(entity.getUUID(), k -> new ConcurrentHashMap<>());
            this.instanceMap = TICKER_MAP.get(entity.getUUID());
        }
        public boolean hasTicker(EntityTicker ticker){
            return instanceMap.containsKey(ticker);
        }
        //获取一个Optional封装的Instance，Optional真的很方便
        public @NotNull Optional<EntityTickerInstance> getOptional(EntityTicker type){
            return Optional.ofNullable(this.getTicker(type));
        }
        //直接获取Instance，可能为null
        public @Nullable EntityTickerInstance getTicker(EntityTicker type){
            return this.instanceMap.get(type);
        }
        //直接把实体对应Ticker的Instance替换成指定的Instance
        public void setTicker(EntityTickerInstance instance){
            if (!this.hasTicker(instance.ticker)){
                instance.ticker.onTickerStart(instance.duration,instance.level,this.entity);
            }
            this.instanceMap.put(instance.ticker,instance);
        }
        //用两个函数将你要添加的Instance和已有的融合。函数控制的是融合算法（比如你想让等级相加就给levelFunction填Integer::sum）
        public void addTicker(EntityTickerInstance instance, BiFunction<Integer,Integer,Integer> levelFunction, BiFunction<Integer,Integer,Integer> timeFunction){
            EntityTickerInstance existing = this.instanceMap.get(instance.ticker);
            int existingLevel = 0;
            int existingTime = 0;
            if (existing!=null){
                existingLevel = existing.level;
                existingTime = existing.duration;
            }
            EntityTickerInstance merged = new EntityTickerInstance(instance.ticker,levelFunction.apply(existingLevel,instance.level),timeFunction.apply(existingTime,instance.duration));
            this.setTicker(merged);
        }
        public void removeTicker(EntityTicker ticker){
            if (this.hasTicker(ticker)){
                ticker.onTickerEnd(this.instanceMap.get(ticker).level,this.entity);
                this.instanceMap.remove(ticker);
            }
        }
    }

}
