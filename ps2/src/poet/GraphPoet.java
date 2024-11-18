package poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import graph.Graph;

/**
 * A graph-based poetry generator.
 */
public class GraphPoet {

    private final Graph<String> graph = Graph.empty();

    // Abstraction function:
    //   Represents a graph where vertices are words from the corpus and edges
    //   represent adjacency relationships with weights equal to the frequency of the adjacency.
    // Representation invariant:
    //   - The graph contains only non-empty, case-insensitive strings as vertices.
    //   - Edge weights are positive integers.
    // Safety from rep exposure:
    //   - The graph field is private and final.
    //   - No methods return direct references to the mutable graph.

    /**
     * Create a new poet with the graph from the corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(corpus))) {
            String line;
            String previousWord = null;

            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+"); // Split by spaces or newlines
                for (String word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    graph.add(word);

                    if (previousWord != null) {
                        int weight = graph.set(previousWord, word, 0) + 1;
                        graph.set(previousWord, word, weight);
                    }

                    previousWord = word;
                }
                previousWord = null; // Reset after each line
            }
        }
        checkRep();
    }

    // Check the representation invariant
    private void checkRep() {
        for (String vertex : graph.vertices()) {
            assert !vertex.isEmpty() : "Graph contains an empty vertex";
            for (Map.Entry<String, Integer> edge : graph.targets(vertex).entrySet()) {
                assert edge.getValue() > 0 : "Graph contains a non-positive edge weight";
            }
        }
    }

    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        String[] words = input.split("\\s+");
        StringBuilder poem = new StringBuilder();

        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i].toLowerCase();
            String w2 = words[i + 1].toLowerCase();

            poem.append(words[i]).append(" "); // Append the original word
            String bridgeWord = findBridgeWord(w1, w2);
            if (bridgeWord != null) {
                poem.append(bridgeWord).append(" "); // Append the bridge word
            }
        }

        // Append the last word
        poem.append(words[words.length - 1]);

        return poem.toString();
    }

    /**
     * Find a bridge word between two words if a two-edge-long path exists.
     * 
     * @param w1 the first word
     * @param w2 the second word
     * @return the bridge word with the maximum weight, or null if no such word exists
     */
    private String findBridgeWord(String w1, String w2) {
        if (!graph.vertices().contains(w1) || !graph.vertices().contains(w2)) {
            return null;
        }

        Map<String, Integer> targetsFromW1 = graph.targets(w1);
        Map<String, Integer> sourcesToW2 = graph.sources(w2);

        String bridgeWord = null;
        int maxWeight = 0;

        for (String candidate : targetsFromW1.keySet()) {
            if (sourcesToW2.containsKey(candidate)) {
                int weight = targetsFromW1.get(candidate) + sourcesToW2.get(candidate);
                if (weight > maxWeight) {
                    maxWeight = weight;
                    bridgeWord = candidate;
                }
            }
        }

        return bridgeWord;
    }

    @Override
    public String toString() {
        return "GraphPoet [graph=" + graph + "]";
    }
}
