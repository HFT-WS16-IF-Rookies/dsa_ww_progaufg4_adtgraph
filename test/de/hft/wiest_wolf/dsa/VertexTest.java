package de.hft.wiest_wolf.dsa;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author Lukas Wiest
 * @author Erik Wolf
 *
 */
public class VertexTest
{
    @Test
    public void testConstructor()
    {
        int current = Vertex.getVertexCount();
        Vertex instance = new Vertex("A");
        assertAll
        (
                () -> assertEquals("A", instance.getName()),
                () -> assertEquals(current + 1, instance.getId())
        );
    }

    @Test
    public void testConstructorBadValues()
    {
        assertAll
        (
            () -> assertThrows(IllegalArgumentException.class, () ->
                {
                    new Vertex("");
                }),
            () -> assertThrows(IllegalArgumentException.class, () ->
                {
                    new Vertex(null);
                })
        );
    }

    @Test
    public void testVertexIdCounter()
    {
        int current = Vertex.getVertexCount();

        for (int i=5; i>0; i--)
        {
            new Vertex("test");
        }

        assertEquals(current + 5, Vertex.getVertexCount());
    }
}
