/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.AcyclicSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private int[][] rgb;
    private double[][] energy;
    private int width;
    private int height;


    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("picture is null");
        }
        width = picture.width();
        height = picture.height();
        rgb = new int[width][height];
        energy = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rgb[i][j] = picture.getRGB(i, j);
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                energy[i][j] = energyCompute(i, j);
            }
        }

    }

    public Picture picture() {
        Picture picture = new Picture(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                picture.setRGB(i, j, rgb[i][j]);
            }
        }
        return picture;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    private double energyCompute(int i, int j) {
        if (i == 0 || i == width - 1 || j == 0 || j == height - 1) {
            return 1000;
        }
        else {
            int dxRight = rgb[i + 1][j];
            int dxLeft = rgb[i - 1][j];
            int dxr = ((dxRight >> 16) & 0xFF) - ((dxLeft >> 16) & 0xFF);
            int dxg = ((dxRight >> 8) & 0xFF) - ((dxLeft >> 8) & 0xFF);
            int dxb = ((dxRight) & 0xFF) - ((dxLeft) & 0xFF);
            int dx2 = dxr * dxr + dxg * dxg + dxb * dxb;
            int dyAbove = rgb[i][j + 1];
            int dyBelow = rgb[i][j - 1];
            int dyr = ((dyAbove >> 16) & 0xFF) - ((dyBelow >> 16) & 0xFF);
            int dyg = ((dyAbove >> 8) & 0xFF) - ((dyBelow >> 8) & 0xFF);
            int dyb = ((dyAbove) & 0xFF) - ((dyBelow) & 0xFF);
            int dy2 = dyr * dyr + dyg * dyg + dyb * dyb;
            return Math.sqrt(dx2 + dy2);
        }
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= width) {
            throw new IllegalArgumentException("x is out of range");
        }
        if (y < 0 || y >= height) {
            throw new IllegalArgumentException("y is out of range");
        }
        return energy[x][y];
    }

    public int[] findHorizontalSeam() {
        int numOfpixel = width * height;
        EdgeWeightedDigraph verticalGraph = new EdgeWeightedDigraph(numOfpixel + 2);
        for (int i = 1; i <= height; i++) {
            verticalGraph.addEdge(new DirectedEdge(0, i, energy[0][i - 1]));
        }
        for (int i = (width - 1) * (height) + 1, j = 0; i <= numOfpixel; i++, j++) {
            verticalGraph.addEdge(new DirectedEdge(i, numOfpixel + 1, 0));
        }
        for (int i = 1; i < width; i++) {
            for (int j = 1; j <= height; j++) {
                if (j != 1) {
                    verticalGraph.addEdge(new DirectedEdge((i - 1) * height + j, i * height + j - 1,
                                                           energy[i][j - 2]));
                }
                verticalGraph.addEdge(new DirectedEdge((i - 1) * height + j, i * height + j,
                                                       energy[i][j - 1]));
                if (j != height) {
                    verticalGraph.addEdge(new DirectedEdge((i - 1) * height + j, i * height + j + 1,
                                                           energy[i][j]));
                }
            }
        }
        AcyclicSP sp = new AcyclicSP(verticalGraph, 0);
        int[] seam = new int[width];
        int i = 0;
        for (DirectedEdge e : sp.pathTo(numOfpixel + 1)) {
            if (i < width) {
                int row = e.to() % height;
                if (row == 0) {
                    seam[i] = height - 1;
                }
                else seam[i] = row - 1;
                i++;
            }
        }
        return seam;
    }

    public int[] findVerticalSeam() {
        int numOfpixel = width * height;
        EdgeWeightedDigraph verticalGraph = new EdgeWeightedDigraph(numOfpixel + 2);
        for (int i = 1; i <= width; i++) {
            verticalGraph.addEdge(new DirectedEdge(0, i, energy[i - 1][0]));
        }
        for (int i = width * (height - 1) + 1, j = 0; i <= numOfpixel; i++, j++) {
            verticalGraph.addEdge(new DirectedEdge(i, numOfpixel + 1, 0));
        }
        for (int i = 1; i < height; i++) {
            for (int j = 1; j <= width; j++) {
                if (j != 1) {
                    verticalGraph.addEdge(new DirectedEdge((i - 1) * width + j, i * width + j - 1,
                                                           energy[j - 2][i]));
                }
                verticalGraph.addEdge(new DirectedEdge((i - 1) * width + j, i * width + j,
                                                       energy[j - 1][i]));
                if (j != width) {
                    verticalGraph.addEdge(new DirectedEdge((i - 1) * width + j, i * width + j + 1,
                                                           energy[j][i]));
                }
            }
        }
        AcyclicSP sp = new AcyclicSP(verticalGraph, 0);
        int[] seam = new int[height];
        int i = 0;
        for (DirectedEdge e : sp.pathTo(numOfpixel + 1)) {
            if (i < height) {
                int column = e.to() % width;
                if (column == 0) {
                    seam[i] = width - 1;
                }
                else seam[i] = column - 1;
                i++;
            }
        }
        return seam;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("seam is null");
        }
        if (seam.length != width) {
            throw new IllegalArgumentException("seam's length is wrong");
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= height) {
                throw new IllegalArgumentException("seam[ " + i + " ] is out of range ");
            }
            if (i + 1 < seam.length && (seam[i + 1] > seam[i] + 1 || seam[i + 1] < seam[i] - 1)) {
                throw new IllegalArgumentException("seam is not adjacent");
            }
        }
        int[][] rgbRemove = new int[width][height - 1];
        double[][] energyRemove = new double[width][height - 1];
        for (int i = 0; i < width; i++) {
            System.arraycopy(rgb[i], 0, rgbRemove[i], 0, seam[i]);
            System.arraycopy(rgb[i], seam[i] + 1, rgbRemove[i], seam[i], height - seam[i] - 1);
            System.arraycopy(energy[i], 0, energyRemove[i], 0, seam[i]);
            System.arraycopy(energy[i], seam[i] + 1, energyRemove[i], seam[i],
                             height - seam[i] - 1);
        }
        rgb = rgbRemove;
        height--;
        for (int i = 0; i < width; i++) {
            if (seam[i] - 1 > 0) {
                energyRemove[i][seam[i] - 1] = energyCompute(i, seam[i] - 1);
            }
            if (seam[i] < height) {
                energyRemove[i][seam[i]] = energyCompute(i, seam[i]);
            }
        }
        energy = energyRemove;
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("seam is null");
        }
        if (seam.length != height) {
            throw new IllegalArgumentException("seam's length is wrong");
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width) {
                throw new IllegalArgumentException("seam[ " + i + " ] is out of range ");
            }
            if (i + 1 < seam.length && (seam[i + 1] > seam[i] + 1 || seam[i + 1] < seam[i] - 1)) {
                throw new IllegalArgumentException("seam is not adjacent");
            }
        }
        int[][] rgbRemove = new int[width - 1][height];
        double[][] energyRemove = new double[width - 1][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0, k = 0; j < width; j++) {
                if (j != seam[i]) {
                    rgbRemove[k][i] = rgb[j][i];
                    energyRemove[k][i] = energy[j][i];
                    k++;
                }
            }
        }
        rgb = rgbRemove;
        width--;
        for (int i = 0; i < height; i++) {
            if (seam[i] - 1 > 0) {
                energyRemove[seam[i] - 1][i] = energyCompute(seam[i] - 1, i);
            }
            if (seam[i] < width) {
                energyRemove[seam[i]][i] = energyCompute(seam[i], i);
            }
        }
        energy = energyRemove;
    }

    public static void main(String[] args) {

    }
}
