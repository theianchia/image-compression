import java.io.*;

public class Utility {

    public void Compress(int[][][] pixels, String outputFileName) throws IOException {
        // The following is a bad implementation that we have intentionally put in the function to make App.java run, you should 
        // write code to reimplement the function without changing any of the input parameters, and making sure the compressed file
        // gets written into outputFileName
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFileName))) { 
            QuadTreeNode root = QuadTreeNode.buildTree(pixels, 0, 0, pixels.length, pixels[0].length);
            oos.writeObject(root);
        }
    }

    public int[][][] Decompress(String inputFileName) throws IOException, ClassNotFoundException {
        // The following is a bad implementation that we have intentionally put in the function to make App.java run, you should 
        // write code to reimplement the function without changing any of the input parameters, and making sure that it returns
        // an int [][][]
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

    private void fillPixels(QuadTreeNode node, int[][][] pixels) {
        if (node.children[0] == null) {
            for (int i = node.x; i < node.x + node.width; i++) {
                for (int j = node.y; j < node.y + node.height; j++) {
                    pixels[i][j][0] = node.avgColor.getRed();
                    pixels[i][j][1] = node.avgColor.getGreen();
                    pixels[i][j][2] = node.avgColor.getBlue();                 
                }
            }
        } else {
            for (QuadTreeNode child : node.children) {
                fillPixels(child, pixels);
            }
        }
    }

}