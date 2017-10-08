import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class readFile {
	
public static Scanner userScanner = new Scanner(System.in);
private String startElement = null;

	public  EulerPath<String> readInputFile(Scanner fileScanner){
		EulerPath<String> newGraph = new EulerPath<>();

		if(fileScanner == null) return null; 	//if: the file doesn't open

		while(fileScanner.hasNextLine()){
			String vertex1, vertex2; 
			vertex1 = fileScanner.nextLine();
			
			if(startElement == null){
				startElement = vertex1;
			}
				
			
			if(fileScanner.hasNextLine())
				vertex2 = fileScanner.nextLine();			
			else
				break;
			
			newGraph.addEdge(vertex1, vertex2, 0);
			
			if(fileScanner.hasNextLine())
				fileScanner.nextLine();
			else
				break;
		}//end while
		
		return newGraph;
	}
	public String getStartEle(){ return startElement;}
	
	public static Scanner openInputFile()
	{
		String filename;
        Scanner scanner=null;
        
		System.out.print("Enter the filename of the map to be loaded: ");
		filename = userScanner.nextLine();
        	File file= new File(filename);

        	try{
        		scanner = new Scanner(file);
        	}// end try
        	catch(FileNotFoundException fe){
        	   System.out.println("Error: input file couldn't open\n");
       	    return null; // array of 0 elements
        	} // end catch
        	return scanner;
	}//end openInputFile

}
