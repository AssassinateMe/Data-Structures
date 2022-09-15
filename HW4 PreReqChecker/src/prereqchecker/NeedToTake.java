package prereqchecker;

import java.util.*;

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
 * NeedToTakeInputFile name is passed through the command line as args[1]
 * Read from NeedToTakeInputFile with the format:
 * 1. One line, containing a course ID
 * 2. c (int): Number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * NeedToTakeOutputFile name is passed through the command line as args[2]
 * Output to NeedToTakeOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */

public class NeedToTake {
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java NeedToTake <adjacency list INput file> <need to take INput file> <need to take OUTput file>");
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

        StdIn.setFile(args[1]);
        String target = StdIn.readString();

        int numTakenCourses = StdIn.readInt();
        HashSet<String> completed = new HashSet<String>();
        
        for(int i = 0; i < numTakenCourses; i++)
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

        ArrayList<String> classNeeded = new ArrayList<String>();
        int targetIndex; // i
        for(targetIndex= 0; targetIndex < classes.size(); targetIndex++)
        {
            if(classes.get(targetIndex).get(0).equals(target))
            {
                break;
            }
        }

        classChecker(targetIndex, 1, classes, completed, classNeeded);

        for(int i = 0; i < classNeeded.size(); i++)
        {
            StdOut.println(classNeeded.get(i));
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

    public static void classChecker(int i, int j, ArrayList<ArrayList<String>> classes, HashSet<String> completed, ArrayList<String> classNeeded)
    {

        if(classes.get(i).size() == 1)
        {
            return;
        }

        if(!completed.contains(classes.get(i).get(j)))
        {
            classNeeded.add(classes.get(i).get(j));
        }
        
        
        int k; // holds the class @ index j
        for(k = 0; k < classes.size(); k++)
        {
            if(classes.get(k).get(0).equals(classes.get(i).get(j)))
            {
                break;
            }
        }
        
        classChecker(k , 1, classes, completed, classNeeded);
        if(j+1 < classes.get(i).size())
            classChecker(i, j+1, classes, completed, classNeeded);
        
    }
}
