import java.util.HashMap;

public class SequenceAlignment {
    private String modifiedString1;
    private String modifiedString2;
    private HashMap<String, Integer> penaltyMap;
    private int gapValue;

    public SequenceAlignment(HashMap<String, Integer> penaltyMap, int gapValue) {
        this.penaltyMap = penaltyMap;
        this.gapValue = gapValue;
    }

    public String getModifiedString1() {
        return modifiedString1;
    }

    public void setModifiedString1(String modifiedString1) {
        this.modifiedString1 = modifiedString1;
    }

    public String getModifiedString2() {
        return modifiedString2;
    }

    public void setModifiedString2(String modifiedString2) {
        this.modifiedString2 = modifiedString2;
    }

    public String generateString(String seq, int[] values) {
        StringBuilder generatedString = new StringBuilder(seq);

        for (int i = 0; i < values.length; i++) {
            char[] chArray = generatedString.toString().toCharArray();
            generatedString.insert(values[i] + 1, chArray, 0, chArray.length);
        }

        return generatedString.toString();
    }

    public int[][] findMinCostMatrix(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        int[][] costMatrix = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            costMatrix[0][i] = i * gapValue;
        }

        for (int i = 0; i <= n; i++) {
            costMatrix[i][0] = i * gapValue;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                    String penaltyStr = s1.charAt(i - 1) + "" + s2.charAt(j - 1);
                    costMatrix[i][j] = Math.min(
                            penaltyMap.get(penaltyStr.toLowerCase()) + costMatrix[i - 1][j - 1], // replacing
                            Math.min(gapValue + costMatrix[i - 1][j], gapValue + costMatrix[i][j - 1]) // inserting and deleting
                    );
                }
            }
        }

        return costMatrix;
    }

    public void constructModifiedSeq(String s1, String s2, int[][] costMatrix) {
        int m = s1.length();
        int n = s2.length();
        int i = m;
        int j = n;

        StringBuilder modFirstString = new StringBuilder();
        StringBuilder modSecondString = new StringBuilder();

        while (!(i == 0 || j == 0)) {
            String penaltyStr = s1.charAt(i - 1) + "" + s2.charAt(j - 1);
            int penaltyValue = penaltyMap.get(penaltyStr.toLowerCase());
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                modFirstString.append(s1.charAt(i - 1));
                modSecondString.append(s2.charAt(j - 1));
                i--;
                j--;
            } else if (costMatrix[i - 1][j - 1] + penaltyValue == costMatrix[i][j]) {
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

        setModifiedString1(modFirstString.reverse().toString());
        setModifiedString2(modSecondString.reverse().toString());
    }

    public int[][] memoryEfficientAlignment(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] costMatrix = new int[m + 1][2];

        for (int i = 0; i <= m; i++) {
            costMatrix[i][0] = i * gapValue;
        }

        for (int i = 1; i <= n; i++) {
            costMatrix[0][1] = i * gapValue;
            for (int j = 1; j <= m; j++) {
                String penaltyStr = s1.charAt(i - 1) + "" + s2.charAt(j - 1);
                costMatrix[i][j] = Math.min(
                        penaltyMap.get(penaltyStr.toLowerCase()) + costMatrix[i - 1][j - 1], // replacing
                        Math.min(gapValue + costMatrix[i - 1][j], gapValue + costMatrix[i][j - 1]) // inserting and deleting
                );
            }
        }

        for (int i = 0; i <= m; i++) {
            costMatrix[i][0] = costMatrix[i][1];
        }

        return costMatrix;
    }
}
