use std::collections::HashMap;


fn lempel_ziv_welch(input: &str) -> Vec<usize> {
    let mut dictionary = HashMap::new();
    let mut w = String::new();
    let mut result = Vec::new();
    for c in input.chars() {
        let wc = format!("{}{}", w, c);
        if dictionary.contains_key(&wc) {
            w = wc;
        } else {
            result.push(dictionary[&w]);
            dictionary.insert(wc, dictionary.len() + 1);
            w = c.to_string();
        }
    }
    if !w.is_empty() {
        result.push(dictionary[&w]);
    }
    result
}


fn main() {
    println!("Hello, world!");
}
