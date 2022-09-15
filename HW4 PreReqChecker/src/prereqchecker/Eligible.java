package prereqchecker;

import java.util.*;

/**
 * 
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
 * EligibleInputFile name is passed through the command line as args[1]
 * Read from EligibleInputFile with the format:
 * 1. c (int): Number of courses
 * 2. c lines, each with 1 course ID
 * 
 * Step 3:
 * EligibleOutputFile name is passed through the command line as args[2]
 * Output to EligibleOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class Eligible {
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.Eligible <adjacency list INput file> <eligible INput file> <eligible OUTput file>");
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

        // hashset the classes we took
        // then for loop through the entire arraylist.
        // check j which is until get(i) size 
        // and if it does not contain anything in the hash set go next;
        StdIn.setFile(args[1]);
        int numCompletedClasses = StdIn.readInt();
        HashSet<String> completed = new HashSet<String>();
        
        for(int i = 0; i < numCompletedClasses; i++)
        {
            String compClass= StdIn.readString();
            
            for(int j = 0; j < classes.size(); j++)
            {
                if(classes.get(j).get(0).equals(compClass))
                {
                    fillHash(j, 0, classes, completed);
                    break;
                }
            }

        }

        ArrayList<String> eligibleClasses= new ArrayList<String>();
        
        for(int i = 0; i < classes.size(); i++)
        {
            boolean eligible = true;
            if(classes.get(i).size()==1)
            {
                eligible = false;
            }
            for(int j = 1; j < classes.get(i).size();j++)
            {
                
                if(completed.contains(classes.get(i).get(0)))
                {
                    eligible = false;
                    break;
                }
                else if(!completed.contains(classes.get(i).get(j)))
                {
                    eligible = false;
                }
                    

            }
            if(eligible == true)
            {
                System.out.println(classes.get(i).get(0));
                eligibleClasses.add(classes.get(i).get(0));
            }
  
        }
        for(int i =0; i < eligibleClasses.size(); i++)
        {
            StdOut.println(eligibleClasses.get(i));
        }

    }

    public static void fillHash(int i, int j, ArrayList<ArrayList<String>> classes, HashSet<String> completed)
    {

        if(classes.get(i).size() == 1)
        {
            if(!completed.contains(classes.get(i).get(0)))
            {
                completed.add(classes.get(i).get(0));
            }
            return;
        }
        
        if(!completed.contains(classes.get(i).get(j)))
        {
            completed.add(classes.get(i).get(j));
        }
        int k;
        for(k = 0; k < classes.size(); k++)
        {
            if(classes.get(k).get(0).equals(classes.get(i).get(j)))
            {
                break;
            }
        }



        fillHash(k , 1, classes, completed);
        if(j + 1 < classes.get(i).size())
            fillHash(i, j+1, classes, completed);

    }

}