package o4.src.Del1;

public class HashNode<V> {

    private V data;
    private HashNode<V> next;

    public HashNode(V data){
        this.data = data;
        this.next = null;
    }

    public void setNext(HashNode<V> next){
        this.next = next;
    }

    public HashNode<V> getNext(){
        return next;
    }

    public V getData(){
        return data;
    }  
}
