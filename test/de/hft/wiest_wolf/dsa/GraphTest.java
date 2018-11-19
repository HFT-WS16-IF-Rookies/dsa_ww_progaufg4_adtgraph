package de.hft.wiest_wolf.dsa;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author Lukas Wiest
 * @author Erik Wolf
 *
 */
public class GraphTest
{
    @Test
    public void testConstructor()
    {
        Graph instance = new Graph(new File("./exampleGraphFile.txt"));

        assertAll
        (
            () -> assertEquals(11, instance.getVertexCount(), "test count of vertexes"),
            () -> assertEquals(18, instance.getEdgeCount(), "test count of edges")
        );
    }

    @Test
    public void testConstructorBadValues()
    {
        assertAll
        (
            () -> assertThrows(FileNotFoundException.class, () ->
                    new Graph(new File("/doesntexist.txt"))),

            () -> assertThrows(IllegalArgumentException.class, () ->
                    new Graph("3\nA, B, C\n")),

            () -> assertThrows(ADTException.VertexCountMismatchException.class, () ->
                    new Graph("10\nA, B, C\n1 2 10\n1 3 5"))
        );
    }

    @Test
    public void testGetGrad()
    {
        Graph instance = new Graph(new File("./exampleGraphFile.txt"));
        assertAll
        (
            () -> assertEquals(3, instance.getGrad("A")),
            () -> assertEquals(4, instance.getGrad("B")),
            () -> assertEquals(4, instance.getGrad("C")),
            () -> assertEquals(5, instance.getGrad("D")),
            () -> assertEquals(2, instance.getGrad("E")),
            () -> assertEquals(3, instance.getGrad("F")),
            () -> assertEquals(4, instance.getGrad("G")),
            () -> assertEquals(2, instance.getGrad("H")),
            () -> assertEquals(3, instance.getGrad("I")),
            () -> assertEquals(3, instance.getGrad("J")),
            () -> assertEquals(3, instance.getGrad("K"))
        );
    }

    @Test
    public void testEulerGraph()
    {
        assertAll
        (
            () ->
            {
                Graph instance = new Graph(new File("./exampleGraphFile.txt"));
                assertFalse(instance.isEulerGraph());
            },
            () ->
            {
                Graph instance = new Graph(new File("./eulerGraph.txt"));
                assertTrue(instance.isEulerGraph());
            }
        );
    }

    @Test
    public void testTiefenSuche()
    {
        Graph instance = new Graph(new File("./exampleGraphFile.txt"));
        String start = "E";
        String expected = "E, B, A, C, D, G, F, I, J, K, H";

        PrintStream orgStream = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            PrintStream redirectStream = new PrintStream(baos);
            System.setOut(redirectStream);
            instance.tiefensuche(start);
        }
        finally
        {
            System.out.flush();
            System.setOut(orgStream);
        }
        assertEquals(expected, baos.toString());
    }

    @Test
    public void testBreitenSuche()
    {
        Graph instance = new Graph(new File("./exampleGraphFile.txt"));
        String start = "E";
        String expected = "E, B, I, F, C, A, J, G, D, K, H";

        PrintStream orgStream = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            PrintStream redirectStream = new PrintStream(baos);
            System.setOut(redirectStream);
            instance.breitensuche(start);
        }
        finally
        {
            System.out.flush();
            System.setOut(orgStream);
        }
        assertEquals(expected, baos.toString());
    }
}
