
class Edge<E> implements Comparable< Edge<E> >
{
	 Vertex<E> source, dest;
	 double cost;

	 Edge( Vertex<E> src, Vertex<E> dst, Double cst)
	 {
	    source = src;
	    dest = dst;
	    cost = cst;
	 }

	 Edge( Vertex<E> src, Vertex<E> dst, Integer cst)
	 {
	    this (src, dst, cst.doubleValue());
	 }

	 Edge()
	 {
	    this(null, null, 0.);
	 }

	 public String toString()
	 { 
		 return "Edge: "+source.getData() + " to " + dest.getData();
	 }

	 public int compareTo( Edge<E> rhs )
	 {
	    return (cost < rhs.cost? -1 : cost > rhs.cost? 1 : 0);
	 }
}