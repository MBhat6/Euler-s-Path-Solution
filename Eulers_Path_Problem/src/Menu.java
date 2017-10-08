import java.io.*;

import java.util.Scanner;

public class Menu {

	public static Scanner userScanner = new Scanner(System.in);
	public static String option;

	static public void mainMenu(readFile read, EulerPath<String> currentGraph, Visitor<String> visitor) {

		do {
			System.out.println("\nEnter the number of the operation you wish to perform:\n"
					+ "1: Display Graph: depth-first traversal\n"
					+ "2: Display Graph: breadth-first traversal\n"
					+ "3: Display Graph: adjacency table of vertices\n"
					+ "4: Add an edge to the graph\n"
					+ "5: Remove an edge from the graph\n"
					+ "6: Undo edge removal\n"
					+ "7: Find Euler's Path\n"
					+ "8: Upload a new Graph\n"
					+ "0: Exit the program\n"
					+ "What would you like to do?");

			option = userScanner.nextLine();
			if(option.equals("1")){
				System.out.println("This is the depth-first traversal of vertices:\n");
				currentGraph.depthFirstTraversal(read.getStartEle(), visitor);
			}
			else if(option.equals("2")){
				System.out.println("This is the breadth-first traversal of vertices:\n");
				currentGraph.breadthFirstTraversal(read.getStartEle(), visitor);
			}
			else if(option.equals("3")){
				System.out.println("This is the adjacency table of vertices:");
				currentGraph.showAdjTable();
			}
			else if(option.equals("4")){
				screen4_add(currentGraph);
			}
			else if(option.equals("5")){
				screen5_remove(currentGraph);
			}
			else if(option.equals("6")) {
				Edge<String> temp = currentGraph.undoRemove();

				String[] tokens = temp.toString().split("to");
				String[] subToken = tokens[0].toString().split(":");

				if(!tokens[1].trim().equals("null") && !subToken[1].trim().equals("null"))
					System.out.print("Removal Undone, " + temp.toString() + " added back to the graph\n");
				else
					System.out.print("You have not removed any edges\n");
			}
			else if (option.equals("7")) {
				screen7_findPath(currentGraph, visitor);
			}
			else if (option.equals("8")) {
				screen8_createNew();
			}
			else if (option.equals("0")) {
				screen0_exit();
			}
			else{
				invalidOption();
			}

		} while (true);

	}

	static public void screen4_add(EulerPath<String> currentGraph) {
		Vertex<String> source;
		Vertex<String> dest;
		Edge<String> edge;

		System.out.println("Please enter a vertex's source and destination on seperate lines\n"
						 + "Example:\n"
						 + "Source\n"
						 + "Destination\n");
		//userScanner.nextLine();
		source = new Vertex<>(userScanner.nextLine());
		//System.out.println("debug source: " + source.getData());
		dest = new Vertex<>(userScanner.nextLine());
		edge = new Edge<String>(source, dest, 0);
		//System.out.println(source.getData() + "  debug  " + dest.getData());
		currentGraph.addEdge(source.getData(), dest.getData(), 0);

		System.out.println("\n" + edge.toString() + " has been added to the graph\n");
	}

	public static void screen5_remove(EulerPath<String> currentGraph){
		System.out.print("Please enter a vertex's source and destination on seperate lines\n"
						 + "Example:\n"
						 + "Source\n"
						 + "Destination\n");
		Vertex<String> source = new Vertex<>(userScanner.nextLine());
		Vertex<String> dest = new Vertex<>(userScanner.nextLine());
		Edge<String> edge = new Edge<String>(source, dest, 0);
		if(currentGraph.remove(source.getData(), dest.getData()))
			System.out.print(edge.toString() + " removed successfully\n");
		else
			System.out.print(edge.toString() + " could not be found\n");
	}

	/*
	 * static public <E> void screen5_remove(EulerPath<String> currentGraph){
	 * Vertex<String> souce; Vertex<String> dest; Edge<String> edge;
	 * System.out.println(
	 * "Please enter a vertex's source and destination you want to remove");
	 * souce = new Vertex<>(userScanner.nextLine()); dest = new
	 * Vertex<>(userScanner.nextLine()); edge = new Edge<>(souce, dest, 0);
	 *
	 * if(currentGraph.remove(souce.getData(), dest.getData()) {
	 * System.out.println("Remove successfully.\n" + edge.toString() +
	 * "has already removed for the graph"); removeStack.push(edge);
	 *
	 * } else System.out.println("There are no edges to remove "); }
	 *
	 * static public void screen6_undo(EulerPath<String> currentGraph){
	 * if(!removeStack.empty()){ Edge<String> edge = removeStack.pop();
	 * currentGraph.addEdge(edge.source.getData(), edge.dest.getData());
	 * System.out.println(
	 * "Removal successfully undone, edge added back to the graph "); } else
	 * System.out.println("There is no removed edge."); }
	 */

	static public void screen7_findPath(EulerPath<String> currentGraph, Visitor<String> visitor) {
		LinkedQueue<Vertex<String>> solution = currentGraph.eulerPathSolution(visitor);
		if (solution == null) {
			System.out.println("The graph has no solutions, please change the graph");
		} else {
			LinkedQueue<Vertex<String>> tempQueue = new LinkedQueue<Vertex<String>>();
			Vertex<String> tempVertex = new Vertex<String>();
			while (!solution.isEmpty()) {
				tempVertex = solution.dequeue();
				tempQueue.enqueue(tempVertex);
				System.out.print(tempVertex.getData() + "\n");
			}
			solution = tempQueue;
			screen7_2(solution);
		}

	}

	static public void screen7_2(LinkedQueue<Vertex<String>> solution) {
		System.out.println("\n\nWould you like to save the solution to a file?\n"
							 + "Enter \"Y\" for yes.");
		option = userScanner.nextLine();
		if (option.equals("Y") || option.equals("y")) {
			System.out.println("Please enter the file name");
			String fileName = userScanner.nextLine();
			saveToFile(fileName, solution);
			System.out.println("Solution has been saved\n");
		}
		//screen7_3();
	}

	static public void screen7_3() {
		while(true){
		System.out.println(
				"\nWhat do you like to do next?\n" + "Enter 8, Input another graph\n" + "Enter 0, Exit the program");
		option = userScanner.nextLine();
		if(option.equals("8"))
			screen8_createNew();
		else if(option.equals("0"))
			screen0_exit();
		else
			invalidOption();
		}
	}

	static public void screen8_createNew(){
		readFile newRead = new readFile();
		EulerPath<String> newGraph = newRead.readInputFile(newRead.openInputFile());
		VertexVisitor newVisitor = new VertexVisitor();
		Menu.mainMenu(newRead, newGraph, newVisitor);
	}

	static public void invalidOption() {
		System.out.println("Please enter correct option.");
	}

	static public void screen0_exit() {
		System.out.println("Exiting the program, Thank you");
		System.exit(0);
	}

	public static void saveToFile(String fileName, LinkedQueue<Vertex<String>> solution) {
        try {
            File file = new File(fileName);
            FileOutputStream output = new FileOutputStream(file);
            OutputStreamWriter outputWriter = new OutputStreamWriter(output);
            Writer w = new BufferedWriter(outputWriter);
            while(!solution.isEmpty()){
            w.write(solution.dequeue().getData() + "\n");
            }
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file, file didn't not save.");
        }
    }

}
