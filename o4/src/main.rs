use std::time::Instant;
use num_format::{Locale, ToFormattedString};
use rand::Rng;
struct HashTable {
    array: Vec<i32>,
    collisions: u32,
    length: usize, 
}

impl HashTable{
    fn new(length: usize ) -> HashTable{
        let size = Self::calculate_size(length);
        HashTable {array: Vec::with_capacity(size), collisions: 0, length: size}
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
        let mut A = key as f32 * (f32::sqrt(5.0) - 1.0) / 2.0;
        A = A - (A as i32) as f32;
        self.length * (f32::abs(A) as usize)
        
    }
    fn modulo_hash(&mut self, key: i32) -> usize {
        let result = (2 * i32::abs(key) + 1) % ((self.length as i32) - 1);
        return result as usize;
    }
    fn put(&mut self, key: i32){
        let h1 = self.multiplicative_hash(key);
        if self.array[h1] == 0 {
            self.array[h1] = key;
        } else {
            self.collisions = self.collisions + 1;
            let mut i = 1;
            while i < self.length {
                let h2 = ((self.modulo_hash(key) * i + h1) % (self.length - 1) + (self.length - 1)) % (self.length - 1);
                if self.array[h2] == 0 {
                    self.array[h2] = key;
                    break;
                } else {
                    i = i + 1;
                    self.collisions = self.collisions + 1;
                }
            }
            
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
fn hash_list(array: Vec<i32>, mut hashTable: HashTable) {
    for i in array {
        hashTable.put(i)
    }
}
fn main() {
    let size = 10_000_000;
    let range = size * 5;
    let random_array = random_array(size, range as i32);

    let mut hashTable = HashTable::new(size);

    hash_list(random_array, hashTable);
    println!("test: ");
}
