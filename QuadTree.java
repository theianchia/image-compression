import java.awt.*;
import java.io.Serializable;

public class QuadTree implements Serializable {
    private static final int MAX_DEPTH = 5;
    private static final double VARIANCE_THRESHOLD = 10.0;
    private static final double COLOR_MERGE_THRESHOLD = 10.0;

    QuadTreeNode root;
    int[][][] pixels;

    public QuadTree(int[][][] pixels) {
        this.pixels = pixels;
        this.root = new QuadTreeNode(0, 0, pixels.length, pixels[0].length);
    }

    public void buildTree(QuadTreeNode node, int depth) {
        Color rgb = computeAverageColor(node.x, node.y, node.width, node.height);
        node.setAvgColor(rgb);

        if (node.width <= 1 || node.height <= 1 || depth >= MAX_DEPTH
                || colorVarianceBelowThreshold(node.x, node.y, node.width, node.height)) {
            return;
        }

        node.children = new QuadTreeNode[4];
        int halfWidth = node.width / 2;
        int halfHeight = node.height / 2;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int newWidth = halfWidth + (i == 1 ? node.width % 2 : 0);
                int newHeight = halfHeight + (j == 1 ? node.height % 2 : 0);
                node.children[i * 2 + j] = new QuadTreeNode(
                        node.x + i * halfWidth,
                        node.y + j * halfHeight,
                        newWidth,
                        newHeight);

                buildTree(node.children[i * 2 + j], depth + 1);
            }
        }
    }

    public void pruneTree(QuadTreeNode node) {
        if (node.children == null) return;

        boolean canMerge = true;

        Color avgColor = node.getAvgColor();
        for (QuadTreeNode child : node.children) {
            pruneTree(child);
            if (colorDistance(avgColor, child.getAvgColor()) > COLOR_MERGE_THRESHOLD) {
                canMerge = false;
                break;
            }
        }

        if (canMerge) {
            node.children = null;  // Remove child nodes to merge
        }
    }

    private double colorDistance(Color c1, Color c2) {
        int redDiff = c1.getRed() - c2.getRed();
        int greenDiff = c1.getGreen() - c2.getGreen();
        int blueDiff = c1.getBlue() - c2.getBlue();
        
        return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
    }

    private boolean colorVarianceBelowThreshold(int x, int y, int width, int height) {
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

    private Color computeAverageColor(int x, int y, int width, int height) {
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
