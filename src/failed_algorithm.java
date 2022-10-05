public class failed_algorithm {
    public static int[] lps_stupid(String string) {

        // longest proper suffix
        int[] arr = new int[string.length()];
        int index = 0;

        for (int i = 1; i < string.length(); i++) {
            boolean b = true;
            index = 0;
            int j = i;
            while (b && j < string.length()) {
                if (string.charAt(j) == string.charAt(index)) {
                    if (index > arr[j]) {
                        arr[j] = index;
                    }
                    j++;
                    index++;

                } else {
                    b = false;
                    index = 0;
                }
            }
        }
        return arr;

    }
    //TLE for the above one
}
