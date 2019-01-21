/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode delSN;
    private boolean isSolvable;


    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("initial can not be null");
        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>();
        delSN = new SearchNode(initial, null, 0);
        minPQ.insert(delSN);
        MinPQ<SearchNode> minPQT = new MinPQ<SearchNode>();
        SearchNode delSNT = new SearchNode(initial.twin(), null, 0);
        minPQT.insert(delSNT);
        while (!delSN.board.isGoal() && !delSNT.board.isGoal()) {
            delSN = minPQ.delMin();
            for (Board bd : delSN.board.neighbors()) {
                if (delSN.previous == null || !bd.equals(delSN.previous.board))
                    minPQ.insert(new SearchNode(bd, delSN, delSN.move + 1));
            }
            delSNT = minPQT.delMin();
            for (Board bd : delSNT.board.neighbors()) {
                if (delSNT.previous == null || !bd.equals(delSNT.previous.board))
                    minPQT.insert(new SearchNode(bd, delSNT, delSNT.move + 1));
            }
        }
        isSolvable = delSN.board.isGoal();
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int move;
        private final int manhattan;
        private final SearchNode previous;

        private SearchNode(Board board, SearchNode previous, int move) {
            manhattan = board.manhattan() + move;
            this.board = board;
            this.previous = previous;
            this.move = move;

        }

        @Override
        public int compareTo(SearchNode that) {
            return Integer.compare(this.manhattan, that.manhattan);
        }
    }


    public boolean isSolvable() {
        return isSolvable;
    }

    public int moves() {
        if (!isSolvable()) return -1;
        return delSN.move;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> track = new Stack<Board>();
        SearchNode trackNode = delSN;
        while (trackNode != null) {
            track.push(trackNode.board);
            trackNode = trackNode.previous;
        }
        return (track);
    }


    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
