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
 * ValidPreReqInputFile name is passed through the command line as args[1]
 * Read from ValidPreReqInputFile with the format:
 * 1. 1 line containing the proposed advanced course
 * 2. 1 line containing the proposed prereq to the advanced course
 * 
 * Step 3:
 * ValidPreReqOutputFile name is passed through the command line as args[2]
 * Output to ValidPreReqOutputFile with the format:
 * 1. 1 line, containing either the word "YES" or "NO"
 */
public class ValidPrereq {
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.ValidPrereq <adjacency list INput file> <valid prereq INput file> <valid prereq OUTput file>");
            return;
        }
	
        StdIn.setFile(args[0]);
        StdOut.setFile(args[2]);

        int numClasses = StdIn.readInt();

        ArrayList<ArrayList<String>> classes = new ArrayList<ArrayList<String>>();
        
        for(int i = 0; i < numClasses; i++)
        {
            String name = StdIn.readString();
            
            ArrayList<String> holder = new ArrayList<String>();
            holder.add(name);
            
            classes.add(holder);
        } 

        // Adding PreReqs to the array list
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

        // Here ALl the PreReqs are added in
        // Next need to take new input file validprereq

        StdIn.setFile(args[1]);

        String course1 = StdIn.readString();
        String course2 = StdIn.readString();

        int i = 0;
        while(i < classes.size()) // goes through class first to find index of course 2 
        {
            if(classes.get(i).get(0).equals(course2))
            {
                break;
            }
            i++;
        }
        // assuming that the class will be in the list. (if the class isnt in the list i gets messed up)
        if(classes.get(i).size() == 1)
            StdOut.print("YES");
        else
            classChecker(i, 1, classes, course1);
        StdIn.setFile(args[2]);
        if(StdIn.isEmpty())
            StdOut.print("Yes");
    }

    // ClassChecker gets given an i and a j
    // First iteration starts from course 2 and checks next preReq;
    public static void classChecker(int i, int j, ArrayList<ArrayList<String>> classes, String target)
    {

        if(classes.get(i).size() == 1)
        {
            return;
        }

        if(classes.get(i).get(j).equals(target))
        {
            StdOut.print( "NO");
        }
        
        
        int k; // holds the class @ index j
        for(k = 0; k < classes.size(); k++)
        {
            if(classes.get(k).get(0).equals(classes.get(i).get(j)))
            {
                break;
            }
        }
        
        classChecker(k , 1, classes, target);
        if(j+1 < classes.get(i).size())
            classChecker(i, j+1, classes, target);
        
    }
}
