import java.util.*;

public class Main {
    public static void main(String[] args) {
        String s1 = "ACTG";
        int[] values1 = {3, 6, 1, 1};

        String s2 = "TACG";
        int[] values2 = {1, 2, 9, 2};

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

        int gapValue = 30;

        long startTime = System.currentTimeMillis();

        SequenceAlignment sequenceAlignment = new SequenceAlignment(penaltyMap, gapValue);

        String input1 = sequenceAlignment.generateString(s1, values1);
        String input2 = sequenceAlignment.generateString(s2, values2);

        int[][] costMatrix = sequenceAlignment.findMinCostMatrix(input1, input2);

        sequenceAlignment.constructModifiedSeq(input1, input2, costMatrix);

        long stopTime = System.currentTimeMillis();

        System.out.println(sequenceAlignment.getModifiedString1().length());
        System.out.println(sequenceAlignment.getModifiedString2().length());
        System.out.println("Time taken --> " + (stopTime - startTime));

        System.out.println(sequenceAlignment.dividenConquerSolution(input1, input2).length());
    }
}
