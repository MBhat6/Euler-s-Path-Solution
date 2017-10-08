
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* 3/19/2017
 * Add method isConnected(), return a boolean value to check whether the given
 * graph is connected or not.
 *
 */
/**
 *
 * @author Sera
 */
public class EulerPath<E> extends Graph<E> {

    public EulerPath() {
    }

    public LinkedQueue<Vertex<E>> eulerPathSolution(Visitor<E> visitor) {

        if (!isConnected()) {
            return null;
        }

        ArrayList<Vertex<E>> oddVerticesList = oddDegreeVertices();
        int oddVerticesListSize = oddVerticesList.size();

        if (oddVerticesListSize > 2 || oddVerticesListSize == 1) {
            return null;
        }

        HashMap<E, Vertex<E>> vertexSetCopy = vertexSet;
        //deep copy of vertexSet
//        for (Map.Entry<E, Vertex<E>> entry : vertexSet.entrySet()) {
//            Vertex<E> temp = new Vertex<E>() ;
//            temp.data = entry.getValue().getData();
//            temp.visited = entry.getValue().isVisited();
//            for (Map.Entry<E, Pair<Vertex<E>, Double>> vertexAdjList : entry.getValue().adjList.entrySet()){
//                E tempE = vertexAdjList.getKey();
//                Pair<Vertex<E>, Double> tempVertex = new Pair<> (vertexAdjList.getValue().first,(Double)0.0);
//                temp.adjList.put(tempE, tempVertex);
//                //temp.addToAdjList(tempVertex.first, 0);
//            }
//
//            vertexSetCopy.put(entry.getKey(), temp);
//        }

        E startElement = null;
        if (oddVerticesListSize == 2) {
            startElement = oddVerticesList.get(0).getData();
        }
        if (oddVerticesListSize == 0) {
            Map.Entry<E, Vertex<E>> entry = vertexSetCopy.entrySet().iterator().next();
            startElement = entry.getKey();
        }

        Vertex<E> startVertex = vertexSetCopy.get(startElement);
        return eulerPathSolutionHelper(startVertex, visitor);
    }

    public boolean isConnected() {

        unvisitVertices();
        LinkedQueue<Vertex<E>> vertexQueue = new LinkedQueue<>();
        Map.Entry<E, Vertex<E>> entry = vertexSet.entrySet().iterator().next();
        E startData = entry.getKey();
        Vertex<E> startVertex = vertexSet.get(startData);

        startVertex.visit();
        int counter = 1;

        vertexQueue.enqueue(startVertex);

        while (!vertexQueue.isEmpty()) {
            Vertex<E> nextVertex = vertexQueue.dequeue();
            Iterator<Map.Entry<E, Pair<Vertex<E>, Double>>> iter
                    = nextVertex.iterator(); // iterate adjacency list

            while (iter.hasNext()) {
                Map.Entry<E, Pair<Vertex<E>, Double>> nextEntry = iter.next();
                Vertex<E> neighborVertex = nextEntry.getValue().first;
                if (!neighborVertex.isVisited()) {
                    vertexQueue.enqueue(neighborVertex);
                    neighborVertex.visit();
                    counter++;
                }
            }
        }
        if (counter != vertexSet.size()) {
            return false;
        }
        return true;
    }

    protected ArrayList<Vertex<E>> oddDegreeVertices() {
        ArrayList<Vertex<E>> oddVerticesList = new ArrayList<Vertex<E>>();

        Iterator<Map.Entry<E, Vertex<E>>> iter;
        iter = vertexSet.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<E, Vertex<E>> v = iter.next();
            Vertex<E> e = v.getValue();
            int degree = e.adjList.size();
            if (degree % 2 != 0) {
                oddVerticesList.add(e);
            }
        }
        return oddVerticesList;
    }

    protected LinkedQueue<Vertex<E>> eulerPathSolutionHelper(Vertex<E> startVertex, Visitor<E> visitor) {

        LinkedQueue<Vertex<E>> path = new LinkedQueue<>();
        LinkedStack<Vertex<E>> vertexStack = new LinkedStack<>();
        LinkedStack<E> undoStackTemp = new LinkedStack<E>();

        while(!undoStack.isEmpty())
            undoStackTemp.push(undoStack.pop());

        //visitor.visit(startData);
        //traversalOrder.enqueue(startVertex);
        vertexStack.push(startVertex);
        while (!vertexStack.isEmpty()) {
            Vertex<E> topVertex = vertexStack.peek();
            Iterator<Map.Entry<E, Pair<Vertex<E>, Double>>> iter
                    = topVertex.iterator(); // iterate adjacency list

            if (iter.hasNext()) {
                Vertex<E> neighborVertex = new Vertex<E>();

                Map.Entry<E, Pair<Vertex<E>, Double>> nextEntry = iter.next();
                neighborVertex = nextEntry.getValue().first;

                //traversalOrder.enqueue(neighborVertex);
                vertexStack.push(neighborVertex);
                //visitor.visit(neighborVertex.getData());

                remove(topVertex.getData(), neighborVertex.getData());

            } else {
                if (vertexStack.peek() != null) {
                    visitor.visit(vertexStack.peek().getData());
                    path.enqueue(vertexStack.pop());
                }
            }
        }
        while (!undoStack.isEmpty()) {
            undoRemove();
        }
        while(!undoStackTemp.isEmpty())
            undoStack.push(undoStackTemp.pop());
//        System.out.println("result:");
//        while (!path.isEmpty())
//            System.out.println(path.dequeue().getData());
        return path;
    }

	//Undo Remove method
    public Edge<E> undoRemove() {
        Vertex<E> source = null;
        Vertex<E> dest = null;
        Edge<E> edge = null;

        source = new Vertex<E>(undoStack.pop());
        dest = new Vertex<E>(undoStack.pop());

        if (source != null && dest != null) {
            addEdge(source.getData(), dest.getData(), 0);
            edge = new Edge<E>(source, dest, 0);
        }
        return edge;
    }

}
