import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class Match implements Callable<String> {

    private final String p1;
    private final String p2;
    private final String round;

    public Match(String p1, String p2, String round) {
        this.p1 = p1;
        this.p2 = p2;
        this.round = round;
    }

    @Override
    public String call() throws Exception {
        System.out.printf("Inicia %s: %s vs %s%n", round, p1, p2);

        int wins1 = 0, wins2 = 0, setNum = 1;

        // Se juegan sets hasta que alguien gane 2
        while (wins1 < 2 && wins2 < 2) {
            String winner = ThreadLocalRandom.current().nextBoolean() ? p1 : p2;
            if (winner.equals(p1)) wins1++; else wins2++;
            System.out.printf("  Set %d: gana %s%n", setNum++, winner);
        }

        String champion = (wins1 > wins2) ? p1 : p2;

        System.out.printf("Resultado final (%s): %d-%d → %s avanza%n%n",
                round, wins1, wins2, champion);

        // Simular duración del partido
        Thread.sleep(1500 + ThreadLocalRandom.current().nextLong(501));
        return champion;
    }
}

