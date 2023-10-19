import java.awt.*;
import java.io.Serializable;

public class QuadTreeNode implements Serializable {
    private static final long serialVersionUID = 1L;

    Color avgColor;
    QuadTreeNode[] children;
    int x, y, width, height;

    public QuadTreeNode(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
