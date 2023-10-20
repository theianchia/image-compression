import java.awt.Color;
import java.io.Serializable;

public class QuadTreeNode implements Serializable {
    private static final long serialVersionUID = 1L;

    private int avgColor;
    QuadTreeNode[] children;
    short x, y, width, height;

    public QuadTreeNode(int x, int y, int width, int height) {
        this.x = (short) x;
        this.y = (short) y;
        this.width = (short) width;
        this.height = (short) height;
    }

    public void setAvgColor(Color color) {
        this.avgColor = (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
    }

    public Color getAvgColor() {
        int r = (avgColor >> 16) & 0xFF;
        int g = (avgColor >> 8) & 0xFF;
        int b = avgColor & 0xFF;
        return new Color(r, g, b);
    }
}
