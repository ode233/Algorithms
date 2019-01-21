/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordNet;

    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    public String outcast(String[] nouns) {
        if (nouns == null) {
            throw new IllegalArgumentException("nouns is null");
        }
        for (String s : nouns) {
            if (!wordNet.isNoun(s)) {
                throw new IllegalArgumentException(s + " is not in the wordNet");
            }
        }
        int size = nouns.length;
        int maxDistance = 0;
        String outcast = "";
        int[][] store = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                store[i][j] = wordNet.distance(nouns[i], nouns[j]);
            }
        }
        for (int i = 0; i < size; i++) {
            int distance = 0;
            for (int j = 0; j < size; j++) {
                if (j < i) {
                    distance += store[j][i];
                }
                else distance += store[i][j];
            }
            if (distance > maxDistance) {
                maxDistance = distance;
                outcast = nouns[i];
            }
        }
        return outcast;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
        In in = new In("outcast5.txt");
        String[] nouns = in.readAllStrings();
        if (!outcast.outcast(nouns).equals("table")) {
            throw new IllegalArgumentException("error");
        }
    }
}
