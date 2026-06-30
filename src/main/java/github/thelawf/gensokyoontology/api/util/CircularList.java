package github.thelawf.gensokyoontology.api.util;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class CircularList<T extends ICircularNode<T>> {
    public ICircularNode<T> head;
    public ICircularNode<T> tail;
    public int depth;
    public List<ICircularNode<T>> nodes;

    // 初始化一个空链表
    public CircularList(int capacity)
    {
        this.depth = capacity;
        this.head = null;
        this.tail = null;
    }

//    public static <T extends ICircularNode<T>> CircularList<T> From(Iterator<T> list)
//    {
//        CircularList<T> circular = new CircularList<T>(0);
//        // list.forEachRemaining(circular::Add);
//        return circular;
//    }
//
//
//
//    public void Add(T data)
//    {
//        ICircularNode<T> newNode = new CircularListNode<T>(data);
//        _list.Add(data);
//        Nodes.Add(newNode);
//        if (Head == null) // 链表为空时
//        {
//            Head = newNode;
//            Tail = newNode;
//            newNode.Next = Head; // 关键：让第一个节点指向自己，形成循环
//            Depth = 1; // 只有一个节点时深度为1
//        }
//        else // 链表已有节点
//        {
//            Tail.Next = newNode; // 当前尾节点指向新节点
//            Tail = newNode;      // 更新尾节点为新节点
//            Tail.Next = Head;    // 关键：让新尾节点的 Next 指向头节点
//            Depth++;
//        }
//    }
//
//    /// <summary>
//    /// 遍历链表（示例方法，循环输出元素，防止无限循环）
//    /// </summary>
//    public void Traverse(int maxIterations = 10)
//    {
//        if (Head == null)
//        {
//            return;
//        }
//
//        var current = Head;
//        var count = 0;
//        do
//        {
//            current = current.Next;
//            count++;
//        } while (current != Head && count < maxIterations); // 回到头节点时停止
//    }
//
//    public void TraverseForEach(Consumer<T> action, int maxIterations)
//    {
//        if (Head == null)
//        {
//            return;
//        }
//
//        var current = Head;
//        var count = 0;
//        do
//        {
//            action.Invoke(Head.Value);
//            current = current.Next;
//            count++;
//        } while (current != Head && count < maxIterations); // 回到头节点时停止
//    }

}
