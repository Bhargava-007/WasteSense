// ═══════════════════════════════════════════════════════════
// WasteSense – CO5: Advanced Sorting & Data Ranking
// Merge Sort + Quick Sort + Heap Sort + Radix Sort
// ═══════════════════════════════════════════════════════════

import java.util.Arrays;

public class WasteSenseCO5 {

    // ─── Merge Sort: sort waste collection records by volume ─
    static void mergeSort(int[] arr, int l, int r) {
        if (l >= r) return;
        int mid = (l + r) / 2;
        mergeSort(arr, l, mid);
        mergeSort(arr, mid + 1, r);
        merge(arr, l, mid, r);
    }

    static void merge(int[] arr, int l, int mid, int r) {
        int n1 = mid - l + 1, n2 = r - mid;
        int[] L = new int[n1], R = new int[n2];
        for (int i = 0; i < n1; i++) L[i] = arr[l + i];
        for (int j = 0; j < n2; j++) R[j] = arr[mid + 1 + j];
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2)
            arr[k++] = (L[i] <= R[j]) ? L[i++] : R[j++];
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    // ─── Quick Sort: rank areas by waste generation ──────────
    static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    static int partition(int[] arr, int low, int high) {
        int pivot = arr[high], i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
            }
        }
        int tmp = arr[i + 1]; arr[i + 1] = arr[high]; arr[high] = tmp;
        return i + 1;
    }

    // ─── Heap Sort: identify most critical waste zones ───────
    static void heapSort(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i);
        for (int i = n - 1; i > 0; i--) {
            int tmp = arr[0]; arr[0] = arr[i]; arr[i] = tmp;
            heapify(arr, i, 0);
        }
    }

    static void heapify(int[] arr, int n, int i) {
        int largest = i, l = 2 * i + 1, r = 2 * i + 2;
        if (l < n && arr[l] > arr[largest]) largest = l;
        if (r < n && arr[r] > arr[largest]) largest = r;
        if (largest != i) {
            int tmp = arr[i]; arr[i] = arr[largest]; arr[largest] = tmp;
            heapify(arr, n, largest);
        }
    }

    // ─── Radix Sort: sort bin identification numbers ─────────
    static void radixSort(int[] arr) {
        int max = Arrays.stream(arr).max().getAsInt();
        for (int exp = 1; max / exp > 0; exp *= 10)
            countingSort(arr, exp);
    }

    static void countingSort(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count  = new int[10];
        for (int x : arr) count[(x / exp) % 10]++;
        for (int i = 1; i < 10; i++) count[i] += count[i - 1];
        for (int i = n - 1; i >= 0; i--) {
            output[--count[(arr[i] / exp) % 10]] = arr[i];
        }
        System.arraycopy(output, 0, arr, 0, n);
    }

    // ─── Driver ──────────────────────────────────────────────
    public static void main(String[] args) {

        // Merge Sort: waste volumes (litres) per collection record
        int[] volumes = {340, 120, 480, 95, 260, 415, 180};
        mergeSort(volumes, 0, volumes.length - 1);
        System.out.print("Merge Sort (waste volumes): ");
        System.out.println(Arrays.toString(volumes));

        // Quick Sort: areas ranked by waste generation (tonnes/month)
        int[] generation = {52, 18, 73, 9, 34, 61, 27};
        quickSort(generation, 0, generation.length - 1);
        System.out.print("Quick Sort (waste generation): ");
        System.out.println(Arrays.toString(generation));

        // Heap Sort: fill levels to identify critical zones (%)
        int[] fillLevels = {65, 90, 42, 78, 55, 88, 33};
        heapSort(fillLevels);
        System.out.print("Heap Sort (fill levels):      ");
        System.out.println(Arrays.toString(fillLevels));
        System.out.println("Most critical zone fill: " + fillLevels[fillLevels.length - 1] + "%");

        // Radix Sort: bin identification numbers
        int[] binIDs = {1042, 1005, 1034, 1018, 1003, 1021, 1056};
        radixSort(binIDs);
        System.out.print("Radix Sort (bin IDs):         ");
        System.out.println(Arrays.toString(binIDs));
    }
}
