use std::time::Instant;
use std::collections::HashMap;
use rand::Rng;
struct HashTable {
    array: Vec<i32>,
    collisions: u32,
    length: usize, 
}

impl HashTable{
    fn new(length: usize ) -> HashTable{
        let size = HashTable::calculate_size(length);
        HashTable {
            array: vec![0; HashTable::calculate_size(size)],
            collisions: 0,
            length: size,
        }
    }

    fn calculate_size(mut length: usize) -> usize {
        length = length | (length >> 1);
        length = length | (length >> 2);
        length = length | (length >> 4);
        length = length | (length >> 8);
        length = length | (length >> 16);

        length = length + 1;

        length
    }

    fn multiplicative_hash(&mut self, key: i32) -> usize {
        let mut a = key as f32 * (f32::sqrt(5.0) - 1.0) / 2.0;
        a = a - (a as i32) as f32;
        self.length * (f32::abs(a) as usize)
        
    }
    fn modulo_hash(&mut self, key: i32) -> usize {
        let result = (2 * i32::abs(key) + 1) % ((self.length as i32) - 1);
        return result as usize;
    }
    fn put(&mut self, key: i32){
        let mut pos = self.multiplicative_hash(key);
        if self.array[pos] == 0 {
            self.array[pos] = key;
        } else {
            self.collisions = self.collisions + 1;
            let mut i = 1;
            let h2 = self.modulo_hash(key);

            while i < self.length {
                pos = (pos + h2) % (self.length - 1);
                if self.array[pos] == 0 {
                    self.array[pos] = key;
                    break;
                } else {
                    i = i + 1;
                    self.collisions = self.collisions + 1;
                }
            }
            
        }

    }
    fn get(&mut self, key: &i32) -> i32{
        let mut pos = self.multiplicative_hash(*key);
        if self.array[pos] == *key {
            return self.array[pos];
        } else {
            self.collisions = self.collisions + 1;
            let mut i = 1;
            let h2 = self.modulo_hash(*key);

            while i < self.length {
                pos = (pos + h2) % (self.length - 1);
                if self.array[pos] == *key {
                    return self.array[pos];
                } else {
                    i = i + 1;
                    self.collisions = self.collisions + 1;
                }
            }
            return -1;
            
        }

    }
}


fn random_array(n: usize, range: i32) -> Vec<i32> {
    let mut arr = Vec::with_capacity(n);
    let mut rng = rand::thread_rng();
    for _ in 0..n {
        arr.push(rng.gen_range(1..range as i32));
    }
    arr
}
fn hash_list(array: &Vec<i32>, hash_table: &mut HashTable) {
    for i in array {
        hash_table.put(*i)
    }
}

fn main() {
    let size = 10_000_000;
    let range = size * 5;
    let random_array = random_array(size, range as i32);

    let mut hash_table = HashTable::new(size);
    let mut hash_map = HashMap::new();
    let mut start = Instant::now();
    hash_list(&random_array, &mut hash_table);
    let fill_time = start.elapsed();

    println!("Dataset size: {}", size);
    println!("HashTable length: {}", hash_table.length);
    println!("Time taken to fill HashTable: {:?}", fill_time);

    start = Instant::now();
    for i in 0..size{
        hash_map.insert(&random_array[i], i);
    }
    let fill_map_time = start.elapsed();

    println!("HashMap length: {}", hash_map.len());
    println!("Time taken to fill HashMap: {:?}", fill_map_time);

    start = Instant::now();
    for i in &random_array{
        hash_table.get(i);
    }
    let find_time = start.elapsed();

    println!("Time taken to find all elements HashTable: {:?}", find_time);

    start = Instant::now();
    for i in &random_array{
        hash_map.get(i);
    }
    let find_map_time = start.elapsed();

    println!("Time taken to find all element HashMap: {:?}", find_map_time);
}
