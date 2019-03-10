package web;

public class Tuple2<T, K> {
    public T _1;
    public K _2;
    
    public Tuple2() {
        _1 = null;
        _2 = null;
    }
    
    public Tuple2(T t, K k) {
        _1 = t;
        _2 = k;
    }
}
