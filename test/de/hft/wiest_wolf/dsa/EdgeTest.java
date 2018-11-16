package de.hft.wiest_wolf.dsa;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author Lukas Wiest
 * @author Erik Wolf
 *
 */
public class EdgeTest
{
    @Test
    public void testConstructor()
    {
        Vertex v0 = new Vertex("A");
        Vertex v1 = new Vertex("B");
        double weight = 5;
        Edge expected = new Edge(v0, v1, weight);

        assertEquals(expected, new Edge(v0, v1, weight));
    }

    @Test
    public void testConstructorBadValues()
    {
        assertAll
        (
            () -> assertThrows(IllegalArgumentException.class, () ->
                {
                    new Edge(null, new Vertex("A"), 5);
                }),
            () -> assertThrows(IllegalArgumentException.class, () ->
                {
                    new Edge(new Vertex("A"), null, 5);
                })
        );
    }
}
