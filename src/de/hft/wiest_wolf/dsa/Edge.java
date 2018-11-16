package de.hft.wiest_wolf.dsa;

import java.util.Objects;

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

    public Vertex getVertex_0()
    {
        return v0;
    }

    public Vertex getVertex_1()
    {
        return v1;
    }

    public double getWeight()
    {
        return w;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(v0, v1);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Edge))
            return false;
        Edge other = (Edge) obj;
        return Objects.equals(v0, other.v0) && Objects.equals(v1, other.v1);
    }
}
