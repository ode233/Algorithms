/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.util.ArrayList;
import java.util.HashSet;

public class BoggleSolver {
    private final TST<Integer> tst = new TST<Integer>();
    private int m;
    private int n;
    private BoggleBoard boggleBoard;
    private HashSet<String> valid;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (int i = 0; i < dictionary.length; i++) {
            tst.put(dictionary[i], 0);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        /*
        int m = board.rows();
        int n = board.cols();
        char[][] chars = new char[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                chars[i][j] = board.getLetter(i, j);
            }
        }
        boggleBoard = new BoggleBoard(chars);
        */
        m = board.rows();
        n = board.cols();
        boggleBoard = board;
        valid = new HashSet<String>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                StringBuilder stringBuilder = new StringBuilder();
                ArrayList<Integer> arrayList = new ArrayList<Integer>();
                search(i, j, stringBuilder, arrayList);
            }
        }
        return valid;
    }


    private void search(int i, int j, StringBuilder stringBuilder, ArrayList<Integer> arrayList) {
        if (i >= m || i < 0) {
            return;
        }
        if (j >= n || j < 0) {
            return;
        }
        int index = i * n + j;
        if (arrayList.contains(index)) {
            return;
        }
        char ch = boggleBoard.getLetter(i, j);
        if (ch == 'Q') {
            stringBuilder.append("QU");
        }
        else stringBuilder.append(ch);
        String string = new String(stringBuilder);
        if (!tst.keysWithPrefix(string).iterator().hasNext()) {
            return;
        }
        arrayList.add(index);
        if (string.length() >= 3 && tst.contains(string)) {
            valid.add(string);
        }
        search(i + 1, j, new StringBuilder(stringBuilder), new ArrayList<Integer>(arrayList));
        search(i - 1, j, new StringBuilder(stringBuilder), new ArrayList<Integer>(arrayList));
        search(i, j + 1, new StringBuilder(stringBuilder), new ArrayList<Integer>(arrayList));
        search(i, j - 1, new StringBuilder(stringBuilder), new ArrayList<Integer>(arrayList));
        search(i + 1, j + 1, new StringBuilder(stringBuilder), new ArrayList<Integer>(arrayList));
        search(i + 1, j - 1, new StringBuilder(stringBuilder), new ArrayList<Integer>(arrayList));
        search(i - 1, j + 1, new StringBuilder(stringBuilder), new ArrayList<Integer>(arrayList));
        search(i - 1, j - 1, new StringBuilder(stringBuilder), new ArrayList<Integer>(arrayList));
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) {
            throw new IllegalArgumentException("word is null");
        }
        int len = word.length();
        if (len >= 3 && tst.contains(word)) {
            if (len <= 4) {
                return 1;
            }
            else if (len == 5) {
                return 2;
            }
            else if (len == 6) {
                return 3;
            }
            else if (len == 7) {
                return 5;
            }
            else return 11;
        }
        else return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        int num = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
            num++;
        }
        StdOut.println("Score = " + score);
        StdOut.println(num);
    }
}
