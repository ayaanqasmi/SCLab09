package poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {

    /**
     * Make sure assertions are enabled.
     */
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // Ensure assertions are enabled with VM argument: -ea
    }

    /**
     * Test basic poem generation with a valid corpus and input.
     */
    @Test
    public void testBasicPoemGeneration() throws IOException {
        File corpus = new File("test/poet/basic-corpus.txt"); // Create this file with appropriate content
        GraphPoet poet = new GraphPoet(corpus);

        String input = "Test the system.";
        String expected = "Test of the system."; // Bridge word "of" should be inserted
        assertEquals("Poem generation with basic input failed", expected, poet.poem(input));
    }

    /**
     * Test when no bridge words exist in the corpus for the given input.
     */
    @Test
    public void testNoBridgeWords() throws IOException {
        File corpus = new File("test/poet/no-bridge-corpus.txt"); // Create this file with content that doesn't bridge
        GraphPoet poet = new GraphPoet(corpus);

        String input = "Hello world.";
        String expected = "Hello world."; // No bridge words, so output is same as input
        assertEquals("Poem generation with no bridge words failed", expected, poet.poem(input));
    }

    /**
     * Test handling of empty input strings.
     */
    @Test
    public void testEmptyInput() throws IOException {
        File corpus = new File("test/poet/empty.txt");
        GraphPoet poet = new GraphPoet(corpus);

        String input = "";
        String expected = ""; // Empty input should result in empty output
        assertEquals("Empty input handling failed", expected, poet.poem(input));
    }

    /**
     * Test case-insensitive handling of words.
     */
    @Test
    public void testCaseInsensitivity() throws IOException {
        File corpus = new File("test/poet/case-insensitive-corpus.txt"); // Contains: "Hello HELLO hello world"
        GraphPoet poet = new GraphPoet(corpus);

        String input = "hello WORLD.";
        String expected = "hello hello world."; // Ensure case insensitivity
        assertEquals("Case-insensitivity failed", expected, poet.poem(input));
    }

    /**
     * Test handling of special characters in the corpus and input.
     */
    @Test
    public void testSpecialCharacters() throws IOException {
        File corpus = new File("test/poet/special-char-corpus.txt"); // Contains: "A! B@ C# D$"
        GraphPoet poet = new GraphPoet(corpus);

        String input = "A! C#.";
        String expected = "A! B@ C#."; // Bridge word "B@" should be inserted
        assertEquals("Special character handling failed", expected, poet.poem(input));
    }

    /**
     * Test when bridge words appear in the middle of the input.
     */
    @Test
    public void testBridgeWordsInMiddle() throws IOException {
        File corpus = new File("test/poet/middle-bridge-corpus.txt"); // Contains: "To explore new worlds"
        GraphPoet poet = new GraphPoet(corpus);

        String input = "To worlds.";
        String expected = "To explore worlds."; // "explore" bridges "To" and "worlds"
        assertEquals("Bridge words in the middle failed", expected, poet.poem(input));
    }

    /**
     * Test handling of input files for graph construction.
     */
    @Test
    public void testGraphConstructionFromFile() throws IOException {
        File corpus = new File("test/poet/seven-words.txt"); // Contains seven unique words
        GraphPoet poet = new GraphPoet(corpus);

        String input = "Seven words connected.";
        String expected = "Seven unique words connected."; // Ensure graph is constructed correctly
        assertEquals("Graph construction from file failed", expected, poet.poem(input));
    }
}
