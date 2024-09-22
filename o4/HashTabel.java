package o4;

import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.util.HashMap;

public class HashTabel<K, V> {
    private List<V> tabel;
    private List<K> keys;
    private long MAX_SIZE;

    public HashTabel(long size){
        this.tabel = new ArrayList<>();
        this.keys = new ArrayList<>();
        this.MAX_SIZE = size;
    }
    public HashTabel(){
        this.tabel = new ArrayList<>();
        this.keys = new ArrayList<>();
    }

    public void put(K key, V value){
        putVal(hash(key), key, value)
    }

    public int hash(K key){
        int hash;
        return (key == null) ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }
}