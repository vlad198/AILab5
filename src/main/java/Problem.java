import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class Problem {
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

        while (Objects.equals(stateIsFinal(state), StateEnum.IS_NOT_FINAL)) {
            System.out.println("Step: " + (state.getStep() + 1));
            state = player.playTurn(state);
            System.out.println("Code:" + state.getK());
        }

        System.out.println(stateIsFinal(state));
    }
}
