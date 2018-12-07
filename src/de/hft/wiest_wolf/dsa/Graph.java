package de.hft.wiest_wolf.dsa;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Lukas Wiest
 * @author Erik Wolf
 */
public class Graph
{
    private int vertexIdCounter = 0;

    private HashSet<Vertex>                     knoten;
    private HashSet<Edge>                       kanten;
    private HashMap<Integer, HashSet<Edge>>     nachbarn;

    private Vertex currentV;

    public Graph()
    {
        knoten          = new HashSet<>();
        kanten          = new HashSet<>();
        nachbarn        = new HashMap<>();
    }

    public Graph(File inputFile) throws FileNotFoundException
    {
        this();

        try (Scanner in = new Scanner(inputFile, "utf-8"))
        {
            int vertexCount = in.nextInt(); in.nextLine();
            String[] vertexNames = in.nextLine().split(",");
            if (vertexCount != vertexNames.length)
                throw new ADTException.VertexCountMismatchException(vertexCount, vertexNames.length);

            for (String v: vertexNames)
                knoten.add(new Vertex(++vertexIdCounter, v.trim()));

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
        this();

        String[] lines = input.split("\n");
        if (lines.length < 3)
            throw new IllegalArgumentException();

        int vertexCount = Integer.valueOf(lines[0]);
        String[] vertexNames = lines[1].split(",");
        if (vertexCount != vertexNames.length)
            throw new ADTException.VertexCountMismatchException(vertexCount, vertexNames.length);

        for (String v: vertexNames)
            knoten.add(new Vertex(++vertexIdCounter, v.trim()));

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

        HashSet<Edge> neighborhood = nachbarn.get(v0.getId());
        if (neighborhood == null)
        {
            nachbarn.put(v0.getId(), new HashSet<>());
            neighborhood = nachbarn.get(v0.getId());
        }

        neighborhood.add(e);

        neighborhood = nachbarn.get(v1.getId());
        if (neighborhood == null)
        {
            nachbarn.put(v1.getId(), new HashSet<>());
            neighborhood = nachbarn.get(v1.getId());
        }

        neighborhood.add(e);
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

        return nachbarn.get(id).size();
    }

    public boolean isEulerGraph()
    {
        Vertex start;
        try
        {
            start = knoten.stream()
            .filter(v -> nachbarn.get(v.getId()).size()%2 != 0)
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
        nachbarn.get(current.getId())
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
            
            nachbarn.get(currentV.getId())
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
        int otherLen = 5;
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

        dijkstraPrintFirstColumn(found.keySet(), inProgress.keySet(), firstLen);

        StringBuilder buf = new StringBuilder();
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

        while (found.size() == 0 || inProgress.size() > 0)
        {
            HashSet<Vertex> changed = new HashSet<>();
            Vertex next = inProgress.keySet()
                    .stream()
                    .sorted((v0, v1) ->
                    {
                        int result = Double.compare(inProgress.get(v0), inProgress.get(v1));

                        if (result == 0)
                            return v0.getId() - v1.getId();
                        else
                            return result;
                    })
                    .findFirst()
                    .get();

            found.put(next, inProgress.remove(next));
            changed.add(next);

            nachbarn.get(next.getId()).forEach(e ->
            {
                Vertex v = e.getVertex_0() == next? e.getVertex_1() : e.getVertex_0();
                if (!found.containsKey(v)
                        && (!inProgress.containsKey(v)
                        || inProgress.get(v) > e.getWeight() + found.get(next)))
                {
                    inProgress.put(v, e.getWeight() + found.get(next));
                    changed.add(v);
                }
            });

            HashMap<Vertex, Double> toPrint = new HashMap<>(found);
            toPrint.putAll(inProgress);
            dijkstraPrintFirstColumn(found.keySet(), inProgress.keySet(), firstLen);
            dijkstraPrintVertexDistance(toPrint, changed, otherLen);
        }

        HashMap<Vertex, Double> toPrint = new HashMap<>(found);
        toPrint.putAll(inProgress);
        dijkstraPrintFirstColumn(found.keySet(), inProgress.keySet(), firstLen);
        dijkstraPrintVertexDistance(toPrint, knoten, otherLen);
    }

    private void dijkstraPrintFirstColumn(Set<Vertex> found, Set<Vertex> inProgress, int formatLength)
    {
        StringBuilder buf = new StringBuilder();
        buf.append('{');
        if (found.size() > 0)
        {
            found.stream().sorted((v0, v1) -> v0.getId() - v1.getId()).forEach(v ->
            {
                buf.append(v.getName());
                buf.append(",");
            });
            buf.deleteCharAt(buf.length()-1);
        }
        buf.append("}{");
        if (inProgress.size() > 0)
        {
            inProgress.stream().sorted((v0, v1) -> v0.getId() - v1.getId()).forEach(v ->
            {
                buf.append(v.getName());
                buf.append(",");
            });
            buf.deleteCharAt(buf.length()-1);
        }
        buf.append("}");
        System.out.print(String.format("%" + formatLength + "s ", buf.toString()));
    }

    private void dijkstraPrintVertexDistance(HashMap<Vertex, Double> l, HashSet<Vertex> c, int formatLength)
    {
        knoten.stream()
            .sorted((v0, v1) -> v0.getId() - v1.getId())
            .forEach(v ->
            {
                String tmp = null;
                if (c.contains(v))
                {
                    tmp = String.valueOf(l.get(v));
                }
                else
                {
                    tmp = "";
                }

                System.out.print(String.format("| %" + formatLength + "s ", tmp));
            });

        System.out.println("|");
    }
}
