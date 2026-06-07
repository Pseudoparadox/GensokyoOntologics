package com.github.fictology.gensokyoontology.common.combat;

import com.github.fictology.gensokyoontology.common.entiy.misc.Danmaku;
import com.github.fictology.gensokyoontology.util.GSKOMathUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class DanmakuUtil {

    public static Vec3 getArchimedeSpiral(Vec3 prevVec, double radius, double angle) {
        return new Vec3(prevVec.x * radius * Math.cos(angle),
                prevVec.y, prevVec.z * radius * Math.signum(angle));
    }


    public static Vec3 rotateRandomAngle(Vec3 preVec, float yawBounds, float pitchBounds) {
        Vec3 nextVec = preVec.yRot(GSKOMathUtil.randomRange(0f, yawBounds));
        nextVec = nextVec.xRot(GSKOMathUtil.randomRange(0f, pitchBounds));
        return nextVec;
    }

    public static Vec3 getRandomPos(Vec3 center, Vector3f radius) {
        double x = GSKOMathUtil.randomRange(-radius.x(), radius.x());
        double y = GSKOMathUtil.randomRange(-radius.y(), radius.y());
        double z = GSKOMathUtil.randomRange(-radius.z(), radius.z());

        return new Vec3(center.x + x, center.y + y, center.z + z);
    }

    public static Vec3 getRandomPosWithin(float radius, Plane planeIn) {
        return getRandomPosWithin(new Vec3(radius, radius, radius), planeIn);
    }

    public static Vec3 getRandomPosWithin(Vec3 radius, Plane planeIn) {
        double x = GSKOMathUtil.randomRange(-radius.x(), radius.x());
        double y = GSKOMathUtil.randomRange(-radius.y(), radius.y());
        double z = GSKOMathUtil.randomRange(-radius.z(), radius.z());

        Vec3 vector3d = Vec3.ZERO;

        switch (planeIn) {
            case XY:
                vector3d = new Vec3(x, y, 0);
                break;
            case XZ:
                vector3d = new Vec3(x, 0, z);
                break;
            case YZ:
                vector3d = new Vec3(0, y, z);
                break;
            case XYZ:
                vector3d = new Vec3(x, y, z);
                break;
        }
        return vector3d;
    }

    public static Vec3 getAimingShootVec(LivingEntity thrower, LivingEntity target) {
        return target.getPosition(0f).subtract(thrower.getPosition(0f));
        // return new Vec3(target.getPosX() - thrower.getPosX(), target.getPosY() - thrower.getPosY() - offset, target.getPosZ() - thrower.getPosZ());
    }

    public static <D extends Danmaku> void shootWithRoseLine(D danmaku, Plane planeIn, Vec3 offsetRotation,
                                                             double radius, double count, double size, int density) {
        // List<Vec3> roseLinePos = getRoseLinePos(radius, count, size, density);
        // List<Vec2> shootVectors = new ArrayList<>();
        // roseLinePos.forEach(vector3d -> shootVectors.add(GSKOMathUtil.getEulerAngle(new Vec3(Vec3.ZP), vector3d)));
    }

    /**
     * 按照玫瑰线来初始化弹幕的位置和旋转
     *
     * @param count 玫瑰线花瓣/叶片的数量
     * @param size  玫瑰线花瓣的大小
     * @param delta 决定着玫瑰线上的弹幕之间的间隔
     */
    public static List<Vec3> getRoseLinePos(double radius, double count, double size, double delta) {
        double x, y;
        List<Vec3> positions = new ArrayList<>();

        count = count / size;

        for (double i = 0; i < 4 * Math.PI; i += delta) {

            double r = Math.sin(count * i);
            x = r * Math.cos(i) * radius;
            y = r * Math.sin(i) * radius;

            positions.add(new Vec3((float) x, (float) y, 0));

        }
        return positions;
    }

    public static List<Vec3> getHeartLinePos(float radius, double delta) {
        double t = 0;
        double maxT = 2 * Math.PI;
        List<Vec3> positions = new ArrayList<>();

        for (int i = 0; i < Math.ceil(maxT / delta); i++) {
            float x = (float) (16 * GSKOMathUtil.pow3(Math.sin(t)));
            float y = (float) (13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t));
            t += delta;
            positions.add(new Vec3(x * radius, y * radius, 0));
        }
        return positions;
    }

    public static List<Vec3> getStarLinePos(float radius, double t, Plane planeIn) {
        List<Vec3> positions = new ArrayList<>();

        for (int i = 0; i < Math.PI * 2; i += t) {
            double x = radius * GSKOMathUtil.pow3(Math.cos(t));
            double y = radius * GSKOMathUtil.pow3(Math.sin(t));

            switch (planeIn) {
                case XY:
                    positions.add(new Vec3(x, y, 0));
                    break;
                default:
                case XZ:
                    positions.add(new Vec3(x, 0, y));
                    break;
                case YZ:
                    positions.add(new Vec3(0, y, x));
                    break;
            }

        }
        return positions;
    }

    /**
     * 这里的旋转角度是弧度制
     *
     * @param prevPositions 之前的弹幕图案的坐标列表
     * @param yaw           对每一个坐标执行的 yaw 旋转角度
     * @param pitch         对每一个坐标执行的 pitch 旋转角度
     */
    public static List<Vec3> getRotatedPos(List<Vec3> prevPositions, float yaw, float pitch) {
        List<Vec3> newPos = new ArrayList<>();
        for (Vec3 prevPos : prevPositions) {
            newPos.add(prevPos.xRot(pitch).yRot(yaw));
        }
        return newPos;
    }

    public static List<Vec3> getParaboloidPos(Vec2 range, double a, double b, double delta) {
        List<Vec3> positions = new ArrayList<>();
        for (int i = 0; i < range.x; i += delta) {
            for (int j = 0; j < range.y; j += delta) {
                double z = GSKOMathUtil.pow2(i) / GSKOMathUtil.pow2(a) -
                        GSKOMathUtil.pow2(j) / GSKOMathUtil.pow2(b);
                positions.add(new Vec3(i, j, z));
            }
        }
        return positions;
    }

    public static List<Vec3> getEllipticParaboloidPos(Vec2 start, Vec2 end, double a, double b, double delta) {
        List<Vec3> positions = new ArrayList<>();
        return positions;
    }

    public static List<Vec3> spheroidPos(double radius, int count) {
        List<Vec3> coordinates = new ArrayList<>();
        List<Vec3> pos1 = ellipticPos(new Vec2(0, 0), radius, count);
        for (int i = 0; i < pos1.size(); i++) {
            for (int j = 0; j < pos1.size(); j++) {
                Vec3 vector3d = pos1.get(j).xRot((float) Math.PI * 2 / pos1.size() * j);
                pos1.set(j, vector3d);
            }
            coordinates.addAll(pos1);
        }
        return coordinates;
    }

    public static List<Vec3> ellipticPos(Vec2 center, double radius, int count) {
        ArrayList<Vec3> coordinates = new ArrayList<>();
        // 定义生成坐标的数量
        // 计算每个点的角度间隔
        double angleInterval = 2 * Math.PI / count;
        // 生成坐标
        for (int i = 0; i < count; i++) {
            double angle = i * angleInterval;
            double x = center.x + radius * Math.cos(angle);
            double y = center.y + radius * Math.sin(angle);
            coordinates.add(new Vec3(x, 0, y));
        }
        return coordinates;
    }

    public static Vec3 getAimedVec(LivingEntity shooter, LivingEntity target) {
        return target.getPosition(0f).subtract(shooter.getPosition(0f));
        // return new Vec3(target.getPosX() - shooter.getPosX(), target.getPosY() - shooter.getPosY(), target.getPosZ() - shooter.getPosZ());
    }

    public enum Plane {
        XZ,
        XY,
        YZ,
        XYZ
    }

}
