package de.hft.wiest_wolf.dsa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OsmParserTest
{
    @Test
    public void testOsmParserSelf()
    {
        System.out.println("SelfParser");
        
        Graph graph = OsmParser.parseToGraph("./additionalFiles/malmsheim-gartenstr-bis-schule-osmexport.osm");

        System.out.println("eingelesene Knoten: " + graph.getVertexCount());
        System.out.println();

        graph.dijkstra("3607596627");

        System.out.println();
    }

    @Test
    public void testOsmTiefensuche()
    {
        System.out.println("Tiefensuche");

        Graph graph = OsmParser.parseToGraph("./additionalFiles/malmsheim-gartenstr-bis-schule-osmexport.osm");

        assertEquals(graph.getVertexCount(),graph.tiefensuche("3607596627").size());

        System.out.println();
    }
}
