use rand::Rng;
use std::time::Instant;

// Helper function to generate random array
fn random_array(n: usize) -> Vec<i32> {
    let mut arr = Vec::with_capacity(n);
    let mut rng = rand::thread_rng();
    for _ in 0..n {
        arr.push(rng.gen_range(0..100));
    }
    arr
}

// Helper function to generate alternating array
fn create_alternating_array(length: usize) -> Vec<i32> {
    let mut result = vec![0; length];
    let value1 = 1;
    let value2 = 2;

    for i in 0..length {
        if i % 2 == 0 {
            result[i] = value1;
        } else {
            result[i] = value2;
        }
    }
    result
}

// Function to check if sum is the same
fn check_sum(original: i64, sorted: i64) -> bool {
    original == sorted
}

// Calculate sum of the array
fn calculate_sum(arr: &[i32]) -> i64 {
    arr.iter().map(|&x| x as i64).sum()
}

// Function to check if array is sorted
fn check_order(arr: &[i32]) -> bool {
    for i in 0..arr.len() - 1 {
        if arr[i] > arr[i + 1] {
            return false;
        }
    }
    true
}

// Swap function
fn swap(arr: &mut [i32], i: usize, j: usize) {
    arr.swap(i, j);
}

// Median of 3 partitioning
fn median3_sort(arr: &mut [i32], low: usize, high: usize) -> usize {
    let mid = (low + high) / 2;
    if arr[low] > arr[mid] {
        swap(arr, low, mid);
    }
    if arr[mid] > arr[high] {
        swap(arr, mid, high);
        if arr[low] > arr[mid] {
            swap(arr, low, mid);
        }
    }
    mid
}

// Partition for quick sort
fn partition(arr: &mut [i32], low: usize, high: usize) -> usize {
    let median = median3_sort(arr, low, high);
    let pivot_value = arr[median];
    swap(arr, median, high - 1);
    
    let mut i = low;
    let mut j = high - 1;
    
    loop {
        while arr[i] < pivot_value {
            i += 1;
        }
        while arr[j] > pivot_value {
            j -= 1;
        }
        if i >= j {
            break;
        }
        swap(arr, i, j);
    }
    
    swap(arr, i, high - 1);
    i
}

// Standard Quick Sort
fn quick_sort(arr: &mut [i32], low: usize, high: usize) {
    if high - low > 2 {
        let pivot = partition(arr, low, high);
        quick_sort(arr, low, pivot - 1);
        quick_sort(arr, pivot + 1, high);
    } else {
        median3_sort(arr, low, high);
    }
}

// Partition for dual pivot quick sort
fn partition2(arr: &mut [i32], low: usize, high: usize) -> (usize, usize) {
    if arr[low + (high - low) / 3] > arr[high - (high - low) / 3] {
        swap(arr, low, high);
    }
    
    let mut j = low + 1;
    let mut g = high - 1;
    let mut k = low + 1;
    let p = arr[low];
    let q = arr[high];
    
    while k <= g {
        if arr[k] < p {
            swap(arr, k, j);
            j += 1;
        } else if arr[k] >= q {
            while arr[g] > q && k < g {
                g -= 1;
            }
            swap(arr, k, g);
            g -= 1;
            if arr[k] < p {
                swap(arr, k, j);
                j += 1;
            }
        }
        k += 1;
    }
    j -= 1;
    g += 1;
    
    swap(arr, low, j);
    swap(arr, high, g);
    
    (j, g)
}

// Dual Pivot Quick Sort
fn dual_pivot_quick_sort(arr: &mut [i32], low: usize, high: usize) {
    if low < high {
        let pivots = partition2(arr, low, high);
        dual_pivot_quick_sort(arr, low, pivots.0 - 1);
        if arr[pivots.0] != arr[pivots.1] {
            dual_pivot_quick_sort(arr, pivots.0 + 1, pivots.1 - 1);
        }
        dual_pivot_quick_sort(arr, pivots.1 + 1, high);
    }
}

// Function to run test
fn run_test(arr: &[i32], low: usize, high: usize) {
    let mut sorted = arr.to_vec();
    let mut sorted2 = arr.to_vec();
    println!("----- Quick Sort -----");
    println!("Array length: {}", high + 1);
    
    let start = Instant::now();
    quick_sort(&mut sorted, low, high);
    println!("Time elapsed: {:?}", start.elapsed());
    
    let original_sum = calculate_sum(arr);
    let sorted_sum = calculate_sum(&sorted);
    
    println!("The sum of the sorted array is the same as the original: {}, {} = {}", 
        check_sum(original_sum, sorted_sum), original_sum, sorted_sum);
    println!("Original array is in order: {}", check_order(arr));
    println!("Sorted array is in order: {}", check_order(&sorted));
    println!("----------------------\n");

    println!("----- Dual Pivot Quick Sort -----");
    println!("Array length: {}", high + 1);
    
    let start = Instant::now();
    dual_pivot_quick_sort(&mut sorted2, low, high);
    println!("Time elapsed: {:?}", start.elapsed());
    
    let sorted_sum2 = calculate_sum(&sorted2);
    println!("The sum of the sorted array is the same as the original: {}, {} = {}", 
        check_sum(original_sum, sorted_sum2), original_sum, sorted_sum2);
    println!("Original array is in order: {}", check_order(arr));
    println!("Sorted array is in order: {}", check_order(&sorted2));
    println!("----------------------\n");
}

fn main() {
    let array_sizes = vec![500_000, 5_000_000, 50_000_000];

    for &size in &array_sizes {
        println!("----- Random Array -----");
        let original = random_array(size);
        run_test(&original, 0, original.len() - 1);
    }

    for &size in &array_sizes {
        println!("----- Alternating Array -----");
        let original = create_alternating_array(size);
        run_test(&original, 0, original.len() - 1);
    }

    for &size in &array_sizes {
        println!("----- Sorted Array -----");
        let original: Vec<i32> = (0..size as i32).collect();
        run_test(&original, 0, original.len() - 1);
    }

    for &size in &array_sizes {
        println!("----- Reverse Sorted Array -----");
        let original: Vec<i32> = (0..size as i32).rev().collect();
        run_test(&original, 0, original.len() - 1);
    }
}
