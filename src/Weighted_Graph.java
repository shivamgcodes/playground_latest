import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

class edge implements Comparable<edge> {
    //primarily made for making it possible to have everything - vertex , parent and weight in the same package for priority queue and not making it anymore
    // complex by storing arraylists
    int weight;
    int vertex;
    int parent;

    public edge(int vertex, int weight) {
        this.weight = weight;
        this.vertex = vertex;

    }

    public edge(int vertex, int weight, int parent) {
        this.weight = weight;
        this.vertex = vertex;
        this.parent = parent;
    }

    @Override
    public int compareTo(edge o) {
        if (o.weight < this.weight) {
            return 1;
        } else {
            return -1;
        }
    }
}

class dijkstra_priority_queue_pair implements Comparable<dijkstra_priority_queue_pair> {
    int totalweight;
    int vertex;

    dijkstra_priority_queue_pair(int totalweight, int vertex) {
        this.totalweight = totalweight;
        this.vertex = vertex;
    }

    @Override
    public int compareTo(dijkstra_priority_queue_pair o) {
        if (o.totalweight < this.totalweight) {
            return 1;
        }
        if (this.totalweight < o.totalweight) {
            return -1;
        }
        return 0;
    }
}

public class Weighted_Graph {
    private static ArrayList<ArrayList<Integer>> from_arr_of_arr_to_arrlist_of_arrlist(int[][] array) {

        ArrayList<ArrayList<Integer>> arrayList = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            ArrayList<Integer> temp = new ArrayList<>();
            for (int j = 0; j < array[i].length; j++) {
                temp.add(array[i][j]);
            }
            arrayList.add(temp);
        }
        return arrayList;
    }

    private static int[][] from_arrlist_of_arrlist_to_arr_of_arr(ArrayList<ArrayList<Integer>> arrayList) {
        int[][] array = new int[arrayList.size()][];

        for (int i = 0; i < arrayList.size(); i++) {
            int[] temp = new int[arrayList.get(i).size()];
            for (int j = 0; j < arrayList.get(i).size(); j++) {
                temp[j] = arrayList.get(i).get(j);

            }
            array[i] = temp;
        }
        return array;
    }

    // DAG - directed acyclic graph, all methods are for weighted ones upto now
    public ArrayList<ArrayList<edge>> adjlist_of_DAG(int no_of_vertices, int no_of_edges, int[][] edges) {

        //edge[i][0] to edge[i][1] with a distance of edge[i][2]
        ArrayList<ArrayList<edge>> arrlist = new ArrayList<>();
        for (int i = 0; i < no_of_vertices; i++) {
            arrlist.add(new ArrayList<edge>());
        }
        for (int i = 0; i < edges.length; i++) {

            arrlist.get(edges[i][0]).add(new edge(edges[i][1], edges[i][2]));
        }

        return arrlist; // this arr list is completly unsorted

    }

    private void dfs_topological_sort_helper_DAG(int cur, Stack<Integer> stack, ArrayList<ArrayList<edge>> adjlist, boolean[] visited) {
        visited[cur] = true;

        for (int i = 0; i < adjlist.get(cur).size(); i++) {
            if (!visited[adjlist.get(cur).get(i).vertex]) {

                dfs_topological_sort_helper_DAG(adjlist.get(cur).get(i).vertex, stack, adjlist, visited);
            }
        }
        stack.push(cur);
    }

    public ArrayList<Integer> topo_sort_DAG(int V, ArrayList<ArrayList<edge>> adjlist) {
        //i chose to return a arraylist , as now i can use the same method for checking whether the graph has a cycle or not as if the
        //arraylist sizw != V , then the graph does have cycle , otherwise an int[] will have constant size

        boolean[] visited = new boolean[V];
        java.util.Stack<Integer> stack = new Stack<>();
        ArrayList<Integer> topological_sort = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                dfs_topological_sort_helper_DAG(i, stack, adjlist, visited);
            }
        }

        int[] temp_topo_sort = new int[V];

        while (!stack.isEmpty()) {

            topological_sort.add(topological_sort.size(), stack.pop());

        }


        return topological_sort;

    }

    public int[] shortestPath_in_DAG(int N, int M, int[][] edges) {

        /*explanation for the algo
         so the reason we need a topologolical sort in finding the shortest path in DAG is , because topo ensures that every edge goes
         left to right , but also that any node on right has its parent on left , which means that when we traverse an array left to
        right we already have the path / edge costs to that node if possible .   It also means that any node when reached will only
        have its shortest possible path in its index as  all the paths reaching it have already been explored - as this node ca not be
        reached from any of the nodes after it*/

        //edge[i][0] to edge[i][1] with a distance of edge[i][2]

        ArrayList<ArrayList<edge>> adjlist = adjlist_of_DAG(N, M, edges);


        ArrayList<Integer> topo = topo_sort_DAG(N, adjlist);

        // printing topo
        for (int t = 0; t < topo.size(); t++) {
            System.out.print(topo.get(t) + " , ");
        }
        System.out.println("");

        int src = 0;
        int[] cost = new int[N];
        for (int i = 0; i < N; i++) {
            cost[i] = Integer.MAX_VALUE;
        }
        cost[0] = 0;


        int index = 0;
        for (index = 0; index < topo.size(); index++) {
            if (topo.get(index) == 0) {
                break;
            } else {
                cost[topo.get(index)] = -1;
            }
        }
        for (int i = index; i < topo.size(); i++) { // we are checking the vertices of the vertex i

            for (int j = 0; j < adjlist.get(topo.get(i)).size(); j++) { // j'th vertex in the adjacency list of i
                int edge_weight = adjlist.get(topo.get(i)).get(j).weight;
                int vertex = adjlist.get(topo.get(i)).get(j).vertex;
                cost[vertex] = Math.min(cost[vertex], edge_weight + cost[topo.get(i)]);
            }
        }
        return cost; //expected one = 0 2 -1 8 10

    }

    static int[] dijkstra(int V, ArrayList<ArrayList<ArrayList<Integer>>> adj, int source) {
        //adj[i] is a list of lists containing two integers where the first integer of each list j denotes there is edge between i and j
        // , second integers corresponds to the weight of that  edge ,
        // at index 0 it is the vertex and at index 1 it is the edge weight
        boolean[] finalized = new boolean[V];
        int[] distance = new int[V];
        PriorityQueue<dijkstra_priority_queue_pair> pq = new PriorityQueue<>();
        pq.add(new dijkstra_priority_queue_pair(0, source));
        while (!pq.isEmpty()) {

            dijkstra_priority_queue_pair p = pq.poll();
            if (!finalized[p.vertex]) {
                finalized[p.vertex] = true;
                distance[p.vertex] = p.totalweight;

                for (int i = 0; i < adj.get(p.vertex).size(); i++) {
                    dijkstra_priority_queue_pair cur = new dijkstra_priority_queue_pair(adj.get(p.vertex).get(i).get(1) + distance[p.vertex]
                            , adj.get(p.vertex).get(i).get(0));
                    pq.add(cur);
                }
            }
        }

        return distance;
    }
    static int[] bellman_ford(int V, ArrayList<ArrayList<Integer>> edges, int S) {
        // Write your code here
        // assuming that no node has total distance < 100000000

        int max = 100000000;
        int[] dist = new int[V];
        for (int i = 0; i < V; i++) {
            dist[i] = 100000000;

        }
        dist[S] = 0;
        for (int i = 0; i < V; i++) {

            for(int j = 0 ; j < edges.size() ; j++){
                int start = edges.get(j).get(0);
                int end = edges.get(j).get(1);
                int weight = edges.get(j).get(2);
                dist[end] = Math.min(weight + dist[start] , dist[end]);
            }
        }
        boolean bool = false;
        for(int j = 0 ; j < edges.size() ; j++){
            int start = edges.get(j).get(0);
            int end = edges.get(j).get(1);
            int weight = edges.get(j).get(2);
            if(dist[start] + weight  < dist[end]){bool = true; break;}

        }
        if(bool){
            return new int[]{-1};
        }
        return dist;
    }
}

