import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;

public class trial {
    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] st ;
        st = br.readLine().split("\\s+");
        int n = Integer.parseInt(st[0]);
        int m = Integer.parseInt(st[1]);
        int [][] arr = new int[m][2];
        for(int i = 0  ;i < m ; i++){st = br.readLine().split("\\s+");
            arr[i][0] = Integer.parseInt(st[0]);
            arr[i][1] = Integer.parseInt(st[1]);

        }
        ArrayList bfs = Graph.BFS(n  ,arr);
        for(int j = 0 ;j < bfs.size() ; j++){System.out.println(bfs.get(j));}


    }

}
