/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BaseballElimination {
    private final int numOfTeams;
    private final ArrayList<String> teams;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] against;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        numOfTeams = in.readInt();
        teams = new ArrayList<String>(numOfTeams);
        wins = new int[numOfTeams];
        losses = new int[numOfTeams];
        remaining = new int[numOfTeams];
        against = new int[numOfTeams][numOfTeams];
        for (int i = 0; !in.isEmpty(); i++) {
            teams.add(i, in.readString());
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < numOfTeams; j++) {
                against[i][j] = in.readInt();
            }
        }
    }

    public int numberOfTeams() {
        return numOfTeams;
    }

    public Iterable<String> teams() {
        return teams;
    }

    public int wins(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("not cantains team: " + team);
        }
        return wins[teams.indexOf(team)];
    }

    public int losses(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("not cantains team: " + team);
        }
        return losses[teams.indexOf(team)];
    }

    public int remaining(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("not cantains team: " + team);
        }
        return remaining[teams.indexOf(team)];
    }

    public int against(String team1, String team2) {
        if (!teams.contains(team1)) {
            throw new IllegalArgumentException("not cantains team: " + team1);
        }
        if (!teams.contains(team2)) {
            throw new IllegalArgumentException("not cantains team: " + team2);
        }
        return against[teams.indexOf(team1)][teams.indexOf(team2)];
    }

    public boolean isEliminated(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("not cantains team: " + team);
        }
        int indexOfteam = teams.indexOf(team);
        int maxWin = wins[indexOfteam] + remaining[indexOfteam];
        for (int i = 0; i < numOfTeams; i++) {
            if (i != indexOfteam && wins[i] > maxWin) {
                return true;
            }
        }
        int indexOfTeamsBegin = (numOfTeams) * (numOfTeams - 1) / 2 + 1;
        int indexOfsink = indexOfTeamsBegin + numOfTeams;
        FlowNetwork flowNetwork = new FlowNetwork(indexOfsink + 1);
        for (int i = 0, indexOfagainst = 1; i < numOfTeams; i++) {
            if (i != indexOfteam) {
                for (int j = i + 1; j < numOfTeams; j++) {
                    if (j != indexOfteam) {
                        flowNetwork.addEdge(new FlowEdge(0, indexOfagainst, against[i][j]));
                        flowNetwork.addEdge(
                                new FlowEdge(indexOfagainst, indexOfTeamsBegin + i,
                                             Double.POSITIVE_INFINITY));
                        flowNetwork.addEdge(
                                new FlowEdge(indexOfagainst, indexOfTeamsBegin + j,
                                             Double.POSITIVE_INFINITY));
                    }
                    indexOfagainst++;
                }
            }
            else indexOfagainst += numOfTeams - (i + 1);
        }
        for (int i = 0; i < numOfTeams; i++) {
            if (i != indexOfteam) {
                flowNetwork.addEdge(
                        new FlowEdge(indexOfTeamsBegin + i, indexOfsink,
                                     wins[indexOfteam] + remaining[indexOfteam] - wins[i]));
            }
        }
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, indexOfsink);
        boolean isElimination = false;
        for (FlowEdge e : flowNetwork.adj(0)) {
            if (e.flow() != e.capacity()) {
                isElimination = true;
            }
        }
        return isElimination;
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("not cantains team: " + team);
        }
        int indexOfteam = teams.indexOf(team);
        int maxWin = wins[indexOfteam] + remaining[indexOfteam];
        ArrayList<String> subset = new ArrayList<String>();
        for (int i = 0; i < numOfTeams; i++) {
            if (i != indexOfteam && wins[i] > maxWin) {
                subset.add(teams.get(i));
            }
        }
        if (!subset.isEmpty()) {
            return subset;
        }
        int indexOfTeamsBegin = (numOfTeams) * (numOfTeams - 1) / 2 + 1;
        int indexOfsink = indexOfTeamsBegin + numOfTeams;
        FlowNetwork flowNetwork = new FlowNetwork(indexOfsink + 1);
        for (int i = 0, indexOfagainst = 1; i < numOfTeams; i++) {
            if (i != indexOfteam) {
                for (int j = i + 1; j < numOfTeams; j++) {
                    if (j != indexOfteam) {
                        flowNetwork.addEdge(new FlowEdge(0, indexOfagainst, against[i][j]));
                        flowNetwork.addEdge(
                                new FlowEdge(indexOfagainst, indexOfTeamsBegin + i,
                                             Double.POSITIVE_INFINITY));
                        flowNetwork.addEdge(
                                new FlowEdge(indexOfagainst, indexOfTeamsBegin + j,
                                             Double.POSITIVE_INFINITY));
                    }
                    indexOfagainst++;
                }
            }
            else indexOfagainst += numOfTeams - (i + 1);
        }
        for (int i = 0; i < numOfTeams; i++) {
            if (i != indexOfteam) {
                flowNetwork.addEdge(
                        new FlowEdge(indexOfTeamsBegin + i, indexOfsink,
                                     wins[indexOfteam] + remaining[indexOfteam] - wins[i]));
            }
        }
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, indexOfsink);
        boolean isElimination = false;
        for (FlowEdge e : flowNetwork.adj(0)) {
            if (e.flow() != e.capacity()) {
                isElimination = true;
            }
        }
        if (!isElimination) {
            return null;
        }
        for (int i = 0; i < numOfTeams; i++) {
            if (i != indexOfteam && fordFulkerson.inCut(i + indexOfTeamsBegin)) {
                subset.add(teams.get(i));
            }
        }
        return subset;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
