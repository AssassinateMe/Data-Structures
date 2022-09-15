package prereqchecker;

import java.util.ArrayList;


/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 * 
 * Step 2:
 * AdjListOutputFile name is passed through the command line as args[1]
 * Output to AdjListOutputFile with the format:
 * 1. c lines, each starting with a different course ID, then 
 *    listing all of that course's prerequisites (space separated)
 */
public class AdjList {
    public static void main(String[] args) {

        if ( args.length < 2 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.AdjList <adjacency list INput file> <adjacency list OUTput file>");
            return;
        }

        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);
        
        // assuming there arent any inputs of the same class
        int numClasses = StdIn.readInt();

        ArrayList<ArrayList<String>> classes = new ArrayList<ArrayList<String>>();
        
        for(int i = 0; i < numClasses; i++)
        {
            String name = StdIn.readString();
            
            ArrayList<String> holder = new ArrayList<String>();
            holder.add(name);
            
            classes.add(holder);
        } 


        int numPreReq = StdIn.readInt();
        for(int i = 0; i < numPreReq; i++) // num of prereq
        {
            String name = StdIn.readString();
            String preReq = StdIn.readString();
            for(int j = 0; j < classes.size(); j++) // going through the classes to find the reference class
            {
                if(classes.get(j).get(0).equals(name))
                {
                    classes.get(j).add(preReq);
                }
            }
        }

        for(int i = 0; i < numClasses; i++)
        {
            String str = "";
            for(int j = 0; j < classes.get(i).size(); j++)
            {
                str += classes.get(i).get(j) + " ";
                
            }
            
            
            StdOut.println(str);
        }
        

    }
}
