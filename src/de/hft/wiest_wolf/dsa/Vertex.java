package de.hft.wiest_wolf.dsa;

import java.util.Objects;

/**
 *
 * @author Lukas Wiest
 * @author Erik Wolf
 */
public class Vertex
{
    private long        id;
    private String      name;
    private Coordinate  pos;

    public Vertex(long id, String name)
    {
        this.id     = id;
        this.name   = name;
    }

    public Vertex(long id, String name, Coordinate pos)
    {
        this(id, name);
        this.pos = pos;
    }

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public Coordinate getPosition()
    {
        return pos;
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

    public static class Coordinate
    {
        public final double lat;
        public final double lon;

        public Coordinate(double latitude, double longtitude)
        {
            this.lat = latitude;
            this.lon = longtitude;
        }
    }
}
