import java.util.*;

public class Main {
    public static void main(String[] args) {
        String s1 = "ACTG";
        int[] values1 = {3, 6, 1, 1};

        String s2 = "TACG";
        int[] values2 = {1, 2, 9, 2};

        HashMap<String, Integer> penaltyMap = new HashMap<>();
        penaltyMap.put("aa", 0);
        penaltyMap.put("ac", 110);
        penaltyMap.put("ag", 48);
        penaltyMap.put("at", 94);
        penaltyMap.put("ca", 110);
        penaltyMap.put("cc", 0);
        penaltyMap.put("cg", 118);
        penaltyMap.put("ct", 48);
        penaltyMap.put("ga", 48);
        penaltyMap.put("gc", 118);
        penaltyMap.put("gg", 0);
        penaltyMap.put("gt", 110);
        penaltyMap.put("ta", 94);
        penaltyMap.put("tc", 48);
        penaltyMap.put("tg", 110);
        penaltyMap.put("tt", 0);

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
