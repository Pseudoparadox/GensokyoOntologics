package github.thelawf.gensokyoontology.common.entity.ai;

import github.thelawf.gensokyoontology.api.util.Tree;
import github.thelawf.gensokyoontology.common.util.GSKOUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.WalkRandomlyTask;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;

/**
 * 任务管理器（还真是）<br>
 *<br>
 * 添加任务的方式使用{@link Tree}数据结构，通过字符串获取父节点、子节点和兄弟节点<br>
 * 需要获取任务时直接使用路径名称的格式化字符串获取即可，如需替换，则需要写明替换的任务的路径名<br>
 *<br>
 * 例如，我添加了如下路径：<br>
 *<br>
 * modid:mob_task/core<br>
 * modid:mob_task/rest<br>
 * modid:mob_task/panic<br>
 * modid:mob_task/wakeup<br>
 * modid:mob_task/core/walk<br>
 * modid:mob_task/core/swim<br>
 * modid:mob_task/rest/idle<br>
 * modid:mob_task/rest/sleep<br>
 * modid:mob_task/rest/find_home<br>
 *<br>
 * 其中modid:mob_task为资源路径，下一级的core，rest，sleep为活动路径，再下一级才是具体任务，具体要求为：<br>
 * 1. 当活动路径下只有一个具体任务时，循环执行该任务直到满足切换条件<br>
 * 2. 当活动路径下有多个具体任务时，依据优先级执行其中一个任务直到满足切换条件<br>
 * 3. 可以在任务状态机内部判断该任务是否应该被启动<br>
 */
public class TaskManager {
    public static final Tree<TaskEntry<? extends LivingEntity>> TASKS = new Tree<>();
    public static final ResourceLocation HUMAN_TASKS = GSKOUtil.withRL("human_tasks");

    public static void registerGSKOTasks() {
        registerTask(HUMAN_TASKS, Activity.CORE, "walk", new WalkRandomlyTask(0,0,0));
    }

    public static void registerTask(ResourceLocation key, Activity activity, String taskName, Task<? extends LivingEntity> task){
        String fullPath = key.toString() + "/" + activity.toString() + "/" + taskName;
        Tree.Node<TaskEntry<? extends LivingEntity>> node = TASKS.find(fullPath).defaultIfNull(() ->
                new Tree.Node<>(fullPath, new TaskEntry<>(key, activity, taskName, task),
                        new Tree.Node<>(key.toString(), null, null)));
        TASKS.addNode(fullPath, node.getData());
    }

    public static void registerTask(String fullPath, Task<? extends LivingEntity> task){
        String[] str = fullPath.split("/");
        ResourceLocation key = null;
        Activity activity = Activity.CORE;
        String taskName = "";

        switch (str.length){
            case 0:
                break;
            case 1:
                key = new ResourceLocation(str[0]);
                break;
            case 2:
                activity = ForgeRegistries.ACTIVITIES.getValue(new ResourceLocation(str[1]));
                break;
            case 3:
                taskName = str[2];
                break;
            default:
                taskName = String.join("/", Arrays.copyOfRange(str, 2, str.length));
                break;
        }
        if (key == null) return;
        registerTask(key, activity, taskName, task);
    }

    public static Task<? extends LivingEntity> nextTask(String fullPath)
    {
        TaskEntry<? extends LivingEntity> indexer = TaskEntry.parse(fullPath);
        return TASKS.getData(indexer.getFullPath()).getTask();
    }
}
