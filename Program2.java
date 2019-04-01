import java.util.HashSet;
import java.util.PriorityQueue;

public class Program2 {
	static HashSet<edge> edgeset;
	static vertex[][] vertices;
	
    public int constructIntensityGraph(int[][] image){
    	//initialize data structures and useful ints
    	int edgesum = 0;
    	int ylen = image.length;
    	int xlen = image[0].length;
    	vertices = new vertex[ylen][xlen];
    	edgeset = new HashSet<edge>();
    	//add each raw input value into vertices array as new vertex class
    	for(int y = 0; y < image.length; y++) {
    		for(int x = 0; x < image[y].length; x++) {
    			//add vertex to set
    			vertex temp = new vertex(image[y][x], x,y);
    			vertices[y][x]=temp;
    		}
    	}
    	//System.out.println("vertices: " + vertices.length*vertices[0].length);
    	for(vertex[] row: vertices) {
    		for(vertex v:row) {
    			//for very vertex, create left and right edge
    			// this will complete intensity graph since starts at top left corner
    			if(v.x != (xlen -1)) {
    				vertex right = vertices[v.y][v.x+1];
    				v.right = right;
    				//new edge
    				edge temp = new edge(v, right, Math.abs(v.data - right.data));
    				edgeset.add(temp);
    				//set both ways for easy access
    				right.leftEdge = temp;
    				v.rightEdge = temp;
        		}
        		if(v.y != (ylen -1)) {
        			vertex down = vertices[v.y+1][v.x];
        			v.down = down;
        			//new edge
        			edge temp = new edge(v, down, Math.abs(v.data - down.data));
        			edgeset.add(temp);
        			//set both ways
        			v.downEdge = temp;
        			down.upEdge = temp;
        		}
        		if(v.y != 0) {
        			//set up for easy access
        			vertex up = vertices[v.y-1][v.x];
    				v.up = up;
        		}
        		if(v.x != 0) {
        			//set down for easy access
        			vertex left = vertices[v.y][v.x-1];
    				v.left = left;
        		}
    		}
    		
    	}
    	//System.out.println("edges: " + edgeset.size());
    	for(edge e: edgeset) {
    		edgesum += e.weight;
    	}
    	return edgesum;
    	//return sum;
    }

    public int constructPrunedGraph(int[][] image){
    	int numedges = 0;
    	PriorityQueue<edge> nextup = new PriorityQueue<edge>();
    	PriorityQueue<vertex> available = new PriorityQueue<vertex>();
    	HashSet<edge> edgesUsed = new HashSet<edge>();
    	int prunedsum = 0;
    	//TODO piazza thing where you can intensity first
    	for(vertex[] row: vertices) {
    		for(vertex v: row) {
    			available.add(v);
    		}
    	}
    	vertex vert = available.peek();
    	available.remove();
    	vert.included = true;
    	while(!available.isEmpty()) {
    	
	    	//update key values and make new priority queue based on key values
	    	if((vert.rightEdge!= null) && !vert.right.included) {
	    		vert.right.key = vert.key + vert.rightEdge.weight;
	    		nextup.add(vert.rightEdge);
	    	}
	    	if((vert.leftEdge!= null) && !vert.left.included) {
	    		vert.left.key = vert.key + vert.leftEdge.weight;
	    		nextup.add(vert.leftEdge);
	    	}
	    	if((vert.upEdge!= null) && !vert.up.included) {
	    		vert.up.key = vert.key + vert.upEdge.weight;
	    		nextup.add(vert.upEdge);
	    	}
	    	if((vert.downEdge!= null) && !vert.down.included) {
	    		vert.down.key = vert.key + vert.downEdge.weight;
	    		nextup.add(vert.downEdge);
	    	}
	    	// get smallest edge with one node in and one node out
	    	edge taken = nextup.peek();
	    	nextup.remove();
	    	//if both in already, skip
	    	if(taken.start.included && taken.end.included) {
	    		taken = nextup.peek();
	    		nextup.remove();
	    	}
	    	if(taken.start.included && !taken.end.included) {
	    		vert = taken.end;
	    		available.remove(taken.end);
	    		taken.end.included = true;
	    		edgesUsed.add(taken);
	    	}
	    	if(taken.end.included && !taken.start.included) {
	    		available.remove(taken.start);
	    		vert = taken.start;
	    		taken.start.included = true;
	    		edgesUsed.add(taken);
	    	}
    	
    	}
    	//get total weight of pruned graph
    	for(edge e: edgesUsed) {
    		prunedsum += e.weight;
    	}
        return prunedsum;
    }
}
class vertex implements Comparable<vertex>{
	public int data;
	public int key;
	public int parent = 0;
	public boolean included;
	public int x = -1; 
	public int y = -1;
	public vertex right = null;
	public vertex left = null;
	public vertex down = null;
	public vertex up = null;
	public edge rightEdge = null;
	public edge downEdge = null;
	public edge leftEdge = null;
	public edge upEdge = null;
	
	vertex(int data, int x, int y ){			
		this.data = data;
		this.key = 99999999;
		this.included = false;
		this.x = x;
		this.y = y;
	}
	public boolean equals(vertex v2) {
		//also not necessaryF
		if((this.x == v2.x)  && (this.y == v2.y)) {
			if(v2.data == this.data) {
			return true;
			}
			else return false;
		}
		else return false;
	}
	@Override
	public int compareTo(vertex v) {
		if(this.key <= v.key) {
			return -1;
		}
		else if(this.key > v.key){
			return 1;
		}
		return 0;
	}	
}


class edge implements Comparable<edge>{
vertex start;
vertex end;
public int weight;

	edge(vertex start, vertex end, int weight){
		this.start = start;
		this.end = end;
		this.weight = weight;
	}
	
	public boolean equals(edge e) {
		//not necessary
		if((this.start.x== e.start.x)  && (this.start.y == e.start.y) && (this.end.x == e.end.x) && (this.end.y == e.end.y)) {
			return true;
		}
		else return false;
	}

	@Override
	public int compareTo(edge arg) {
		// used for sorting in priority queue
		if(this.weight <= arg.weight) {
			return -1;
		}
		else if(this.weight > arg.weight){
			return 1;
		}
		return 0;
	}
}