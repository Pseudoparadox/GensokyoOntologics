package github.thelawf.gensokyoontology.api.util;

public class CircularNode<T> implements ICircularNode<T, CircularNode<T>>{

    private CircularNode<T> prev;
    private CircularNode<T> next;
    private T value;

    public CircularNode(T value) {
        this.value = value;
    }

    @Override
    public CircularNode<T> prev() {
        return this.prev;
    }

    @Override
    public CircularNode<T> next() {
        return this.next;
    }

    @Override
    public T value() {
        return this.value;
    }

    public Maybe<CircularNode<T>> tryGetNext(){
        return this.next == null ? Maybe.empty() : Maybe.ofNullable(this.next);
    }

    @Override
    public void setPrev(CircularNode<T> node) {
        this.prev = node;
    }

    @Override
    public void setNext(CircularNode<T> node) {
        this.next = node;
    }

    public boolean orderMatches(CircularList<T> list, CircularNode<T> other){
        return list.compareTo(this) == list.compareTo(other);
    }
}
