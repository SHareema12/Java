import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import apps.MST;
import apps.PartialTreeList;
import structures.Graph;
import apps.PartialTree;

public class MSTDriver {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in); 
		System.out.println("Please enter graph file: ");
		String file = sc.next(); 
		Graph graph = new Graph(file); 
		graph.print();
		MST mst = new MST(); 
		PartialTreeList PTL = mst.initialize(graph);
		for (PartialTree x: PTL){
			System.out.println(x);
		}
		ArrayList<PartialTree.Arc> res = mst.execute(PTL); 
		
		//PartialTree test = PTL.remove(); 
		//System.out.println("Removed partial tree: " + test);
		for (PartialTree x: PTL){
			System.out.println(x);
		}
		
		/*
		test = PTL.remove(); 
		System.out.println("Removed partial tree: " + test);
		for (PartialTree x: PTL){
			System.out.println(x);
		}
		*/
		for (PartialTree.Arc x: res){
			System.out.println(x);
		}
		
		
	}

}
