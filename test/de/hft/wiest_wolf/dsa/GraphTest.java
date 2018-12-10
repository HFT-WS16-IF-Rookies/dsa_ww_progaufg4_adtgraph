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

        String columnName = "{fertig}{in Arbeit}";
        String[] knoten = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"};
        int firstLen = knoten.length*2 +3 > columnName.length() ? knoten.length*2+3 : columnName.length();
        int otherLen = 5;
        expected.append(String.format("%" + firstLen + "s ", columnName));
        for (String s: knoten)
            expected.append(String.format("| %" + otherLen + "s ", s));
        expected.append("|\n");

        expected.append(String.format("%" + firstLen + "s ", "{}{E}"));
        for (String s: new String[]{"inf", "inf", "inf", "inf", "0.0", "inf", "inf", "inf", "inf", "inf", "inf"})
            expected.append(String.format("| %" + otherLen + "s ", s));
        expected.append("|\n");

        expected.append(String.format("%" + firstLen + "s ", "{E}{B,I}"));
        for (String s: new String[]{"", "7.0", "", "", "0.0", "", "", "", "9.0", "", ""})
            expected.append(String.format("| %" + otherLen + "s ", s));
        expected.append("|\n");

        expected.append(String.format("%" + firstLen + "s ", "{B,E}{A,C,F,I}"));
        for (String s: new String[]{"19.0", "7.0", "11.0", "", "", "9.0", "", "", "", "", ""})
            expected.append(String.format("| %" + otherLen + "s ", s));
        expected.append("|\n");

        expected.append(String.format("%" + firstLen + "s ", "{B,E,F}{A,C,G,I}"));
        for (String s: new String[]{"", "", "", "", "", "9.0", "11.0", "", "", "", ""})
            expected.append(String.format("| %" + otherLen + "s ", s));
        expected.append("|\n");

        expected.append(String.format("%" + firstLen + "s ", "{B,E,F,I}{A,C,G,J}"));
        for (String s: new String[]{"", "", "", "", "", "", "", "", "9.0", "11.0", ""})
            expected.append(String.format("| %" + otherLen + "s ", s));
        expected.append("|\n");

        expected.append(String.format("%" + firstLen + "s ", "{B,C,E,F,I}{A,D,G,J}"));
        for (String s: new String[]{"", "", "11.0", "16.0", "", "", "", "", "", "", ""})
            expected.append(String.format("| %" + otherLen + "s ", s));
        expected.append("|\n");

        expected.append(String.format("%" + firstLen + "s ", "{B,C,E,F,G,I}{A,D,J}"));
        for (String s: new String[]{"", "", "", "14.0", "", "", "11.0", "", "", "", ""})
            expected.append(String.format("| %" + otherLen + "s ", s));
        expected.append("|\n");

        expected.append(String.format("%" + firstLen + "s ", "{B,C,E,F,G,I,J}{A,D,K}"));
        for (String s: new String[]{"", "", "", "", "", "", "", "", "", "11.0", "12.0"})
            expected.append(String.format("| %" + otherLen + "s ", s));
        expected.append("|\n");

        expected.append(String.format("%" + firstLen + "s ", "{B,C,E,F,G,I,J,K}{A,D,H}"));
        for (String s: new String[]{"", "", "", "", "", "", "", "15.0", "", "", "12.0"})
            expected.append(String.format("| %" + otherLen + "s ", s));
        expected.append("|\n");

        expected.append(String.format("%" + firstLen + "s ", "{B,C,D,E,F,G,I,J,K}{A,H}"));
        for (String s: new String[]{"16.0", "", "", "14.0", "", "", "", "", "", "", ""})
            expected.append(String.format("| %" + otherLen + "s ", s));
        expected.append("|\n");

        expected.append(String.format("%" + firstLen + "s ", "{B,C,D,E,F,G,H,I,J,K}{A}"));
        for (String s: new String[]{"", "", "", "", "", "", "", "15.0", "", "", ""})
            expected.append(String.format("| %" + otherLen + "s ", s));
        expected.append("|\n");

        expected.append(String.format("%" + firstLen + "s ", "{A,B,C,D,E,F,G,H,I,J,K}{}"));
        for (String s: new String[]{"16.0", "", "", "", "", "", "", "", "", "", ""})
            expected.append(String.format("| %" + otherLen + "s ", s));
        expected.append("|\n");

        expected.append(String.format("%" + firstLen + "s ", "{A,B,C,D,E,F,G,H,I,J,K}{}"));
        for (String s: new String[]{"16.0", "7.0", "11.0", "14.0", "0.0", "9.0", "11.0", "15.0", "9.0", "11.0", "12.0"})
            expected.append(String.format("| %" + otherLen + "s ", s));
        expected.append("|\n");

        PrintStream orgStream = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            PrintStream redirectStream = new PrintStream(baos);
            System.setOut(redirectStream);
            instance.dijkstra(start, true);
        }
        finally
        {
            System.out.flush();
            System.setOut(orgStream);
        }
        System.out.println(baos.toString());
        assertEquals(expected.toString(), baos.toString());
    }
}
