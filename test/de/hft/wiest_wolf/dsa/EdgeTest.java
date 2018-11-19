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
        Vertex v0 = new Vertex(1, "A");
        Vertex v1 = new Vertex(2, "B");
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
                    new Edge(null, new Vertex(1, "A"), 5);
                }),
            () -> assertThrows(IllegalArgumentException.class, () ->
                {
                    new Edge(new Vertex(1, "A"), null, 5);
                })
        );
    }
}
