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
     /**
     * Generate up to k unique objects (each is a List<String>) from the given properties.
     * Each property is a list of possible values; one value is picked per property.
     *
     * If total possible unique combinations < k, returns all combinations.
     */
    public static List<List<String>> generateUniqueObjects(List<List<String>> properties, int k) {
        if(properties==null ||k<=0) return new ArrayList<>();

        for (List<String> prop : properties) {
            if (prop == null || prop.isEmpty()) {
                return Collections.emptyList();
            }
        }

        List<List<String>> all = new ArrayList<>();
        List<String> current = new ArrayList<>(properties.size());

        buildAll(properties, 0, current, all);

        Collections.shuffle(all);

        int take = Math.min(k, all.size());
        return new ArrayList<>(all.subList(0, take));
    }

    // recursive helper to enumerate cartesian product
    private static void buildAll(List<List<String>> props, int idx, List<String> cur, List<List<String>> out) {
        if (idx == props.size()) {
            out.add(new ArrayList<>(cur));
            return;
        }
        for (String val : props.get(idx)) {
            cur.add(val);
            buildAll(props, idx + 1, cur, out);
            cur.remove(cur.size() - 1);
        }
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

        int n=12;
        List<List<String>> result = generateUniqueObjects(properties, n);
        System.out.println("\nRequested n = " + n + ", returning = " + result.size());
        for (List<String> obj : result) {
            System.out.println(obj);
        }
    }
}
