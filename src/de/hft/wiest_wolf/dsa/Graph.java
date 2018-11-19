package de.hft.wiest_wolf.dsa;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

/**
 *
 * @author Lukas Wiest
 * @author Erik Wolf
 */
public class Graph
{
    private int vertexIdCounter = 0;

    private HashSet<Vertex>     knoten;
    private HashSet<Edge>       kanten;
    private HashSet<Edge>[]     nachbarn;

    public Graph(File inputFile) throws FileNotFoundException
    {
        knoten = new HashSet<>();
        kanten = new HashSet<>();

        try (Scanner in = new Scanner(inputFile, "utf-8"))
        {
            int vertexCount = in.nextInt(); in.nextLine();
            String[] vertexNames = in.nextLine().split(",");
            if (vertexCount != vertexNames.length)
                throw new ADTException.VertexCountMismatchException(vertexCount, vertexNames.length);

            for (String v: vertexNames)
                knoten.add(new Vertex(++vertexIdCounter, v.trim()));

            nachbarn = new HashSet[knoten.size()];
            for (int i=0; i<nachbarn.length; i++)
                nachbarn[i] = new HashSet<>();

            while (in.hasNextLine())
            {
                String line = in.nextLine();
                if (line.length() > 0)
                    createEdgeFromString(line);
            }
        }
    }

    public Graph(String input)
    {
        // check length minimum 3
        // check for count of Vertexes
        // split Vertexes and cheeck if array has expected length
        // create vertexes
        // for leftover lines create edges between vertexes
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void createEdgeFromString(String line)
    {
        String[] split = line.split(" ");
        Vertex v0 = knoten
                .stream()
                .filter(v -> v.getId() == Integer.valueOf(split[0]))
                .findFirst()
                .get();
        Vertex v1 = knoten
                .stream()
                .filter(v -> v.getId() == Integer.valueOf(split[1]))
                .findFirst()
                .get();

        Edge e = new Edge(v0, v1, Double.valueOf(split[2]));
        kanten.add(e);
        nachbarn[v0.getId()-1].add(e);
        nachbarn[v1.getId()-1].add(e);
;    }

    public int getVertexCount()
    {
        return knoten.size();
    }

    public int getEdgeCount()
    {
        return kanten.size();
    }

    public int getGrad(String knotenName)
    {
        int id = knoten.stream()
                .filter(v -> v.getName().equals(knotenName))
                .findFirst()
                .get()
                .getId();

        return nachbarn[id-1].size();
    }

    public boolean isEulerGraph()
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void tiefensuche(String nameStartknoten)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void breitensuche(String nameStartknoten)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
