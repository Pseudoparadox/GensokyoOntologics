package github.thelawf.gensokyoontology.common.capability.entity;

import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapability;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import github.thelawf.gensokyoontology.common.compat.touhoulittlemaid.TouhouLittleMaidCompat;
import github.thelawf.gensokyoontology.common.network.GSKONetworking;
import github.thelawf.gensokyoontology.common.network.packet.PowerChangedPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.concurrent.atomic.AtomicReference;

public class GSKOPowerCapability implements INBTSerializable<FloatNBT> {
    private float count;
    private boolean isDirty;
    public static final float MAX = 5.00F;
    public static final float MIN = 0.00F;
    public static GSKOPowerCapability INSTANCE;
    public static final String ID = "gsko_power";

    public GSKOPowerCapability(float count) {
        this.count = count;
    }

    @Override
    public FloatNBT serializeNBT() {

        return FloatNBT.valueOf(this.count);
    }

    @Override
    public void deserializeNBT(FloatNBT nbt) {
        this.count = nbt.getFloat();
    }

    public void setCount(float count) {
        // MinecraftForge.EVENT_BUS.post(new ValueChangedEvent<>(this.count, count));

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        AtomicReference<PowerCapability> cap = new AtomicReference<>();

        if (player == null) return;

        if (TouhouLittleMaidCompat.isTouhouMaidLoaded()) {
            player.getCapability(PowerCapabilityProvider.POWER_CAP).ifPresent(tlmCap -> {
                cap.set(tlmCap);
                tlmCap.set(count);
            });
        }

        this.count = MathHelper.clamp(count, MIN, MAX);
        GSKONetworking.CHANNEL.sendToServer(new PowerChangedPacket(this.count));
        this.markDirty();
    }

    public float getCount() {
        return this.count;
    }

    public void add(float count) {
        this.setCount(MathHelper.clamp(this.count + count, MIN, MAX));
        this.markDirty();
    }

    public void markDirty() {
        this.isDirty = true;
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public void setDirty(boolean dirty) {
        this.isDirty = dirty;
    }
}
