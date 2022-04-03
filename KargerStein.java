package lesson_4;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KargerStein extends Karger {
    public static void main(String[] args) {
        KargerStein kargerStein = new KargerStein();
        Map<Character, Set<Integer>> graph = kargerStein.getRandomGraph(50, 100);

        int result = kargerStein.runSteinAlgorithm(graph);
        System.out.println("Smallest cut – " + result);
    }

    // run the algorithm
    public int runSteinAlgorithm(Map<Character, Set<Integer>> graph) {
        int iterationCount = (int) Math.round(Math.sqrt(graph.size()));

        // graph is shrinking
        contractGraph(graph);

        // run  main algorithm
        return runAlgorithm(graph, iterationCount);
    }

    // pull the graph several times
    private void contractGraph(Map<Character, Set<Integer>> graph) {
        int size = (int) Math.floor(Math.sqrt(graph.size()));

        Set<Integer> totalSet = new HashSet<>();
        for (Map.Entry<Character, Set<Integer>> entry: graph.entrySet())
            totalSet.addAll(entry.getValue());

        while (graph.size() != size) {
            int randomEdge = getRandomElement(totalSet);
            kargetStep(graph, randomEdge, totalSet);
        }
    }
}
