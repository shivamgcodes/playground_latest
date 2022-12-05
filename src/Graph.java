import java.lang.reflect.Array;
import java.util.*;

public class Graph {

    public int[][] printAdjacency(int n, int m, int[][] edges) {

        // n - > number of nodes
        // m - > number of edges
        // adjacence list of type - for node 0 - the list is 1 ,4 5, 6 and not 0 , 1 , 4 ,5,6
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
            int temp[] = new int[arraylist[i].size()];
            //temp[0] = i;

            arraylist[i].sort(Comparator.naturalOrder());
            for (int j = 0; j < arraylist[i].size(); j++) {
                temp[j] = arraylist[i].get(j);
            }

            adjlist[i] = temp;
        }

        return adjlist;
    }

    public ArrayList<Integer> BFS(int vertex, int edges[][]) {

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

    public ArrayList<Integer> dfs(boolean[] visited, ArrayList<ArrayList<Integer>> adj, ArrayList<Integer> dfslist, int cur) {
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
                    if (sonparent.get(adj.get(cur).get(i)) == null) {
                        return true;
                    }
                    if (sonparent.get(adj.get(cur).get(i)) != cur) {

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

        //will we have to keep track of parent if we know that  a graph is completely connected ???

        boolean[] visited = new boolean[V];
        HashMap<Integer, Integer> sonparent = new HashMap<>();

        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                boolean b = undirected_cycle_detection_dfs_helper(visited, adj, i, sonparent);
                if (b) {
                    return true;
                }
            }
        }
        // true means cycle is present

        return false;


    }

    private boolean directed_cycle_detection_dfs_helper(boolean[] visited, ArrayList<ArrayList<Integer>> adj,
                                                        int cur, HashMap<Integer, Integer> sonparent, boolean[] dfsvisited) {

        visited[cur] = true;
        dfsvisited[cur] = true;

        // cur is parent and
        // adj.get(cur).get(i) is the child

        for (int i = 0; i < adj.get(cur).size(); i++) {

            if (dfsvisited[adj.get(cur).get(i)]) {
                // System.out.println("cur" + cur + " i " + i);
                return true;
            }


            if (!visited[adj.get(cur).get(i)]) {
                // start dfss
                sonparent.put(adj.get(cur).get(i), cur);

                if (directed_cycle_detection_dfs_helper(visited, adj, adj.get(cur).get(i), sonparent, dfsvisited)) {

                    return true;
                }
            }

        }
        dfsvisited[cur] = false;
        return false;
    }

    public boolean directed_cycle_detection_dfs(int V, ArrayList<ArrayList<Integer>> adjlist) {

        boolean[] visited = new boolean[V];
        HashMap<Integer, Integer> sonparent = new HashMap<>();
        boolean[] dfsvisited = new boolean[V];

        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                boolean b = directed_cycle_detection_dfs_helper(visited, adjlist, i, sonparent, dfsvisited);
                if (b) return true;
            }
        }


        return false;
    }

    public int[] topoSort(int V, ArrayList<ArrayList<Integer>> adj) {
        // add your code here
        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[V];
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                dfs_topological_sort_helper(i, stack, adj, visited);

            }
        }

        int[] topo_sort = new int[V];
        int index = 0;

        while (!stack.isEmpty()) {
            topo_sort[index] = stack.pop();
            index++;

        }


        return topo_sort;
    }

    private void dfs_topological_sort_helper(int cur, Stack<Integer> stack, ArrayList<ArrayList<Integer>> adj, boolean[] visited) {
        visited[cur] = true;
        for (int i = 0; i < adj.get(cur).size(); i++) {

            if (!visited[adj.get(cur).get(i)]) {

                dfs_topological_sort_helper(adj.get(cur).get(i), stack, adj, visited);
            }


        }

        stack.push(cur);
    }

    // kahns algo is used to find topological sort using bfs algorithm - we cannot use just straight up bfs because
    // then it will give wrong anser for such graphs

    // a - > b - > c   if we use straght up bfs then the sort will be a , b , d , c which is wrong
    //  \          /   the edge points from a to d and then from d to c
    //   d-------
    int[] Kahns_algorithm(int V, ArrayList<ArrayList<Integer>> adj) {
        // add your code here
        // adj(1)  - > 2 , 34 ,6  - > means that vertex 1 has edges going out towards 2 , 34 , 6


        int[] indegree = new int[V];

        for (int i = 0; i < adj.size(); i++) {
            for (int j = 0; j < adj.get(i).size(); j++) {
                indegree[adj.get(i).get(j)]++;
            }
        }


        int[] arr = new int[V];
        int index = 0;
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < indegree.length; i++) {
            if (indegree[i] == 0) {
                arrayList.add(i);

            }
        }
        //for(int i = 0 ; i < indegree.length ; i++){
        //   System.out.print(i + "----->" + indegree[i] + " ");
        //}
        // System.out.println("");


        while (arrayList.size() > 0) {
            int cur = arrayList.get(arrayList.size() - 1); // idk why the index i used is arraylist.size - 1 ,i checked on gfg with 0 , it does not matter
            arrayList.remove(arrayList.size() - 1);// probably it was influence from the course teacher , but it works with 0 as well
            // and acc to my manalysis it will work with any arbitary index as well
            arr[index] = cur;
            index++;

            for (int j = 0; j < adj.get(cur).size(); j++) {
                indegree[adj.get(cur).get(j)]--;
                if (indegree[adj.get(cur).get(j)] == 0) {
                    arrayList.add(adj.get(cur).get(j));

                }

            }
        }
        int[] res = new int[arr.length];

        //   for(int i = 0 ; i < arr.length ; i ++ ){System.out.print(arr[i] + " ");
        //        res[res.length - 1 - i] = arr[i]    ;    }


        return arr;
    }

    public boolean cycle_detection_directed_graph_using_bfs(int V, ArrayList<ArrayList<Integer>> adj) {

        // returns true if a cycle is present
        // same as lahns algortithm , the only difference is that in this algorithm if the length of the topological sort != vertices - then it is an invalid topological sort
        // ND THE REASON for that is the presence of a cycle
        int[] indegree = new int[V];

        for (int i = 0; i < adj.size(); i++) {
            for (int j = 0; j < adj.get(i).size(); j++) {
                indegree[adj.get(i).get(j)]++;
            }
        }


        int count = 0;

        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < indegree.length; i++) {
            if (indegree[i] == 0) {
                arrayList.add(i);

            }
        }


        while (arrayList.size() > 0) {
            int cur = arrayList.get(arrayList.size() - 1);
            arrayList.remove(arrayList.size() - 1);
            count++; //to keep track of the length of the topological sort

            for (int j = 0; j < adj.get(cur).size(); j++) {
                indegree[adj.get(cur).get(j)]--;
                if (indegree[adj.get(cur).get(j)] == 0) {
                    arrayList.add(adj.get(cur).get(j));

                }

            }
        }


        return (count != V); // check and returning true if there is a cycle present
    }

    //shortest path in undirected graph having same weight can be found by using bfs
    public int[] shortestPath_to_all_nodes_using_bfs(int[][] edges, int n, int m, int src) {
        // my implementation of lec 93
        // returns an array containing shortest possible path length to all nodes
        int[][] adj = printAdjacency(n, m, edges);

        ArrayList<Integer> ar = new ArrayList<>();
        boolean[] visited = new boolean[n];
        int[] steps = new int[n];
        steps[src] = 0;
        int count = 1;
        visited[src] = true;

        for (int i = 0; i < adj[src].length; i++) {
            ar.add(adj[src][i]);

            visited[adj[src][i]] = true;


        }

        while (ar.size() > 0) {
            int size = ar.size();

            for (int j = 0; j < size; j++) {
                int k = ar.get(0);
                ar.remove(0);
                steps[k] = count;
                for (int i = 0; i < adj[k].length; i++) {
                    if (!visited[adj[k][i]]) {
                        ar.add(adj[k][i]);
                        visited[adj[k][i]] = true;

                    }
                }
            }
            count++;
        }
        return steps;

    }

    public ArrayList<ArrayList<edge>> adjlist_of_undirected_using_edges(int no_of_vertices, int no_of_edges, int[][] edgearray) {
        // was made for prims algorithm
// edges are used here for storing weight also in the same package in priority queue for prims algorithm
        //edgearray[i][0] to edgearray[i][1] with a distance of edgearray[i][2]
        ArrayList<ArrayList<edge>> arrlist = new ArrayList<>();
        for (int i = 0; i <= no_of_vertices; i++) {
            arrlist.add(new ArrayList<edge>());
        }
        for (int i = 0; i < edgearray.length; i++) {

            arrlist.get(edgearray[i][0]).add(new edge(edgearray[i][1], edgearray[i][2], edgearray[i][0]));
            // uncomment the below line if all edges are given twice - one from node to vertex and then once after reversing roles
            arrlist.get(edgearray[i][1]).add(new edge(edgearray[i][0], edgearray[i][2], edgearray[i][1]));
        }

        return arrlist; // this arr list is completly unsorted

    }

    public ArrayList<ArrayList<Integer>> prims_mst(int n, int m, ArrayList<ArrayList<edge>> adjlist) {
//whenever , we are making a MST , there is only one possible parent of any integer , which is the integer which includes the son in the MST ,
// as any node will get included(added to MST) only once , none of the nodes will have any more than a single node
        int count_of_mst_nodes = 1;
        int[] edgeweight = new int[n + 1];
        int[] parentarray = new int[n + 1];
        boolean[] included_in_mst = new boolean[n + 1];
        PriorityQueue<edge> pq = new PriorityQueue<>();
        int cur = 1;
        included_in_mst[1] = true;
        while (count_of_mst_nodes != n) {

            for (int i = 0; i < adjlist.get(cur).size(); i++) {
                if (!included_in_mst[adjlist.get(cur).get(i).vertex]) {
                    if (cur == adjlist.get(cur).get(i).vertex) continue;
                    pq.add(adjlist.get(cur).get(i));
                }
            }

            edge least_weighted_edge = pq.poll();
            int a = 0;
            if (least_weighted_edge == null) {
                System.out.println("L debugger " + pq.size() + "  " + count_of_mst_nodes + "  ");
                a = 10;
            }

            if (!included_in_mst[least_weighted_edge.vertex]) {

                int parent = least_weighted_edge.parent;
                int weight = least_weighted_edge.weight;
                int vertex = least_weighted_edge.vertex;

                parentarray[vertex] = parent;
                edgeweight[vertex] = weight;
                included_in_mst[vertex] = true;
                count_of_mst_nodes++;
                cur = vertex;
            }

        }

        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        for (int i = 2; i < n + 1; i++) {
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(parentarray[i]);
            temp.add(i);
            temp.add(edgeweight[i]);
            result.add(temp);
        }
        return result;
    }

    private static void checkbridge(ArrayList<ArrayList<Integer>> adj, int[] disc, int[] low, int[] parent, boolean[] visited, int curdisc, int node, int c, int d, ArrayList<ArrayList<Integer>> bridges) {
        visited[node] = true;
        low[node] = Math.min(low[node], curdisc);
        disc[node] = curdisc;
        curdisc++;
        int i;
        boolean backtrack = true;
        for (i = 0; i < adj.get(node).size(); i++) {

            if (!visited[adj.get(node).get(i)]) {

                backtrack = false;
                parent[adj.get(node).get(i)] = node;
                checkbridge(adj, disc, low, parent, visited, curdisc, adj.get(node).get(i), c, d, bridges);
                if (low[adj.get(node).get(i)] > disc[node]) {
                    ArrayList<Integer> ar = new ArrayList<>();
                    ar.add(node);
                    ar.add(adj.get(node).get(i));
                    bridges.add(ar);
                }
            }
        }
        if (backtrack) {
            for (int j = 0; j < adj.get(node).size(); j++) {
                if (parent[node] != adj.get(node).get(j)) {
                    low[node] = Math.min(low[node], disc[adj.get(node).get(j)]);
                }
            }
        } else {
            for (int j = 0; j < adj.get(node).size(); j++) {
                if (parent[node] != adj.get(node).get(j)) {
                    low[node] = Math.min(low[node], low[adj.get(node).get(j)]);
                }
            }
        }
    }

    public ArrayList<ArrayList<Integer>> ReturnBridges(int V, ArrayList<ArrayList<Integer>> adj, int c, int d) {
        // in bridgse arraylist , every arraylist is of size - 2 and has both the vertices of the bridge
        // also in this algorithm i am counting discorvery a little differently
        // for a graph have edges - > 1-2, 2-3 , 3-4, 1-5 . and lets say we start dfs from 1 and then go to 2 , then 3 and then 4 , then complete backtracking to 1 and
        // then we visit 5 - > the discovery array will look like this- > 0 , 1 , 2 , 3 , 4 , 1 instead of - > 0, 1, 2, 3, 4, 5 , but it still works as this is a dfs
        //also the other way (the traditional one as shown in lectures) - will need a static variable in the class or an Integer object type variable which is uselessly crumbersoe
        int[] disc = new int[V];
        int[] low = new int[V];
        for (int i = 0; i < V; i++) {
            low[i] = Integer.MAX_VALUE;
        }
        boolean[] visited = new boolean[V];
        int[] parent = new int[V];
        ArrayList<ArrayList<Integer>> bridges = new ArrayList<>();

        for (int node = 0; node < V; node++) {
            checkbridge(adj, disc, low, parent, visited, 0, node, c, d, bridges);
        }
        return bridges;
    }

    private static void check_articulation_point(ArrayList<ArrayList<Integer>> adj, int[] disc, int[] low, int[] parent, boolean[] visited, int curdisc, int node, boolean[] articulation_point_boolean) {
        visited[node] = true;
        low[node] = Math.min(low[node], curdisc);
        disc[node] = curdisc;
        curdisc++;
        int i;
        int child = 0;
        boolean backtrack = true;
        for (i = 0; i < adj.get(node).size(); i++) {

            if (!visited[adj.get(node).get(i)]) {

                backtrack = false;
                parent[adj.get(node).get(i)] = node;
                check_articulation_point(adj, disc, low, parent, visited, curdisc, adj.get(node).get(i), articulation_point_boolean);
                if (low[adj.get(node).get(i)] >= disc[node]) {
                    articulation_point_boolean[node] = true;

                }

                child++;
            }
        }


        if (backtrack) {
            for (int j = 0; j < adj.get(node).size(); j++) {
                if (parent[node] != adj.get(node).get(j)) {
                    low[node] = Math.min(low[node], disc[adj.get(node).get(j)]);
                }
            }
        } else {
            for (int j = 0; j < adj.get(node).size(); j++) {
                if (parent[node] != adj.get(node).get(j)) {
                    low[node] = Math.min(low[node], low[adj.get(node).get(j)]);
                }
            }
        }
        if (parent[node] == -1) {
            if (child >= 2) {
                articulation_point_boolean[node] = true;
            } else {
                articulation_point_boolean[node] = false;
            }
        }
    }

    public ArrayList<Integer> articulationPoints(int V, ArrayList<ArrayList<Integer>> adj) {
        int[] disc = new int[V];
        int[] low = new int[V];
        int[] parent = new int[V];
        for (int i = 0; i < V; i++) {
            low[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }
        boolean[] visited = new boolean[V];

        boolean[] articulation_point_boolean = new boolean[V];


        for (int node = 0; node < V; node++) {
            check_articulation_point(adj, disc, low, parent, visited, 0, node, articulation_point_boolean);
        }

        ArrayList<Integer> ar = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            if (articulation_point_boolean[i]) {
                ar.add(i);
            }
        }


        if (ar.size() == 0) {
            ar.add(-1);
            return ar;
        }
        ar.sort(Comparator.naturalOrder());
        return ar;


    }

    static ArrayList<ArrayList<Integer>> reverse_graph_edges(ArrayList<ArrayList<Integer>> adj, int V) {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        ArrayList<Integer> temp = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            res.add(new ArrayList<>());
        }


        for (int i = 0; i < adj.size(); i++) {


            for (int j = 0; j < adj.get(i).size(); j++) {

                res.get(adj.get(i).get(j)).add(i);
            }

        }

        return res;
    }

    public int kosaraju(int V, ArrayList<ArrayList<Integer>> adj) {
        //code here


        int[] topological = topoSort(V, adj); // to go like the way we do in stack , go left to right
        ArrayList<ArrayList<Integer>> reversed_graph = reverse_graph_edges(adj, V);
        ArrayList<ArrayList<Integer>> scc = new ArrayList<>();// here every arraylist is a list of nodes of a scc - strongly connected component


        boolean[] visited = new boolean[V];
        for (int i = 0; i < V; i++) {
            if (!visited[topological[i]]) {
                ArrayList<Integer> temp = new ArrayList<>();
                //temp.add(i);
                dfs(visited, reversed_graph, temp, topological[i]);
                visited[topological[i]] = true;
                scc.add(temp);
            }

        }

        return scc.size();
    }
}

   




