import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryEfficientAlignment {
    private HashMap<String, Integer> penaltyMap;
    private int gapValue;
    String inputModSeq1;
    String inputModSeq2;
    List<int[]> P = new ArrayList<>();

    public MemoryEfficientAlignment() {
        HashMap<String, Integer> penaltyMap = new HashMap<>();
        penaltyMap.put("AA", 0);
        penaltyMap.put("AC", 110);
        penaltyMap.put("AG", 48);
        penaltyMap.put("AT", 94);
        penaltyMap.put("CA", 110);
        penaltyMap.put("CC", 0);
        penaltyMap.put("CG", 118);
        penaltyMap.put("CT", 48);
        penaltyMap.put("GA", 48);
        penaltyMap.put("GC", 118);
        penaltyMap.put("GG", 0);
        penaltyMap.put("GT", 110);
        penaltyMap.put("TA", 94);
        penaltyMap.put("TC", 48);
        penaltyMap.put("TG", 110);
        penaltyMap.put("TT", 0);
        this.penaltyMap = penaltyMap;
        this.gapValue = 30;
        inputModSeq1 = "";
        inputModSeq2 = "";
    }

    public int[][] findMinCostMatrix(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        int[][] costMatrix = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            costMatrix[i][0] = i * gapValue;
        }

        for (int i = 0; i <= n; i++) {
            costMatrix[0][i] = i * gapValue;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                String penaltyStr = s1.charAt(i - 1) + "" + s2.charAt(j - 1);
                costMatrix[i][j] = Math.min(
                        penaltyMap.get(penaltyStr) + costMatrix[i - 1][j - 1], // replacing
                        Math.min(gapValue + costMatrix[i - 1][j], gapValue + costMatrix[i][j - 1]) // inserting and deleting
                );
            }
        }

        return costMatrix;
    }

    public String constructModifiedSeq(String s1, String s2, int[][] costMatrix) {
        int m = s1.length();
        int n = s2.length();
        int i = m;
        int j = n;

        StringBuilder modFirstString = new StringBuilder();
        StringBuilder modSecondString = new StringBuilder();

        while (!(i == 0 || j == 0)) {
            String penaltyStr = s1.charAt(i - 1) + "" + s2.charAt(j - 1);
            int penaltyValue = penaltyMap.get(penaltyStr);
            if (costMatrix[i - 1][j - 1] + penaltyValue == costMatrix[i][j]) {
                modFirstString.append(s1.charAt(i - 1));
                modSecondString.append(s2.charAt(j - 1));
                j--;
                i--;
            } else if (costMatrix[i][j - 1] + gapValue == costMatrix[i][j]) {
                modFirstString.append('_');
                modSecondString.append(s2.charAt(j - 1));
                j--;
            } else if (costMatrix[i - 1][j] + gapValue == costMatrix[i][j]) {
                modFirstString.append(s1.charAt(i - 1));
                modSecondString.append('_');
                i--;
            }
        }

        while (i > 0) {
            modFirstString.append(s1.charAt(i - 1));
            modSecondString.append('_');
            i--;
        }

        while (j > 0) {
            modFirstString.append('_');
            modSecondString.append(s2.charAt(j - 1));
            j--;
        }
        String[] modifiedSequence = new String[2];
        modifiedSequence[0] = modFirstString.reverse().toString();
        modifiedSequence[1] = modSecondString.reverse().toString();

        return modifiedSequence[0] + " " + modifiedSequence[1];
    }

    public int[] memoryEfficientAlignmentForward(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] costMatrix = new int[2][m + 1];

        for (int i = 0; i <= m; i++) {
            costMatrix[0][i] = i * gapValue;
        }

        for (int i = 1; i <= n; i++) {
            costMatrix[1][0] = i * gapValue;
            for (int j = 1; j <= m; j++) {
                String penaltyStr = s1.charAt(j - 1) + "" + s2.charAt(i - 1);
                costMatrix[1][j] = Math.min(
                        penaltyMap.get(penaltyStr) + costMatrix[0][j - 1], // replacing
                        Math.min(gapValue + costMatrix[0][j - 1], gapValue + costMatrix[1][j]) // inserting and deleting
                );
            }

            for (int k = 0; k <= m; k++) {
                costMatrix[0][k] = costMatrix[1][k];
            }
        }

        return costMatrix[1];
    }

    public String divdeAndConquer(String X, String Y) {
        int a = 0;
        int b = X.length();
        int c = 0;
        int d = Y.length();

        if (X.length() < 10 || Y.length() < 10) {
            int[][] costMatrix = findMinCostMatrix(X, Y);
            return constructModifiedSeq(X, Y, costMatrix);
        } else {
            int mid = d / 2;
            String xRev = (new StringBuilder(X)).reverse().toString();
            String yRev = (new StringBuilder(Y)).reverse().toString();

            int[] prefixScore = memoryEfficientAlignmentForward(X, Y.substring(c, mid));
            int[] suffixScore = memoryEfficientAlignmentForward(xRev, yRev.substring(mid + 1, d));

            int posMax = 0;
            int vMax = prefixScore[0] + suffixScore[0];

            for (int i = a; i <= b; i++) {
                int score = prefixScore[i] + suffixScore[i];
                if (score < vMax) {
                    vMax = score;
                    posMax = i;
                }
            }
            
            P.add(new int[]{posMax, mid});

            String prefix = divdeAndConquer(X.substring(a, posMax), Y.substring(c, mid));
            String suffix = divdeAndConquer(X.substring(posMax + 1, b), Y.substring(mid + 1, d));

            return prefix + " " + suffix;
        }
    }

    public static void main(String[] args) {
        String[] strings = null;

        if(args.length>=1) {
            String inputFileName = args[0];
            File file = new File(inputFileName);
            try {
                strings = StringGenerator.generateStringsFromFile(file);
                MemoryEfficientAlignment memoryEfficientAlignment = new MemoryEfficientAlignment();
                String s = memoryEfficientAlignment.divdeAndConquer(strings[0], strings[1]);

                System.out.println(strings[0]);
                System.out.println(memoryEfficientAlignment.inputModSeq1);

                System.out.println(strings[1]);
                System.out.println(memoryEfficientAlignment.inputModSeq2);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please enter an input filename as an argument");
        }
    }
}
