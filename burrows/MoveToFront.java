/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    public static void encode() {
        char[] alphabet = new char[R];
        for (int i = 0; i < R; i++) {
            alphabet[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char tmp = alphabet[0];
            int loc = 0;
            for (; c != tmp; loc++) {
                char tmpTmp = tmp;
                tmp = alphabet[loc + 1];
                alphabet[loc + 1] = tmpTmp;
            }
            alphabet[0] = tmp;
            BinaryStdOut.write(loc, 8);
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        char[] alphabet = new char[R];
        for (int i = 0; i < R; i++) {
            alphabet[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int loc = BinaryStdIn.readInt(8);
            BinaryStdOut.write(alphabet[loc], 8);
            char tmp = alphabet[0];
            for (int i = 0; i != loc; i++) {
                char tmpTmp = tmp;
                tmp = alphabet[i + 1];
                alphabet[i + 1] = tmpTmp;
            }
            alphabet[0] = tmp;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("-")) {
                encode();
            }
            else if (args[0].equals("+")) {
                decode();
            }
        }
    }
}
