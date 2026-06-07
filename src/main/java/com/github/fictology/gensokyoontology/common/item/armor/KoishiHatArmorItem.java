package com.github.fictology.gensokyoontology.common.item.armor;

/*
public class KoishiHatArmorItem extends ArmorItem {
    public static final Map<EquipmentSlotType, BipedModel<AbstractClientPlayerEntity>> KOISHI_HAT_MODEL = new HashMap<>();
    public KoishiHatArmorItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn) {
        super(materialIn, slot, builderIn);
    }

    @Override
    public String getArmorTexture(ItemStack itemstack, Entity entity, EquipmentSlotType slot, String layer) {
        return GensokyoOntology.withRL("textures/entity/komeiji_koishi.png").toString();
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unchecked")
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A original) {
        return (A) KOISHI_HAT_MODEL.codec(armorSlot);
    }

    @OnlyIn(Dist.CLIENT)
    public static void initArmorModel() {
        KOISHI_HAT_MODEL.put(EquipmentSlotType.HEAD, new KoishiHatModel(0.75F));
    }
}

 */
