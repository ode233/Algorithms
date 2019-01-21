/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {
    private final int[][] blocks;
    private final int dim;
    private int manhattan;
    private boolean isGoal = true;

    public Board(int[][] blocks) {
        if (blocks == null) throw new IllegalArgumentException("blocks can not be null");
        dim = blocks.length;
        this.blocks = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                this.blocks[i][j] = blocks[i][j];
            }
        }
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (blocks[i][j] == 0) continue;
                if (blocks[i][j] != i * dim + j + 1) {
                    int i1 = blocks[i][j] / dim;
                    int j1 = blocks[i][j] % dim - 1;
                    if (blocks[i][j] % dim == 0) {
                        i1--;
                        j1 = dim - 1;
                    }
                    manhattan = manhattan + Math.abs(i - i1) + Math.abs(j - j1);
                }
            }
        }
        if (blocks[dim - 1][dim - 1] != 0) isGoal = false;
        for (int i = dim - 1, j = 0; j < dim - 1; j++) {
            if (blocks[i][j] != i * dim + j + 1) isGoal = false;
        }
        for (int i = 0; i < dim - 1; i++) {
            for (int j = 0; j < dim; j++) {
                if (blocks[i][j] != i * dim + j + 1) isGoal = false;
            }
        }
    }

    public int dimension() {
        return dim;
    }

    public int hamming() {
        int count = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (blocks[i][j] == 0) continue;
                if (blocks[i][j] != i * dim + j + 1) {
                    count++;
                }
            }
        }
        return count;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public Board twin() {
        int[][] twinArray = new int[dim][dim];
        if (blocks[0][0] != 0 && blocks[0][1] != 0) {
            twinArray[0][0] = blocks[0][1];
            twinArray[0][1] = blocks[0][0];
            for (int j = 2; j < dim; j++) {
                twinArray[0][j] = blocks[0][j];
            }
            for (int i = 1; i < dim; i++)
                for (int j = 0; j < dim; j++) {
                    twinArray[i][j] = blocks[i][j];
                }
        }
        else {
            twinArray[1][0] = blocks[1][1];
            twinArray[1][1] = blocks[1][0];
            for (int j = 0; j < dim; j++) {
                twinArray[0][j] = blocks[0][j];
            }
            for (int j = 2; j < dim; j++) {
                twinArray[1][j] = blocks[1][j];
            }
            for (int i = 2; i < dim; i++)
                for (int j = 0; j < dim; j++) {
                    twinArray[i][j] = blocks[i][j];
                }
        }
        return new Board(twinArray);
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dim != that.dim) return false;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (this.blocks[i][j] != that.blocks[i][j]) return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        int[][][] neighbors = new int[4][dim][dim];
        boolean[] haveNb = new boolean[4];
        int ib = -1, jb = -1;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                neighbors[0][i][j] = blocks[i][j];
                neighbors[1][i][j] = blocks[i][j];
                neighbors[2][i][j] = blocks[i][j];
                neighbors[3][i][j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    ib = i;
                    jb = j;
                }
            }
        }
        if (ib == -1) throw new IllegalArgumentException("the puzzle is wrong,not have zero");

        if (ib > 0) {
            haveNb[0] = true;
            neighbors[0][ib][jb] = blocks[ib - 1][jb];
            neighbors[0][ib - 1][jb] = 0;
        }
        if (ib < dim - 1) {
            haveNb[1] = true;
            neighbors[1][ib][jb] = blocks[ib + 1][jb];
            neighbors[1][ib + 1][jb] = 0;
        }
        if (jb > 0) {
            haveNb[2] = true;
            neighbors[2][ib][jb] = blocks[ib][jb - 1];
            neighbors[2][ib][jb - 1] = 0;
        }
        if (jb < dim - 1) {
            haveNb[3] = true;
            neighbors[3][ib][jb] = blocks[ib][jb + 1];
            neighbors[3][ib][jb + 1] = 0;
        }
        Board[] nbOfboard = new Board[4];
        for (int i = 0, j = 0; i < 4; i++) {
            if (haveNb[i]) {
                nbOfboard[j] = new Board(neighbors[i]);
                j++;
            }
        }
        return () -> new Iterator<Board>() {
            private int n = 0;

            @Override
            public boolean hasNext() {
                if (n == 4 || nbOfboard[n] == null) return false;
                return true;
            }

            @Override
            public Board next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("no next");
                }
                n++;
                return nbOfboard[n - 1];
            }
        };
    }


    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dim + "\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                s.append(blocks[i][j] + "  ");
            }
            s.append("\n");
        }
        return s.toString();
    }


    public static void main(String[] args) {

        // for each command-line argument
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }
            Board board = new Board(tiles);
            System.out.println(board.twin().toString());
        }
/*        for (Board b : board.neighbors()) {
            System.out.println(b.toString());
            for (Board c : b.neighbors()) {
                System.out.println(c.toString());
            }
        }*/
    }
}
