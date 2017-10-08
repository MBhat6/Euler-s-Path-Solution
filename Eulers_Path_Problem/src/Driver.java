import java.util.Scanner;
import java.io.*;
import java.util.*;

public class Driver {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		readFile read = new readFile();
		EulerPath<String> currentGraph = read.readInputFile(read.openInputFile());
		VertexVisitor visitor = new VertexVisitor();
		Menu.mainMenu(read, currentGraph, visitor);
		
	}

}
