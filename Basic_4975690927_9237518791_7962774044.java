import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Basic_4975690927_9237518791_7962774044 {

    private HashMap<String, Integer> penaltyMap;
    private int gapValue;

    public Basic_4975690927_9237518791_7962774044() {
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

    public static void main(String[] args) {
        String[] strings = null;
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();

        if(args.length>=1) {
            String inputFileName = args[0];
            File file = new File(inputFileName);
            try {
                strings = StringGenerator_4975690927_9237518791_7962774044.generateStringsFromFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }

            long startTime = System.currentTimeMillis();

            Basic_4975690927_9237518791_7962774044 sequenceAlignment = new Basic_4975690927_9237518791_7962774044();
            int[][] costMatrix = sequenceAlignment.findMinCostMatrix(strings[0], strings[1]);
            String[] modifiedSequence = sequenceAlignment.constructModifiedSeq(strings[0], strings[1], costMatrix);

            long stopTime = System.currentTimeMillis();
            double timeTaken = (stopTime - startTime) / 1000.0;

            long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            double actualMemUsed = (afterUsedMem - beforeUsedMem) / 1000;

            try {
                FileWriter fileWriter = new FileWriter("output.txt");
                int l1 = Math.min(modifiedSequence[0].length(), 50);
                int l2 = Math.min(modifiedSequence[1].length(), 50);
                int k1 = Math.max(0, modifiedSequence[0].length()-50);
                int k2 = Math.max(0, modifiedSequence[1].length()-50);
                fileWriter.write(modifiedSequence[0].substring(0,l1) + " " + modifiedSequence[0].substring(k1) + "\n");
                fileWriter.write(modifiedSequence[1].substring(0,l2) + " " + modifiedSequence[1].substring(k2) + "\n");
                fileWriter.write(String.valueOf(costMatrix[strings[0].length()][strings[1].length()]) + "\n"); //Cost of alignment
                fileWriter.write(String.valueOf(actualMemUsed) + "\n"); //Memory
                fileWriter.write(String.valueOf(timeTaken)); //Time
                fileWriter.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }        

        }
        else {
            System.out.println("Please enter an input filename as an argument");
        }
    }
}
