import java.io.*;

public class Utility {

    public void Compress(int[][][] pixels, String outputFileName) throws IOException {
        // QuadTree
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFileName))) { 
            QuadTree tree = new QuadTree(pixels);
            tree.buildTree(tree.root, 0);
            tree.pruneTree(tree.root);
            oos.writeObject(tree.root);
        }
    }

    public int[][][] Decompress(String inputFileName) throws IOException, ClassNotFoundException {
        // QuadTree
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFileName))) {
            Object object = ois.readObject();
    
            if (object instanceof QuadTreeNode) {
                QuadTreeNode root = (QuadTreeNode) object;
                int[][][] pixels = new int[root.width][root.height][3];
                fillPixels(root, pixels);
                return pixels;
            } else {
                throw new IOException("Invalid object type in the input file");
            }
        }
    }

    // QuadTree
    private void fillPixels(QuadTreeNode node, int[][][] pixels) {
        if (node.children == null) {
            for (int i = node.x; i < node.x + node.width; i++) {
                for (int j = node.y; j < node.y + node.height; j++) {
                    pixels[i][j][0] = node.getAvgColor().getRed();
                    pixels[i][j][1] = node.getAvgColor().getGreen();
                    pixels[i][j][2] = node.getAvgColor().getBlue();
                }
            }
        } else {
            for (QuadTreeNode child : node.children) {
                fillPixels(child, pixels);
            }
        }
    }
}