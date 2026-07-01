package github.thelawf.gensokyoontology.api.util;

public interface ICircularNode<T, N extends ICircularNode<T, N>> {
    N prev();
    N next();
    T value();

    void setPrev(N node);
    void setNext(N node);
    default boolean hasNext() {
        return next() == null;
    }
    default boolean hasPrev() {
        return prev() == null;
    }
}
