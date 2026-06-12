// ═══════════════════════════════════════════════════════════
// WasteSense – CO6: Greedy Algorithms & Dynamic Programming
// Activity Selection + Fractional Knapsack + 0/1 Knapsack + LIS
// ═══════════════════════════════════════════════════════════

import java.util.*;

public class WasteSenseCO6 {

    // ─── Activity Selection: schedule collection vehicles ────
    // Each activity: {start, end, vehicleID}
    static void activitySelection(int[][] activities) {
        // sort by finish time
        Arrays.sort(activities, (a, b) -> a[1] - b[1]);
        System.out.println("Activity Selection (vehicle schedules):");
        int lastEnd = -1;
        for (int[] a : activities) {
            if (a[0] >= lastEnd) {
                System.out.println("  Vehicle " + a[2]
                        + " | slot [" + a[0] + " - " + a[1] + "]");
                lastEnd = a[1];
            }
        }
    }

    // ─── Fractional Knapsack: optimize vehicle waste capacity ─
    static void fractionalKnapsack(int[] weight, int[] value, int capacity) {
        int n = weight.length;
        Double[] ratio = new Double[n];
        Integer[] idx  = new Integer[n];
        for (int i = 0; i < n; i++) { ratio[i] = (double) value[i] / weight[i]; idx[i] = i; }
        Arrays.sort(idx, (a, b) -> Double.compare(ratio[b], ratio[a]));

        double totalValue = 0;
        System.out.println("\nFractional Knapsack (vehicle loading):");
        for (int i : idx) {
            if (capacity == 0) break;
            int take = Math.min(weight[i], capacity);
            double val = take * ratio[i];
            System.out.printf("  Item %d: take %d kg  value %.1f%n", i, take, val);
            totalValue += val;
            capacity   -= take;
        }
        System.out.printf("  Total value loaded: %.1f%n", totalValue);
    }

    // ─── 0/1 Knapsack: select waste processing resources ─────
    static void knapsack01(int[] weight, int[] value, int capacity) {
        int n = weight.length;
        int[][] dp = new int[n + 1][capacity + 1];
        for (int i = 1; i <= n; i++)
            for (int w = 0; w <= capacity; w++) {
                dp[i][w] = dp[i - 1][w];
                if (weight[i - 1] <= w)
                    dp[i][w] = Math.max(dp[i][w],
                            dp[i - 1][w - weight[i - 1]] + value[i - 1]);
            }

        System.out.println("\n0/1 Knapsack (resource selection):");
        System.out.println("  Max processing value: " + dp[n][capacity]);

        // traceback selected items
        int w = capacity;
        System.out.print("  Selected resources: ");
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                System.out.print("R" + (i - 1) + " ");
                w -= weight[i - 1];
            }
        }
        System.out.println();
    }

    // ─── LIS: analyse waste generation growth trend ──────────
    static void lis(int[] arr) {
        int n = arr.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        for (int i = 1; i < n; i++)
            for (int j = 0; j < i; j++)
                if (arr[j] < arr[i]) dp[i] = Math.max(dp[i], dp[j] + 1);

        int maxLen = Arrays.stream(dp).max().getAsInt();
        System.out.println("\nLIS (waste growth trend):");
        System.out.println("  Monthly waste (tonnes): " + Arrays.toString(arr));
        System.out.println("  Longest increasing subsequence length: " + maxLen);
    }

    // ─── Driver ──────────────────────────────────────────────
    public static void main(String[] args) {

        // Activity Selection: vehicle collection slots {start, end, vehicleID}
        int[][] activities = {
            {1, 4, 0}, {3, 6, 1}, {0, 2, 2},
            {5, 8, 3}, {7, 9, 4}, {6, 10, 5}
        };
        activitySelection(activities);

        // Fractional Knapsack: waste items {weight kg}, {value units}, capacity
        int[] fWeight   = {10, 20, 30, 15, 25};
        int[] fValue    = {60, 100, 120, 80, 90};
        int   fCapacity = 50;
        fractionalKnapsack(fWeight, fValue, fCapacity);

        // 0/1 Knapsack: processing resources {weight}, {value}, capacity
        int[] kWeight   = {2, 3, 4, 5, 9};
        int[] kValue    = {3, 4, 5, 6, 10};
        int   kCapacity = 10;
        knapsack01(kWeight, kValue, kCapacity);

        // LIS: monthly waste generation trend (tonnes)
        int[] monthly = {12, 15, 11, 18, 17, 22, 20, 25};
        lis(monthly);
    }
}
