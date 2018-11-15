package de.hft.wiest_wolf.dsa;

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
}
