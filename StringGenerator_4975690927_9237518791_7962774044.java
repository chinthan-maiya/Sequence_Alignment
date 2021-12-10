import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class StringGenerator_4975690927_9237518791_7962774044 {

    public static String generateString(String baseString, List<Integer> indices) {
        StringBuilder string = new StringBuilder(baseString);
        for(int index: indices)
            string.insert(index+1, new String(string));
        return string.toString();
    }

    public static String[] generateStringsFromFile(File file) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;
        List<Integer> indices = new ArrayList<>();
        String[] strings = new String[2];
        String base = br.readLine();
        while ((str = br.readLine()) != null) {
            if(str.matches("[a-zA-Z]+")) {
                strings[0] = generateString(base, indices);
                indices = new ArrayList<>();
                base = str;
            }
            else
                indices.add(Integer.parseInt(str));
        }
        strings[1] = generateString(base, indices);
        br.close();
        return strings;
    }

    public static String[] generateRandomStrings() {
        return new String[2];
    }
}