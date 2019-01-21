/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class Test {
    public static void main(String[] args) {
        String[] strings = { "a", "b", "c" };
        String[] strings1 = strings;
        for (int i = 0; i < 3; i++) {
            StdOut.println(strings1[i]);
        }
        strings[2] = "d";
        for (int i = 0; i < 3; i++) {
            StdOut.println(strings1[i]);
        }
    }
}
