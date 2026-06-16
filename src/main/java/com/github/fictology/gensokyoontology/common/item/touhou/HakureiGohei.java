package com.github.fictology.gensokyoontology.common.item.touhou;

import com.github.fictology.gensokyoontology.common.entiy.HakureiReimuEntity;
import com.github.fictology.gensokyoontology.common.entiy.misc.Danmaku;
import com.github.fictology.gensokyoontology.common.entiy.misc.DreamSealEntity;
import com.github.fictology.gensokyoontology.data.DanmakuColor;
import com.github.fictology.gensokyoontology.registry.DataRegistry;
import com.github.fictology.gensokyoontology.registry.EntityRegistry;
import com.github.fictology.gensokyoontology.util.GSKOUtil;
import com.github.fictology.gensokyoontology.util.api.IRayTraceReader;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static com.github.fictology.gensokyoontology.registry.ItemRegistry.INYO_JADE_RED;
import static com.github.fictology.gensokyoontology.registry.ItemRegistry.SC_DREAM_SEAL;

/**
 * 博丽灵梦的御币
 */

public class HakureiGohei extends Item implements IRayTraceReader {
    public static final Identifier TITLE = GSKOUtil.affixKey("gui", "hakurei_gohei.title");

    public HakureiGohei(Item.Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResult use(Level level, Player playerIn, InteractionHand handIn) {
        GSKOUtil.info("player has " + playerIn.getData(DataRegistry.IDENTITY).power() + "point of power");
        if (playerIn.getCooldowns().isOnCooldown(playerIn.getItemInHand(handIn)) && !playerIn.isCreative())
            return InteractionResult.PASS;

        ItemStack stack = playerIn.getItemInHand(handIn);
        var magic = stack.get(DataRegistry.SPECIAL_MAGIC);
        if (magic != null) {

            var item = magic.getItem(magic.selectedIndex());
            if (item == INYO_JADE_RED.get()) {
                var inYoJade = Danmaku.create(level, INYO_JADE_RED.get(), playerIn);
                Danmaku.shootTo(level, playerIn, inYoJade, 0.7f);
            }
            else if (item == SC_DREAM_SEAL.get()) {
                fireDreamSeal(level, playerIn);
            }
        }

        if (playerIn.isCreative()) return InteractionResult.PASS;
        playerIn.getCooldowns().addCooldown(stack, 10);

        var hakurei = new HakureiReimuEntity(EntityRegistry.HAKUREI_REIMU.get(), level);
        level.addFreshEntity(hakurei);
        return InteractionResult.SUCCESS;
    }

    public static void fireDreamSeal(Level worldIn, Player playerIn) {
        for (int i = 0; i < 8; i++) {
            int i1 = i % 3;
            String color = switch (i1) {
                case 1 -> DanmakuColor.BLUE;
                case 2 -> DanmakuColor.GREEN;
                default -> DanmakuColor.RED;
            };
            var vector3d = i % 2 == 0 ? new Vec3(2, 3, 0).xRot((float) Math.PI * 2 / i) :
                    new Vec3(2, 3, 0).xRot((float) -Math.PI * 2 / i);
            // Vector3d shootVec = playerIn.getLookVec();
            var initPos = vector3d.add(playerIn.getPosition(0));

            var dreamSeal = new DreamSealEntity(worldIn, playerIn, color);
            dreamSeal.setNoGravity(true);
            dreamSeal.shoot(vector3d.x, vector3d.y, vector3d.z, 1.2f, 0f);
            dreamSeal.setOldPosAndRot(initPos, playerIn.yRotO, playerIn.xRotO);
            worldIn.addFreshEntity(dreamSeal);
        }
    }

    public enum Mode {
        DANMAKU,
        SPELL_CARD,
        DREAM_SEAL
    }
}

