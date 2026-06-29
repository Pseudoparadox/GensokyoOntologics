package github.thelawf.gensokyoontology.common.util.math;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class RotMatrix {
    private float m00, m01, m02,
                  m10, m11, m12,
                  m20, m21, m22;

    public static final RotMatrix IDENTITY = new RotMatrix(1F, 0F, 0F,
                                                           0F, 1F, 0F,
                                                           0F, 0F, 1F);
    public RotMatrix() {
        this(1F, 0F, 0F,
                0F, 1F, 0F,
                0F, 0F, 1F);
    }

    public RotMatrix(Quaternion quaternionIn) {
        float f = quaternionIn.getX();
        float f1 = quaternionIn.getY();
        float f2 = quaternionIn.getZ();
        float f3 = quaternionIn.getW();
        float f4 = 2.0F * f * f;
        float f5 = 2.0F * f1 * f1;
        float f6 = 2.0F * f2 * f2;
        this.m00 = 1.0F - f5 - f6;
        this.m11 = 1.0F - f6 - f4;
        this.m22 = 1.0F - f4 - f5;
        float f7 = f * f1;
        float f8 = f1 * f2;
        float f9 = f2 * f;
        float f10 = f * f3;
        float f11 = f1 * f3;
        float f12 = f2 * f3;
        this.m10 = 2.0F * (f7 + f12);
        this.m01 = 2.0F * (f7 - f12);
        this.m20 = 2.0F * (f9 - f11);
        this.m02 = 2.0F * (f9 + f11);
        this.m21 = 2.0F * (f8 + f10);
        this.m12 = 2.0F * (f8 - f10);
    }

    public static RotMatrix from(Quaternion quaternionIn) {
        return new RotMatrix(quaternionIn);
    }

    public static RotMatrix from(EulerAngle eulerAngleIn) {
        return new RotMatrix(GSKOMathUtil.toQuaternion(eulerAngleIn));
    }

    // 添加setColumn方法
    public void setColumn(int columnIndex, Vector3d vector) {
        switch (columnIndex) {
            case 0: // X轴（法线）
                this.m00 = (float) vector.x;
                this.m10 = (float) vector.y;
                this.m20 = (float) vector.z;
                break;
            case 1: // Y轴（副法线）
                this.m01 = (float) vector.x;
                this.m11 = (float) vector.y;
                this.m21 = (float) vector.z;
                break;
            case 2: // Z轴（切线）
                this.m02 = (float) vector.x;
                this.m12 = (float) vector.y;
                this.m22 = (float) vector.z;
                break;
            default:
                throw new IllegalArgumentException("Column index must be 0, 1, or 2");
        }
    }

    // 添加setColumn方法的重载版本，接受Vector3f
    public void setColumn(int columnIndex, Vector3f vector) {
        switch (columnIndex) {
            case 0: // X轴（法线）
                this.m00 = vector.getX();
                this.m10 = vector.getY();
                this.m20 = vector.getZ();
                break;
            case 1: // Y轴（副法线）
                this.m01 = vector.getX();
                this.m11 = vector.getY();
                this.m21 = vector.getZ();
                break;
            case 2: // Z轴（切线）
                this.m02 = vector.getX();
                this.m12 = vector.getY();
                this.m22 = vector.getZ();
                break;
            default:
                throw new IllegalArgumentException("Column index must be 0, 1, or 2");
        }
    }

    public static RotMatrix from(Vector3d tangent, Vector3d upReference) {
        if (tangent.lengthSquared() < 1e-5 || upReference.lengthSquared() < 1e-5) {
            return IDENTITY;
        }

        tangent = tangent.normalize();

        Vector3d binormal = tangent.crossProduct(upReference);

        if (binormal.lengthSquared() < 1e-5) {
            Vector3d altReference = Math.abs(tangent.dotProduct(new Vector3d(1, 0, 0))) > 0.9 ?
                    new Vector3d(0, 1, 0) : new Vector3d(1, 0, 0);
            binormal = tangent.crossProduct(altReference);
        }

        binormal = binormal.normalize();
        Vector3d normal = binormal.crossProduct(tangent).normalize();
        return new RotMatrix(
                (float) normal.x, (float) binormal.x, (float) tangent.x,
                (float) normal.y, (float) binormal.y, (float) tangent.y,
                (float) normal.z, (float) binormal.z, (float) tangent.z
        );
    }

    public static RotMatrix from(Vector3d tangent, Vector3d normal, Vector3d binormal) {
        tangent = tangent.normalize();

        double dotTN = tangent.dotProduct(normal);
        normal = normal.subtract(tangent.scale(dotTN)).normalize();
        binormal = tangent.crossProduct(normal).normalize();

        if (Math.abs(tangent.dotProduct(normal)) > 1e-5 ||
                Math.abs(tangent.dotProduct(binormal)) > 1e-5 ||
                Math.abs(normal.dotProduct(binormal)) > 1e-5) {
            binormal = tangent.crossProduct(normal).normalize();
        }

        RotMatrix matrix = RotMatrix.IDENTITY;

        matrix.m00 = (float) normal.x;
        matrix.m10 = (float) normal.y;
        matrix.m20 = (float) normal.z;

        matrix.m01 = (float) binormal.x;
        matrix.m11 = (float) binormal.y;
        matrix.m21 = (float) binormal.z;

        matrix.m02 = (float) tangent.x;
        matrix.m12 = (float) tangent.y;
        matrix.m22 = (float) tangent.z;

        return matrix.copy();
    }

    RotMatrix(float m00, float m01, float m02,
              float m10, float m11, float m12,
              float m20, float m21, float m22) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
    }

    public void set(RotMatrix matrix) {
        this.m00 = matrix.m00;
        this.m01 = matrix.m01;
        this.m02 = matrix.m02;
        this.m10 = matrix.m10;
        this.m11 = matrix.m11;
        this.m12 = matrix.m12;
        this.m20 = matrix.m20;
        this.m21 = matrix.m21;
        this.m22 = matrix.m22;
    }

    public RotMatrix set(float m00, float m01, float m02,
                         float m10, float m11, float m12,
                         float m20, float m21, float m22) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        return this;
    }

    public RotMatrix copy() {
        return new RotMatrix(
                this.m00, this.m01, this.m02,
                this.m10, this.m11, this.m12,
                this.m20, this.m21, this.m22);
    }

    public static RotMatrix fromXHandle(float rotationX) {
        // 将角度转换为弧度
        float angleRad = (float) Math.toRadians(rotationX);

        // 根据旋转轴创建旋转矩阵
        RotMatrix matrix = RotMatrix.IDENTITY;
        float cos = (float) Math.cos(angleRad);
        float sin = (float) Math.sin(angleRad);

        matrix.set(1, 0, 0,
                   0,      cos,    -sin,
                   0,      sin,    cos);

        return matrix;
    }

    public static RotMatrix fromYHandle(float rotationY) {
        // 将角度转换为弧度
        float angleRad = (float) Math.toRadians(rotationY);

        // 根据旋转轴创建旋转矩阵
        RotMatrix matrix = RotMatrix.IDENTITY;
        float cos = (float) Math.cos(angleRad);
        float sin = (float) Math.sin(angleRad);

        matrix.set(cos, 0, sin,
                0, 1, 0,
                  -sin, 0, cos);

        return matrix;
    }

    public static RotMatrix fromZHandle(float rotationZ) {
        float angleRad = (float) Math.toRadians(rotationZ);
        RotMatrix matrix = RotMatrix.IDENTITY;
        float cos = (float) Math.cos(angleRad);
        float sin = (float) Math.sin(angleRad);

        matrix.set(cos, -sin, 0,
                sin, cos, 0,
                0, 0, 1);
        return matrix;
    }


    public Vector3d multiply(Vector3d vector) {
        double x = this.m00 * vector.x + this.m01 * vector.y + this.m02 * vector.z;
        double y = this.m10 * vector.x + this.m11 * vector.y + this.m12 * vector.z;
        double z = this.m20 * vector.x + this.m21 * vector.y + this.m22 * vector.z;
        return new Vector3d(x, y, z);
    }


    public Quaternion toQuaternion() {
        // 使用Minecraft的Quaternion构造方法
        // 注意：Minecraft的Quaternion类没有直接从矩阵构造的方法，所以需要自己转换
        // 这里使用四元数公式
        float trace = this.m00 + this.m11 + this.m22;
        float w, x, y, z;
        if (trace > 0) {
            float s = 0.5f / (float)Math.sqrt(trace + 1.0f);
            w = 0.25f / s;
            x = (this.m21 - this.m12) * s;
            y = (this.m02 - this.m20) * s;
            z = (this.m10 - this.m01) * s;
        } else if (this.m00 > this.m11 && this.m00 > this.m22) {
            float s = 2.0f * (float)Math.sqrt(1.0f + this.m00 - this.m11 - this.m22);
            w = (this.m21 - this.m12) / s;
            x = 0.25f * s;
            y = (this.m01 + this.m10) / s;
            z = (this.m02 + this.m20) / s;
        } else if (this.m11 > this.m22) {
            float s = 2.0f * (float)Math.sqrt(1.0f + this.m11 - this.m00 - this.m22);
            w = (this.m02 - this.m20) / s;
            x = (this.m01 + this.m10) / s;
            y = 0.25f * s;
            z = (this.m12 + this.m21) / s;
        } else {
            float s = 2.0f * (float)Math.sqrt(1.0f + this.m22 - this.m00 - this.m11);
            w = (this.m10 - this.m01) / s;
            x = (this.m02 + this.m20) / s;
            y = (this.m12 + this.m21) / s;
            z = 0.25f * s;
        }
        return new Quaternion(x, y, z, w);
    }

    public EulerAngle toEulerAngle() {
        float pitch, yaw, roll;

        // 从旋转矩阵中提取欧拉角（使用Y-X-Z顺序）
        pitch = (float) Math.asin(-this.m12);
        yaw = (float) Math.atan2(this.m02, this.m22);
        roll = (float) Math.atan2(this.m10, this.m11);

        // 转换为角度
        pitch = (float) Math.toDegrees(pitch);
        yaw = (float) Math.toDegrees(yaw);
        roll = (float) Math.toDegrees(roll);

        // 确保角度在合适范围内
        yaw = MathHelper.wrapDegrees(yaw);
        roll = MathHelper.wrapDegrees(roll);

        return EulerAngle.of(yaw, pitch, roll);
    }

    public Vector3d toHandleValue() {
        double x = Math.toDegrees(Math.atan2(this.m12, this.m22));
        double y = Math.toDegrees(Math.atan2(this.m02, this.m22));
        double z = Math.toDegrees(Math.atan2(this.m10, this.m00));
        return new Vector3d(x, y, z);
    }

    /**
     * @return Z轴方向的单位向量当前的位置，在轨道中用作切线
     */
    public Vector3f tangent() {
        return new Vector3f(this.m02, this.m12, this.m22);
    }

    /**
     * @return X轴方向的单位向量当前的位置，在轨道中用作法线
     */
    public Vector3f normal() {
        return new Vector3f(this.m00, this.m10, this.m20);
    }

    /**
     * @return Y轴方向的单位向量当前的位置，在轨道中暂无作用
     */
    public Vector3f binormal() {
        return new Vector3f(this.m00, this.m11, this.m21);
    }
}
