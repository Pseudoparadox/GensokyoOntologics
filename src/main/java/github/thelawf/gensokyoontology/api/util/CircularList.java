package github.thelawf.gensokyoontology.api.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CircularList<T> {
    private CircularNode<T> head;
    private CircularNode<T> tail;
    private int depth;
    private List<CircularNode<T>> nodes;

    // 初始化一个空链表
    public CircularList() {
        this.head = null;
        this.tail = null;
        this.depth = 0;
        nodes = new ArrayList<>();
    }

    public List<T> toList() {
        return nodes.stream()
                .map(CircularNode::value)
                .collect(Collectors.toList());
    }

    public List<CircularNode<T>> toNodeList() {
        return new ArrayList<>(nodes);
    }

    public CircularNode<T> head() {
        return this.head;
    }

    public CircularNode<T> tail() {
        return this.tail;
    }

    public int size() {
        return depth;
    }

    public boolean isEmpty() {
        return depth == 0;
    }

    public static <T> CircularList<T> from(Iterable<T> list) {
        CircularList<T> circular = new CircularList<>();
        list.forEach(circular::add);
        return circular;
    }

    public Maybe<CircularNode<T>> tryFind(CircularNode<T> node) {
        Maybe<CircularNode<T>> maybe = Maybe.empty();
        this.nodes.stream()
                .filter(n -> n == node)
                .findFirst()
                .ifPresent(maybe::set);
        return maybe;
    }

    public Maybe<CircularNode<T>> tryFind(Predicate<T> predicate) {
        Maybe<CircularNode<T>> maybe = Maybe.empty();
        this.nodes.stream()
                .filter(node -> predicate.test(node.value()))
                .findFirst()
                .ifPresent(maybe::set);
        return maybe;
    }

    public Maybe<T> tryFindValue(Predicate<T> predicate) {
        Maybe<T> maybe = Maybe.empty();
        this.nodes.stream()
                .map(CircularNode::value)
                .filter(predicate)
                .findFirst()
                .ifPresent(maybe::set);
        return maybe;
    }

    /**
     * 删除指定节点
     */
    public boolean remove(CircularNode<T> node) {
        if (node == null || !nodes.contains(node)) {
            return false;
        }

        // 只有一个节点的情况
        if (depth == 1) {
            head = null;
            tail = null;
        }
        // 删除头节点
        else if (node == head) {
            head = head.next();
            head.setPrev(tail);
            tail.setNext(head);
        }
        // 删除尾节点
        else if (node == tail) {
            tail = tail.prev();
            tail.setNext(head);
            head.setPrev(tail);
        }
        // 删除中间节点
        else {
            node.prev().setNext(node.next());
            node.next().setPrev(node.prev());
        }

        nodes.remove(node);
        depth--;

        // 清理节点引用
        node.setPrev(null);
        node.setNext(null);

        return true;
    }

    /**
     * 删除满足条件的第一个节点
     */
    public boolean removeIf(Predicate<T> predicate) {
        Optional<CircularNode<T>> nodeOpt = nodes.stream()
                .filter(n -> predicate.test(n.value()))
                .findFirst();

        return nodeOpt.map(this::remove).orElse(false);
    }

    /**
     * 删除指定节点之前的所有节点（不包括该节点）
     */
    public void removeAllPrevious(CircularNode<T> node) {
        if (node == null || !nodes.contains(node) || depth <= 1) {
            return;
        }

        CircularNode<T> current = head;
        List<CircularNode<T>> toRemove = new ArrayList<>();

        // 收集要删除的节点（从头节点到目标节点的前一个节点）
        while (current != node) {
            toRemove.add(current);
            current = current.next();

            // 安全检查，防止无限循环
            if (current == head) {
                break;
            }
        }

        // 如果node就是head，则不需要删除任何节点
        if (toRemove.isEmpty()) {
            return;
        }

        // 更新head为新节点
        head = node;

        // 更新连接
        if (toRemove.size() == depth) {
            // 删除了所有节点，只剩node
            head = node;
            tail = node;
            node.setPrev(node);
            node.setNext(node);
        } else {
            // 获取原来的tail到新的head的连接
            CircularNode<T> oldTail = toRemove.get(toRemove.size() - 1);
            CircularNode<T> actualTail = oldTail.prev();

            // 重新连接
            actualTail.setNext(head);
            head.setPrev(actualTail);
            tail = actualTail;
        }

        // 从nodes列表中移除
        nodes.removeAll(toRemove);
        depth -= toRemove.size();

        // 清理被删除节点的引用
        toRemove.forEach(n -> {
            n.setPrev(null);
            n.setNext(null);
        });
    }

    /**
     * 删除指定节点之后的所有节点（不包括该节点）
     */
    public void removeAllNext(CircularNode<T> node) {
        if (node == null || !nodes.contains(node) || depth <= 1) {
            return;
        }

        CircularNode<T> current = node.next();
        List<CircularNode<T>> toRemove = new ArrayList<>();

        // 收集要删除的节点（从目标节点的下一个节点到尾节点）
        while (current != head) {
            toRemove.add(current);
            current = current.next();
        }

        // 如果node就是tail，则不需要删除任何节点
        if (toRemove.isEmpty()) {
            return;
        }

        // 更新tail为新节点
        tail = node;
        tail.setNext(head);
        head.setPrev(tail);

        // 从nodes列表中移除
        nodes.removeAll(toRemove);
        depth -= toRemove.size();

        // 清理被删除节点的引用
        toRemove.forEach(n -> {
            n.setPrev(null);
            n.setNext(null);
        });
    }

    /**
     * 删除满足条件的节点之前的所有节点
     */
    public void removeAllPrevious(Predicate<T> predicate) {
        tryFind(predicate).ifPresent(this::removeAllPrevious);
    }

    /**
     * 删除满足条件的节点之后的所有节点
     */
    public void removeAllNext(Predicate<T> predicate) {
        tryFind(predicate).ifPresent(this::removeAllNext);
    }

    public void add(T value) {
        CircularNode<T> node = new CircularNode<>(value);
        nodes.add(node);

        if (head == null) { // 链表为空时
            head = node;
            tail = node;
            node.setNext(node); // 关键：让第一个节点指向自己，形成循环
            node.setPrev(node); // 设置prev指向自己
            depth = 1;
        } else { // 链表已有节点
            // 设置新节点的prev为当前尾节点
            node.setPrev(tail);
            // 设置新节点的next为头节点
            node.setNext(head);
            // 当前尾节点的next指向新节点
            tail.setNext(node);
            // 头节点的prev指向新节点
            head.setPrev(node);
            // 更新尾节点为新节点
            tail = node;
            depth++;
        }
    }

    /**
     * 遍历链表
     */
    public void forEach(Consumer<T> action) {
        if (head == null) {
            return;
        }

        CircularNode<T> current = head;
        int count = 0;
        do {
            action.accept(current.value()); // 修复：使用current.value()而不是head.value()
            current = current.next();
            count++;
        } while (current != head && count < depth);
    }

    /**
     * 带最大迭代次数的遍历
     */
    public void forEach(Consumer<T> action, int maxIterations) {
        if (head == null) {
            return;
        }

        CircularNode<T> current = head;
        int count = 0;
        do {
            action.accept(current.value()); // 修复：使用current.value()而不是head.value()
            current = current.next();
            count++;
        } while (current != head && count < maxIterations);
    }

    /**
     * 清空链表
     */
    public void clear() {
        nodes.clear();
        head = null;
        tail = null;
        depth = 0;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        forEach(value -> sb.append(value).append(", "));
        // 移除最后的逗号和空格
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("]");
        return sb.toString();
    }
}