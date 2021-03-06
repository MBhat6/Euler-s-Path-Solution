
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;

interface Visitor<T> {

    public void visit(T obj);
}

// --- assumes definition of simple class Pair<E, F>
// --- Vertex class ------------------------------------------------------
class Vertex<E> {

    public static final double INFINITY = Double.MAX_VALUE;
    public HashMap<E, Pair<Vertex<E>, Double>> adjList
            = new HashMap<E, Pair<Vertex<E>, Double>>();
    public E data;
    public boolean visited;

    public Vertex(E x) {
        data = x;
    }

    public Vertex() {
        this(null);
    }

    public E getData() {
        return data;
    }

    public boolean isVisited() {
        return visited;
    }

    public void visit() {
        visited = true;
    }

    public void unvisit() {
        visited = false;
    }

    public Iterator<Map.Entry<E, Pair<Vertex<E>, Double>>> iterator() {
        return adjList.entrySet().iterator();
    }

    public void addToAdjList(Vertex<E> neighbor, double cost) {
        if (adjList.get(neighbor.data) == null) {
            adjList.put(neighbor.data, new Pair<Vertex<E>, Double>(neighbor, cost));
        }
        // Note: if you want to change the cost, you'll need to remove it and then add it back
    }

    public void addToAdjList(Vertex<E> neighbor, int cost) {
        addToAdjList(neighbor, (double) cost);
    }

    public boolean equals(Object rhs) {
        if (!(rhs instanceof Vertex<?>)) {
            return false;
        }
        Vertex<E> other = (Vertex<E>) rhs;

        return (data.equals(other.data));

    }

    public int hashCode() {
        return (data.hashCode());
    }

    public void showAdjList() {
        Iterator<Entry<E, Pair<Vertex<E>, Double>>> iter;
        Entry<E, Pair<Vertex<E>, Double>> entry;
        Pair<Vertex<E>, Double> pair;

        System.out.print("Adj List for " + data + ": ");
        iter = adjList.entrySet().iterator();
        while (iter.hasNext()) {
            entry = iter.next();
            pair = entry.getValue();
            System.out.print(pair.first.data + "("
                    + String.format("%3.1f", pair.second)
                    + ") ");
        }
        System.out.println();
    }

}

//--- Graph class ------------------------------------------------------
public class Graph<E> {
    // the graph data is all here --------------------------

    protected HashMap<E, Vertex<E>> vertexSet;
    public LinkedStack<E> undoStack = new LinkedStack<E>();

    // public graph methods --------------------------------
    public Graph() {
        vertexSet = new HashMap<E, Vertex<E>>();
    }

    public void addEdge(E source, E dest, double cost) {
        Vertex<E> src, dst;

        // put both source and dest into vertex list(s) if not already there
        src = addToVertexSet(source);
        dst = addToVertexSet(dest);

        // add dest to source's adjacency list
        src.addToAdjList(dst, cost);
        dst.addToAdjList(src, cost); // ADD THIS IF UNDIRECTED GRAPH
    }

    public void addEdge(E source, E dest, int cost) {
        addEdge(source, dest, (double) cost);
    }

    // adds vertex with x in it, and always returns ref to it
    public Vertex<E> addToVertexSet(E x) {
        Vertex<E> retVal = null;
        Vertex<E> foundVertex;

        // find if Vertex already in the list:
        foundVertex = vertexSet.get(x);

        if (foundVertex != null) // found it, so return it
        {
            return foundVertex;
        }

        // the vertex not there, so create one
        retVal = new Vertex<E>(x);
        vertexSet.put(x, retVal);

        return retVal;   // should never happen
    }

    public boolean remove(E start, E end) {
        Vertex<E> startVertex = vertexSet.get(start);
        boolean removedOK = false;

        if (startVertex != null) {
            Pair<Vertex<E>, Double> endPair = startVertex.adjList.remove(end);
            undoStack.push(end);
            removedOK = endPair != null;
        }
        // Add if UNDIRECTED GRAPH:
		Vertex<E> endVertex = vertexSet.get(end);
		if( endVertex != null )
		{
			Pair<Vertex<E>, Double> startPair = endVertex.adjList.remove(start);
			undoStack.push(start);
			removedOK = startPair!=null ;
		}
        return removedOK;
    }

    public void showAdjTable() {
        Iterator<Entry<E, Vertex<E>>> iter;

        System.out.println("------------------------ ");
        iter = vertexSet.entrySet().iterator();
        while (iter.hasNext()) {
            (iter.next().getValue()).showAdjList();
        }
        System.out.println();
    }

    public void clear() {
        vertexSet.clear();
    }

    // reset all vertices to unvisited
    public void unvisitVertices() {
        Iterator<Entry<E, Vertex<E>>> iter;

        iter = vertexSet.entrySet().iterator();
        while (iter.hasNext()) {
            iter.next().getValue().unvisit();
        }
    }

    /**
     * Breadth-first traversal from the parameter startElement
     */
    public void breadthFirstTraversal(E startElement, Visitor<E> visitor) {
        unvisitVertices();

        Vertex<E> startVertex = vertexSet.get(startElement);
        breadthFirstTraversalHelper(startVertex, visitor);
    }

    /**
     * Depth-first traversal from the parameter startElement
     */
    public void depthFirstTraversal(E startElement, Visitor<E> visitor) {
        unvisitVertices();

        Vertex<E> startVertex = vertexSet.get(startElement);
        depthFirstTraversalHelper(startVertex, visitor);
    }

    protected void breadthFirstTraversalHelper(Vertex<E> startVertex,
            Visitor<E> visitor) {
        LinkedQueue<Vertex<E>> vertexQueue = new LinkedQueue<>();
        E startData = startVertex.getData();

        startVertex.visit();
        visitor.visit(startData);
        vertexQueue.enqueue(startVertex);
        while (!vertexQueue.isEmpty()) {
            Vertex<E> nextVertex = vertexQueue.dequeue();
            Iterator<Map.Entry<E, Pair<Vertex<E>, Double>>> iter
                    = nextVertex.iterator(); // iterate adjacency list

            while (iter.hasNext()) {
                Entry<E, Pair<Vertex<E>, Double>> nextEntry = iter.next();
                Vertex<E> neighborVertex = nextEntry.getValue().first;
                if (!neighborVertex.isVisited()) {
                    vertexQueue.enqueue(neighborVertex);
                    neighborVertex.visit();
                    visitor.visit(neighborVertex.getData());
                }
            }
        }
    } // end breadthFirstTraversalHelper

    protected void depthFirstTraversalHelper(Vertex<E> startVertex, Visitor<E> visitor) {
        // YOU COMPLETE THIS (USE THE ALGORITHM GIVEN FOR LESSON 11 EXERCISE)
        //LinkedQueue<Vertex<E>> traversalOrder = new LinkedQueue<>();
        int i = 1;
        LinkedStack<Vertex<E>> vertexStack = new LinkedStack<>();
        E startData = startVertex.getData();

        startVertex.visit();
        visitor.visit(startData);
        //traversalOrder.enqueue(startVertex);
        vertexStack.push(startVertex);
        while (!vertexStack.isEmpty()) {
            Vertex<E> topVertex = vertexStack.peek();
            Iterator<Map.Entry<E, Pair<Vertex<E>, Double>>> iter
                    = topVertex.iterator(); // iterate adjacency list

            if (iter.hasNext()) {
                Vertex<E> neighborVertex = new Vertex<E>();
                do {
                    Entry<E, Pair<Vertex<E>, Double>> nextEntry = iter.next();
                    neighborVertex = nextEntry.getValue().first;
                } while (neighborVertex.isVisited()&& iter.hasNext() );
                if (!neighborVertex.isVisited()) {

                    neighborVertex.visit();
                    //traversalOrder.enqueue(neighborVertex);
                    vertexStack.push(neighborVertex);
                    visitor.visit(neighborVertex.getData());
                } else {
                    vertexStack.pop();
                }
            }else
                vertexStack.pop();
        }
    }

// WRITE THE INSTANCE METHOD HERE TO
    //         WRITE THE GRAPH's vertices and its
    //         adjacency list TO A TEXT FILE (SUGGEST TO PASS AN
    //        ALREADY OPEN PrintWriter TO THIS) !
   
}
