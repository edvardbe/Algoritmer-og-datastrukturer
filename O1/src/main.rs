use rand::Rng;


fn maximum_subarray(numbers: &[i32]) -> i32 {
    let mut max_so_far = numbers[0];
    let mut max_end_here = numbers[0]; 
    let mut start = 0;
    let mut end = 0;
    let mut s = 0;
    for i in 0..numbers.len() {
        // Add the current element to the sum
        max_end_here += numbers[i];
        // If the sum is less than the current element, then the sum is the current element
        // Set start at s and end at i
        if max_so_far < max_end_here{
            max_so_far = max_end_here;
            start = s;
            end = i;
        }
        // 
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

fn random_array(number: &i32) -> Vec<i32> {
    let mut rng = rand::thread_rng();
    let mut numbers = Vec::new();
    for _ in 0..*number {
        numbers.push(rng.gen_range(-10..10));
    }
    return numbers;

}

fn main() {
    let num = vec![-1, 3, -9, 2, 2, -1, 2, -1, -5];

    maximum_subarray(&num);

    let numbers = random_array(&10);
    maximum_subarray(&numbers);
}
