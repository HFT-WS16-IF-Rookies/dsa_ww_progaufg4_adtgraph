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
        Vertex instance = new Vertex(1, "A");
        assertAll
        (
                () -> assertEquals("A", instance.getName()),
                () -> assertEquals(1, instance.getId())
        );
    }

    @Test
    public void testConstructorBadValues()
    {
        assertAll
        (
            () -> assertThrows(IllegalArgumentException.class, () ->
                {
                    new Vertex(1, "");
                }),
            () -> assertThrows(IllegalArgumentException.class, () ->
                {
                    new Vertex(1, null);
                })
        );
    }
}
