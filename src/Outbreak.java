import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     *
     * State for every person v (looking only at v and everything below it): the smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed") and with the real costs ("Cheapest testing").
     */
    static int[] parent;
    static int[] notTested;
    static int[] tested;

    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        parent = new int[n];
        notTested = new int[n];
        tested = new int[n];
        parent[0] = -1;
        dfs(0, nb, cost);
        boolean[] result = new boolean[n];
        rebuild(0, tested[0] <= notTested[0], nb, result);
        return result;
    }

    static void dfs(int v, int[][] nb, int[] cost) {
        tested[v] = cost[v];
        notTested[v] = 0;
        for (int u : nb[v]) {
            if (u == parent[v]) continue;
            parent[u] = v;
            dfs(u, nb, cost);
            tested[v] += Math.min(tested[u], notTested[u]);
            notTested[v] += tested[u];
        }
    }

    static void rebuild(int v, boolean take, int[][] nb, boolean[] result) {
        result[v] = take;
        for (int u : nb[v]) {
            if (u == parent[v]) continue;
            if (take) rebuild(u, tested[u] <= notTested[u], nb, result);
            else rebuild(u, true, nb, result);
        }
    }

    static int best;

    /**
     * The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3 has chain 2.
     */
    public static int longestChain(int n, int[][] nb) {
        best = 0;
        down(0, -1, nb);
        return best;
    }

    static int down(int v, int par, int[][] nb) {
        int first = 0, second = 0;
        for (int u : nb[v]) {
            if (u == par) continue;
            int len = down(u, v, nb) + 1;
            if (len > first) { second = first; first = len; }
            else if (len > second) second = len;
        }
        best = Math.max(best, first + second);
        return first;
    }
}
