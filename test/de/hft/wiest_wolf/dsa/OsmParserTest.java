package de.hft.wiest_wolf.dsa;

import org.junit.jupiter.api.Test;

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
}
