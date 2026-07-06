package github.thelawf.gensokyoontology.api;

import net.minecraft.block.BlockState;
import net.minecraft.block.StructureBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface IRayTracer {

    default AxisAlignedBB createCubeBox(Vector3d pos, int radius) {
        return new AxisAlignedBB(pos.subtract(new Vector3d(radius, radius, radius)), pos.add(new Vector3d(radius, radius, radius)));
    }

    /**
     * 计算线段与方形碰撞箱是否相交的检测函数
     *
     * @param start  线段起点
     * @param end    线段终点
     * @param boxMin 碰撞箱所有顶点中的最小坐标
     * @param boxMax 碰撞箱所有顶点中的最大坐标
     * @return 是否相交
     */
    default boolean isIntersecting(Vector3d start, Vector3d end, Vector3d boxMin, Vector3d boxMax) {
        // 计算射线的参数
        double tMin = (boxMin.x - start.x) / (end.x - start.x);
        double tMax = (boxMax.x - start.x) / (end.x - start.x);
        if (tMin > tMax) {
            double temp = tMin;
            tMin = tMax;
            tMax = temp;
        }
        double tyMin = (boxMin.y - start.y) / (end.y - start.y);
        double tyMax = (boxMax.y - start.y) / (end.y - start.y);
        if (tyMin > tyMax) {
            double temp = tyMin;
            tyMin = tyMax;
            tyMax = temp;
        }
        if ((tMin > tyMax) || (tyMin > tMax)) {
            return false;
        }
        if (tyMin > tMin) {
            tMin = tyMin;
        }
        if (tyMax < tMax) {
            tMax = tyMax;
        }
        double tzMin = (boxMin.z - start.z) / (end.z - start.z);
        double tzMax = (boxMax.z - start.z) / (end.z - start.z);

        if (tzMin > tzMax) {
            double temp = tzMin;
            tzMin = tzMax;
            tzMax = temp;
        }

        return (!(tMin > tzMax)) && (!(tzMin > tMax));
    }

    /**
     * 计算线段与方形碰撞箱是否相交的检测函数。
     *
     * @param start 线段起点
     * @param end   线段终点
     * @param aabb  碰撞箱
     * @return 是否相交
     */
    default boolean isIntersecting(Vector3d start, Vector3d end, AxisAlignedBB aabb) {
        return isIntersecting(start, end,
                new Vector3d(aabb.minX, aabb.minY, aabb.minZ),
                new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
    }

    default List<Entity> rayTrace(World world, Entity originEntity, float radius, Vector3d offset){
        List<Entity> tracedEntities = new ArrayList<>();
        Vector3d start = originEntity.getEyePosition(0f).add(offset);
        Vector3d end = originEntity.getLookVec().normalize().scale(radius).add(start);
        BlockRayTraceResult btr = world.rayTraceBlocks(new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, originEntity));
        end = btr.getHitVec();
        AxisAlignedBB range = originEntity.getBoundingBox().expand(end.subtract(start));
        List<RayTraceResult> rayTraces = new ArrayList<>();
        List<? extends Entity> entities = world.getEntitiesInAABBexcluding(originEntity, range, Entity::isAlive);
        for (Entity entity: entities) {
            Vector3d intersection = entity.getBoundingBox().rayTrace(start, end).orElse(null);
            if (intersection != null) {
                EntityRayTraceResult err = new EntityRayTraceResult(entity, intersection);
                rayTraces.add(err);
            }
        }

        if (rayTraces.isEmpty()) return new ArrayList<>();
        // rayTraces.sort((o1, o2) -> o1.getHitVec().distanceTo(start) < o2.getHitVec().distanceTo(start)? -1 : 1);
        RayTraceResult result = rayTraces.get(0);
        if (result instanceof EntityRayTraceResult) {
            tracedEntities.add(((EntityRayTraceResult) result).getEntity());
        }
        return tracedEntities;
    }

    default Optional<Entity> rayTrace(World world, Entity sourceEntity, Vector3d startVec, Vector3d endVec) {
        double closestDistance = startVec.distanceTo(endVec);
        // GSKOUtil.log(world.getEntitiesWithinAABB(Entity.class, sourceEntity.getBoundingBox().grow(startVec.distanceTo(endVec))).size());
        for (Entity entity : world.getEntitiesWithinAABB(Entity.class, sourceEntity.getBoundingBox().grow(startVec.distanceTo(endVec)))) {
            if (entity != sourceEntity) {
                AxisAlignedBB entityAABB = entity.getBoundingBox();
                Optional<Vector3d> result = entityAABB.rayTrace(startVec, endVec);

                if (result.isPresent()) {
                    double distance = startVec.squareDistanceTo(result.get());

                    if (distance < closestDistance) {
                        return Optional.of(entity);
                    }
                }
            }
        }
        return Optional.empty();
    }

    static List<Entity> rayCast(World world, Entity originEntity, float radius, Vector3d offset){
        List<Entity> tracedEntities = new ArrayList<>();
        Vector3d start = originEntity.getEyePosition(0f).add(offset);
        Vector3d end = originEntity.getLookVec().normalize().scale(radius).add(start);
        BlockRayTraceResult btr = world.rayTraceBlocks(new RayTraceContext(start, end,
                RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, originEntity));
        end = btr.getHitVec();
        AxisAlignedBB range = originEntity.getBoundingBox().expand(end.subtract(start));
        List<RayTraceResult> rayTraces = new ArrayList<>();
        List<? extends Entity> entities = world.getEntitiesInAABBexcluding(originEntity, range, Entity::isAlive);
        for (Entity entity: entities) {
            Vector3d intersection = entity.getBoundingBox().rayTrace(start, end).orElse(null);
            if (intersection != null) {
                EntityRayTraceResult err = new EntityRayTraceResult(entity, intersection);
                rayTraces.add(err);
            }
        }

        if (rayTraces.isEmpty()) return new ArrayList<>();
        // rayTraces.sort((o1, o2) -> o1.getHitVec().distanceTo(start) < o2.getHitVec().distanceTo(start)? -1 : 1);
        RayTraceResult result = rayTraces.get(0);
        if (result instanceof EntityRayTraceResult) {
            tracedEntities.add(((EntityRayTraceResult) result).getEntity());
        }
        return tracedEntities;
    }

    static Optional<Entity> rayCast(World world, Entity sourceEntity, Vector3d startVec, Vector3d endVec){
        double closestDistance = startVec.distanceTo(endVec);
        // GSKOUtil.log(world.getEntitiesWithinAABB(Entity.class, sourceEntity.getBoundingBox().grow(startVec.distanceTo(endVec))).size());
        for (Entity entity : world.getEntitiesWithinAABB(Entity.class, sourceEntity.getBoundingBox().grow(startVec.distanceTo(endVec)))) {
            if (entity != sourceEntity) {
                AxisAlignedBB entityAABB = entity.getBoundingBox();
                Optional<Vector3d> result = entityAABB.rayTrace(startVec, endVec);

                if (result.isPresent()) {
                    double distance = startVec.squareDistanceTo(result.get());

                    if (distance < closestDistance) {
                        return Optional.of(entity);
                    }
                }
            }
        }
        return Optional.empty();
    }

    default Vector3d getIntersectedPos(Vector3d start, Vector3d end, AxisAlignedBB aabb) {
        return getIntersectedPos(start, end,
                new Vector3d(aabb.minX, aabb.minY, aabb.minZ),
                new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
    }

    default Vector3d getIntersectedPos(Vector3d start, Vector3d end, Vector3d boxMin, Vector3d boxMax) {
        // 计算射线的参数
        double tMin = (boxMin.x - start.x) / (end.x - start.x);
        double tMax = (boxMax.x - start.x) / (end.x - start.x);
        if (tMin > tMax) {
            double temp = tMin;
            tMin = tMax;
            tMax = temp;
        }
        double tyMin = (boxMin.y - start.y) / (end.y - start.y);
        double tyMax = (boxMax.y - start.y) / (end.y - start.y);
        if (tyMin > tyMax) {
            double temp = tyMin;
            tyMin = tyMax;
            tyMax = temp;
        }
        if ((tMin > tyMax) || (tyMin > tMax)) {
            return Vector3d.ZERO;
        }
        if (tyMin > tMin) {
            tMin = tyMin;
        }
        if (tyMax < tMax) {
            tMax = tyMax;
        }
        double tzMin = (boxMin.z - start.z) / (end.z - start.z);
        double tzMax = (boxMax.z - start.z) / (end.z - start.z);

        if (tzMin > tzMax) {
            double temp = tzMin;
            tzMin = tzMax;
            tzMax = temp;
        }

        if ((tMin > tzMax) || (tzMin > tMax)) {
            return Vector3d.ZERO;
        }

        if (tyMin > tMin) {
            tMin = tyMin;
        }
        if (tyMax < tMax) {
            tMax = tyMax;
        }

        return new Vector3d(
                start.x + tMin * (end.x - start.x),
                start.y + tMin * (end.y - start.y),
                start.z + tMin * (end.z - start.z));
    }

    /**
     * 以传入的碰撞箱体的中心为圆心，获取所有位于这个球形的碰撞区域以内的生物。
     *
     * @param worldIn     世界
     * @param entityClass 生物的类
     * @param radius      球形的半径
     * @param aabb        碰撞箱
     * @return 位于球形碰撞箱内的生物的列表
     */
    default <T extends Entity> List<T> getEntityWithinSphere(World worldIn, Class<? extends T> entityClass,
                                                             AxisAlignedBB aabb, float radius) {
        return worldIn.getEntitiesWithinAABB(entityClass, aabb).stream()
                .filter(t -> aabb.getCenter().distanceTo(t.getPositionVec()) <= radius)
                .collect(Collectors.toList());
    }

    default Vector3d getLookEnd(Vector3d startPos, Vector3d lookVec, double eyeHeight, double distance) {
        return lookVec.scale(distance).add(startPos.add(0,eyeHeight,0));
    }

    default Vector3d getAimedVec(LivingEntity source, Entity target) {
        return target.getPositionVec().subtract(source.getPositionVec());
    }

    /**
     * 以传入的碰撞箱体的中心为圆心，获取所有位于这个球形的碰撞区域以内，以及同时满足其它条件的生物。
     *
     * @param worldIn     世界
     * @param entityClass 生物的类
     * @param radius      球形的半径
     * @param predicate   其它必要条件
     * @param aabb        碰撞箱
     * @return 位于球形碰撞箱内且满足其它条件的所有生物的列表
     */
    default <T extends Entity> List<T> getEntityWithinSphere(World worldIn, Class<? extends T> entityClass,
                                                             Predicate<? super T> predicate, AxisAlignedBB aabb, float radius) {
        return worldIn.getEntitiesWithinAABB(entityClass, aabb).stream()
                .filter(t -> aabb.getCenter().distanceTo(t.getPositionVec()) <= radius && predicate.test(t))
                .collect(Collectors.toList());
    }

    default boolean checkRayHitBox(AxisAlignedBB aabb, Vector3d from, Vector3d to){
        double max = Double.MAX_VALUE;
        Optional<Vector3d> optional = aabb.rayTrace(from, to);
        if (optional.isPresent()) {
            double distance = from.squareDistanceTo(optional.get());
            return distance < max;
        }
        return false;
    }
}
