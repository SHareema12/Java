package apps;

import structures.*;

import java.util.ArrayList;

public class MST {

	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph
	 *            Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
		PartialTreeList L = new PartialTreeList();
		Vertex v;
		PartialTree T;
		MinHeap<PartialTree.Arc> minHeap;
		PartialTree.Arc arc;
		int weight;
		for (int i = 0; i < graph.vertices.length; i++) {
			v = graph.vertices[i];
			T = new PartialTree(v);
			for (Vertex.Neighbor j = v.neighbors; j != null; j = j.next) {
				minHeap = T.getArcs();
				weight = j.weight;
				arc = new PartialTree.Arc(v, j.vertex, weight);
				minHeap.insert(arc);
			}
			L.append(T);
		}
		return L;
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree
	 * list
	 * 
	 * @param ptlist
	 *            Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is
	 *         irrelevant
	 */
	public static ArrayList<PartialTree.Arc> execute(PartialTreeList ptlist) {
		/* COMPLETE THIS METHOD */
		ArrayList<PartialTree.Arc> resultArcs = new ArrayList<PartialTree.Arc>();
		while (ptlist.size() > 1) {
			PartialTree PTX = ptlist.remove();
			MinHeap<PartialTree.Arc> PQX = PTX.getArcs();
			Vertex root = PTX.getRoot();
			Vertex v1, v2, rootPTX, rootPTY;
			PartialTree.Arc arc;
			boolean belongs;
			do {
				arc = PQX.deleteMin();
				v2 = arc.v2; 
				belongs = v2.getRoot().equals(PTX.getRoot()); 
			} while (belongs);
			PartialTree PTY = ptlist.removeTreeContaining(v2);
			MinHeap<PartialTree.Arc> PQY = PTY.getArcs();
			PTX.merge(PTY);
			resultArcs.add(arc);
			ptlist.append(PTX);
		}
		return resultArcs;
	}
}
