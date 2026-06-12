// ═══════════════════════════════════════════════════════════
// WasteSense – CO3: Graph Algorithms for Waste Networks
// Graph (Adjacency List) + BFS + DFS + Prim MST
// ═══════════════════════════════════════════════════════════

import java.util.*;

// Graph: nodes = collection zones / recycling centers
//        edges = roads connecting them (weighted)
class WasteGraph {
    private int V;
    private List<int[]>[] adj; // adj[u] = {v, weight}

    @SuppressWarnings("unchecked")
    WasteGraph(int v) {
        V = v;
        adj = new List[V];
        for (int i = 0; i < V; i++)
            adj[i] = new ArrayList<>();
    }

    void addEdge(int u, int v, int w) {
        adj[u].add(new int[]{v, w});
        adj[v].add(new int[]{u, w}); // undirected
    }

    // BFS: find nearest reachable collection zones from source
    void bfs(int src) {
        boolean[] visited = new boolean[V];
        Queue<Integer> q = new LinkedList<>();
        visited[src] = true;
        q.add(src);
        System.out.print("BFS from zone " + src + ": ");
        while (!q.isEmpty()) {
            int u = q.poll();
            System.out.print(u + " ");
            for (int[] e : adj[u]) {
                if (!visited[e[0]]) {
                    visited[e[0]] = true;
                    q.add(e[0]);
                }
            }
        }
        System.out.println();
    }

    // DFS: detect route connectivity issues
    void dfs(int src) {
        boolean[] visited = new boolean[V];
        System.out.print("DFS from zone " + src + ": ");
        dfsHelper(src, visited);
        System.out.println();
    }

    void dfsHelper(int u, boolean[] vis) {
        vis[u] = true;
        System.out.print(u + " ");
        for (int[] e : adj[u])
            if (!vis[e[0]]) dfsHelper(e[0], vis);
    }

    // Prim MST: optimal collection network (minimum total road cost)
    void primMST() {
        int[] key    = new int[V];
        int[] parent = new int[V];
        boolean[] inMST = new boolean[V];
        Arrays.fill(key, Integer.MAX_VALUE);
        key[0] = 0;
        parent[0] = -1;

        for (int i = 0; i < V - 1; i++) {
            // pick min-key vertex not yet in MST
            int u = -1;
            for (int v = 0; v < V; v++)
                if (!inMST[v] && (u == -1 || key[v] < key[u])) u = v;
            inMST[u] = true;
            for (int[] e : adj[u]) {
                int v = e[0], w = e[1];
                if (!inMST[v] && w < key[v]) {
                    key[v] = w;
                    parent[v] = u;
                }
            }
        }

        System.out.println("MST Edges (Prim):");
        int totalCost = 0;
        for (int v = 1; v < V; v++) {
            System.out.println("  Zone " + parent[v] + " -- Zone " + v + "  cost: " + key[v]);
            totalCost += key[v];
        }
        System.out.println("Total MST Cost: " + totalCost);
    }
}

// ─── Driver ──────────────────────────────────────────────
public class WasteSenseCO3 {
    public static void main(String[] args) {
        // 6 zones: 0=Depot, 1=ZoneA, 2=ZoneB, 3=ZoneC, 4=ZoneD, 5=RecyclingCenter
        WasteGraph g = new WasteGraph(6);
        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 3);
        g.addEdge(1, 2, 2);
        g.addEdge(1, 3, 6);
        g.addEdge(2, 4, 5);
        g.addEdge(3, 4, 1);
        g.addEdge(3, 5, 7);
        g.addEdge(4, 5, 8);

        g.bfs(0);
        g.dfs(0);
        g.primMST();
    }
}
