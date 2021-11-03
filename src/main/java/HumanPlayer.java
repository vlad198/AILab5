import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HumanPlayer implements Player {
    @Override
    public State playTurn(State state) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        String line = reader.readLine();
        System.out.println(line);

        state.setB_colors(getListOfNumbersFromLine(line));
        state.setK(Utils.compareAndGetCode(state.getA_colors(), state.getB_colors()));
        state.incStep();

        return state;
    }

    private List<Integer> getListOfNumbersFromLine(String line) {
        String[] words = line.split("\\s+");

        return Arrays.stream(words).map(Integer::parseInt).collect(Collectors.toList());
    }
}
