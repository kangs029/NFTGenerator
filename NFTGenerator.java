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
    public static List<List<String>> generateUniqueKObjects(List<List<String>> properties, int k) {
        if(k<=0) return new ArrayList<>();

        List<List<String>> all = generateUniqueObjects(properties);

        int take = Math.min(k, all.size());
        return new ArrayList<>(all.subList(0, take));
    }
    //helper to get unique objects
    private static List<List<String>> generateUniqueObjects(List<List<String>> properties) {
        if(properties==null) return new ArrayList<>();

        for (List<String> prop : properties) {
            if (prop == null || prop.isEmpty()) {
                return Collections.emptyList();
            }
        }

        List<List<String>> all = new ArrayList<>();
        List<String> current = new ArrayList<>(properties.size());

        buildAll(properties, 0, current, all);

        Collections.shuffle(all);
        return all;
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

    /**
     *  level 3: Generate up to k unique objects while respecting rarity limits.
     * - properties: List<List<String>>
     * - limits: List<List<Integer>> (same shape as properties; null = unlimited)
     * - k: how many objects to generate
     */
    public static List<List<String>> generateUniqueWithRarity(List<List<String>> properties,List<List<Integer>> limits,int k) {
        if(k<=0) return new ArrayList<>();

        List<List<String>> all = generateUniqueObjects(properties);
        if(limits==null) return generateUniqueKObjects(properties,k);//no limits return k objects

        int eachObjectSize=properties.size();

        //Prepare counts : counts usage of every value in properties
        int[][] counts = new int[eachObjectSize][];
        for (int i = 0; i < eachObjectSize; i++) {
            counts[i] = new int[properties.get(i).size()];
        }

        List<List<String>> selected = new ArrayList<>();

        for (List<String> object : all) {
            boolean shouldTakeCurrObj = true;

            for (int i = 0; i < eachObjectSize; i++) {
                String val = object.get(i);
                int vidx = properties.get(i).indexOf(val);

                int currentCount = counts[i][vidx];

                //if limit for any val is null(infinte use) just keep it
                if(limits.get(i)==null || limits.get(i).get(vidx)==null) continue; 
                int actualLimit=limits.get(i).get(vidx);
                
                if (currentCount + 1 > actualLimit) {
                    shouldTakeCurrObj = false;
                    break;
                }
            }

            if (shouldTakeCurrObj) {
                for (int i = 0; i < eachObjectSize; i++) {
                    int vidx = properties.get(i).indexOf(object.get(i));
                    counts[i][vidx]++;
                }
                selected.add(object);

                if (selected.size() == k) break;
            }
        }
        return selected;
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
        List<List<String>> result = generateUniqueKObjects(properties, n);
        System.out.println("\nRequested n = " + n + ", returning = " + result.size());
        for (List<String> obj : result) {
            System.out.println(obj);
        }

        // limits for rarity
        List<List<Integer>> limits = new ArrayList<>();
        limits.add(Arrays.asList(null, 2, 1));  // Medium =2, Tall = 1 max
        limits.add(Arrays.asList(null, null, 2));  // Red = 2 max

        List<List<String>> result3 = generateUniqueWithRarity(properties, limits, 7);

        System.out.println("\ngenerateUniqueWithRarity Generated: " + result3.size());
        for (List<String> r : result3) {
            System.out.println(r);
        }
    }
}
