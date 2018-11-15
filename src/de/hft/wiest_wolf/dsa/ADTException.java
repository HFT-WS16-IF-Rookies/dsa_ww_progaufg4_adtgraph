package de.hft.wiest_wolf.dsa;

/**
 *
 * @author Lukas Wiest
 * @author Erik Wolf
 */
public class ADTException extends RuntimeException
{
    private ADTException(String msg)
    {
        super(msg);
    }

    public static class VertexCountMismatchException extends ADTException
    {
        /**
         * 
         * @param expected count of Vertexes we expected according from the first line
         * @param actual count of Vertexes we actually have
         */
        public VertexCountMismatchException(int expected, int actual)
        {
            super(String.format("expected graphs: %d\tactual graphs: %d", expected, actual));
        }
    }

    public static class UnknownVertexException extends ADTException
    {
        /**
         * 
         * @param requestedVertexName Name of requested Vertex not found
         */
        public UnknownVertexException(String requestedVertexName)
        {
            super(String.format("Don't know Vertex with name: %s", requestedVertexName));
        }

        /**
         * 
         * @param requestedVertexId ID of requested Vertex not found
         */
        public UnknownVertexException(int requestedVertexId)
        {
            super(String.format("Don't know Vertex with ID: %d", requestedVertexId));
        }
    }
}
