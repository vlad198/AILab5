import lombok.AllArgsConstructor;
import org.paukov.combinatorics3.Generator;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class Problem {
    private static final int INF = (int) 2e30;

    private final int n;
    private final int m;
    private final int k;

    private State generateInitialState(int n, int m, int k) {
        List<Integer> A_colors = Utils.getCombination(n, m, k);
        List<Integer> B_colors = new ArrayList<>();
        int step = -1;
        int kk = -1;

        return new State(B_colors, A_colors, step, k);
    }

    private StateEnum stateIsFinal(State state) {
        if (state.getStep() >= 2 * n)
            return StateEnum.B_WON;

        if (Utils.sameLists(state.getA_colors(), state.getB_colors()))
            return StateEnum.A_WON;

        return StateEnum.IS_NOT_FINAL;
    }

    public void solve(Player player) throws IOException {
        State state = generateInitialState(n, m, k);

        System.out.println(state);
        System.out.println(Utils.numberOfAppearances(state.getA_colors(), 0));
        while (Objects.equals(stateIsFinal(state), StateEnum.IS_NOT_FINAL)) {
            System.out.println("Step: " + (state.getStep() + 1));
            state = player.playTurn(state);
            System.out.println("Code:" + state.getK());
        }

        System.out.println(stateIsFinal(state));
    }

    public void solveMiniMax() {
        Random random = new Random();

        Set<List<Integer>> candidates = generateAllCandidates();
        System.out.println(candidates.size());

        List<Integer> A_colors = Utils.getCombination(n, m, k);
        System.out.println(A_colors);

        List<Integer> B_colors = (new LinkedList<>(candidates)).get(random.nextInt(candidates.size()));
        candidates.remove(B_colors);

        int prevScore = Utils.compareAndGetCode(A_colors, B_colors);
        int step = 1;

        while (step <= 2 * n && candidates.size() > 1 && prevScore < k) {
            List<Integer> nextB_colors = miniMax(B_colors, prevScore, candidates);
            int nextPrevScore = Utils.compareAndGetCode(A_colors, nextB_colors);

            if (!nextB_colors.equals(B_colors)) {
                Pair<Integer, Integer> positionColor = getPositionColorPair(nextB_colors, B_colors);

                candidates = generateSequencesLeft(nextB_colors, positionColor.getFirst(), positionColor.getSecond(), candidates, nextPrevScore, prevScore);

                B_colors = nextB_colors;
                prevScore = nextPrevScore;
            }

            step++;
        }

        if (prevScore == k || candidates.size() == 1)
        {
            System.out.println(StateEnum.B_WON);
            System.out.println(B_colors);
        }
        else {
            System.out.println(StateEnum.A_WON);
            System.out.println(candidates.size());
            System.out.println((new LinkedList<>(candidates)).get(0));
        }
    }

    public Set<List<Integer>> generateAllCandidates() {
        Set<List<Integer>> set = new HashSet<>();

        Generator.combination(Utils.generateAllBalls(n, m))
                .simple(k)
                .stream()
                .forEach(integers -> {
                    Generator.permutation(integers).simple().stream().forEach(set::add);
                });

        return set;
    }

    public List<List<Integer>> generateValidCombinations(List<Integer> B_colors, Set<List<Integer>> combinationsLeft) {
        List<List<Integer>> result = new LinkedList<>();

        for (int i = 0; i < B_colors.size(); i++)
            for (int color = 0; color < n; color++)
                if (color != B_colors.get(i)) {
                    List<Integer> copy = new LinkedList<>(B_colors);
                    copy.set(i, color);

                    if (combinationsLeft.contains(copy))
                        result.add(copy);
                }

        return result;
    }

    public void removeFromSetByPositionAndColor(Set<List<Integer>> combinationsLeft, int position, int color) {
        combinationsLeft = combinationsLeft.stream().filter(combination -> !Objects.equals(combination.get(position), color)).collect(Collectors.toSet());
    }

    public Set<List<Integer>> filterCombinationsLeftBasedOnResponse(List<Integer> B_colors, int response, List<Integer> previousCombination, int previousResponse, Set<List<Integer>> combinationsLeft) {
        Pair<Integer, Integer> positionColor = getPositionColorPair(B_colors, previousCombination);

        if (response > previousResponse)
            return combinationsLeft.stream()
                    .filter(combination -> combination.get(positionColor.getFirst()).equals(positionColor.getSecond()))
                    .collect(Collectors.toSet());
        else
            return combinationsLeft.stream()
                    .filter(combination -> !combination.get(positionColor.getFirst()).equals(positionColor.getSecond()))
                    .collect(Collectors.toSet());
    }

    private Pair<Integer, Integer> getPositionColorPair(List<Integer> b_colors, List<Integer> previousCombination) {
        for (int i = 0; i < b_colors.size(); i++)
            if (!Objects.equals(b_colors.get(i), previousCombination.get(i)))
                return new Pair<>(i, b_colors.get(i));
        throw new IllegalArgumentException("combinations does not share 1 dif color" + b_colors + " " + previousCombination);
    }

    public Set<List<Integer>> generateSequencesLeft(List<Integer> B_colors, int position, int color, Set<List<Integer>> combinationsLeft, int possibleScore, int previousScore) {
        if (possibleScore <= previousScore)
            return combinationsLeft.stream().filter(combination -> combination.get(position) != color).collect(Collectors.toSet());
        else
            return combinationsLeft.stream().filter(combination -> combination.get(position) == color).collect(Collectors.toSet());
    }

    public List<Integer> miniMax(List<Integer> B_colors, int previousResponse, Set<List<Integer>> combinationsLeft) {
        List<List<Integer>> validCombinations = generateValidCombinations(B_colors, combinationsLeft);

        Map<List<Integer>, Set<List<Integer>>> maxCombinations = new HashMap<>();
        for (List<Integer> combination : validCombinations) {
            Set<List<Integer>> resultNotOk = filterCombinationsLeftBasedOnResponse(combination, previousResponse - 1, B_colors, previousResponse, combinationsLeft);
            Set<List<Integer>> resultOk = filterCombinationsLeftBasedOnResponse(combination, previousResponse + 1, B_colors, previousResponse, combinationsLeft);

            if (resultNotOk.size() > resultOk.size())
                maxCombinations.put(combination, resultNotOk);
            else
                maxCombinations.put(combination, resultOk);
        }

        if (maxCombinations.keySet().size() == 0)
            return B_colors;

        long min = (long) 1e60;
        List<List<Integer>> resultsList = new LinkedList<>();
        for (List<Integer> combination : maxCombinations.keySet())
            if (maxCombinations.get(combination).size() < min) {
                resultsList = new LinkedList<>(Collections.singletonList(combination));
                min = maxCombinations.get(combination).size();
            } else if (maxCombinations.get(combination).size() == min)
                resultsList.add(combination);

        Random random = new Random();

        return resultsList.get(random.nextInt(resultsList.size()));
    }
}
