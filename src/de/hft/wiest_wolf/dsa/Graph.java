package de.hft.wiest_wolf.dsa;

import java.io.File;
import java.util.HashSet;

/**
 *
 * @author Lukas Wiest
 * @author Erik Wolf
 */
public class Graph
{
    private HashSet<Vertex>     knoten;
    private HashSet<Edge>       kanten;
    private HashSet<Edge>[]     nachbarn;

    public Graph(File inputFile)
    {
        // check if file exists
        // open file readonly
        // read first line and check count of Vertexes
        // read next line, split and check if array has expected length
        // create vertexes
        // for leftover lines create edges between vertexes
        throw new UnsupportedOperationException("Not yet implemented");
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
        //create Edge based on data in line
    }

    public int getVertexCount()
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int getEdgeCount()
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int getGrad(String knotenName)
    {
        throw new UnsupportedOperationException("Not yet implemented");
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
