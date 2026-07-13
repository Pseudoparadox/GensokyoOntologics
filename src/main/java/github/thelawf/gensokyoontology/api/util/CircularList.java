package github.thelawf.gensokyoontology.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CircularList<T> implements Collection<CircularNode<T>>, Comparable<CircularNode<T>> {
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
        return nodes.stream().map(CircularNode::value).collect(Collectors.toList());
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

    @Override
    public boolean contains(Object o) {
        return this.toNodeList().contains(o);
    }

    public static <T> CircularList<T> from(Iterable<T> list) {
        CircularList<T> circular = new CircularList<>();
        list.forEach(circular::addLast);
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
     * 将原循环链表切断为两个循环链表
     */
    public Maybe<CircularList<T>> trySplit(CircularNode<T> node) {
        if (node == null || !nodes.contains(node)) {
            return Maybe.empty();
        }

        CircularList<T> splitList = new CircularList<>();
        // 只有一个节点的情况
        if (depth == 1) {
            head = null;
            tail = null;
            return Maybe.empty();
        }
        // 删除头节点
        else if (node == head) {
            head = head.next();
            head.setPrev(tail);
            tail.setNext(head);
            return Maybe.empty();
        }
        // 删除尾节点
        else if (node == tail) {
            tail = tail.prev();
            tail.setNext(head);
            head.setPrev(tail);
            return Maybe.empty();
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

        return Maybe.ofNullable(this);
    }

    /**
     * 删除满足条件的第一个节点
     */
    public boolean removeWhen(Predicate<T> predicate) {
        Optional<CircularNode<T>> nodeOpt = nodes.stream()
                .filter(n -> predicate.test(n.value()))
                .findFirst();

        return nodeOpt.map(this::remove).orElse(false);
    }

    /**
     * 替换指定节点的值
     * @param oldNode 要替换的旧节点
     * @param newValue 新的值
     * @return 是否替换成功
     */
    public boolean replace(CircularNode<T> oldNode, T newValue) {
        if (oldNode == null || !nodes.contains(oldNode)) {
            return false;
        }

        // 创建新节点
        CircularNode<T> newNode = new CircularNode<>(newValue);

        // 复制旧节点的连接关系
        newNode.setPrev(oldNode.prev());
        newNode.setNext(oldNode.next());

        // 更新前后节点的连接
        if (oldNode.prev() != null) {
            oldNode.prev().setNext(newNode);
        }
        if (oldNode.next() != null) {
            oldNode.next().setPrev(newNode);
        }

        // 更新head和tail
        if (oldNode == head) {
            head = newNode;
        }
        if (oldNode == tail) {
            tail = newNode;
        }

        // 更新nodes列表
        int index = nodes.indexOf(oldNode);
        nodes.set(index, newNode);

        // 清理旧节点
        oldNode.setPrev(null);
        oldNode.setNext(null);

        return true;
    }

    /**
     * 替换满足条件的第一个节点的值
     */
    public boolean replaceIf(Predicate<T> predicate, T newValue) {
        Optional<CircularNode<T>> nodeOpt = nodes.stream()
                .filter(n -> predicate.test(n.value()))
                .findFirst();

        return nodeOpt.map(node -> replace(node, newValue)).orElse(false);
    }

    /**
     * 替换指定节点为另一个节点
     * @param oldNode 要替换的旧节点
     * @param newNode 新节点
     * @return 是否替换成功
     */
    public boolean replaceNode(CircularNode<T> oldNode, CircularNode<T> newNode) {
        if (oldNode == null || newNode == null || !nodes.contains(oldNode)) {
            return false;
        }

        // 确保新节点不在当前链表中
        if (nodes.contains(newNode)) {
            throw new IllegalArgumentException("New node already exists in the list");
        }

        // 复制旧节点的连接关系
        newNode.setPrev(oldNode.prev());
        newNode.setNext(oldNode.next());

        // 更新前后节点的连接
        if (oldNode.prev() != null) {
            oldNode.prev().setNext(newNode);
        }
        if (oldNode.next() != null) {
            oldNode.next().setPrev(newNode);
        }

        // 更新head和tail
        if (oldNode == head) {
            head = newNode;
        }
        if (oldNode == tail) {
            tail = newNode;
        }

        // 更新nodes列表
        int index = nodes.indexOf(oldNode);
        nodes.set(index, newNode);

        // 清理旧节点
        oldNode.setPrev(null);
        oldNode.setNext(null);

        return true;
    }

    /**
     * 替换满足条件的第一个节点为另一个节点
     */
    public boolean replaceIf(Predicate<T> predicate, CircularNode<T> newNode) {
        Optional<CircularNode<T>> nodeOpt = nodes.stream()
                .filter(n -> predicate.test(n.value()))
                .findFirst();

        return nodeOpt.map(node -> replaceNode(node, newNode)).orElse(false);
    }

    /**
     * 批量替换节点值
     * @param predicate 匹配条件
     * @param newValue 新值
     * @return 替换的节点数量
     */
    public int replaceAll(Predicate<T> predicate, T newValue) {
        int count = 0;
        List<CircularNode<T>> toReplace = nodes.stream()
                .filter(n -> predicate.test(n.value()))
                .collect(Collectors.toList());

        for (CircularNode<T> node : toReplace) {
            if (replace(node, newValue)) {
                count++;
            }
        }

        return count;
    }

    /**
     * 交换两个节点的位置
     * @param node1 第一个节点
     * @param node2 第二个节点
     * @return 是否交换成功
     */
    public boolean swap(CircularNode<T> node1, CircularNode<T> node2) {
        if (node1 == null || node2 == null || node1 == node2 ||
                !nodes.contains(node1) || !nodes.contains(node2)) {
            return false;
        }

        // 保存节点1的连接信息
        CircularNode<T> prev1 = node1.prev();
        CircularNode<T> next1 = node1.next();

        // 保存节点2的连接信息
        CircularNode<T> prev2 = node2.prev();
        CircularNode<T> next2 = node2.next();

        // 处理特殊情况：相邻节点
        boolean adjacent = (next1 == node2) || (next2 == node1);

        if (adjacent) {
            // node1和node2相邻
            if (next1 == node2) {
                // node1 -> node2
                node1.setNext(next2);
                node2.setPrev(prev1);
                node2.setNext(node1);
                node1.setPrev(node2);

                if (prev1 != null) prev1.setNext(node2);
                if (next2 != null) next2.setPrev(node1);
            } else {
                // node2 -> node1
                node2.setNext(next1);
                node1.setPrev(prev2);
                node1.setNext(node2);
                node2.setPrev(node1);

                if (prev2 != null) prev2.setNext(node1);
                if (next1 != null) next1.setPrev(node2);
            }
        } else {
            // 非相邻节点
            node1.setPrev(prev2);
            node1.setNext(next2);
            node2.setPrev(prev1);
            node2.setNext(next1);

            if (prev1 != null) prev1.setNext(node2);
            if (next1 != null) next1.setPrev(node2);
            if (prev2 != null) prev2.setNext(node1);
            if (next2 != null) next2.setPrev(node1);
        }

        // 更新head和tail
        if (node1 == head) {
            head = node2;
        } else if (node2 == head) {
            head = node1;
        }

        if (node1 == tail) {
            tail = node2;
        } else if (node2 == tail) {
            tail = node1;
        }

        // 更新nodes列表中的顺序
        int index1 = nodes.indexOf(node1);
        int index2 = nodes.indexOf(node2);
        Collections.swap(nodes, index1, index2);

        return true;
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

    public void addFirst(T value) {
        CircularNode<T> newNode = new CircularNode<>(value);

        if (isEmpty()) {
            // 空链表情况
            head = newNode;
            tail = newNode;
            newNode.setNext(newNode);
            newNode.setPrev(newNode);
        } else {
            // 非空链表情况
            newNode.setNext(head);
            newNode.setPrev(tail);
            head.setPrev(newNode);
            tail.setNext(newNode);
            head = newNode;
        }

        nodes.add(0, newNode);
        depth++;
    }

    /**
     * 在链表尾部添加节点
     */
    public void addLast(T value) {
        CircularNode<T> newNode = new CircularNode<>(value);

        if (isEmpty()) {
            // 空链表情况
            head = newNode;
            tail = newNode;
            newNode.setNext(newNode);
            newNode.setPrev(newNode);
        } else {
            // 非空链表情况
            newNode.setNext(head);
            newNode.setPrev(tail);
            tail.setNext(newNode);
            head.setPrev(newNode);
            tail = newNode;
        }

        nodes.add(newNode);
        depth++;
    }

    /**
     * 在指定节点之前插入新节点
     */
    public void addBefore(CircularNode<T> targetNode, T value) {
        if (targetNode == null || !nodes.contains(targetNode)) {
            throw new IllegalArgumentException("Target node is not in the list");
        }

        CircularNode<T> newNode = new CircularNode<>(value);

        // 设置新节点的连接
        newNode.setPrev(targetNode.prev());
        newNode.setNext(targetNode);

        // 更新相邻节点的连接
        targetNode.prev().setNext(newNode);
        targetNode.setPrev(newNode);

        // 如果是在头节点之前插入，更新头节点
        if (targetNode == head) {
            head = newNode;
        }

        // 更新nodes列表
        int index = nodes.indexOf(targetNode);
        nodes.add(index, newNode);
        depth++;
    }

    /**
     * 在指定节点之后插入新节点
     */
    public void addAfter(CircularNode<T> targetNode, T value) {
        if (targetNode == null || !nodes.contains(targetNode)) {
            throw new IllegalArgumentException("Target node is not in the list");
        }

        CircularNode<T> newNode = new CircularNode<>(value);

        // 设置新节点的连接
        newNode.setPrev(targetNode);
        newNode.setNext(targetNode.next());

        // 更新相邻节点的连接
        targetNode.next().setPrev(newNode);
        targetNode.setNext(newNode);

        // 如果是在尾节点之后插入，更新尾节点
        if (targetNode == tail) {
            tail = newNode;
        }

        // 更新nodes列表
        int index = nodes.indexOf(targetNode);
        nodes.add(index + 1, newNode);
        depth++;
    }


    /**
     * 遍历链表
     */
    public void iterate(Consumer<T> action) {
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
    public void iterate(Consumer<T> action, int maxIterations) {
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
        // forEach(value -> sb.append(value).append(", "));
        // 移除最后的逗号和空格
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("]");
        return sb.toString();
    }

    public CircularList<T> copy(){
        CircularList<T> newList = new CircularList<>();
        this.nodes.forEach(node -> newList.addLast(node.value()));
        return newList;
    }

    @Override
    public int compareTo(@NotNull CircularNode<T> o) {
        return nodes.indexOf(o);
    }

    @Override
    public @NotNull Iterator<CircularNode<T>> iterator() {
        return this.toNodeList().iterator();
    }

    @Override
    public @NotNull Object[] toArray() {
        return new Object[0];
    }

    @Override
    public @NotNull <T> T[] toArray(@NotNull T[] a) {
        return null;
    }

    @Override
    public boolean add(CircularNode<T> node) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends CircularNode<T>> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return false;
    }
}