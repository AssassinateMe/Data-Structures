package transit;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered linked
 * list to simulate transit
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class Transit {
	private TNode trainZero; // a reference to the zero node in the train layer

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */ 
	public Transit() { trainZero = null; }

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */
	public Transit(TNode tz) { trainZero = tz; }
	
	/*
	 * Getter method for trainZero
	 *
	 * DO NOT remove from this file.
	 */
	public TNode getTrainZero () {
		return trainZero;
	}

	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0. Store the zero node in the train layer in
	 * the instance variable trainZero.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 */
	public void makeList(int[] trainStations, int[] busStops, int[] locations) {

		TNode walkZero = new TNode();
		TNode busZero = new TNode(0, null, walkZero);
		
		
			
		if(trainZero == null)
			trainZero = new TNode(0, null, busZero);
		
		TNode holder = trainZero;

		for(int i  = 0; i < trainStations.length; i ++)
		{
			holder.setNext(new TNode(trainStations[i], null, busZero));
			holder = holder.getNext();
		}
		
		holder = busZero;

		for(int i  = 0; i < busStops.length; i ++)
		{
			holder.setNext(new TNode(busStops[i], null, busZero));
			holder = holder.getNext();
		}

		holder = walkZero;

		for(int i  = 0; i < locations.length; i ++)
		{
			holder.setNext(new TNode(locations[i], null, busZero));
			holder = holder.getNext();
		}

		holder = trainZero;
		TNode bholder = busZero;
		while(holder.getNext()!= null)
		{
			if(bholder.getNext().getLocation() == holder.getNext().getLocation())
			{
				holder = holder.getNext();
				holder.setDown(bholder.getNext());
			}
			bholder = bholder.getNext();
		}

		holder = busZero;
		TNode wholder = walkZero;
		while(holder.getNext()!= null)
		{
			if(wholder.getNext().getLocation() == holder.getNext().getLocation())
			{
				holder = holder.getNext();
				holder.setDown(wholder.getNext());
			}
			wholder = wholder.getNext();
		}
	}
	
	/**
	 * Modifies the layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param station The location of the train station to remove
	 */
	public void removeTrainStation(int station) {
		
		if(trainZero.getLocation() == station)
		{
			trainZero = trainZero.getNext();
			return;
		}
		TNode n = trainZero;
		while(n.getNext() != null)
		{
			if(n.getNext().getLocation() == station)
			{
				n.setNext(n.getNext().getNext());
				return;
			}
			n = n.getNext();
		}
		
	}

	/**
	 * Modifies the layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param busStop The location of the bus stop to add
	 */
	public void addBusStop(int busStop) {
	    
		TNode n = trainZero.getDown();
		
		TNode holder = n.getDown();
		while(holder.getLocation() != busStop && holder.getNext() != null)
		{
			holder = holder.getNext();
		}

		while(n.getNext() != null && n.getLocation() != busStop)
		{
			if(n.getNext().getLocation() > busStop)
			{
				TNode temp = n.getNext();
				n.setNext(new TNode(busStop));
				n.getNext().setNext(temp);
				n.getNext().setDown(holder);
				return;
			}
			n = n.getNext();
		}

	}
	
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param destination An int representing the destination
	 * @return
	 */
	public ArrayList<TNode> bestPath(int destination) {

		// assuming that the destination is not out of the locations listed.
	    ArrayList<TNode> path = new ArrayList<TNode>();
		TNode current = trainZero;

		while(current.getLocation() != destination)
		{
			path.add(current);
			if( current.getNext() == null  && current.getLocation() != destination)
			{
				current = current.getDown();
			}
			else if(current.getNext().getLocation() > destination)
			{
				current = current.getDown();
			}
			
			else
			{
				current = current.getNext();
			}
			
		}
		
		
		while(current.getDown().getDown() != null)
		{
			path.add(current);
			current = current.getDown();
			
		}
	    return path;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @return A reference to the train zero node of a deep copy
	 */
	public TNode duplicate() {

		TNode newTrainZero = new TNode(trainZero.getLocation(), null, null);
		

		TNode busZero = new TNode(0, null, null);
		TNode walkZero = new TNode();
		TNode ptr = newTrainZero;
		newTrainZero.setDown(busZero);
		newTrainZero.getDown().setDown(walkZero);

		TNode holder = trainZero.getNext();

		while(holder != null)
		{
			ptr.setNext(new TNode(holder.getLocation(), null, null));
			holder = holder.getNext();
			ptr = ptr.getNext();
		}
		
		holder = trainZero.getDown().getNext();
		ptr = busZero;
		while(holder != null)
		{
			ptr.setNext(new TNode(holder.getLocation(), null, null));
			holder = holder.getNext();
			ptr = ptr.getNext();
		}

		holder = trainZero.getDown().getDown().getNext();
		ptr = walkZero;
		while(holder != null)
		{
			ptr.setNext(new TNode(holder.getLocation(), null, null));
			holder = holder.getNext();
			ptr = ptr.getNext();
		}

		// here is where we start connecting things
		// Unknown why its not techinically a deep copy.
		ptr = newTrainZero;
		TNode bholder = busZero;
		while(ptr.getNext()!= null)
		{
			if(bholder.getNext().getLocation() == ptr.getNext().getLocation())
			{
				ptr = ptr.getNext();
				ptr.setDown(bholder.getNext());
			}
			bholder = bholder.getNext();
		}

		ptr = busZero;
		TNode wholder = walkZero;
		while(ptr.getNext()!= null)
		{
			if(wholder.getNext().getLocation() == ptr.getNext().getLocation())
			{
				ptr = ptr.getNext();
				ptr.setDown(wholder.getNext());
			}
			wholder = wholder.getNext();
		}

	    return newTrainZero;
	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public void addScooter(int[] scooterStops) {

		// down once is bus layer
		// down twice is walk
		// want to add another down node and refresh downs for bus layer and etc.

	    TNode n = trainZero.getDown(); // holder
		TNode temp = trainZero.getDown().getDown(); // walk

		TNode scooter = new TNode(0, null,null); // scooter node

		n.setDown(scooter); // n is on bus rn, sets down to scooter node
		n = n.getDown(); // n becomes scooter node
		n.setDown(temp); // n on scooter node set down to temp.

		for(int i  = 0; i < scooterStops.length; i ++)
		{
			n.setNext(new TNode(scooterStops[i], null, null));
			n = n.getNext();
		}

		// Want to check bus to scooter, then scooter to locations
		n = trainZero.getDown(); // bus 
		TNode sholder = trainZero.getDown().getDown(); // scooter
		while(n.getNext()!= null)
		{
			if(sholder.getNext().getLocation() == n.getNext().getLocation())
			{
				n = n.getNext();
				n.setDown(sholder.getNext());
			}
			sholder = sholder.getNext();
		}

		n = trainZero.getDown().getDown(); // scooter
		TNode wholder = trainZero.getDown().getDown().getDown(); // walk
		while(n.getNext()!= null)
		{
			if(wholder.getNext().getLocation() == n.getNext().getLocation())
			{
				n = n.getNext();
				n.setDown(wholder.getNext());
			}
			wholder = wholder.getNext();
		}

	}

	/**
	 * Used by the driver to display the layered linked list. 
	 * DO NOT edit.
	 */
	public void printList() {
		// Traverse the starts of the layers, then the layers within
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// Output the location, then prepare for the arrow to the next
				StdOut.print(horizPtr.getLocation());
				if (horizPtr.getNext() == null) break;
				
				// Spacing is determined by the numbers in the walking layer
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print("--");
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print("-");
				}
				StdOut.print("->");
			}

			// Prepare for vertical lines
			if (vertPtr.getDown() == null) break;
			StdOut.println();
			
			TNode downPtr = vertPtr.getDown();
			// Reset horizPtr, and output a | under each number
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				while (downPtr.getLocation() < horizPtr.getLocation()) downPtr = downPtr.getNext();
				if (downPtr.getLocation() == horizPtr.getLocation() && horizPtr.getDown() == downPtr) StdOut.print("|");
				else StdOut.print(" ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
	
	/**
	 * Used by the driver to display best path. 
	 * DO NOT edit.
	 */
	public void printBestPath(int destination) {
		ArrayList<TNode> path = bestPath(destination);
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the number if this node is in the path, otherwise spaces
				if (path.contains(horizPtr)) StdOut.print(horizPtr.getLocation());
				else {
					int numLen = String.valueOf(horizPtr.getLocation()).length();
					for (int i = 0; i < numLen; i++) StdOut.print(" ");
				}
				if (horizPtr.getNext() == null) break;
				
				// ONLY print the edge if both ends are in the path, otherwise spaces
				String separator = (path.contains(horizPtr) && path.contains(horizPtr.getNext())) ? ">" : " ";
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print(separator + separator);
					
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print(separator);
				}

				StdOut.print(separator + separator);
			}
			
			if (vertPtr.getDown() == null) break;
			StdOut.println();

			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the vertical edge if both ends are in the path, otherwise space
				StdOut.print((path.contains(horizPtr) && path.contains(horizPtr.getDown())) ? "V" : " ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
}