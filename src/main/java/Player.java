import java.io.IOException;

public interface Player {
    State playTurn(State state) throws IOException;
}
