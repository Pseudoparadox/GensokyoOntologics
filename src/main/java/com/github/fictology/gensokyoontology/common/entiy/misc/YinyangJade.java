package com.github.fictology.gensokyoontology.common.entiy.misc;

import com.github.fictology.gensokyoontology.GensokyoOntology;
import com.github.fictology.gensokyoontology.api.IDamageHandler;
import com.github.fictology.gensokyoontology.api.render.IModelGetter;
import com.github.fictology.gensokyoontology.api.render.ITextureGetter;
import com.github.fictology.gensokyoontology.common.item.touhou.YinyangJadeItem;
import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.registry.ItemRegistry;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class YinyangJade extends ThrowableItemProjectile implements ItemSupplier, IDamageHandler, IModelGetter {
    public YinyangJade(EntityType<? extends ThrowableItemProjectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    protected @NonNull Item getDefaultItem() {
        return ItemRegistry.YINYANG_JADE_BLACK.get();
    }

    public YinyangJade(ServerLevel serverLevel, LivingEntity owner, ItemStack itemStack) {
        super(EntityRegistry.YIN_YANG_JADE.get(), owner, serverLevel, itemStack);
        // this.behavior = new ClosureExpression();
    }

    @Override
    public @NonNull ItemStack getItem() {
        return super.getItem();
    }

    @Override
    public void hurtLiving(LivingEntity living, Level level, ResourceKey<DamageType> damageType, float amount) {

    }

    @Override
    public Identifier modelPath() {
        var registryName = BuiltInRegistries.ITEM.getKey(this.getItem().getItem());
        var fileName = registryName.toString().replace(GensokyoOntology.MODID + ":", "");
        return GSKOUtil.key("models/entity/" + fileName + ".obj");
    }
}
