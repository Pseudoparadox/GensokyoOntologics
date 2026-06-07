package com.github.fictology.gensokyoontology.common.item.touhou;


import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

/**
 * PlayerEntity.noClip 字段决定玩家能否与方块进行碰撞，所以这里需要将这个字段设为false以便让玩家使用霍青娥的能力。当能力启动时，将会以
 * 1 power/秒 的速度通过消耗玩家的power点数来让玩家实现穿墙效果。
 */
public class SeigaHairpin extends Item {
    private static final AABB ZERO = new AABB(0, 0, 0, 0, 0, 0);
    private static AABB PLAYER_AABB;
    private int maxTick = 0;

    public SeigaHairpin(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        PLAYER_AABB = player.getBoundingBox();
        player.setBoundingBox(ZERO);
        player.setNoGravity(true);
        return super.use(level, player, hand);
    }
}
