/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class Test {
    public static void main(String[] args) {
        SET<Integer> set = new SET<Integer>();
        set.add(10);
        set.add(10);
        for (int i : set) {
            StdOut.println(i);
        }
    }
}
