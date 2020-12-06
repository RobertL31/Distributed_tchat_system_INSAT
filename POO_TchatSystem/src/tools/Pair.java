package tools;

public class Pair<K, V> {

    private final K fst;
    private final V scd;

    public static <K, V> Pair<K, V> createPair(K fst, V scd) {
        return new Pair<K, V>(fst, scd);
    }

    public Pair(K fst, V scd) {
        this.fst = fst;
        this.scd = scd;
    }

    public K getFirst() {
        return fst;
    }

    public V getSecond() {
        return scd;
    }

}