
fn maximum_subarray(numbers: &[i32]) -> i32 {
    let mut max_so_far = numbers[0];
    let mut max_end_here = numbers[0]; 
    let mut start = 0;
    let mut end = 0;
    let mut s = 0;
    for i in 0..numbers.len() {
        max_end_here += numbers[i];

        if max_so_far < max_end_here{
            max_so_far = max_end_here;
            start = s;
            end = i;
        }

        if max_end_here < 0 {
            max_end_here = 0;
            s = i + 1;
        }
    }

    println!("Maximum subarray sum: {}", max_so_far);
    println!("Start index: {}", start);
    println!("End index: {}", end);
    return 0;
    
}

fn main() {
    let num = vec![-1, 3, -9, 2, 2, -1, 2, -1, -5];

    maximum_subarray(&num);
}
