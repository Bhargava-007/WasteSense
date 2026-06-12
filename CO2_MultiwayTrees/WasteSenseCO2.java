// ═══════════════════════════════════════════════════════════
// WasteSense – CO2: Multiway Trees & Range Query Structures
// B-Tree (order t=3) + Segment Tree for waste analytics
// ═══════════════════════════════════════════════════════════

// ─── B-Tree Node (order t=3, max 2t-1=5 keys) ───────────
class BTreeNode {
    int[] keys;
    int n;
    BTreeNode[] children;
    boolean isLeaf;
    static final int T = 3;

    BTreeNode(boolean leaf) {
        isLeaf = leaf;
        keys     = new int[2 * T - 1];
        children = new BTreeNode[2 * T];
        n = 0;
    }
}

// ─── B-Tree (indexes waste collection records by date) ───
class BTree {
    BTreeNode root = new BTreeNode(true);

    public boolean search(BTreeNode node, int key) {
        int i = 0;
        while (i < node.n && key > node.keys[i]) i++;
        if (i < node.n && key == node.keys[i]) return true;
        if (node.isLeaf) return false;
        return search(node.children[i], key);
    }

    public void insert(int key) {
        BTreeNode r = root;
        if (r.n == 2 * BTreeNode.T - 1) {
            BTreeNode s = new BTreeNode(false);
            root = s;
            s.children[0] = r;
            splitChild(s, 0, r);
            insertNonFull(s, key);
        } else {
            insertNonFull(r, key);
        }
    }

    private void splitChild(BTreeNode parent, int i, BTreeNode y) {
        int t = BTreeNode.T;
        BTreeNode z = new BTreeNode(y.isLeaf);
        z.n = t - 1;
        for (int j = 0; j < t - 1; j++) z.keys[j] = y.keys[j + t];
        if (!y.isLeaf)
            for (int j = 0; j < t; j++) z.children[j] = y.children[j + t];
        y.n = t - 1;
        for (int j = parent.n; j >= i + 1; j--) parent.children[j + 1] = parent.children[j];
        parent.children[i + 1] = z;
        for (int j = parent.n - 1; j >= i; j--) parent.keys[j + 1] = parent.keys[j];
        parent.keys[i] = y.keys[t - 1];
        parent.n++;
    }

    private void insertNonFull(BTreeNode node, int key) {
        int i = node.n - 1;
        if (node.isLeaf) {
            while (i >= 0 && node.keys[i] > key) {
                node.keys[i + 1] = node.keys[i];
                i--;
            }
            node.keys[i + 1] = key;
            node.n++;
        } else {
            while (i >= 0 && node.keys[i] > key) i--;
            if (node.children[i + 1].n == 2 * BTreeNode.T - 1) {
                splitChild(node, i + 1, node.children[i + 1]);
                if (key > node.keys[i + 1]) i++;
            }
            insertNonFull(node.children[i + 1], key);
        }
    }

    public void traverse(BTreeNode node) {
        int i;
        for (i = 0; i < node.n; i++) {
            if (!node.isLeaf) traverse(node.children[i]);
            System.out.print(node.keys[i] + " ");
        }
        if (!node.isLeaf) traverse(node.children[i]);
    }
}

// ─── Segment Tree (range sum of waste volumes) ──────────
class SegmentTree {
    int[] tree, data;
    int n;

    SegmentTree(int[] arr) {
        n = arr.length;
        data = arr;
        tree = new int[4 * n];
        build(0, 0, n - 1);
    }

    void build(int v, int l, int r) {
        if (l == r) { tree[v] = data[l]; return; }
        int mid = (l + r) / 2;
        build(2 * v + 1, l, mid);
        build(2 * v + 2, mid + 1, r);
        tree[v] = tree[2 * v + 1] + tree[2 * v + 2];
    }

    int query(int v, int l, int r, int ql, int qr) {
        if (qr < l || r < ql) return 0;
        if (ql <= l && r <= qr) return tree[v];
        int mid = (l + r) / 2;
        return query(2 * v + 1, l, mid, ql, qr)
             + query(2 * v + 2, mid + 1, r, ql, qr);
    }
}

// ─── Driver ──────────────────────────────────────────────
public class WasteSenseCO2 {
    public static void main(String[] args) {

        // B-Tree: index waste records by collection date (YYYYMMDD)
        BTree bt = new BTree();
        int[] dates = {20250101, 20250115, 20250201,
                       20250210, 20250301, 20250318, 20250401};
        for (int d : dates) bt.insert(d);

        System.out.println("B-Tree Traversal (sorted dates):");
        bt.traverse(bt.root);
        System.out.println();

        boolean found = bt.search(bt.root, 20250201);
        System.out.println("Search 20250201: " + found);

        // Segment Tree: range sum queries on daily waste volumes (kg)
        int[] waste = {120, 95, 210, 180, 300, 145, 230};
        SegmentTree seg = new SegmentTree(waste);

        System.out.println("Total waste [day 0-6]: " + seg.query(0, 0, 6, 0, 6) + " kg");
        System.out.println("Total waste [day 2-4]: " + seg.query(0, 0, 6, 2, 4) + " kg");
    }
}
