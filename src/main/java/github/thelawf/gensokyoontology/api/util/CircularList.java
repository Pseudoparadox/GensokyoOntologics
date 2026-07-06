package github.thelawf.gensokyoontology.api.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CircularList<T> {
    private CircularNode<T> head;
    private CircularNode<T> tail;
    private int depth;
    private List<CircularNode<T>> nodes;

    // 初始化一个空链表
    public CircularList()
    {
        this.head = null;
        this.tail = null;
        nodes = new ArrayList<>();
    }

    public List<T> toList(){
        return nodes.stream().map(CircularNode::value).collect(Collectors.toList());
    }
    public CircularNode<T> head(){
        return this.head;
    }
    public CircularNode<T> tail(){
        return this.tail;
    }

    public static <T> CircularList<T> from(Iterable<T> list)
    {
        CircularList<T> circular = new CircularList<>();
        list.forEach(circular::add);
        return circular;
    }

    public Maybe<CircularNode<T>> tryFind(CircularNode<T> node){
        Maybe<CircularNode<T>> maybe = Maybe.empty();
        this.nodes.stream().filter(n -> n == node).findFirst().ifPresent(maybe::set);
        return maybe;
    }

    public Maybe<T> tryFindValue(Predicate<T> predicate){
        Maybe<T> maybe = Maybe.empty();
        this.nodes.stream().map(CircularNode::value).filter(predicate).findFirst().ifPresent(maybe::set);
        return maybe;
    }

    public void add(T value)
    {
        CircularNode<T> node = new CircularNode<>(value);
        nodes.add(node);
        if (this.head == null) // 链表为空时
        {
            this.head = node;
            this.tail = node;
            node.setNext(this.head); // 关键：让第一个节点指向自己，形成循环
            this.depth = 1; // 只有一个节点时深度为1
        }
        else // 链表已有节点
        {
            this.tail.setNext(node); // 当前尾节点指向新节点
            this.tail = node;      // 更新尾节点为新节点
            this.tail.setNext(this.head);    // 关键：让新尾节点的 Next 指向头节点
            this.depth++;
        }
    }

    /// <summary>
    /// 遍历链表（示例方法，循环输出元素，防止无限循环）
    /// </summary>
    public void forEach(Consumer<T> action)
    {
        if (this.head == null)
        {
            return;
        }

        CircularNode<T> current = this.head;
        int count = 0;
        do
        {
            action.accept(this.head.value());
            current = current.next();
            count++;
        } while (current != this.head && count < this.depth); // 回到头节点时停止
    }

    public void forEach(Consumer<T> action, int maxIterations)
    {
        if (this.head == null)
        {
            return;
        }

        CircularNode<T> current = this.head;
        int count = 0;
        do
        {
            action.accept(this.head.value());
            current = current.next();
            count++;
        } while (current != this.head && count < maxIterations); // 回到头节点时停止
    }

}
