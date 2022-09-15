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
 * SchedulePlanInputFile name is passed through the command line as args[1]
 * Read from SchedulePlanInputFile with the format:
 * 1. One line containing a course ID
 * 2. c (int): number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * SchedulePlanOutputFile name is passed through the command line as args[2]
 * Output to SchedulePlanOutputFile with the format:
 * 1. One line containing an int c, the number of semesters required to take the
 * course
 * 2. c lines, each with space separated course ID's
 */
public class SchedulePlan {

    public static Boolean checkPreReq(HashMap<String, ArrayList<String>> x, ArrayList<String> z, String y) {
        
        for (String t : z) {
            if (t.equals(y)) {
                return true;
            } else {
                if (checkPreReq(x, x.get(t), y))
                    return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {

        StdIn.setFile(args[0]);

        int num = Integer.parseInt(StdIn.readLine());
        HashSet<String> coursesIndex = new HashSet<>();
        for (int i = 0; i < num; i++) {
            coursesIndex.add(StdIn.readLine());
        }

        HashMap<String, ArrayList<String>> scheduleCourses = new HashMap<String, ArrayList<String>>();
        for (String index : coursesIndex) {
            scheduleCourses.put(index, new ArrayList<String>());
        }
        int p = Integer.parseInt(StdIn.readLine());
        for (int l = 0; l < p; l++) {
            String start = StdIn.readLine();
            if (coursesIndex.contains(start.substring(0, start.indexOf(" ")))) {
                scheduleCourses.get(start.substring(0, start.indexOf(" "))).add(start.substring(start.indexOf(" ") + 1));
            }

        }

        StdIn.setFile(args[1]);
        StdOut.setFile(args[2]);
        String targetCourse = StdIn.readLine();
        int num2 = Integer.parseInt(StdIn.readLine());
        ArrayList<String> courseChecks = new ArrayList<String>();
        ArrayList<String> coursesToTake = new ArrayList<String>();
        for (int w = 0; w < num2; w++) {
            String temp = StdIn.readLine();
            courseChecks.add(temp);
        }

        for (String index : scheduleCourses.keySet()) {
            if (checkPreReq(scheduleCourses, courseChecks, index)) {
                if (!courseChecks.contains(index)) {
                    courseChecks.add(index);
                    num2++;
                }
            }
        }
        for (String index : scheduleCourses.keySet()) {
            for (String name : scheduleCourses.get(index)) {
                if (checkPreReq(scheduleCourses, scheduleCourses.get(targetCourse), name) && !courseChecks.contains(name)) {
                    coursesToTake.add(name);
                }
            }
        }

        for (String index : scheduleCourses.keySet()) {
            if (courseChecks.contains(index)) {
                coursesToTake.remove(index);
            }
        }

        HashSet<String> coursesToTakeHash = new HashSet<String>();
        for (String index : coursesToTake)
            coursesToTakeHash.add(index);

        ArrayList<ArrayList<String>> schedule = new ArrayList<ArrayList<String>>();
        for(int u = 0; u<40;u++){
            schedule.add(new ArrayList<String>());
        }
        coursesToTake = new ArrayList<String>(coursesToTakeHash);
        
        boolean courseCheck = false;
        int scheduledCounter = 0;
        ArrayList<String> temporary = new ArrayList<String>();
        courseChecks.clear();
        while (coursesToTake.size() > 0) {
            for (int s = 0; s < coursesToTake.size(); s++) {
                courseCheck = true;
                for (int r = 0; r < coursesToTake.size(); r++) {
                    if (checkPreReq(scheduleCourses, scheduleCourses.get(coursesToTake.get(s)), coursesToTake.get(r))
                            && !coursesToTake.get(r).equals(coursesToTake.get(s))) {
                        courseCheck = false;
                    }
                }

                if(courseCheck) {
                    temporary.add(coursesToTake.get(s));
                    courseChecks.add(coursesToTake.get(s));
                }
            }
            for(String index : courseChecks){
                if(coursesToTake.contains(index)){
                    coursesToTake.remove(index);
                }
            }
            
            for(String index : temporary)
            schedule.get(scheduledCounter).add(index);
            scheduledCounter++;
            temporary.clear();
        }

        StdOut.println(scheduledCounter);
        
        for (int d = 0; d <schedule.size(); d++) {
            for (String index : schedule.get(d)) {
                StdOut.print(index + " ");
            }
            StdOut.println();
        }

    }

}