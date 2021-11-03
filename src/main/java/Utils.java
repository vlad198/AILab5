import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {
    public static List<Integer> generateAllBalls(int n, int m) {
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                result.add(i);

        return result;
    }

    public static List<Integer> getCombination(int n, int m, int k) {
        Random random = new Random();

        List<Integer> colors = generateAllBalls(n, m);
        Collections.shuffle(colors);

        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            int index = random.nextInt(colors.size());
            result.add(colors.get(index));
            colors.remove(index);
        }

        return result;
    }

    public static boolean sameLists(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size())
            return false;

        for (int i = 0; i < a.size(); i++)
            if (!Objects.equals(a.get(i), b.get(i)))
                return false;
        return true;
    }

    public static int compareAndGetCode(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size())
            return -1;

        int nr = 0;
        for (int i = 0; i < a.size(); i++)
            if (Objects.equals(a.get(i), b.get(i)))
                nr = nr + 1;
        return nr;
    }
}
