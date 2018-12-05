package de.hft.wiest_wolf.dsa;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
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
    
    private Vertex currentV;

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
        String[] lines = input.split("\n");
        if (lines.length < 3)
            throw new IllegalArgumentException();

        int vertexCount = Integer.valueOf(lines[0]);
        String[] vertexNames = lines[1].split(",");
        if (vertexCount != vertexNames.length)
            throw new ADTException.VertexCountMismatchException(vertexCount, vertexNames.length);

        for (String v: vertexNames)
            knoten.add(new Vertex(++vertexIdCounter, v.trim()));

        nachbarn = new HashSet[knoten.size()];
        for (int i=0; i<nachbarn.length; i++)
            nachbarn[i] = new HashSet<>();

        for (int i=2; i<lines.length; i++)
        {
            String line = lines[i];
            if (line.length() > 0)
                createEdgeFromString(line);
        }
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
    }

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
        Vertex start;
        try
        {
            start = knoten.stream()
            .filter(v -> nachbarn[v.getId()-1].size()%2 != 0)
            .findFirst().get();

        }
        catch (NoSuchElementException e)
        {
            start = knoten.stream().findFirst().get();
            if ((tiefensuche(start.getName())).size() == knoten.size())
                return true;
            else
                return false;
        }

        return false;

    }

    public LinkedList<Vertex> tiefensuche(String nameStartknoten)
    {
        HashSet<Vertex> visited = new HashSet<>();
        LinkedList<Vertex> order = new LinkedList<>();
        Vertex start = knoten.stream()
                .filter(v -> v.getName().equals(nameStartknoten))
                .findFirst()
                .get();
        tiefensucheRekursiv(visited, order, start);

        StringBuilder buf = new StringBuilder();
        for (Vertex v: order)
        {
            buf.append(v.getName());
            buf.append(", ");
        }
        for (int i=0; i<2;i++)
            buf.deleteCharAt(buf.length()-1);

        System.out.print(buf.toString());
        return order;
    }

    private void tiefensucheRekursiv(HashSet<Vertex> visited, LinkedList<Vertex> order, Vertex current)
    {
        visited.add(current);
        order.addLast(current);
        nachbarn[current.getId()-1]
                .stream()
                .map(e ->
                {
                    if (e.getVertex_0().equals(current))
                        return e.getVertex_1();
                    else
                        return e.getVertex_0();
                }).sorted((v0, v1) -> v0.getId() - v1.getId())
                .forEach(v ->
                {
                    if (!visited.contains(v))
                        tiefensucheRekursiv(visited, order, v);
                });
    }

    public void breitensuche(String nameStartknoten)
    {
        HashSet<Vertex> visited = new HashSet<>();
        LinkedList<Vertex> order = new LinkedList<>();
        LinkedList<Vertex> queue = new LinkedList<>();

        
        Vertex start = knoten.stream()
                .filter(v -> v.getName().equals(nameStartknoten))
                .findFirst()
                .get();
        visited.add(start);
        currentV = start;
        order.add(currentV);
        while(true)
        {
            
            nachbarn[currentV.getId()-1]
                    .stream()
                    .sorted((e0, e1) -> Double.compare(e0.getWeight() , e1.getWeight()))
                    .map(e ->
                        {
                            if (e.getVertex_0().equals(currentV))
                                return e.getVertex_1();
                            else
                                return e.getVertex_0();
                        })
                    .forEach(v ->
                    {
                        if (!visited.contains(v) && !queue.contains(v))
                        {
                            queue.addLast(v);
                        }
                    });
            if((currentV = queue.pollFirst()) == null)
            {
                break;
            }
            visited.add(currentV);
            order.add(currentV);

        }
        StringBuilder buf = new StringBuilder();
        for (Vertex v: order)
        {
            buf.append(v.getName());
            buf.append(", ");
        }
        for (int i=0; i<2;i++)
            buf.deleteCharAt(buf.length()-1);

        System.out.print(buf.toString());
        
    }

    public void dijkstra(String nameStartKnoten)
    {
        String columnName = "{fertig}{in Arbeit}";

        int firstLen = knoten.size()*2 +3 > columnName.length() ? knoten.size()*2+3 : columnName.length();
        int otherLen = 3;
        System.out.print((String.format("%" + firstLen + "s ", columnName)));
        knoten.stream()
            .sorted((v0, v1) -> v0.getId() - v1.getId())
            .forEach(s -> System.out.print(String.format("| %" + otherLen + "s ", s.getName())));
        System.out.println("|");

        HashMap<Vertex, Double> found           = new HashMap<>();
        HashMap<Vertex, Double> inProgress      = new HashMap<>();

        Vertex startV = knoten.stream()
                        .filter(v -> v.getName()
                        .equals(nameStartKnoten))
                        .findFirst()
                        .get();

        inProgress.put(startV, 0d);

        StringBuilder buf = new StringBuilder();
        buf.append('{');
        if (found.size() > 0)
        {
            found.keySet().stream().sorted((v0, v1) -> v0.getId() - v1.getId()).forEach(v ->
            {
                buf.append(v.getName());
                buf.append(",");
            });
            buf.deleteCharAt(buf.length()-1);
        }
        buf.append("}{");
        if (inProgress.size() > 0)
        {
            inProgress.keySet().stream().sorted((v0, v1) -> v0.getId() - v1.getId()).forEach(v ->
            {
                buf.append(v.getName());
                buf.append(",");
            });
            buf.deleteCharAt(buf.length()-1);
        }
        buf.append("}");
        System.out.print(String.format("%" + firstLen + "s ", buf.toString()));

        buf.setLength(0);
        knoten.stream().sorted((v0, v1) -> v0.getId() - v1.getId()).forEach(v ->
        {
            Double distance = found.get(v);
            if (distance != null)
            {
                buf.append(String.format("| %" + otherLen + "s ", String.valueOf(distance)));
            }
            else if ((distance = inProgress.get(v)) != null)
            {

                buf.append(String.format("| %" + otherLen + "s ", String.valueOf(distance)));
            }
            else
            {

                buf.append(String.format("| %" + otherLen + "s ", "inf"));
            }
        });
        buf.append("|");
        System.out.println(buf);

        // TODO: implement algorithm
    }
}
