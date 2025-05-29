import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // 1. Lista de jugadores
        List<String> players = IntStream.rangeClosed(1, 16)
                .mapToObj(i -> "Jugador " + i)
                .collect(Collectors.toList());

        // 2. Pool de hilos para ejecutar los partidos simultáneamente
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // 3. Nombres de las rondas
        String[] rounds = { "Octavos de Final", "Cuartos de Final", "Semifinal", "Final" };
        List<String> currentPlayers = new ArrayList<>(players);

        // 4. Simulación de cada ronda
        for (String round : rounds) {
            System.out.println("=== " + round + " ===");
            List<Future<String>> resultados = new ArrayList<>();

            for (int i = 0; i < currentPlayers.size(); i += 2) {
                String p1 = currentPlayers.get(i);
                String p2 = currentPlayers.get(i + 1);
                resultados.add(executor.submit(new Match(p1, p2, round)));
            }

            // 5. Esperar a que todos los partidos terminen y recolectar ganadores
            currentPlayers = new ArrayList<>();
            for (Future<String> future : resultados) {
                currentPlayers.add(future.get());
            }
        }

        // 6. Mostrar campeón
        System.out.println("🏆 ¡Campeón del torneo: " + currentPlayers.get(0) + "!");
        executor.shutdown();

    }
}
