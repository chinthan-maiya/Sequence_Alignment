import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryEfficientAlignment {
    private HashMap<String, Integer> penaltyMap;
    private int gapValue;
    StringBuilder inputModSeq1 = new StringBuilder();
    StringBuilder inputModSeq2 = new StringBuilder();
    List<int[]> P = new ArrayList<>();
    String[] modifiedSeq = new String[]{"", ""};

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

    public String[] constructModifiedSeq(String s1, String s2, int[][] costMatrix) {
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

        return modifiedSequence;
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

    public String[] divdeAndConquer(String X, String Y) {
        String Z = "";
        String W = "";
        String[] res = new String[2];

        if (X.length() == 0) {
            for (int i = 0; i < Y.length(); i++) {
                Z = Z + "_";
                W = W + Y.charAt(i);
            }
        } else if (Y.length() == 0) {
            for (int i = 0; i < X.length(); i++) {
                Z = Z + X.charAt(i);
                W = W + "_";
            }
        } else if (X.length() <= 2 || Y.length() <= 2) {
            int[][] costMatrix = findMinCostMatrix(X, Y);
            String[] result = constructModifiedSeq(X, Y, costMatrix);
            Z = result[0];
            W = result[1];
        } else {
            int xLen = X.length();
            int yLen = Y.length();
            int yMid = yLen / 2;

            String xRev = (new StringBuilder(X)).reverse().toString();
            String yRev = (new StringBuilder(Y)).reverse().toString();

            int[] prefixScore = memoryEfficientAlignmentForward(X, Y.substring(0, yMid));
            int[] suffixScore = memoryEfficientAlignmentForward(xRev, yRev.substring(yMid + 1, yLen));

            int xMid = 0;
            int vMin = prefixScore[0] + suffixScore[0];

            for (int i = 0; i <= X.length(); i++) {
                int score = prefixScore[i] + suffixScore[i];
                if (score < vMin) {
                    vMin = score;
                    xMid = i;
                }
            }

            P.add(new int[]{xMid, yMid});

            String[] prefix = divdeAndConquer(X.substring(0, xMid), Y.substring(0, yMid));
            String[] suffix = divdeAndConquer(X.substring(xMid + 1, xLen), Y.substring(yMid + 1, yLen));

            Z = prefix[0] + suffix[0];
            W = prefix[1] + suffix[1];

            inputModSeq1.append(Z);
            inputModSeq2.append(W);
        }

        res[0] = Z;
        res[1] = W;

        return res;
    }

    public static void main(String[] args) {
        String[] strings = null;

        if(args.length>=1) {
            String inputFileName = args[0];
            File file = new File(inputFileName);
            try {
                strings = StringGenerator.generateStringsFromFile(file);
                MemoryEfficientAlignment memoryEfficientAlignment = new MemoryEfficientAlignment();
                String[] s = memoryEfficientAlignment.divdeAndConquer(strings[0], strings[1]);

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
