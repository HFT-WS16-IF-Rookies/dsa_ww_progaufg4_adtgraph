package de.hft.wiest_wolf.dsa;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import de.hft.wiest_wolf.dsa.Vertex.Coordinate;

/**
 * Die Klasse OsmParser implementiert einen Rahmen für einen Parser von
 * OpenStreetMap Daten. Als Parser wird ein StaXParser verwendet.
 * 
 * @author Lukas Wiest
 *
 */
public class OsmParser
{
    private static Graph graph;

    /**
     * Method parses directly into a Graph.<br />
     * declared synchronized to ensure that Calls from multiple
     * threads doesn't override each others graph instances.
     * @param inputFileName
     * @return
     */
    public static synchronized Graph parseToGraph(String inputFileName)
    {
        // create new Graph into we want to parse
        graph = new Graph();

        try
        {
            // First, create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();

            // Setup a new eventReader
            InputStream in = new FileInputStream(inputFileName);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            // read the XML document
            while (eventReader.hasNext())
            {
                XMLEvent event = eventReader.nextEvent();

                // check for start elements
                if (event.isStartElement())
                {
                    StartElement startElement = event.asStartElement();

                    // If we have found a node or way element, go ahead and read it
                    if (startElement.getName().getLocalPart().equals("node"))
                        readVertex(eventReader, startElement);
                    else if (startElement.getName().getLocalPart().equals("way"))
                        readEdge(eventReader, startElement);
                }
                
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (XMLStreamException e)
        {
            e.printStackTrace();
        }

        // before returning the graph, remove all nodes again, we didn't connect with anyone
        graph.deleteUnconnectedVertexes();

        return graph;
    }

    private static void readVertex(XMLEventReader eventReader, StartElement startElement) throws XMLStreamException
    {
        // create HashMap to store id and latitude/longtitude values in temporarily
        HashMap<String, String> values = new HashMap<>();

        // search for id, latitude and longtitude
        Iterator<Attribute> attributes = startElement.getAttributes();
        while (attributes.hasNext())
        {
            Attribute attribute = attributes.next();
            if (attribute.getName().toString().equals("id"))
                values.put("id", attribute.getValue());
            else if (attribute.getName().toString().equals("lat"))
                values.put("lat", attribute.getValue());
            else if (attribute.getName().toString().equals("lon"))
                values.put("lon", attribute.getValue());
        }

        // create a new Vertex from the parsed info and put it into
        // our Graph
        graph.addVertex(new Vertex(
                Long.valueOf(values.get("id")),
                values.get("id"),
                new Coordinate(
                        Double.valueOf(values.get("lat"))
                        , Double.valueOf(values.get("lon")))));

        // scroll over all following elements until the current node
        // is over
        while (eventReader.hasNext())
        {
            XMLEvent event = eventReader.nextEvent();

            if (event.isEndElement())
            {
                EndElement endElement = event.asEndElement();

                if (endElement.getName().getLocalPart() == ("node"))
                {
                    break;
                }
            }
        }
    }

    private static void readEdge(XMLEventReader eventReader, StartElement startElement) throws XMLStreamException
    {
        // create List for the node id's this way references
        LinkedList<Long> nodeIDs = new LinkedList<>();

        while (eventReader.hasNext())
        {
            XMLEvent event = eventReader.nextEvent();

            if (event.isStartElement())
            {
                startElement = event.asStartElement();

                // for each nd element, save the ref attribute
                if (startElement.getName().getLocalPart().equals("nd"))
                {
                    Iterator<Attribute> attributes = startElement.getAttributes();
                    while (attributes.hasNext())
                    {
                        Attribute attribute = attributes.next();
                        if (attribute.getName().toString().equals("ref"))
                            nodeIDs.add(Long.valueOf(attribute.getValue()));
                    }
                }
                // for tag attributes search for the highway tag
                // if found, create the edges we got from the read previously
                // and stop.
                // if this waypoint isn't of type highway, we never will
                // create the edges this waypoint references, and therefore
                // data from this waypoint isn't used
                else if (startElement.getName().getLocalPart().equals("tag"))
                {
                    Iterator<Attribute> attributes = startElement.getAttributes();
                    while (attributes.hasNext())
                    {
                        Attribute attribute = attributes.next();
                        if (attribute.getName().toString().equals("k"))
                        {
                            if (attribute.getValue().equals("highway"))
                            {
                                createEdges(nodeIDs);
                                break;
                            }
                        }
                    }
                }
            }
            else if (event.isEndElement())
            {
                EndElement endElement = event.asEndElement();

                if (endElement.getName().getLocalPart() == ("way"))
                {
                    break;
                }
            }
        }
    }

    private static void createEdges(LinkedList<Long> nodeIDs)
    {
        Iterator<Long> points = nodeIDs.iterator();

        // get the first two referenced nodes
        if (!points.hasNext())
            return;
        Vertex v0 = graph.getVertex(points.next());

        if (!points.hasNext())
            return;
        Vertex v1 = graph.getVertex(points.next());

        // create first edge
        graph.addEdge(new Edge(
                v0,
                v1,
                distanceBetweenCoordinates(v0.getPosition(), v1.getPosition())));

        // for all left node references, put the node prevoiusly was the second
        // to be the first and read the next node from the next reference id
        // and create the according edge
        while (points.hasNext())
        {
            v0 = v1;
            v1 = graph.getVertex(points.next());

            graph.addEdge(new Edge(
                    v0,
                    v1,
                    distanceBetweenCoordinates(v0.getPosition(), v1.getPosition())));
        }
    }

    private static double distanceBetweenCoordinates(Coordinate c0, Coordinate c1)
    {
        int radius = 6371;
        double lat = Math.toRadians(c1.lat - c0.lat);
        double lon = Math.toRadians(c1.lon- c0.lon);
        double a = Math.sin(lat / 2) * Math.sin(lat / 2)
                + Math.cos(Math.toRadians(c0.lat))
                * Math.cos(Math.toRadians(c1.lat))
                * Math.sin(lon / 2)
                * Math.sin(lon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = radius * c;
        return Math.abs(d)*1000;
    }
}
