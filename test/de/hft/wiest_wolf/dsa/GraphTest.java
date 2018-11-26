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
    public void testConstructor() throws FileNotFoundException
    {
        Graph instance = new Graph(new File("./additionalFiles/exampleGraphFile.txt"));

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
    public void testGetGrad() throws FileNotFoundException
    {
        Graph instance = new Graph(new File("./additionalFiles/exampleGraphFile.txt"));
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
                Graph instance = new Graph(new File("./additionalFiles/exampleGraphFile.txt"));
                assertFalse(instance.isEulerGraph(), "example Graph failed");
            },
            () ->
            {
                Graph instance = new Graph(new File("./additionalFiles/eulerGraph.txt"));
                assertTrue(instance.isEulerGraph(), "euler Graph failed");
            }
        );
    }

    @Test
    public void testTiefenSuche() throws FileNotFoundException
    {
        Graph instance = new Graph(new File("./additionalFiles/exampleGraphFile.txt"));
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
    public void testBreitenSuche() throws FileNotFoundException
    {
        Graph instance = new Graph(new File("./additionalFiles/exampleGraphFile.txt"));
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

    @Test
    public void testDijkstra() throws FileNotFoundException
    {
        Graph instance = new Graph(new File("./additionalFiles/exampleGraphFile.txt"));
        String start = "E";
        StringBuffer expected = new StringBuffer();
        expected.append(String.format("%15s ", "Knoten"));
        for (String s: new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"})
            expected.append(String.format("| %3s ", s));
        expected.append("|\n");

        expected.append(String.format("%15s ", "{}"));
        for (String s: new String[]{"inf", "inf", "inf", "inf", "0", "inf", "inf", "inf", "inf", "inf", "inf"})
            expected.append(String.format("| %3s ", s));
        expected.append("|\n");

        expected.append(String.format("%15s ", "{B,I}"));
        for (String s: new String[]{"", "7", "", "", "", "", "", "", "9", "", ""})
            expected.append(String.format("| %3s ", s));
        expected.append("|\n");

        expected.append(String.format("%15s ", "{A,C,F,I}"));
        for (String s: new String[]{"19", "", "11", "", "", "9", "", "", "", "", ""})
            expected.append(String.format("| %3s ", s));
        expected.append("|\n");

        expected.append(String.format("%15s ", "{A,C,G,I}"));
        for (String s: new String[]{"", "", "", "", "", "9", "11", "", "", "", ""})
            expected.append(String.format("| %3s ", s));
        expected.append("|\n");

        expected.append(String.format("%15s ", "{A,C,G,J}"));
        for (String s: new String[]{"", "", "", "", "", "", "", "", "9", "11", ""})
            expected.append(String.format("| %3s ", s));
        expected.append("|\n");

        expected.append(String.format("%15s ", "{A,D,G,J}"));
        for (String s: new String[]{"", "", "11", "16", "", "", "", "", "", "", ""})
            expected.append(String.format("| %3s ", s));
        expected.append("|\n");

        expected.append(String.format("%15s ", "{A,D,J}"));
        for (String s: new String[]{"", "", "", "14", "", "", "11", "", "", "", ""})
            expected.append(String.format("| %3s ", s));
        expected.append("|\n");

        expected.append(String.format("%15s ", "{A,D,K}"));
        for (String s: new String[]{"", "", "", "", "", "", "", "", "", "11", "12"})
            expected.append(String.format("| %3s ", s));
        expected.append("|\n");

        expected.append(String.format("%15s ", "{A,D,H}"));
        for (String s: new String[]{"", "", "", "", "", "", "", "15", "", "", "12"})
            expected.append(String.format("| %3s ", s));
        expected.append("|\n");

        expected.append(String.format("%15s ", "{A,H}"));
        for (String s: new String[]{"", "", "", "14", "", "", "", "", "", "", ""})
            expected.append(String.format("| %3s ", s));
        expected.append("|\n");

        expected.append(String.format("%15s ", "{A}"));
        for (String s: new String[]{"", "", "", "", "", "", "", "15", "", "", ""})
            expected.append(String.format("| %3s ", s));
        expected.append("|\n");

        expected.append(String.format("%15s ", "{}"));
        for (String s: new String[]{"19", "7", "11", "14", "0", "9", "11", "15", "9", "11", "12"})
            expected.append(String.format("| %3s ", s));
        expected.append("|\n");

        PrintStream orgStream = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            PrintStream redirectStream = new PrintStream(baos);
            System.setOut(redirectStream);
            instance.dijkstra(start);
        }
        finally
        {
            System.out.flush();
            System.setOut(orgStream);
        }
        System.out.println(expected);
        assertEquals(expected.toString(), baos.toString());
    }
}
