import java.util.*;

public class NFTGenerator {

    // Generates k random objects
    public static List<List<String>> generateRandomObjects(List<List<String>> properties, int k) {
        Random random = new Random();
        List<List<String>> result = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            List<String> obj = new ArrayList<>();

            // Pick one random value from each inner list
            for (List<String> propValues : properties) {
                int idx = random.nextInt(propValues.size());
                obj.add(propValues.get(idx));
            }

            result.add(obj);
        }

        return result;
    }

    public static void main(String[] args) {
        List<List<String>> properties = new ArrayList<>();
        properties.add(Arrays.asList("Short", "Medium", "Tall"));
        properties.add(Arrays.asList("Green", "Yellow", "Red"));

        int k = 5;

        List<List<String>> generated = generateRandomObjects(properties, k);

        for (List<String> obj : generated) {
            System.out.println(obj);
        }
    }
}
