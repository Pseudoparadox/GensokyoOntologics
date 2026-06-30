package github.thelawf.gensokyoontology.api;

public class Color4f {
    public final float r;
    public final float g;
    public final float b;
    public final float a;

    public Color4f(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public float getRed() {
        return r;
    }

    public float getGreen() {
        return g;
    }

    public float getBlue() {
        return b;
    }

    public float getAlpha() {
        return a;
    }
}
