package com.github.fictology.gensokyoontology.util.api;

import com.github.fictology.gensokyoontology.util.GSKOMathUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface IRayTraceReader {

    default List<List<AABB>> getRayTraceBox(Vec3 globalPos, Vec3 rayDirection, int length, float size) {
        List<List<AABB>> boxes = new ArrayList<>();
        List<AABB> aabb = new ArrayList<>();
        for (int i = 0; i < 50; i++) {

            Vec3 posRow = new Vec3(rayDirection.x > 0 ? Vector3f.XP.cast() : Vector3f.XN.cast());
            Vec3 posColumn = new Vec3(rayDirection.z > 0 ? Vector3f.ZP.cast() : Vector3f.ZN.cast());
            Vec3 posVertical = new Vec3(rayDirection.y > 0 ? Vector3f.YP.cast() : Vector3f.YN.cast());

            Vec3 rayPos = globalPos.add(rayDirection);

            AABB aabb0 = new AABB(GSKOMathUtil.vecFloor(rayPos), GSKOMathUtil.vecCeil(rayPos));
            AABB aabb1 = new AABB(GSKOMathUtil.vecFloor(rayPos.add(posRow)), GSKOMathUtil.vecCeil(rayPos.add(posRow)));
            AABB aabb2 = new AABB(GSKOMathUtil.vecFloor(rayPos.add(posColumn)), GSKOMathUtil.vecCeil(rayPos.add(posColumn)));
            AABB aabb3 = new AABB(GSKOMathUtil.vecFloor(rayPos.add(posVertical)), GSKOMathUtil.vecCeil(rayPos.add(posVertical)));

            aabb.add(aabb0.inflate(size));
            aabb.add(aabb1.inflate(size));
            aabb.add(aabb2.inflate(size));
            aabb.add(aabb3.inflate(size));

            boxes.add(aabb);
        }
        return boxes;
    }

    default AABB createCubeBox(Vec3 pos, int radius) {
        return new AABB(pos.subtract(new Vec3(radius, radius, radius)), pos.add(new Vec3(radius, radius, radius)));
    }

    default List<Entity> rayTrace(Level world, Entity originEntity, float radius, Vec3 offset) {
        List<Entity> tracedEntities = new ArrayList<>();
        Vec3 start = originEntity.getEyePosition(0f).add(offset);
        Vec3 end = originEntity.getLookAngle().normalize().scale(radius).add(start);
        BlockHitResult bhr = world.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, originEntity));
        end = bhr.getLocation();
        AABB range = originEntity.getBoundingBox().expandTowards(end.subtract(start));
        List<HitResult> rayTraces = new ArrayList<>();
        List<? extends Entity> entities = world.getEntities(originEntity, range, Entity::isAlive);
        for (Entity entity : entities) {
            Vec3 intersection = entity.getBoundingBox().clip(start, end).orElse(null);
            if (intersection != null) {
                EntityHitResult ehr = new EntityHitResult(entity, intersection);
                rayTraces.add(ehr);
            }
        }

        if (rayTraces.isEmpty()) return new ArrayList<>();
        // rayTraces.sort((o1, o2) -> o1.getHitVec().distanceTo(start) < o2.getHitVec().distanceTo(start)? -1 : 1);
        HitResult result = rayTraces.get(0);
        if (result instanceof EntityHitResult res) {
            tracedEntities.add(res.getEntity());
        }
        return tracedEntities;
    }

    default Optional<Entity> rayTrace(Level world, Entity sourceEntity, Vec3 startVec, Vec3 endVec) {
        double closestDistance = startVec.distanceTo(endVec);
        // GSKOUtil.log(world.getEntitiesWithinAABB(Entity.class, sourceEntity.getBoundingBox().inflate(startVec.distanceTo(endVec))).size());
        for (Entity entity : world.getEntitiesOfClass(Entity.class, sourceEntity.getBoundingBox().inflate(startVec.distanceTo(endVec)))) {
            if (entity != sourceEntity) {
                AABB entityAABB = entity.getBoundingBox();
                Optional<Vec3> result = entityAABB.clip(startVec, endVec);

                if (result.isPresent()) {
                    double distance = startVec.distanceToSqr(result.get());

                    if (distance < closestDistance) {
                        return Optional.of(entity);
                    }
                }
            }
        }
        return Optional.empty();
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
    default <T extends Entity> List<T> getEntityWithinSphere(Level worldIn, Class<? extends T> entityClass,
                                                             AABB aabb, float radius) {
        return worldIn.getEntitiesOfClass(entityClass, aabb).stream()
                .filter(t -> aabb.getCenter().distanceTo(t.getPosition(0)) <= radius)
                .collect(Collectors.toList());
    }

    default Vec3 getAimedVec(LivingEntity source, Entity target) {
        return target.getPosition(0).subtract(source.getPosition(0));
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
    default <T extends Entity> List<T> getEntityWithinSphere(Level worldIn, Class<? extends T> entityClass,
                                                             Predicate<? super T> predicate, AABB aabb, float radius) {
        return worldIn.getEntitiesOfClass(entityClass, aabb).stream()
                .filter(t -> aabb.getCenter().distanceTo(t.getPosition(0)) <= radius && predicate.test(t))
                .collect(Collectors.toList());
    }
}
