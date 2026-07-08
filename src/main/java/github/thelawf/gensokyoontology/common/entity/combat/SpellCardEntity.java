package github.thelawf.gensokyoontology.common.entity.combat;

import com.google.gson.JsonObject;
import github.thelawf.gensokyoontology.common.entity.AffiliatedEntity;
import github.thelawf.gensokyoontology.common.entity.combat.spell.SimpleSpells;
import github.thelawf.gensokyoontology.common.item.DanmakuItem;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.core.SpellCardRegistry;
import github.thelawf.gensokyoontology.core.init.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.security.acl.Owner;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class SpellCardEntity extends AffiliatedEntity implements IRendersAsItem {
    public static final DataParameter<Integer> DATA_LIFE = EntityDataManager.createKey(
            SpellCardEntity.class, DataSerializers.VARINT);
    public static final DataParameter<String> DATA_SPELL_ID = EntityDataManager.createKey(
            SpellCardEntity.class, DataSerializers.STRING);
    public static final int MAX_LIFE = 200;

    public SpellCardEntity(EntityType<AbstractSpellCardEntity> entityType, World world, Item spellItem) {
        super(entityType, world);
    }

    public SpellCardEntity(EntityType<AbstractSpellCardEntity> entityType, World world, Item spellItem, JsonObject jsonSpell) {
        super(entityType, world);
    }

    public SpellCardEntity(EntityType<SpellCardEntity> entityType, World world) {
        super(entityType, world);
    }

    public SpellCardEntity(EntityType<SpellCardEntity> entityType, Entity owner, World world) {
        this(entityType, world);
        this.setOwnerId(owner.getUniqueID());
    }

    @Override
    public void tick() {
        super.tick();
        ResourceLocation key = new ResourceLocation(this.getSpellId());
        SIMPLE_SPELLS.getOrDefault(key, SimpleSpells.EMPTY_SPELl).accept(this.world, this);
        if (this.ticksExisted >= MAX_LIFE) this.remove();
    }

    public void setLifespan(int lifespan) {
        this.dataManager.set(DATA_LIFE, lifespan);
    }

    public int getLifespan() {
        return this.dataManager.get(DATA_LIFE);
    }

    public void setSpellId(String spellId) {
        this.dataManager.set(DATA_SPELL_ID, spellId);
    }
    public String getSpellId() {
        return this.dataManager.get(DATA_SPELL_ID);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(DATA_LIFE, 200);
        this.dataManager.register(DATA_SPELL_ID, GSKOUtil.withRL("empty_spell_card").toString());
    }

    @Override
    protected void writeAdditional(@NotNull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Lifespan", this.getLifespan());
        compound.putString("SpellId", this.getSpellId());

    }

    public void readAdditional(@NotNull CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("Lifespan")) {
            this.setLifespan(compound.getInt("Lifespan"));
        }
        if (compound.contains("SpellId")) {
            this.setSpellId(compound.getString("SpellId"));
        }
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
    }

    @Override
    public @NotNull ItemStack getItem() {
        return ItemRegistry.SPELL_CARD_BLANK.get().getDefaultInstance();
    }

    public static final Map<ResourceLocation, BiConsumer<World, SpellCardEntity>> SIMPLE_SPELLS = Util.make(new HashMap<>(), (map) -> {
        map.put(GSKOUtil.withRL("empty_spell_card"), SimpleSpells.EMPTY_SPELl);
        map.put(GSKOUtil.withRL("laser_maze"), SimpleSpells.LASER_MAZE);
        map.put(GSKOUtil.withRL("mobius_ring"), SimpleSpells.MOBIUS_RING);
        map.put(GSKOUtil.withRL("hell_eclipse"), SimpleSpells.HELL_ECLIPSE);
        map.put(GSKOUtil.withRL("master_spark"), SimpleSpells.MASTER_SPARK);
    });
}
