import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }



public int getMaxOccurrences(String components, int minLength, int maxLength, int maxUnique) {
    Map<String, Integer> map = new HashMap<>();
    int maxFr = 0;
        for (int i = 0; i < components.length(); i++) {
        for (int j = 0; j <= maxLength; j++) {
                if (components.substring(i, j).length() >= minLength) {
                    map.put(components.substring(i, j), map.getOrDefault(components.substring(i, j), 0) + 1);
                }
        }
    }


    }

    public boolean Unik(String s){
        List<char[]> list = new ArrayList<>();
        list.add(s.toCharArray());
        for (int i = 0; i < list.size(); i++) {
            int unit
        }

    }
}
