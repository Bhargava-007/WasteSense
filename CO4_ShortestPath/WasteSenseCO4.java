// ═══════════════════════════════════════════════════════════
// WasteSense – CO4: Shortest Path Optimization
// Dijkstra + Bellman-Ford + Floyd-Warshall + Topological Sort
// ═══════════════════════════════════════════════════════════

import java.util.*;

public class WasteSenseCO4 {

    static final int INF = Integer.MAX_VALUE / 2;

    // ─── Dijkstra: shortest collection route from depot ──────
    static void dijkstra(int[][] graph, int src, int V) {
        int[] dist = new int[V];
        boolean[] visited = new boolean[V];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        for (int i = 0; i < V - 1; i++) {
            // pick unvisited vertex with minimum distance
            int u = -1;
            for (int v = 0; v < V; v++)
                if (!visited[v] && (u == -1 || dist[v] < dist[u])) u = v;
            visited[u] = true;
            for (int v = 0; v < V; v++) {
                if (!visited[v] && graph[u][v] != 0
                        && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                }
            }
        }

        System.out.println("Dijkstra shortest distances from Zone " + src + ":");
        for (int i = 0; i < V; i++)
            System.out.println("  Zone " + i + " : " + dist[i] + " km");
    }

    // ─── Bellman-Ford: routes with varying traffic weights ───
    static void bellmanFord(int[][] edges, int V, int E, int src) {
        int[] dist = new int[V];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        for (int i = 0; i < V - 1; i++) {
            for (int[] e : edges) {
                int u = e[0], v = e[1], w = e[2];
                if (dist[u] != INF && dist[u] + w < dist[v])
                    dist[v] = dist[u] + w;
            }
        }

        System.out.println("\nBellman-Ford shortest distances from Zone " + src + ":");
        for (int i = 0; i < V; i++)
            System.out.println("  Zone " + i + " : " + dist[i] + " km");
    }

    // ─── Floyd-Warshall: all-pairs route distance matrix ─────
    static void floydWarshall(int[][] dist, int V) {
        for (int k = 0; k < V; k++)
            for (int i = 0; i < V; i++)
                for (int j = 0; j < V; j++)
                    if (dist[i][k] + dist[k][j] < dist[i][j])
                        dist[i][j] = dist[i][k] + dist[k][j];

        System.out.println("\nFloyd-Warshall All-Pairs Distance Matrix:");
        System.out.print("     ");
        for (int i = 0; i < V; i++) System.out.printf("Z%-4d", i);
        System.out.println();
        for (int i = 0; i < V; i++) {
            System.out.printf("Z%d   ", i);
            for (int j = 0; j < V; j++) {
                if (dist[i][j] == INF) System.out.print("INF  ");
                else System.out.printf("%-5d", dist[i][j]);
            }
            System.out.println();
        }
    }

    // ─── Topological Sort: schedule waste processing ops ─────
    static void topoSort(List<Integer>[] adj, int V) {
        int[] inDegree = new int[V];
        for (int u = 0; u < V; u++)
            for (int v : adj[u]) inDegree[v]++;

        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < V; i++)
            if (inDegree[i] == 0) q.add(i);

        System.out.println("\nTopological Order (processing schedule):");
        List<String> steps = List.of("Collect", "Sort", "Weigh", "Compress", "Transfer", "Recycle");
        while (!q.isEmpty()) {
            int u = q.poll();
            System.out.println("  Step " + u + ": " + steps.get(u));
            for (int v : adj[u])
                if (--inDegree[v] == 0) q.add(v);
        }
    }

    // ─── Driver ──────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        int V = 5;

        // Adjacency matrix for Dijkstra (0 = no edge)
        int[][] graph = {
            {0, 10,  0,  0, 5},
            {0,  0,  1, 0, 2},
            {0,  0,  0, 4, 0},
            {7,  0,  6, 0, 0},
            {0,  3,  9, 2, 0}
        };
        dijkstra(graph, 0, V);

        // Edge list for Bellman-Ford {u, v, weight}
        int[][] edges = {
            {0, 1, 10}, {0, 4, 5}, {1, 2, 1}, {1, 4, 2},
            {2, 3, 4},  {3, 0, 7}, {3, 2, 6}, {4, 1, 3},
            {4, 2, 9},  {4, 3, 2}
        };
        bellmanFord(edges, V, edges.length, 0);

        // Distance matrix for Floyd-Warshall
        int[][] dist = {
            {0,   10,  INF, INF, 5  },
            {INF, 0,   1,   INF, 2  },
            {INF, INF, 0,   4,   INF},
            {7,   INF, 6,   0,   INF},
            {INF, 3,   9,   2,   0  }
        };
        floydWarshall(dist, V);

        // DAG for topological sort (waste processing pipeline)
        int dagV = 6;
        List<Integer>[] dag = new List[dagV];
        for (int i = 0; i < dagV; i++) dag[i] = new ArrayList<>();
        dag[0].add(1); dag[0].add(2);
        dag[1].add(3); dag[2].add(3);
        dag[3].add(4); dag[4].add(5);
        topoSort(dag, dagV);
    }
}
