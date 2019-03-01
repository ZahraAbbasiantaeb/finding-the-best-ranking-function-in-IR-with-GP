import java.util.Comparator;
import java.util.Map;

class MyComparator2 implements Comparator<Object> {

    Map<String, Double> map;

    public MyComparator2(Map<String, Double> map) {
        this.map = map;
    }

    public int compare(Object o1, Object o2) {

        if (map.get(o2) == map.get(o1))
            return 1;
        else
            return ((Double) map.get(o2)).compareTo((Double)
                    map.get(o1));

    }
}