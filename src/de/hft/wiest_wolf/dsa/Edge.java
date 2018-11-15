package de.hft.wiest_wolf.dsa;

/**
 *
 * @author Lukas Wiest
 * @author Erik Wolf
 */
public class Edge
{
    private Vertex  v0;
    private Vertex  v1;
    private double  w;

    public Edge(Vertex v0, Vertex v1, double w)
    {
        this.v0 = v0;
        this.v1 = v1;
        this.w  = w;
    }
}
