package invoicer.invoicer.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OcrDataProcessor {

    private static final String dateRegex = "(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d|(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])";
    private static final String priceRegex = "(\\d+\\.\\d{1,2})|(\\d+\\,\\d{1,2})";
    private static final String plnRegex = "\\bPLN\\b";
    private static Map<String, String> proceedData = new HashMap<>();
    public static ArrayList<String> contentSeparator(String content){
        String[] separatedContent = content.split("\\R");
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < separatedContent.length; i++) {
            if (!separatedContent[i].equals("")){
                arrayList.add(separatedContent[i]);
            }
        }
        getReceiptImportantInfo(arrayList);
        return arrayList;
    }
    public static void getReceiptImportantInfo(ArrayList<String> content){
        Pattern pattern = Pattern.compile(dateRegex);
        Pattern patterPrice = Pattern.compile(priceRegex);
        Pattern patternPlnRegex = Pattern.compile(plnRegex);

        for(String line:content) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                proceedData.put("date", matcher.group(0));
            }
        }
        for(String line:content) {
            Matcher matcherPrices = patterPrice.matcher(line);
            Matcher matcherPln = patternPlnRegex.matcher(line);
            if (matcherPrices.find() && matcherPln.find()) {
                proceedData.put("price", matcherPrices.group(0));
            }
        }
        System.out.println(proceedData);
    }
}
