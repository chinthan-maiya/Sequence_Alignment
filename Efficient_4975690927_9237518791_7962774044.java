import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Efficient_4975690927_9237518791_7962774044 {
    private HashMap<String, Integer> penaltyMap;
    private int gapValue;
    List<int[]> P = new ArrayList<>();

    public Efficient_4975690927_9237518791_7962774044() {
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

    public int[][] memoryEfficientAlignmentForward(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        int[][] B = new int[m + 1][2];

        for (int i = 0; i <= m; i++) {
            B[i][0] = i * gapValue;
        }

        for (int j = 1; j <= n; j++) {
            B[0][1] = j * gapValue;
            for (int i = 1; i <= m; i++) {
                String penaltyStr = X.charAt(i - 1) + "" + Y.charAt(j - 1);
                B[i][1] = Math.min(
                        penaltyMap.get(penaltyStr) + B[i - 1][0],
                        Math.min(
                                gapValue + B[i - 1][1],
                                gapValue + B[i][0]
                        )
                    );
            }

            for (int i = 0; i <= m; i++) {
                B[i][0] = B[i][1];
            }
        }
        return B;
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
        } else if (X.length() < 2 || Y.length() < 2) {
            Basic_4975690927_9237518791_7962774044 sequenceAlignment = new Basic_4975690927_9237518791_7962774044();
            int[][] costMatrix = sequenceAlignment.findMinCostMatrix(X, Y);
            String[] result = sequenceAlignment.constructModifiedSeq(X, Y, costMatrix);
            Z = result[0];
            W = result[1];
        } else {
            int yLen = Y.length();
            int yMid = yLen / 2;

            String xRev = (new StringBuilder(X)).reverse().toString();
            String yRev = (new StringBuilder(Y)).reverse().toString();

            int[][] f = memoryEfficientAlignmentForward(X, Y.substring(0, yMid));
            int[][] g = memoryEfficientAlignmentForward(xRev, yRev.substring(0, yMid));

            for(int i=0; i<g.length/2; i++) {
                int temp = g[i][1];
                g[i][1] = g[g.length - i - 1][1];
                g[g.length - i - 1][1] = temp;
            }
            int xMid = 0;
            int vMin = f[0][1] + g[0][1];

            for (int i = 1; i <= X.length(); i++) {
                int score = f[i][1] + g[i][1];
                if (score < vMin) {
                    vMin = score;
                    xMid = i;
                }
            }

            String[] prefix = divdeAndConquer(X.substring(0, xMid), Y.substring(0, yMid));
            String[] suffix = divdeAndConquer(X.substring(xMid), Y.substring(yMid));

            Z = prefix[0] + suffix[0];
            W = prefix[1] + suffix[1];
        }
        res[0] = Z;
        res[1] = W;
        return res;
    }

    public static void main(String[] args) {
        String[] strings = null;
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();

        if(args.length>=1) {
            String inputFileName = args[0];
            File file = new File(inputFileName);
            try {
                strings = StringGenerator.generateStringsFromFile(file);

                long startTime = System.currentTimeMillis();

                Efficient_4975690927_9237518791_7962774044 memoryEfficientAlignment = new Efficient_4975690927_9237518791_7962774044();
                String[] s = memoryEfficientAlignment.divdeAndConquer(strings[0], strings[1]);

                long stopTime = System.currentTimeMillis();
                double timeTaken = (stopTime - startTime) / 1000.0;

                long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                double actualMemUsed = (afterUsedMem - beforeUsedMem) / 1000;

                int alignmentCost = 0;
                for(int i = 0; i < s[0].length(); i++) {
                    if(s[0].charAt(i)=='_' || s[1].charAt(i)=='_')
                        alignmentCost += memoryEfficientAlignment.gapValue;
                    else
                        alignmentCost += memoryEfficientAlignment.penaltyMap.get(s[0].charAt(i) + "" + s[1].charAt(i));
                }

                FileWriter fileWriter = new FileWriter("output.txt");
                int l1 = Math.min(s[0].length(), 50);
                int l2 = Math.min(s[1].length(), 50);
                int k1 = Math.max(0, s[0].length()-50);
                int k2 = Math.max(0, s[1].length()-50);
                fileWriter.write(s[0].substring(0,l1) + " " + s[0].substring(k1) + "\n");
                fileWriter.write(s[1].substring(0,l2) + " " + s[1].substring(k2) + "\n");
                fileWriter.write(String.valueOf(alignmentCost) + "\n"); //Cost of alignment
                fileWriter.write(String.valueOf(actualMemUsed) + "\n"); //Memory
                fileWriter.write(String.valueOf(timeTaken)); //Time
                fileWriter.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please enter an input filename as an argument");
        }
    }
}