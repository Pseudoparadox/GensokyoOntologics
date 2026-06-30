package github.thelawf.gensokyoontology.api.util;

public interface ICircularNode<T> {
    ICircularNode<T> prev();
    ICircularNode<T> next();
    T value();
    default boolean HasNext() {
        return next() == null;
    }
    default boolean HasPrev() {
        return prev() == null;
    }
}
