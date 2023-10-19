import java.awt.*;
import java.io.Serializable;

class QuadTreeNode implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final double VARIANCE_THRESHOLD = 0.20;

    int x, y; // top-left corner of this node
    int width, height; // width and height of this node
    Color avgColor; // average color of the pixels within this node
    QuadTreeNode[] children; // four children if this node is split

    public QuadTreeNode(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        children = new QuadTreeNode[4];
    }

    public static QuadTreeNode buildTree(int[][][] pixels, int x, int y, int width, int height) {
        QuadTreeNode node = new QuadTreeNode(x, y, width, height);
        if (width == 1 || height == 1 || colorVarianceBelowThreshold(pixels, x, y, width, height)) {
            node.avgColor = computeAverageColor(pixels, x, y, width, height);
        } else {
            int halfWidth = width / 2;
            int halfHeight = height / 2;
            node.children[0] = buildTree(pixels, x, y, halfWidth, halfHeight);
            node.children[1] = buildTree(pixels, x + halfWidth, y, width - halfWidth, halfHeight);
            node.children[2] = buildTree(pixels, x, y + halfHeight, halfWidth, height - halfHeight);
            node.children[3] = buildTree(pixels, x + halfWidth, y + halfHeight, width - halfWidth, height - halfHeight);
        }
        return node;
    }

    private static boolean colorVarianceBelowThreshold(int[][][] pixels, int x, int y, int width, int height) {
        long redSum = 0, greenSum = 0, blueSum = 0;
        long redSquaredSum = 0, greenSquaredSum = 0, blueSquaredSum = 0;
        int totalPixels = width * height;

        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                redSum += pixels[i][j][0];
                greenSum += pixels[i][j][1];
                blueSum += pixels[i][j][2];
                redSquaredSum += pixels[i][j][0] * pixels[i][j][0];
                greenSquaredSum += pixels[i][j][1] * pixels[i][j][1];
                blueSquaredSum += pixels[i][j][2] * pixels[i][j][2];
            }
        }

        double redVariance = (redSquaredSum - (redSum * redSum) / totalPixels) / totalPixels;
        double greenVariance = (greenSquaredSum - (greenSum * greenSum) / totalPixels) / totalPixels;
        double blueVariance = (blueSquaredSum - (blueSum * blueSum) / totalPixels) / totalPixels;

        return redVariance < VARIANCE_THRESHOLD && greenVariance < VARIANCE_THRESHOLD
                && blueVariance < VARIANCE_THRESHOLD;
    }

    private static Color computeAverageColor(int[][][] pixels, int x, int y, int width, int height) {
        long redSum = 0, greenSum = 0, blueSum = 0;
        int totalPixels = width * height;

        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                redSum += pixels[i][j][0];
                greenSum += pixels[i][j][1];
                blueSum += pixels[i][j][2];
            }
        }

        int avgRed = (int) (redSum / totalPixels);
        int avgGreen = (int) (greenSum / totalPixels);
        int avgBlue = (int) (blueSum / totalPixels);

        return new Color(avgRed, avgGreen, avgBlue);
    }
}
