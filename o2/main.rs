use std::time::Instant;

fn recursive_exponent_v1(base: f64, exponent: i32) -> f64 {
    if exponent == 1{
        return base;
    }
    else {
        return base * (recursive_exponent_v1(base, exponent - 1) as f64);
    }
}


fn recursive_exponent_v2(base: f64, exponent: i32) -> f64 {
    if exponent == 1{
        return base;
    }
    else if (exponent & 1) == 1 {
        let temp = recursive_exponent_v2(base * base, (exponent - 1) / 2);
        return base * (temp as f64);
    } else {
        let temp = recursive_exponent_v2(base * base, exponent / 2);
        return temp;
    }
}


fn main() {
    let base = 2.0;

    let exponents = vec![8, 16, 32, 64, 128, 256, 512];

    let mut start;
    let mut result;
    let mut duration;


    println!("-----------Method 1----------\n");
    for exponent in &exponents {
        start = Instant::now();
        result = recursive_exponent_v1(base, *exponent);
        duration = start.elapsed();
        if result < 1000000000.0 {
            println!("Time elapsed in recursive_exponent_v2(), {} ^ {} = {} is: {:?}\n", base, *exponent, result, duration);
        }
        else {
            println!("Time elapsed in recursive_exponent_v2(), {} ^ {} is: {:?}\n", base, *exponent, duration);
        }
    }

    println!("-----------Method 2----------\n");

    for exponent in &exponents {
        start = Instant::now();
        result = recursive_exponent_v2(base, *exponent);
        duration = start.elapsed();
        if result < 1000000000.0 {
            println!("Time elapsed in recursive_exponent_v2(), {} ^ {} = {} is: {:?}\n", base, *exponent, result, duration);
        }
        else {
            println!("Time elapsed in recursive_exponent_v2(), {} ^ {} is: {:?}\n", base, *exponent, duration);
        }
    }

    println!("-----------.pow()----------\n");

    for exponent in &exponents {
        start = Instant::now();
        result = base.powf(*exponent as u32 as f64);
        duration = start.elapsed();
        if result < 1000000000.0 {
            println!("Time elapsed in recursive_exponent_v2(), {} ^ {} = {} is: {:?}\n", base, *exponent, result, duration);
        }
        else {
            println!("Time elapsed in recursive_exponent_v2(), {} ^ {} is: {:?}\n", base, *exponent, duration);
        }
    }
}