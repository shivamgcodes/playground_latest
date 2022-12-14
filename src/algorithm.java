import java.util.ArrayList;

public class algorithm {


    // had to copy the lps , as i failed in meeting time complexities
    public static int[] lps(String string) {
        // 0 1 2 3 4 5 6
        //   a b c a b d
        //   0 0 0 1 2 0 -> expected lps

        int i = 1;
        int[] lps = new int[string.length()];
        int index = 0;
        lps[0] = 0;
        while (i < string.length()) {
            if (string.charAt(i) == string.charAt(index)) {
                index++; // i just cant understand this line , is this because we areconsidering
                // 1 based indexing , imo we cant have 0 based indexing here bcoz then there will b
                // be no diff between the two cases of 0 and 1 above , like in the above case when we
                // land at 0 we check a, when we land at 1 we check b , but in 0 based indexing there
                // will be no such difference

                lps[i] = index;
                i++;


            } else {
                if (index == 0) {
                    lps[i] = 0;
                    i++;
                } else {
                    index = lps[index - 1]; // eg for pattern abcabdabcabc when i = length -2 and index = 4

                }
            }
        }
        return lps;
    }

    public int longestCommonSubsequence(String text1, String text2) {
        //dp implementation
        int m = text1.length();
        int n = text2.length();

        int[][] dparr = new int[m + 1][n + 1];

        int cur = 0;

        for (int i = 0; i <= m; i++) {
            for (int j = cur; j <= n; j++) {
                if (i == 0 || j == 0) {
                    dparr[i][j] = 0;
                } else if (text1.charAt(i - 1) == text2.charAt(j - 1)) {

                    dparr[i][j] = dparr[i - 1][j - 1] + 1;

                } else {
                    dparr[i][j] = Math.max(dparr[i - 1][j], dparr[i][j - 1]);
                }
            }
        }
        return dparr[m][n];
    }


    public static void kmp(String target, String pattern, int[] lsparray) {

        // finds the number of times the pattern occurs in the target

        int index = 0; // index on pattern
        ArrayList<Integer> indexofpatternfound = new ArrayList<>();

        for (int i = 0; i < target.length(); i++) {
            if (target.charAt(i) == pattern.charAt(index)) {
                index++;
                if (index == pattern.length()) {
                    indexofpatternfound.add(i - pattern.length());
                    index = lsparray[index - 1];
                }
            } else {
                if (index != 0) {
                    index = lsparray[index - 1];
                    i--;
                }
            }
        }
    }

    public static ArrayList<Integer> seive_of_eratosthenes(int n) {
//returns a list of primes


        // n is the max number upto which u want the seive to work , i will be putting booleans as markers
        // false ones are prime , ture ones are composite
        boolean[] bool = new boolean[n + 1];
        bool[0] = true;
        bool[1] = true;
        bool[2] = false;
        for (int i = 2; i < n + 1; i++) {
            if (!bool[i]) {
                for (int j = i * i; j < n + 1; j += i) {
                    bool[j] = true;
                }
            }

        }
        ArrayList<Integer> ar = new ArrayList<>();
        for (int i = 0; i < bool.length; i++) {
            if (!bool[i]) {
                ar.add(i);
            }
        }
        return ar;
    }
    // for smallest prime factor using eratosthenes - instead of making a boolean array make a integer array and then whenev u visit it for the first
    //time (check whether it is 0 or not ) , judt set the index as the i for which the array has been visited

    private int kadane(int[] arr, int arraylength) {

        int sum = 0;
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            sum = sum + arr[i];
            if (sum < 0) {
                sum = 0;
            }
            if (sum > max) {
                max = sum;
            }
        }


        return max;
    }

    private static int[] kadane(int[] arr) { // returns an array of sixe containing the start and end of the required subarray

        int sum = 0;
        int max = 0;
        int start = 0;
        int end = 0;
        int curstart = 0;
        int[] res = new int[2]; //res [0] = start , res[1] = end
        for (int i = 0; i < arr.length; i++) {
            sum = sum + arr[i];
            if (sum < 0) {
                sum = 0;
                curstart = i + 1;
            }
            if (sum > max) {
                max = sum;
                end = i;
                start = curstart;
            }
        }

        res[0] = start;
        res[1] = end;

        return res;
    }

}

