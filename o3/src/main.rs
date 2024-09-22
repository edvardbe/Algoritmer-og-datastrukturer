use std::time::Instant;
use num_format::{Locale, ToFormattedString};
use rand::Rng;

fn random_array(n: usize) -> Vec<i32> {
    let mut arr = Vec::with_capacity(n);
    let mut rng = rand::thread_rng();
    for _ in 0..n {
        arr.push(rng.gen_range(-50..50 as i32));
    }
    arr
}

fn create_alternating_array(length: usize) -> Vec<i32> {
    let mut result = Vec::with_capacity(length);
    let value1 = 1;
    let value2 = 2;

    for i in 0..length {
        if i % 2 == 1 {
            result.push(value1);
        } else {
            result.push(value2);
        }
    }

    result
}

fn sorted_array(n: usize) -> Vec<i32> {
    (0..n as i32).collect()
}

fn reversed_array(n: usize) -> Vec<i32> {
    (0..n as i32).rev().collect()
}

fn check_sum(original: i64, sorted: i64) -> bool {
    original == sorted
}

fn calculate_sum(arr: &[i32]) -> i64 {
    arr.iter().map(|&x| x as i64).sum()
}

fn check_order(sorted: &[i32]) -> bool {
    sorted.windows(2).all(|w| w[0] <= w[1])
}

fn time_taker<F>(func: F, arr: &mut [i32], begin: usize, end: usize) -> usize
where
    F: Fn(&mut [i32], usize, usize),
{
    let start = Instant::now();
    func(arr, begin, end);
    start.elapsed().as_nanos() as usize
}

fn format_with_space(number: usize) -> String {
    number.to_formatted_string(&Locale::en)
}

fn run_test(size: usize, low: usize, high: usize) {
    let mut random = random_array(size);
    let random_c = random.clone();

    let mut alternating = create_alternating_array(size);
    let alternating_c = alternating.clone();

    let mut sorted = sorted_array(size);
    let sorted_c = sorted.clone();

    let mut reversed = reversed_array(size);
    let reversed_c = reversed.clone();

    println!("----- Quick Sort -----");
    println!("{:<18} | {:<10} | {:<17} | {:<10} | {:<8}",
        "Array type", "Array size", "Time elapsed", "Equal sum", "Sorted");

    println!("{:<18} | {:<10} | {:<14} ns | {:<10} | {:<8}",
        "Random array", format_with_space(size),
        format_with_space(time_taker(quick_sort, &mut random, low, high)),
        check_sum(calculate_sum(&random), calculate_sum(&random_c)),
        check_order(&random));

    println!("{:<18} | {:<10} | {:<14} ns | {:<10} | {:<8}",
        "Alternating array", format_with_space(size),
        format_with_space(time_taker(quick_sort, &mut alternating, low, high)),
        check_sum(calculate_sum(&alternating), calculate_sum(&alternating_c)),
        check_order(&alternating));

    println!("{:<18} | {:<10} | {:<14} ns | {:<10} | {:<8}",
        "Sorted array", format_with_space(size),
        format_with_space(time_taker(quick_sort, &mut sorted, low, high)),
        check_sum(calculate_sum(&sorted), calculate_sum(&sorted_c)),
        check_order(&sorted));

    println!("{:<18} | {:<10} | {:<14} ns | {:<10} | {:<8}",
        "Reversed array", format_with_space(size),
        format_with_space(time_taker(quick_sort, &mut reversed, low, high)),
        check_sum(calculate_sum(&reversed), calculate_sum(&reversed_c)),
        check_order(&reversed));

    println!("\n\n");

    println!("----- Dual Pivot Quick Sort -----");
    println!("{:<18} | {:<10} | {:<17} | {:<10} | {:<8}",
        "Array type", "Array size", "Time elapsed", "Equal sum", "Sorted");

    println!("{:<18} | {:<10} | {:<14} ns | {:<10} | {:<8}",
        "Random array", format_with_space(size),
        format_with_space(time_taker(dual_pivot_quick_sort, &mut random, low, high)),
        check_sum(calculate_sum(&random), calculate_sum(&random_c)),
        check_order(&random));

    println!("{:<18} | {:<10} | {:<14} ns | {:<10} | {:<8}",
        "Alternating array", format_with_space(size),
        format_with_space(time_taker(dual_pivot_quick_sort, &mut alternating, low, high)),
        check_sum(calculate_sum(&alternating), calculate_sum(&alternating_c)),
        check_order(&alternating));

    println!("{:<18} | {:<10} | {:<14} ns | {:<10} | {:<8}",
        "Sorted array", format_with_space(size),
        format_with_space(time_taker(dual_pivot_quick_sort, &mut sorted, low, high)),
        check_sum(calculate_sum(&sorted), calculate_sum(&sorted_c)),
        check_order(&sorted));

    println!("{:<18} | {:<10} | {:<14} ns | {:<10} | {:<8}",
        "Reversed array", format_with_space(size),
        format_with_space(time_taker(dual_pivot_quick_sort, &mut reversed, low, high)),
        check_sum(calculate_sum(&reversed), calculate_sum(&reversed_c)),
        check_order(&reversed));

    println!("\n\n");
}

fn swap(arr: &mut [i32], i: usize, j: usize) {
    arr.swap(i, j);
}

fn partition(arr: &mut [i32], low: usize, high: usize) -> usize {
    let median = median3_sort(arr, low, high);
    let median_value = arr[median];
    swap(arr, median, high - 1);

    let mut internal_low = low;
    let mut internal_high = high - 1;

    loop {
        internal_low += 1;
        internal_high -= 1;
        while arr[internal_low] < median_value {
            internal_low += 1;
        }
        while arr[internal_high] > median_value {
            internal_high -= 1;
        }
        if internal_low >= internal_high {
            break;
        }
        swap(arr, internal_low, internal_high);
        
    }
    swap(arr, internal_low, high - 1);
    internal_low
}

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

fn quick_sort(arr: &mut [i32], low: usize, high: usize) {
    if high - low > 2 {
        let pivot = partition(arr, low, high);
        if pivot > 0 {
            quick_sort(arr, low, pivot - 1);
        }
        quick_sort(arr, pivot + 1, high);
    } else {
        median3_sort(arr, low, high);
    }
}

fn dual_pivot_quick_sort(arr: &mut [i32], low: usize, high: usize) {
    if low < high {
        let piv = dual_partition(arr, low, high);
        if piv[0] > low {
            dual_pivot_quick_sort(arr, low, piv[0] - 1);
        }
        if arr[piv[0]] != arr[piv[1]] {
            dual_pivot_quick_sort(arr, piv[0] + 1, piv[1] - 1);
        }
        if piv[1] < high {
            dual_pivot_quick_sort(arr, piv[1] + 1, high);
        }
    }
}

fn dual_partition(arr: &mut [i32], low: usize, high: usize) -> [usize; 2] {
    let one_third = (high - low) / 3;
    swap(arr, low, low + one_third);
    swap(arr, low, high - one_third);
    if arr[low] > arr[high] {
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

    // Returning the indices of the pivots
    // because we cannot return two elements
    // from a function, we do that using an array.
    return [j, g];
}

fn main() { let array_sizes = vec![5_000, 50_000, 500_000, 5_000_000, 50_000_000];

    for &size in &array_sizes {
        run_test(size, 0, size - 1);
    }
}
