

public class Graph {
	
	boolean[][] adjacency;
	int nbNodes;
	
	public Graph (int nb){
		this.nbNodes = nb;
		this.adjacency = new boolean [nb][nb];
		for (int i = 0; i < nb; i++){
			for (int j = 0; j < nb; j++){
				this.adjacency[i][j] = false;
			}
		}
	}
	
	public void addEdge (int i, int j){
		
		this.adjacency[i][j]=true;
		this.adjacency[j][i] = true;
	}
	
	public void removeEdge (int i, int j){
		
		this.adjacency[i][j]=false;
		this.adjacency[j][i] = false;
	}
	
	public int nbEdges(){
		
		int edges=0;
		int selfLoopEdges=0;
		
		for (int i = 0; i < this.nbNodes; i++){
			for (int j = 0; j < this.nbNodes; j++){
				if(i!=j) {
					if(this.adjacency[i][j] == true && this.adjacency[j][i]==true) {
						edges++;
					}
				} else {
					if(this.adjacency[i][j] == true && this.adjacency[j][i]==true) {
						selfLoopEdges++;
					}
				}
			}
		}
		//each edge is counted twice, so need to divide by 2
		return edges/2+selfLoopEdges; 
	}
	
	public boolean cycle(int start){
		
		//Declaring and initializing variables that are passed as input for the dfs method
		//Initializing to -1 for array elements(i.e. empty)
		boolean[] visited = new boolean[nbNodes];
		int[] nodesVisited = new int[this.nbNodes*this.nbNodes];
		for(int i=0; i<nodesVisited.length; i++) {
			nodesVisited[i]= -1;
		}		
		
		//Performing dfs by calling the method and passing as input the variables that were initialized above
		this.dfs(visited,-1,start, nodesVisited);  
            
		//Finding the start node in the array
		//if it is present in the array, it must be part of a cycle
		//if it is not present in the array, it is not part of a cycle(dfs never went back to the start node)
		for(int i=0; i<nodesVisited.length; i++) {
			if(nodesVisited[i]==start) {
				return true;
			}
		}
	
		return false;
		
	}
	//A helper method for the cycle method
	//Using an int array, nodesVisited,  to store the nodes that were visited(excluding the start node and the node visited right before a particular node)
	//Since the start node was not stored in the array, if it appears in the array after dfs, start must be in a cycle
	
	public int[] dfs(boolean[] visited, int u, int start, int[] nodesVisited) {
		//setting the start variable to visited
		visited[start]=true;
		//Creating an array to store the neighbours of start
		int[] neighbours = this.neighboursArray(start);
		for(int i=0; i<neighbours.length; i++) {
			//If these neighbours have not been visited yet, call dfs on them
			if(!visited[neighbours[i]]) {
				this.dfs(visited, start, neighbours[i], nodesVisited);
			
			} else if(neighbours[i]!=u && neighbours[i]!=start) {//neighbour has been visited and is not the previous node that was visited
				this.addElement(nodesVisited, neighbours[i]); //add the neighbour to the nodesVisited array
				this.addElement(nodesVisited, start); //add the start variable to the nodesVisited array
				
				
			}
			
		}	
		return nodesVisited;
	}
	
	//A helper method for the dfs helper method(adds a node to the array)
	public void addElement(int[] visited, int toAdd) {
		for(int i=0; i<visited.length; i++) {
			if(visited[i]==-1) {
				visited[i] = toAdd;
				break;
			}
			
		}
		
	}
	
	//A helper method for the dfs helper method
	//Creates an array of the neighbours a the node given as input
	//Used the adjacency[][] attribute to check if nodes are neighbours
	//Calls the numNeighbours method to initialize the array
	public int[] neighboursArray(int node) {
		int [] adjacentNodes = new int[this.numNeighbours(node)];
		int i=0;
		while(i<adjacentNodes.length) {
			for (int j = 0; j < this.nbNodes; j++){
				if(this.adjacency[node][j]==true) {
					adjacentNodes[i]=j;
					i++;
				}
			}
		}
		return adjacentNodes;
	}
	
	//A helper method for the neighboursArray helper method
	//Finds the number of neighbours that a node has
	public int numNeighbours(int node) {
		int neighbours = 0;
		for(int i=0; i<this.nbNodes; i++) {
			if(this.adjacency[node][i]==true) {
				neighbours++;
			}
		}
		
		return neighbours;
	}
	
	public int shortestPath(int start, int end){
		
		// start node equals end node
		if (start == end) {
			// if is a self-loop 
			if (this.adjacency[start][end]) { 
				return 1;
			} else if (!this.adjacency[start][end] && this.cycle(start)){ //cycle exists
				int path = this.lengthOfCycle(start, end); //find the length of the cycle
				if(path==2) {
					path++;
				}
				return path;
			} else {
				// if no self loop exists 
				return nbNodes + 1; 
			}
		}
		
		if(adjacency[start][end]==true) {//if start and end are adjacent or there is a self-loop
			return 1;
		}
		
		//declaring and initializing variables that are taken as input for the bfs method
		//setting the elements to be -1(i.e. empty array)
		boolean[] visited = new boolean[this.nbNodes];
		int[] queue = new int[this.nbNodes];
		for(int i=0; i<queue.length; i++) {
			queue[i]=-1;
		}
		int[] dequeue = new int[this.nbNodes];
		for(int i=0; i<dequeue.length; i++) {
			dequeue[i]=-1;
		}
		int[][] nodeAndParent = new int[nbNodes][2];
		for(int i=0; i<nodeAndParent.length; i++) {
			for(int j=0; j<nodeAndParent[i].length; j++) {
				nodeAndParent[i][j]=-1;
			}
		}
		//Calling bfs with the input variables that were initialized above
		bfs(start,end,visited, queue, dequeue, nodeAndParent);
		
		int indexOfEnd=-1; //first initializing the index of the end node to be -1
		for(int i=1; i<dequeue.length; i++) { //finding the end node starting from index one (index 0 is the start)
			if(dequeue[i]==end) {
				indexOfEnd=i; //update when the end node is found in the dequeue array
			}
		}
		if(indexOfEnd==-1) {//end node index was not updated
			return this.nbNodes+1; //no path was found from the start node to the end node
		}
		
		int shortestPath=0; //first initializing the shortest path to be 0
		//Calling backtrack method to find the length of the shortest path
		shortestPath = this.backtrack(nodeAndParent, shortestPath, indexOfEnd);
		
		return shortestPath;
		
	}
	//Helper method to check if a 2D array is empty(i.e. all entries are -1)
	//Used in pathOfDescending and pathOfAscending helper methods for shortestPath
	public boolean isEmpty(int[][] array) {
		// just need to check if the node is empty or not 
		for (int i = 0; i < array.length; i++) {
			if (array[i][1] != -1) {
				return false;
				
			}
		}
		return true; 
	}
	
	//Finds the descending path that is a cycle
	public int[][] pathOfDescendingCycle(int start, int end){
		// store all popped nodes
		int[][] popped = new int[nbEdges() * nbEdges()][2]; 
		for (int i = 0; i < popped.length; i++) {
			for (int j = 0; j < popped[i].length; j++) {
				popped[i][j] = -1; 
			}
		}
		// create a stack using int[][]
		// since each node only appears in the stack once, max size is nbNodes
		int[][] stack = new int[2 * nbEdges()][2]; 
		for (int i = 0; i < stack.length; i++) {
			for (int j = 0; j < stack[i].length; j++) {
				stack[i][j] = -1; 
			}
		}
		boolean[] visited = new boolean[nbNodes]; 
		// a self-loop has path 1
		// parent of starting node is not important
		stack[0][0] = -1;
		stack[0][1] = start; 
		// i represents index of popped array
		int i = 0; 
		// j represents index of stack 
		int j = 0; 
		int numOccurrences = 0; 
		while (!this.isEmpty(stack)) {
			// store parent and currentNode
			int parent = stack[j][0];
			int currentNode = stack[j][1]; 
			// once have reached end node 
			if (currentNode == end) {
				numOccurrences++;
				// pop
				if (numOccurrences == 2) {
					stack[j][0] = -1;	
					stack[j][1] = -1;
					popped[i][0] = parent;
					popped[i][1] = currentNode;  
							
					return popped; 
				}
			} 
					
					
			// if haven't reached end node yet 
					
			if (!visited[currentNode]){
				// pop 
				stack[j][0] = -1;
				stack[j][1] = -1;
				popped[i][0] = parent;
				popped[i][1] = currentNode; 
				i++; 
				// set to visited 
				visited[currentNode] = true; 
				// push all neighbours
				//Descending path(starts iterating from the highest node to find the neighbours)
				for (int k=nbNodes-1; k>=0 ; k--) {
					if (this.adjacency[currentNode][k] == true) {
						if(k!=parent) {
							stack[j][0] = currentNode;
							stack[j][1] = k; 
							j++;  
						} 
					}
				}
			} else {
				stack[j][0] = -1;
				stack[j][1] = -1;
				popped[i][0] = parent;
				popped[i][1] = currentNode; 
				i++; 
			}
			j--; 
		}	
		return popped;	
	}
	
	//A helper method that finds the ascending path that is a cycle
	
	public int[][] pathOfAscendingCycle(int start, int end){
		
		// store all popped nodes
		int[][] popped = new int[nbEdges() * nbEdges()][2]; 
		for (int i = 0; i < popped.length; i++) {
			for (int j = 0; j < popped[i].length; j++) {
				popped[i][j] = -1; 
			}
		}
		// create a stack using a 2D int array
		// since each node only appears in the stack once, max size is nbNodes
		int[][] stack = new int[2 * nbEdges()][2]; 
		for (int i = 0; i < stack.length; i++) {
			for (int j = 0; j < stack[i].length; j++) {
				stack[i][j] = -1; 
			}
		}
		boolean[] visited = new boolean[nbNodes]; 
		// parent of starting node is not important
		stack[0][0] = -1;
		stack[0][1] = start; 
		// i represents index of popped array
		int i = 0; 
		// j represents index of stack 
		int j = 0; 
		int numOccurrences = 0; 
		while (!this.isEmpty(stack)) {
			// store the parent and currentNode
			int parent = stack[j][0];
			int currentNode = stack[j][1]; 
			// once have reached end node 
			if (currentNode == end) {
				numOccurrences++;
				// pop
				if (numOccurrences == 2) {
					stack[j][0] = -1;	
					stack[j][1] = -1;
					popped[i][0] = parent;
					popped[i][1] = currentNode;  
					
					return popped; 
				}
			} 
			
			
			// if have not reached end node yet 
			
			if (!visited[currentNode]){
				// pop the current node from stack
				stack[j][0] = -1;
				stack[j][1] = -1;
				popped[i][0] = parent;
				popped[i][1] = currentNode; 
				i++; 
				// set the currentNode to visited 
				visited[currentNode] = true; 
				// put all neighbours into stack
				//Ascending (starts iterating from the lowest node to find the neighbours)
				for (int k = 0; k < nbNodes; k++) {
					if (this.adjacency[currentNode][k] == true) {
						if(k!=parent) {
							stack[j][0] = currentNode;
							stack[j][1] = k; 
							j++;  
						} 
					}
				}
			} else {
				stack[j][0] = -1;
				stack[j][1] = -1;
				popped[i][0] = parent;
				popped[i][1] = currentNode; 
				i++; 
			}
			j--; 
		}	
		return popped; 
	}
	//Finds the shortest path that is a cycle
	public int lengthOfCycle(int start, int end) {
		//Initialize 2 2D arrays to store the nodes and parents of the two paths
		int[][] pathA = this.pathOfAscendingCycle(start, end);
		int[][] pathD =  this.pathOfDescendingCycle(start,end);
		
		//initialize the length of pathA and the index
		int lengthA = 0; 
		int indexA = -1; 
		// find the index of the last search
		// the last search stops at the end node 
		for (int i = pathA.length - 1; i >= 0; i--) {
			if (pathA[i][1] == end) {
				indexA = i; 
				break; 
			}
		} 
		
		if (indexA != -1) {
			// backtrack from end node back to start to find the length of the path 
			int currentNode = pathA[indexA][0]; 
			for (int i = indexA; i >= 0; i--) {
				if (pathA[i][1] == currentNode) {
					lengthA++; 
					currentNode = pathA[i][0];
				}
			}
		} 
		//initialize the length of pathD and the index
		int lengthD = 0; 
		int indexD = -1; 
		// find index of the last search
		// the last search stops at the end node 
		for (int i = pathD.length - 1; i >= 0; i--) {
			if (pathD[i][1] == end) {
				indexD = i; 
				break; 
			}
		} 
		
		if (indexD != -1) {
			// backtrack from end node back to start to find the length of the path 
			int currentNode = pathD[indexD][0]; 
			for (int i = indexD; i >= 0; i--) {
				if (pathD[i][1] == currentNode) {
					lengthD++; 
					currentNode = pathD[i][0];
				}
			}
		}
		
		if(indexA==-1 && indexD==-1) { //no cycle found
			return this.nbNodes+1;
		}
		if(lengthA>lengthD) {
			return lengthD; //lengthD path is shorter, return lengthD
		} 
		return lengthA; //If lengthA is shorter or lengthA and lengthD are same, return lengthA
	}

		
	//A helper method for the shortestPath method
	//backtracks from the index of the end node the index of its parent 
	//Updating the shortestPath variable for each backtrack step
	public int backtrack(int[][] nodeAndParent, int shortestPath, int index) {
		for(int i=index; i>-1; i--) {
			if (nodeAndParent[i][1] == nodeAndParent[index][0]) {
				shortestPath++;
				index=i;
			}
		}
		return shortestPath;
	}
	//A helper method for the shortestPath method
	/*Takes as input the start and end nodes, a boolean array to keep track of the nodes that have been visited
	int arrays to store the queued and dequeued elements, and a 2D array to store the visited nodes and its
	corresponding parents*/
	public int[][] bfs(int start, int end, boolean[] visited, int[] queue, int[] dequeue, int[][] nodeAndParent) {
		//set start as visited
		
		visited[start] = true;
		
		//enqueue start
		this.enqueue(queue,start);
		while(!checkForEmptyQueue(queue)) { //check if queue is empty before preceding to dequeue
			int dequeued = this.updateDequeue(queue,dequeue);
			this.nodeAndParentArray(start,nodeAndParent,dequeue, dequeued);//updating the nodeAndParent array
			int[] neighbours = this.neighboursArray(dequeued); //array to store the neighbours of the dequeued node
			for(int i=0; i<neighbours.length; i++) {
				
				if(!visited[neighbours[i]]) { //checking if the neighbours have been visited
					visited[neighbours[i]]=true; //if not, set them as visited
					this.enqueue(queue, neighbours[i]); //enqueue the neighbours
					
				}
			}
		}
		//returning the complete 2D array of the visited nodes and their corresponding parent
		return nodeAndParent;
	}
	
	//A helper method for the bfs helper method
	//Stores the visited nodes in a 2D array(the first element of each subarray is the node's parent, and the second element is the node)
	public int[][] nodeAndParentArray(int start, int[][] nodeAndParent, int[] dequeue, int node) {
		
		if(node==start) { //initializing the first subarray with -1 as the parent and start as the node
			nodeAndParent[0][0]=-1;
			nodeAndParent[0][1]=start;
			return nodeAndParent;
		}
		//Finding the first parent, that has already been visited, of a particular node
		//storing this parent node into a the variable
		int parent= -1;
		for(int k=0; k<dequeue.length; k++) {
			if(dequeue[k]!=-1) {
				if(this.adjacency[dequeue[k]][node]) {
					parent=dequeue[k];
					break;
				}
			}
		}
		//Finding an available subarray(i.e. first one with the node still initialized at -1) to store the node and its parent
		for(int i=0; i<nodeAndParent.length; i++) {
			if (nodeAndParent[i][1]==-1) {
				nodeAndParent[i][0]= parent;
				nodeAndParent[i][1]= node;
				break;
			
			}
		}
		return nodeAndParent;
	}
	
	//A helper method for the bfs helper method
	//Adding a node to the front of the queue array by finding the first spot(starting from the front) that is not occupied
	public int[] enqueue(int[] queue, int node) {
		for(int i=0; i<queue.length; i++) {
			if(queue[i]==-1) {
				queue[i]=node;
				break;
			}
		}
		return queue;
	}
	
	//Helper method for bfs helper method
	//Update the dequeue array with the element at the front of the queue
	//Removes the first element from the queue array and shifts all the elements forward 
	public int updateDequeue(int[] queue, int[] dequeue) {
		int dequeued = queue[0];
		this.addElement(dequeue, dequeued);
		for(int i=0; i<queue.length-1; i++) {
			queue[i] = queue[i+1];
		}
		
		return dequeued;
	}
	
	//Helper method for bfs helper method
	//Checks if the queue is empty and returning true if it is
	public boolean checkForEmptyQueue(int[] queue) {
		for(int i=0; i<queue.length; i++) {
			if(queue[i]!=-1) {
				return false;
			}
		}
		return true;
	}	
}
