import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class State {
    private List<Integer> B_colors;
    private List<Integer> A_colors;
    private int step;
    private int k;

    public void incStep() {
        step = step + 1;
    }
}
