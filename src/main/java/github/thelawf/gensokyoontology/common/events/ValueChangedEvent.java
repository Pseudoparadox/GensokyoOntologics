package github.thelawf.gensokyoontology.common.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.Event;

public class ValueChangedEvent<T> extends Event {
    public T prevValue;
    public T currentValue;

    public ValueChangedEvent(T prevValue, T currentValue) {
        this.prevValue = prevValue;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    @Override
    public boolean hasResult() {
        return true;
    }

    public T getPrevValue() {
        return this.prevValue;
    }

    public T getCurrentValue() {
        return this.currentValue;
    }

    public static class PlayerCap<T> extends ValueChangedEvent<T> {
        public PlayerEntity player;
        public PlayerCap(T prevValue, T currentValue) {
            super(prevValue, currentValue);
        }

        public PlayerEntity getPlayer() {
            return this.player;
        }
    }
}
