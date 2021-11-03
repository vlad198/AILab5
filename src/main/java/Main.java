import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Player player = new HumanPlayer();
        Problem p = new Problem(8, 4, 4);
        p.solve(player);
    }
}
