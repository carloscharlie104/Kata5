package software.ulpgc.kata5.architecture.viewmodel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Histogram implements Iterable<Integer> {
    private final Map<String, String> labels;
    private final Map<Integer, Integer> bins;

    public Histogram(Map<String, String> labels) {
        this.labels = labels;
        this.bins = new HashMap<>();
    }

    public void put(int bin) {
        this.bins.put(bin, count(bin) + 1);
    }

    public int count(int bin) {
        return this.bins.getOrDefault(bin, 0);
    }

    @Override
    public Iterator<Integer> iterator() {
        return bins.keySet().iterator();
    }

    public int size() {
        return bins.size();
    }

    public String title() {
        return labels.get("title");
    }

    public String x() {
        return labels.get("x");
    }

    public String y() {
        return labels.get("y");
    }

    public String legend() {
        return labels.get("legend");
    }
}
