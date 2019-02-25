/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private int len;
    private Integer[] sort;

    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("the string is null");
        }
        len = s.length();
        sort = new Integer[len];
        for (int i = 0; i < len; i++) {
            sort[i] = i;
        }
        Arrays.sort(sort, new Comparator<Integer>() {
            public int compare(Integer first, Integer second) {
                int p = first;
                int q = second;
                for (int i = 0; i < len; i++) {
                    if (p >= len) {
                        p = 0;
                    }
                    if (q >= len) {
                        q = 0;
                    }
                    if (s.charAt(p) < s.charAt(q)) {
                        return -1;
                    }
                    if (s.charAt(p) > s.charAt(q)) {
                        return 1;
                    }
                    p++;
                    q++;
                }
                return 0;
            }
        });
    }

    public int length() {
        return len;
    }

    public int index(int i) {
        if (i < 0 || i > len - 1) {
            throw new IllegalArgumentException("out of range");
        }
        return sort[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray suffix = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < suffix.len; i++) {
            StdOut.println(suffix.index(i));
        }
    }
}
