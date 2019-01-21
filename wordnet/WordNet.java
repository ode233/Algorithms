/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class WordNet {
    private final ArrayList<String> synsets = new ArrayList<String>();
    private final SeparateChainingHashST<String, ArrayList<Integer>> hashST
            = new SeparateChainingHashST<String, ArrayList<Integer>>();
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("arg is null");
        }
        In sv = new In(synsets);
        while (!sv.isEmpty()) {
            this.synsets.add(sv.readLine().split(",")[1]);
        }
        int size = this.synsets.size();
        for (int i = 0; i < size; i++) {
            String[] lineOfSynsets = this.synsets.get(i).split(" ");
            for (String w : lineOfSynsets) {
                if (!hashST.contains(w)) {
                    ArrayList<Integer> tmp = new ArrayList<Integer>();
                    tmp.add(i);
                    hashST.put(w, tmp);
                }
                else {
                    hashST.get(w).add(i);
                }
            }
        }
        In hy = new In(hypernyms);
        Digraph digraph = new Digraph(size);
        while (!hy.isEmpty()) {
            String[] line = hy.readLine().split(",");
            int v = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                digraph.addEdge(v, Integer.parseInt(line[i]));
            }
        }
        int root = 0;
        for (int i = 0; i < size; i++) {
            if (digraph.outdegree(i) == 0) {
                root++;
            }
        }
        if (root != 1) {
            throw new IllegalArgumentException("not a rooted DAG");
        }
        sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return hashST.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return hashST.contains(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("noun is null");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("noun is not in wordnet");
        }
        ArrayList<Integer> indexA = hashST.get(nounA);
        ArrayList<Integer> indexB = hashST.get(nounB);
        return sap.length(indexA, indexB);
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("noun is null");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("noun is not in wordnet");
        }
        ArrayList<Integer> indexA = hashST.get(nounA);
        ArrayList<Integer> indexB = hashST.get(nounB);
        int indexOfSap = sap.ancestor(indexA, indexB);
        return synsets.get(indexOfSap);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wd = new WordNet("synsets.txt", "hypernyms.txt");
        while (!StdIn.isEmpty()) {
            String nounA = StdIn.readString();
            String nounB = StdIn.readString();
            StdOut.println(wd.isNoun(nounA));
            StdOut.println(wd.sap(nounA, nounB));
            StdOut.println(wd.distance(nounA, nounB));
        }
    }
}
