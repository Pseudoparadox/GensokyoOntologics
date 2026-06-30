package github.thelawf.gensokyoontology.common.item.touhou;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.thelawf.gensokyoontology.api.IHasCooldown;
import github.thelawf.gensokyoontology.api.IRayTracer;
import github.thelawf.gensokyoontology.client.gui.screen.skill.GoheiModeSelectScreen;
import github.thelawf.gensokyoontology.client.particle.ParticleRegistry;
import github.thelawf.gensokyoontology.client.particle.PowerParticleData;
import github.thelawf.gensokyoontology.common.capability.GSKOCapabilities;
import github.thelawf.gensokyoontology.common.capability.entity.GSKOPowerCapability;
import github.thelawf.gensokyoontology.common.item.MultiModeItem;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import github.thelawf.gensokyoontology.common.util.danmaku.DanmakuColor;
import github.thelawf.gensokyoontology.core.init.BlockRegistry;
import github.thelawf.gensokyoontology.core.init.TileEntityRegistry;
import github.thelawf.gensokyoontology.core.init.itemtab.GSKOItemTab;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4i;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 博丽灵梦的御币
 */
public class HakureiGohei extends MultiModeItem implements IRayTracer, IHasCooldown {
    public static final ITextComponent TITLE = GSKOUtil.translateText("gui.", ".hakurei_gohei.title");
    public HakureiGohei(Properties properties) {
        super(properties);
    }

    private void setMode(CompoundNBT nbt, Mode mode) {
        nbt.putInt("mode", mode.ordinal());
    }

    public static Mode getMode(CompoundNBT nbt) {
        return Mode.values()[nbt.getInt("mode")];
    }


    @Override
    @NotNull
    public ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, PlayerEntity playerIn, @NotNull Hand handIn) {
        if (playerIn.getCooldownTracker().hasCooldown(this) && !playerIn.isCreative())
            return ActionResult.resultPass(playerIn.getHeldItem(handIn));

        ItemStack stack = playerIn.getHeldItem(handIn);
        if (stack.getTag() != null) {
            switch (getMode(stack.getTag())) {
                case DANMAKU:
//                    InYoJadeDanmakuEntity inYoJade = new InYoJadeDanmakuEntity(worldIn, playerIn);
//                    DanmakuUtil.shootDanmaku(worldIn, playerIn, inYoJade, 1F, 0F);
                    this.setCD(playerIn, stack, 40);
                    break;
                case DREAM_SEAL:
                    this.fireDreamSeal(worldIn, playerIn);
                    this.setCD(playerIn, stack, 1800);
                    break;
                case POWER:
                    try {
                        this.powering(worldIn, playerIn, 10);
                    } catch (CommandSyntaxException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case SPELL_CARD:
                default:
                    this.setCD(playerIn, stack, 2000);
                    break;

            }
        }

        if (playerIn.isCreative()) return ActionResult.resultPass(playerIn.getHeldItem(handIn));
        return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
    }

    /**
     * 御币的充能模式，当玩家P点不足时
     */
    public void powering(World world, LivingEntity user, double radius) throws CommandSyntaxException {
        LazyOptional<GSKOPowerCapability> optional = user.getCapability(GSKOCapabilities.POWER);
        if (!user.getCapability(GSKOCapabilities.POWER).isPresent()) return;

        AtomicReference<Float> floatRef = new AtomicReference<>(.1F);
        optional.ifPresent(cap -> {
            floatRef.set(cap.getCount());
        });

        float consumedCount = this.getConsumedCount(floatRef, Screen.hasShiftDown());

        Vector3d start = user.getEyePosition(0f);
        Vector3d end = user.getLookVec().normalize().scale(radius).add(start);
        BlockRayTraceResult btr = world.rayTraceBlocks(new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, user));

        if (world.isRemote) return;
        ServerWorld serverWorld = (ServerWorld) world;

        for (int i = 0; i < 100; i++) {
            Vector3d pos = user.getLookVec().scale(i * 0.1).add(0, user.getEyeHeight(), 0);
            Vector3d motion = user.getLookVec().normalize().scale(0.1F * i);
            serverWorld.spawnParticle(ParticleRegistry.POWER_PARTICLE.get().getDeserializer().deserialize(
                    ParticleRegistry.POWER_PARTICLE.get(), new StringReader(" 1 1 1 1 1 1 1")),
                    1.0,1,1,1,1,1,1,1);
            serverWorld.spawnParticle(new PowerParticleData(motion, new Vector4i(255, 0, 0, 120)),
                    user.getPositionVec().add(pos).x,
                    user.getPositionVec().add(pos).y,
                    user.getPositionVec().add(pos).z,
                    1,
                    motion.x,
                    motion.y,
                    motion.z,
                    1);
        }
        GSKOUtil.getTileByType(world, btr.getPos(), TileEntityRegistry.DANMAKU_TABLE_TILE.get()).ifPresent(tile -> {
            tile.setPower(tile.getPower() + consumedCount);
        });

        optional.ifPresent(cap -> cap.setCount(cap.getCount() - consumedCount));

    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        if (world.getBlockState(pos).getBlock() == BlockRegistry.TREFOIL_KNOT_CORE.get()){

        }
        return super.onItemUse(context);
    }

    public void fireDreamSeal(World worldIn, PlayerEntity playerIn) {
        for (int i = 0; i < 8; i++) {
            int i1 = i % 3;
            DanmakuColor color;
            switch (i1) {
                default:
                case 0:
                    color = DanmakuColor.RED;
                    break;
                case 1:
                    color = DanmakuColor.BLUE;
                    break;
                case 2:
                    color = DanmakuColor.GREEN;
                    break;
            }
            Vector3d vector3d = i % 2 == 0 ? new Vector3d(2, 3, 0).rotatePitch((float) Math.PI * 2 / i) :
                    new Vector3d(2, 3, 0).rotatePitch((float) -Math.PI * 2 / i);
            // Vector3d shootVec = playerIn.getLookVec();
            Vector3d initPos = vector3d.add(playerIn.getPositionVec());

            // DreamSealEntity dreamSeal = new DreamSealEntity(worldIn, playerIn, color);
            // dreamSeal.setNoGravity(true);
            // dreamSeal.shoot(vector3d.x, vector3d.y, vector3d.z, 1.2f, 0f);
            // dreamSeal.setLocationAndAngles(initPos.x, initPos.y, initPos.z, playerIn.rotationYaw, playerIn.rotationPitch);
            // worldIn.addEntity(dreamSeal);

        }
    }

    @Override
    public void fillItemGroup(@NotNull ItemGroup group, @NotNull NonNullList<ItemStack> items) {
        if (group == GSKOItemTab.GSKO_ITEM_TAB) {
            ItemStack itemStack = new ItemStack(this);
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("mode", Mode.DANMAKU.ordinal());
            itemStack.setTag(tag);
            items.add(itemStack);
        }
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        ITextComponent text = GSKOUtil.translateText("tooltip.", ".hakurei_gohei.mode");
        if (stack.getTag() != null) {
            switch (getMode(stack.getTag())) {
                default:
                case DANMAKU:
                    tooltip.add(GoheiModeSelectScreen.DANMAKU);
                    break;
                case SPELL_CARD:
                    break;
                case DREAM_SEAL:
                    tooltip.add(GoheiModeSelectScreen.DREAM_SEAL);
                    break;
            }
        }
    }

    private float getConsumedCount(AtomicReference<Float> f, boolean hasShiftDown) {
        float consumedCount = 0.0F;
        if (hasShiftDown) {
            if (f.get() > 1F) {
                consumedCount = 1;
            }
            else consumedCount = f.get();
        }
        else
        {
            if (f.get() > 0.1F) {
                consumedCount = 0.1F;
            }
            else  consumedCount = f.get();
        }
        return consumedCount;
    }

    @Override
    @NotNull
    public UseAction getUseAction(@NotNull ItemStack stack) {
        return UseAction.BLOCK;
    }

    public enum Mode {
        POWER,
        DANMAKU,
        DREAM_SEAL,
        SPELL_CARD
    }
}
