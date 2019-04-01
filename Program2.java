import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

/**
 * Created by tkalbar on 3/2/19.
 */
//
//{ 5,  4,  3,  2},
//{ 3, 10, 11,  1},
//{ 1, 12, 10,  0},
//{ 0,  3,  2,  1}
public class Program2 {
	static HashSet<ArrayList<vertex>> adjlists;
	static HashSet<vertex> discovered;
	static int IntensitySum = 0;
	
    public int constructIntensityGraph(int[][] image){
    	HashSet<edge> edgeset = new HashSet<edge>();
    	//TreeMap<int[], Integer> vertices = new TreeMap<int[], Integer>();
    	int sum = 0;
    	int ylen = image.length;
    	int xlen = image[0].length;
    	for(int y = 0; y < image.length; y++) {
    		for(int x = 0; x< image[y].length; x++) {
    			int[] coord = {x,y};
    			//vertices.put(coord, image[y][x]);
    			//create a list of vertices for ease of access
    			if(x != (xlen -1)) {
    				//add to sum
    				sum += Math.abs(image[y][x+1]-image[y][x]);
    				//create edge for right
    				int[] start = {x,y};
        			int[] end = {x+1,y};
        			int weight = Math.abs(image[y][x] - image[y][x+1]);
        			edgeset.add(new edge(start, end, weight));
        		}
        		if(y != (ylen -1)) {
        			sum += Math.abs(image[y+1][x]-image[y][x]);
        			//add below to sum
        			int[] start = {x,y};
        			int[] end = {x,y+1};
        			int weight = Math.abs(image[y][x] - image[y+1][x]);
        			edgeset.add(new edge(start, end, weight));
        		}
    		}
    	}
    	System.out.println("edges: " + edgeset.size());
    	return sum;
    }

    public int constructPrunedGraph(int[][] image){
    	int ylen = image.length;
    	int xlen = image[0].length;
    	int total = ylen*xlen;
    	vertex vertices[] = new vertex[total];
      
        // Initialize all keys as INFINITE 
        for (int i = 0; i < total; i++) {
        		int x = i/xlen;
        		int y = i%ylen;
        		int data = image[y][x];
        		vertices[i] = new vertex(data);
        }
        
        // Always include first vertex in MST. 
        // Make key 0 so that this vertex is picked as first vertex. 
        vertices[0].setKey(0);      
        vertices[0].setParent(-1); // First node is always root of MST  
      
        // The MST will have V vertices 
        for (int i = 0; i < total - 1; i++) 
        { 
            // Pick the minimum key vertex from the  
            // set of vertices not yet included in MST 
            int u = getMin(vertices); 
      
            // Add the picked vertex to the MST Set 
            vertices[u].included = true; 
      
            // Update key value and parent index of  
            // the adjacent vertices of the picked vertex.  
            // Consider only those vertices which are not  
            // yet included in MST 
            for (int v = 0; v < V; v++) 
      
            // graph[u][v] is non zero only for adjacent vertices of m 
            // mstSet[v] is false for vertices not yet included in MST 
            // Update the key only if graph[u][v] is smaller than key[v] 
            if (graph[u][v] && mstSet[v] == false && graph[u][v] < key[v]) 
                parent[v] = u, key[v] = graph[u][v]; 
        } 
        return 0;
    }

    public int getMin(vertex[] vertices) {
    	// get smallest value key
    	int index = -1;
    	int min = 99999999; 
    	int total = vertices.length;
    	  
    	for (int ver = 0; ver < total; ver++) {
    	    if (!vertices[ver].included  && vertices[ver].key < min) { 
    	        min = vertices[ver].key;
    			index = ver; 
    	    }
    	}
    	  
    	return index; 
    }
}
class vertex {
	public int data;
	public int key;
	public int parent = 0;
	public boolean included;
	
	vertex(int data){			
		this.data = data;
		this.key = 99999999;
		this.included = false;
	}
	public void setParent(int parent) {
		this.parent = parent;
	}
	public void setKey(int key) {
		this.key = key;
	}
}


class edge{
public int startX;
public int endX;
public int startY;
public int endY;
public int weight;

	edge(int[] start, int[] end, int weight){
		this.startX = start[0];
		this.startY = start[1];
		this.endX = end[0];
		this.endY = end[1];
		this.weight = weight;
	}
}
