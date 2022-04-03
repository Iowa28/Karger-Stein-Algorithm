package lesson_4;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Karger {
    public static void main(String[] args) {
        Karger karger = new Karger();
        Map<Character, Set<Integer>> graph = karger.getRandomGraph(50, 100);
        karger.printGraph(graph);

        int n = graph.size();
        int iterationCount = 5 * n * (n - 1);

        int result = karger.runAlgorithm(graph, iterationCount);
        System.out.println("Smallest cut – " + result);
    }

    public int runAlgorithm(Map<Character, Set<Integer>> graph, int count) {
        int minCut = Integer.MAX_VALUE;

        while (count > 0) {
            Map<Character, Set<Integer>> copy = new HashMap<>(graph);
            int cutsNumber = randomCut(copy);
            if (cutsNumber < minCut)
                minCut = cutsNumber;

            count--;
        }
        return minCut;
    }

    // find the smallest cut (edges number)
    protected int randomCut(Map<Character, Set<Integer>> graph) {
        int cutsNumber = 0;

        // create common set of edges
        Set<Integer> totalSet = new HashSet<>();
        for (Map.Entry<Character, Set<Integer>> entry: graph.entrySet())
            totalSet.addAll(entry.getValue());

        // algorithm is executed until there are 2 vertices left
        while (graph.size() != 2) {
            // берется рандомное ребро и запускается шаг алгоритма
            int randomEdge = getRandomElement(totalSet);
            kargetStep(graph, randomEdge, totalSet);
        }

        // calculation and section print
        System.out.println("Cut: ");
        for (Map.Entry<Character, Set<Integer>> entry: graph.entrySet()) {
            for (Integer edge : entry.getValue())
                System.out.print(edge + " ");
            System.out.println();
            cutsNumber = entry.getValue().size();
            break;
        }

        return cutsNumber;
    }

    // execute a step of the algorithm with vertex contraction
    protected void kargetStep(Map<Character, Set<Integer>> graph, int randomEdge, Set<Integer> totalSet) {
        // find vertices of this edge
        Map.Entry<Character, Set<Integer>> firstVertex = null;
        Map.Entry<Character, Set<Integer>> secondVertex = null;
        for (Map.Entry<Character, Set<Integer>> entry: graph.entrySet()) {
            if (entry.getValue().contains(randomEdge)) {
                if (firstVertex == null)
                    firstVertex = entry;
                else
                    secondVertex = entry;
            }
        }
        assert firstVertex != null;
        assert secondVertex != null;

        // Two vertices converge into one
        Set<Integer> commonVertex = new HashSet<>(firstVertex.getValue());
        commonVertex.addAll(secondVertex.getValue());

        // Loops are removed
        Set<Integer> vertexWithoutLoops = new HashSet<>();
        for (Integer edge : commonVertex) {
            boolean loop = firstVertex.getValue().contains(edge) && secondVertex.getValue().contains(edge);
            if (!loop)
                vertexWithoutLoops.add(edge);
            else
                totalSet.remove(edge);
        }

        // The first vertex is updated, the second one is deleted
        graph.put(firstVertex.getKey(), vertexWithoutLoops);
        graph.entrySet().remove(secondVertex);
    }

    // remove a random element from the set
    protected <T> T getRandomElement(Set<T> set) {
        int index = ThreadLocalRandom.current().nextInt(set.size());
        int i = 0;
        for (T e : set) {
            if (i == index) {
                set.remove(e);
                return e;
            }
            i++;
        }
        return null;
    }


    // get static created graph 
    protected Map<Character, Set<Integer>> getStaticGraph() {
        Map<Character, Set<Integer>> graph = new HashMap<>();
        graph.put('A', Set.of(1, 2));
        graph.put('B', Set.of(1, 3));
        graph.put('C', Set.of(2, 4, 5));
        graph.put('D', Set.of(3, 4, 6, 7));
        graph.put('E', Set.of(5, 6, 8));
        graph.put('F', Set.of(7, 8, 9));
        graph.put('G', Set.of(9));
        return graph;
    }

    // get randomly generated graph 
    protected Map<Character, Set<Integer>> getRandomGraph(int vertexCount, int edgeCount) {
        Map<Character, Set<Integer>> graph = new HashMap<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // create graph without edges 
        char[] vertexes = new char[vertexCount];
        char key = 'A';
        for (int i = 0; i < vertexCount; i++) {
            vertexes[i] = key;
            graph.put(key, new HashSet<>());
            key++;
        }

        for (int i = 0; i < edgeCount; i++) {
            // obtain two random vertices
            int firstIndex = Math.abs(random.nextInt(vertexCount));
            int secondIndex = Math.abs(random.nextInt(vertexCount));
            while (secondIndex == firstIndex)
                secondIndex = Math.abs(random.nextInt(vertexCount));
            char firstVertex = vertexes[firstIndex];
            char secondVertex = vertexes[secondIndex];

            // add common edge to the vertices
            graph.get(firstVertex).add(i);
            graph.get(secondVertex).add(i);
        }

        // remove vertices without edges from the graph
        graph.entrySet().removeIf(entry -> entry.getValue().size() == 0);

        return graph;
    }

    // print graph to console 
    protected void printGraph(Map<Character, Set<Integer>> graph) {
        for (Map.Entry<Character, Set<Integer>> entry: graph.entrySet()) {
            System.out.println(entry);
        }
    }
}