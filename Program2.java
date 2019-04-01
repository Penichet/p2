import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;
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
	static LinkedHashSet<edge> edgeset;
	static vertex[][] vertices;
	
    public int constructIntensityGraph(int[][] image){
    	//LinkedHashSet<edge> edgeset = new LinkedHashSet<edge>();
    	//TreeMap<int[], Integer> vertices = new TreeMap<int[], Integer>();
    	int edgesum = 0;
    	int ylen = image.length;
    	int xlen = image[0].length;
    	vertices = new vertex[ylen][xlen];
    	edgeset = new LinkedHashSet<edge>();
    	for(int y = 0; y < image.length; y++) {
    		for(int x = 0; x< image[y].length; x++) {
    			//add vertex to set
    			vertex temp = new vertex(image[y][x], x,y);
    			vertices[y][x]=temp;
    		}
    	}
    	System.out.println("vertices: " + vertices.length*vertices[0].length);
    	for(vertex[] row: vertices) {
    		for(vertex v:row) {
    			if(v.x != (xlen -1)) {
    				vertex right = vertices[v.y][v.x+1];
    				v.setRight(right);
    				edge temp = new edge(v, right, Math.abs(v.data - right.data));
    				edgeset.add(temp);
    				right.leftEdge = temp;
    				v.rightEdge = temp;
        		}
        		if(v.y != (ylen -1)) {
        			vertex down = vertices[v.y+1][v.x];
        			v.setDown(down);
        			edge temp = new edge(v, down, Math.abs(v.data - down.data));
        			edgeset.add(temp);
        			v.downEdge = temp;
        			down.upEdge = temp;
        		}
        		if(v.y != 0) {
        			vertex up = vertices[v.y-1][v.x];
    				v.setUp(up);
        		}
        		if(v.x != 0) {
        			vertex left = vertices[v.y][v.x-1];
    				v.setLeft(left);
        		}
    		}
    		
    	}
    	System.out.println("edges: " + edgeset.size());
    	for(edge e: edgeset) {
    		edgesum += e.weight;
    	}
    	return edgesum;
    	//return sum;
    }

    public int constructPrunedGraph(int[][] image){
    	PriorityQueue<edge> nextup = new PriorityQueue<edge>();
    	PriorityQueue<vertex> available = new PriorityQueue<vertex>();
    	HashSet<vertex> discovered = new HashSet<vertex>();
    	HashSet<edge> edgesUsed = new HashSet<edge>();
    	int prunedsum = 0;
    	//set key to zero to begin
    	vertices[0][0].key = 0;
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
    	// get vertex with smallest 
    	edge taken = nextup.peek();
    	nextup.remove();
    	edgesUsed.add(taken);
    	if(taken.start.included && taken.end.included) {
    		taken = nextup.peek();
    		nextup.remove();
    		edgesUsed.add(taken);
    	}
    	else if(taken.start.included) {
    		vert = taken.end;
    		available.remove(taken.end);
    		taken.end.included = true;
    		prunedsum += taken.weight;
    	}
    	else if(taken.end.included) {
    		available.remove(taken.start);
    		vert = taken.start;
    		taken.start.included = true;
    		prunedsum += taken.weight;
    	}
    	
    	}

        return prunedsum;
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
class vertex implements Comparable<vertex>{
	public int data;
	public int key;
	public int parent = 0;
	public boolean included;
	public vertex right = null;
	public vertex left = null;
	public vertex down = null;
	public vertex up = null;
	public int x = -1; 
	public int y = -1;
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
	public void setParent(int parent) {
		this.parent = parent;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public boolean equals(vertex v2) {
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
	public vertex getRight() {
		return right;
	}
	public void setRight(vertex right) {
		this.right = right;
	}
	public vertex getLeft() {
		return left;
	}
	public void setLeft(vertex left) {
		this.left = left;
	}
	public vertex getDown() {
		return down;
	}
	public void setDown(vertex down) {
		this.down = down;
	}
	public vertex getUp() {
		return up;
	}
	public void setUp(vertex up) {
		this.up = up;
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
		if((this.start.x== e.start.x)  && (this.start.y == e.start.y) && (this.end.x == e.end.x) && (this.end.y == e.end.y)) {
			return true;
		}
		else return false;
	}
//	public boolean contains(edge search) {
//		for(edge e: Program2.edgeset) {
//			if(search.equals(e)) {
//				return true;
//			}
//		}
//		return false;
//	}


	@Override
	public int compareTo(edge arg) {
		if(this.weight <= arg.weight) {
			return -1;
		}
		else if(this.weight > arg.weight){
			return 1;
		}
		return 0;
	}
}
