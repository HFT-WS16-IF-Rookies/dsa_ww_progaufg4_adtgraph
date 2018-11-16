package de.hft.wiest_wolf.dsa;

import java.util.Objects;

/**
 *
 * @author Lukas Wiest
 * @author Erik Wolf
 */
public class Vertex
{
    private static int nextID = 0;

    private int     id;
    private String  name;

    public Vertex(int id, String name)
    {
        this.id     = ++nextID;
        this.id     = id;
        this.name   = name;
    }

    /**
     * 
     * @return count of created Vertexes
     */
    public static int getVertexCount()
    {
        return nextID;
    }

    @Override
    public int hashCode()
    {
        final int p = 97;
        int result = 17;

        result = p * result + Objects.hashCode(id);
        result = p * result + Objects.hash(name);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Vertex))
            return false;
        Vertex other = (Vertex) obj;
        return id == other.id && Objects.equals(name, other.name);
    }
}
