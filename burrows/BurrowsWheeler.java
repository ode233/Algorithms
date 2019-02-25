/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;

public class BurrowsWheeler {
    private static final int R = 256;

    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray suffixArray = new CircularSuffixArray(s);
        int len = suffixArray.length();
        for (int i = 0; i < len; i++) {
            if (suffixArray.index(i) == 0) {
                BinaryStdOut.write(i, 32);
                break;
            }
        }
        for (int i = 0; i < len; i++) {
            if (suffixArray.index(i) == 0) {
                BinaryStdOut.write(s.charAt(len - 1));
            }
            else BinaryStdOut.write(s.charAt(suffixArray.index(i) - 1));
        }
        BinaryStdOut.close();
    }


    public static void inverseTransform() {
        int first = BinaryStdIn.readInt(32);
        class Node {
            private char key;
            private int value;

            public Node(char key, int value) {
                this.key = key;
                this.value = value;
            }
        }
        ArrayList<Node> nodeArrayList = new ArrayList<Node>();
        for (int i = 0; !BinaryStdIn.isEmpty(); i++) {
            Node node = new Node(BinaryStdIn.readChar(), i);
            nodeArrayList.add(node);
        }
        int n = nodeArrayList.size();
        int[] count = new int[R + 1];
        Node[] aux = new Node[n];
        for (int i = 0; i < n; i++) {
            count[nodeArrayList.get(i).key + 1]++;
        }
        for (int r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }
        for (int i = 0; i < n; i++) {
            aux[count[nodeArrayList.get(i).key]++] = nodeArrayList.get(i);
        }
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(aux[first].key);
            first = aux[first].value;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("-")) {
                transform();
            }
            else if (args[0].equals("+")) {
                inverseTransform();
            }
        }
    }
}
