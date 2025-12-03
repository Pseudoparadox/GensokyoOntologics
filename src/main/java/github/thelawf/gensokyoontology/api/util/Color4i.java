package github.thelawf.gensokyoontology.api.util;


public class Color4i {
    public final int r;
    public final int g;
    public final int b;
    public final int a;

    public static final Color4i RED = new Color4i(255, 0, 0, 255);
    public static final Color4i GREEN = new Color4i(0, 255, 0, 255);
    public static final Color4i BLUE = new Color4i(0, 0, 255, 255);
    public static final Color4i YELLOW = new Color4i(255, 255, 0, 255);
    public static final Color4i MAGENTA = new Color4i(0, 255, 255, 255);
    public static final Color4i CYAN = new Color4i(0, 0, 255, 255);
    public static final Color4i WHITE = new Color4i(255, 255, 255, 255);
    public static final Color4i BLACK = new Color4i(0, 0, 0, 255);

    public Color4i(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static Color4i of(int r, int g, int b, int a) {
        return new Color4i(r, g, b, a);
    }

    public Color4i copy() {
        return new Color4i(this.r, this.g, this.b, this.a);
    }

    public Color4f toColor4f(){
        return new Color4f(r / 255F, g / 255F, b / 255F, a / 255F);
    }
}
