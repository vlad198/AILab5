import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@AllArgsConstructor
public class Problem {
    private final int n;
    private final int m;
    private final int k;

    private State generateInitialState() {
        List<Integer> B_colors = Utils.getCombination(n, m, k);
        List<Integer> A_colors = new ArrayList<>();
        int step = -1;
        int k = -1;

        return new State(B_colors, A_colors, step, k);
    }

    private StateEnum stateIsFinal(State state) {
        if (state.getStep() >= 2 * n)
            return StateEnum.B_WON;

        if (Utils.sameLists(state.getA_colors(), state.getB_colors()))
            return StateEnum.A_WON;

        return StateEnum.IS_NOT_FINAL;
    }

    public void solve() {
        State initialState = generateInitialState();
        System.out.println(initialState);
        System.out.println(stateIsFinal(initialState));
    }
}
