import java.lang.reflect.Array;
import java.util.*;

public class Graph {

    public static int[][] printAdjacency(int n, int m, int[][] edges) {
        // Write your code here.
        int[][] adjlist = new int[n][];
        ArrayList<Integer>[] arraylist = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            arraylist[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            arraylist[edges[i][0]].add(edges[i][1]);
            arraylist[edges[i][1]].add(edges[i][0]);
        }

        for (int i = 0; i < n; i++) {
            int temp[] = new int[arraylist[i].size() + 1];
            temp[0] = i;
            arraylist[i].sort(Comparator.naturalOrder());
            for (int j = 0; j < arraylist[i].size(); j++) {
                temp[j + 1] = arraylist[i].get(j);
            }

            adjlist[i] = temp;
        }

        return adjlist;
    }

    public static ArrayList<Integer> BFS(int vertex, int edges[][]) {
        // WRITE YOUR CODE HERE
        //ArrayList<Integer> bfs = new ArrayList<>();

        //make a visitd array - make a for loop on the array , then for every visited node , visit the first node in
        // queue - so make a while loop for the queue - also make an arraylist and keep pushing the nodes in that
        // how to get all connections for a node -> just traverse the complete length of edges[node][j]
        int[][] adjlist = printAdjacency(vertex, edges.length, edges);

        boolean[] visted = new boolean[vertex];
        ArrayList<Integer> queue = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < vertex; i++) {
            if (!visted[i]) {
                queue.add(i);
                visted[i] = true;

                while (index != queue.size()) {
                    int cur = queue.get(index);
                    for (int j = 0; j < adjlist[cur].length; j++) {

                        if (!visted[adjlist[cur][j]]) {
                            queue.add(adjlist[cur][j]);
                            visted[adjlist[cur][j]] = true;
                        }

                    }
                    index++;

                }

            }
        }
        return queue;
    }



    public ArrayList<Integer> dfs(boolean[] visited, ArrayList<ArrayList<Integer>> adj, ArrayList<Integer> dfslist, int cur ) {
        //visited,adjlist , keep returning the latest dfs
        visited[cur] = true;
        dfslist.add(cur);
        for (int i = 0; i < adj.get(cur).size(); i++) {

            if (!visited[adj.get(cur).get(i)]) {// start dfss

                dfslist = dfs(visited, adj, dfslist, adj.get(cur).get(i));
            }
        }


        return dfslist;
    }

    public ArrayList<Integer> dfsOfGraph(int V, ArrayList<ArrayList<Integer>> adj) {
        // Function to return a list containing the DFS traversal of the graph.
        boolean[] visited = new boolean[V];
        ArrayList<Integer> dfslist = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                dfslist = dfs(visited, adj, dfslist, i);
            }
        }

        return dfslist;


    }

    public boolean undirected_cycle_detection_dfs_helper(boolean[] visited, ArrayList<ArrayList<Integer>> adj, int cur, HashMap<Integer, Integer> sonparent) {
        visited[cur] = true;

        // cur is parent and
        // adj.get(cur).get(i) is the child

        for (int i = 0; i < adj.get(cur).size(); i++) {

            if (visited[adj.get(cur).get(i)]) {
                if (sonparent.get(cur) != null && !Objects.equals(sonparent.get(cur), adj.get(cur).get(i))) {
                    if(sonparent.get(adj.get(cur).get(i))== null){return true;}
                    if ( sonparent.get(adj.get(cur).get(i)) != cur) {

                        return true;         // true means cycle is present
                    }
                }
            }

            if (!visited[adj.get(cur).get(i)]) {
                // start dfss
                sonparent.put(adj.get(cur).get(i), cur);

                if (undirected_cycle_detection_dfs_helper(visited, adj, adj.get(cur).get(i), sonparent)) {

                    return true;
                }
            }
        }


        return false;
    }

    public boolean undirected_cycle_detection_dfs(int V, ArrayList<ArrayList<Integer>> adj) {

        //will we have to keep track of parent if we know that  a graph is completely connected



        boolean[] visited = new boolean[V];

        HashMap<Integer, Integer> sonparent = new HashMap<>();
        boolean result = false;
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                boolean b = undirected_cycle_detection_dfs_helper(visited, adj, i, sonparent);
                if (b) {
                    return b;
                }
            }
        }
        // true means cycle is present

        return false;


    }

}


