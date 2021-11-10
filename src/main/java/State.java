import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

@Data
public class State {
    private List<Integer> B_colors;
    private List<Integer> A_colors;
    private int step;
    private int k;

    public void incStep() {
        step = step + 1;
    }

    public State(List<Integer> b_colors, List<Integer> a_colors, int step, int k) {
        B_colors = b_colors;
        A_colors = a_colors;
        this.step = step;
        this.k = k;
    }

    public State(State state) {
        B_colors = new LinkedList<>(state.getB_colors());
        A_colors = new LinkedList<>(state.getA_colors());
        step = state.getStep();
        k = state.getK();
    }
}
