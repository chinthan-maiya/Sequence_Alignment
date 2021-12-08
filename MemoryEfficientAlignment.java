import java.io.File;
import java.util.HashMap;

public class MemoryEfficientAlignment {
    private HashMap<String, Integer> penaltyMap;
    private int gapValue;

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
    
    public int[][] memoryEfficientAlignmentForward(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] costMatrix = new int[m + 1][2];

        for (int i = 0; i <= m; i++) {
            costMatrix[i][0] = i * gapValue;
        }

        for (int i = 1; i <= n; i++) {
            costMatrix[0][1] = i * gapValue;
            for (int j = 1; j <= m; j++) {
                String penaltyStr = s1.charAt(j - 1) + "" + s2.charAt(i - 1);
                costMatrix[j][1] = Math.min(
                        penaltyMap.get(penaltyStr) + costMatrix[j - 1][0], // replacing
                        Math.min(gapValue + costMatrix[j - 1][0], gapValue + costMatrix[j][1]) // inserting and deleting
                );
            }
        }

        for (int i = 0; i <= m; i++) {
            costMatrix[i][0] = costMatrix[i][1];
        }

        return costMatrix;
    }

    public String dividenConquerSolution(String X, String Y) {

        int m = X.length();
        int n = Y.length();
  
        if (m <= 10 || n <= 10) {
            // int[][] costMatrix = findMinCostMatrix(X, Y);
          //   return constructModifiedSeq(X, Y, costMatrix);
        }
        String xRev = (new StringBuilder(X)).reverse().toString();
        String yRev = (new StringBuilder(Y.substring(n / 2 + 1, n))).reverse().toString();
  
        int[][] prefixSeq = memoryEfficientAlignmentForward(X, Y.substring(0, n / 2));
      //   int[][] suffixSeq = memoryEfficientAlignmentForward(X, Y.substring(n / 2 + 1, n));
        int[][] suffixSeq = memoryEfficientAlignmentForward(xRev, yRev);
  
        int posMax = n / 2 - 1;
        int vMax = prefixSeq[n / 2 - 1][1] + suffixSeq[n / 2 - 1][1];
  
        for (int i = n / 2; i <= n; i++) {
            int score = prefixSeq[i][1] + suffixSeq[i][1];
            if (score > vMax) {
                vMax = score;
                posMax = i;
            }
        }
  
        String prefix = dividenConquerSolution(X.substring(1, posMax), Y.substring(1, n / 2));
        String suffix = dividenConquerSolution(X.substring(posMax, n), Y.substring(n / 2 + 1, n));
  
        return prefix + suffix;
    }

    public static void main(String[] args) {
        String[] strings = null;

        if(args.length>=1) {
            String inputFileName = args[0];
            File file = new File(inputFileName);
            try {
                strings = StringGenerator.generateStringsFromFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // long startTime = System.currentTimeMillis();

            // MemoryEfficientAlignment memoryEfficientAlignment = new MemoryEfficientAlignment();
            // String[] modifiedSequence = memoryEfficientAlignment.constructAlignment(strings[0], strings[1]);

            // long stopTime = System.currentTimeMillis();

            // System.out.println(modifiedSequence[0]);
            // System.out.println(modifiedSequence[1]);
            // System.out.println("Time taken --> " + (stopTime - startTime));
        }
        else {
            System.out.println("Please enter an input filename as an argument");
        }
    }
}
