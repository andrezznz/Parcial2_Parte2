import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws Exception {
        List<String> players = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            players.add("Jugador " + i);
        }

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        String[] rounds = { "Octavos de Final", "Cuartos de Final", "Semifinal", "Final" };
        List<String> currentPlayers = new ArrayList<>(players);

        for (int ronda = 0; ronda < rounds.length; ronda++) {
            String roundName = rounds[ronda];
            System.out.println("===== " + roundName.toUpperCase() + " =====");

            List<Future<String>> resultados = new ArrayList<>();
            List<String> emparejados = new ArrayList<>();

            // === Emparejamientos ===
            if (ronda == 0) {
                // Octavos: emparejamiento fijo (1-16, 2-15, ..., 8-9)
                for (int i = 0; i < currentPlayers.size() / 2; i++) {
                    String p1 = currentPlayers.get(i);
                    String p2 = currentPlayers.get(currentPlayers.size() - 1 - i);
                    emparejados.add(p1);
                    emparejados.add(p2);
                }
            } else {
                // Resto de rondas: se emparejan secuencialmente
                emparejados = new ArrayList<>(currentPlayers);
            }

            for (int i = 0; i < emparejados.size(); i += 2) {
                String p1 = emparejados.get(i);
                String p2 = emparejados.get(i + 1);
                resultados.add(executor.submit(new Match(p1, p2, roundName)));
            }

            currentPlayers = new ArrayList<>();
            for (Future<String> f : resultados) {
                currentPlayers.add(f.get());
            }
        }

        System.out.println("🏆 ¡Campeón del torneo: " + currentPlayers.get(0) + "!");
        executor.shutdown();
    }
}
