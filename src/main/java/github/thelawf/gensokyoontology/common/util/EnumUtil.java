package github.thelawf.gensokyoontology.common.util;

import java.util.function.Consumer;
import java.util.function.Function;

public class EnumUtil {
    public static <T extends Enum<T>> T switchEnum(Class<T> enumClass, T value) {
        T[] values = enumClass.getEnumConstants();
        int nextIndex = (value.ordinal() + 1) % values.length;
        return values[nextIndex];
    }

    public static <T extends Enum<T>> T moveTo(Class<T> enumClass, T value, int index) {
        T[] values = enumClass.getEnumConstants();
        int currentIndex = value.ordinal();

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // 堆栈的第0个是getStackTrace方法本身，第1个是当前方法（moveTo），第2个是调用者
        if (stackTrace.length >= 3) {
            StackTraceElement caller = stackTrace[2];
            GSKOUtil.log("Called by: " + caller.getClassName() + "." + caller.getMethodName());
        }

        // 根据参数规定移动至values[]的对应位置
        int targetIndex = (currentIndex + index) % values.length;
        if (targetIndex < 0) {
            targetIndex += values.length; // 如果超出索引则从尾部继续
        }
        return values[targetIndex];
    }

    public static <T extends Enum<T>> T readEnum(Class<T> enumClass, int ordinalValue){
        return enumClass.getEnumConstants()[ordinalValue];
    }

    public static <T extends Enum<T>> void forEachAct(Class<T> enumClass, Consumer<T> action){
        for (T enumConst : enumClass.getEnumConstants()) {
            action.accept(enumConst);
        }
    }

}
