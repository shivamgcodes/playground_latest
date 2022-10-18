import java.util.ArrayList;
import java.util.Stack;

class edge {
    int weight;
    int vertex;

    public edge(int vertex, int weight) {
        this.weight = weight;
        this.vertex = vertex;

    }
}

public class Weighted_Graph {

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
        //edge[i][0] to edge[i][1] with a distance of edge[i][2]
        //Code here
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
}

