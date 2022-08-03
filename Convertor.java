import java.util.Scanner;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.RoundingMode;

class Convertor {
    private final int SCALE = 5;
    private final int SCALE_ROUND = 10;
    
    private final int BASE_HEX = 16;
    private final int BASE_DECIMAL = 10;
    
    private final String STR_MENU = "Enter two numbers in format: {source base} {target base} (To quit type /exit)";
    private final String[] STR_NUMBER = {"Enter number in base ", " to convert to base ", " (To go back type /back) "};
    private final String STR_EXIT = "/exit";
    private final String STR_BACK = "/back";
    private final String STR_RESULT = "Conversion result: ";
    
    public Scanner scanner = new Scanner(System.in);
    
    public void work() {
        String strData;
        String number;
        String frac = "";
        int baseFrom = BASE_DECIMAL;
        int baseTo = BASE_DECIMAL;
        String result = "";
        String resultFrac = "";
        boolean goBack = true;
        boolean isFrac = false;
        
        do {
            if (goBack) {
                goBack = false;
                strData = getData(STR_MENU);
                if (strData.equals(STR_EXIT)) {
                    break;
                }
                baseFrom = Integer.parseInt(strData.split(" ")[0]);
                baseTo = Integer.parseInt(strData.split(" ")[1]);
            }
            number = getData(STR_NUMBER[0] + baseFrom + STR_NUMBER[1] + baseTo + STR_NUMBER[2]);
            if (number.equals(STR_BACK)) {
                goBack = true;
                System.out.println("");
                continue;
            }
            if (number.contains(".")) {
                isFrac = true;
                String[] parts = number.split("\\.");
                number = parts[0];
                frac = parts[1];
            }
            result = convertInt(number, baseFrom, baseTo);
            if (isFrac) {
                resultFrac = convertFrac(frac, baseFrom, baseTo);
                result = result + "." + resultFrac;
                isFrac = false;
            }
            printResult(STR_RESULT + result);
        } while(true);
        return;
    }
    
    private String convertInt(String number, int baseFrom, int baseTo) {
        return new BigInteger(number, baseFrom).toString(baseTo);
    }
    
    private String convertFrac(String number, int baseFrom, int baseTo) {
        BigDecimal doubleNumber = BigDecimal.ZERO;
        BigDecimal baseResult = new BigDecimal(baseTo);   
        BigDecimal currentDigit;
        String result = "";
        
        if (baseFrom != BASE_DECIMAL) {
            BigDecimal base = new BigDecimal(baseFrom);
            BigDecimal current = BigDecimal.ONE.divide(base, SCALE_ROUND, RoundingMode.FLOOR);
            BigDecimal add;
            final String border = "0.000000";
            for (char digit: number.toCharArray()) {
                add = new BigDecimal(Character.getNumericValue(digit));
                add = add.multiply(current);
                doubleNumber = doubleNumber.add(add);
                current = current.divide(base, SCALE_ROUND, RoundingMode.CEILING);
                if (current.toString().length() > 8) {
                    if (current.toString().substring(0,9).equals(border)) {   
                        break;
                    }
                }
            }
        } else {
            doubleNumber = new BigDecimal("0." + number);
        }
        doubleNumber = doubleNumber.setScale(SCALE, RoundingMode.FLOOR);
        
        for (int i = 0; i < SCALE; i++) {
            doubleNumber = doubleNumber.multiply(baseResult);
            currentDigit = doubleNumber.setScale(0, RoundingMode.FLOOR);
            result += String.valueOf(Character.forDigit(currentDigit.intValue(), baseTo));
            doubleNumber = doubleNumber.subtract(currentDigit);
        }
        
        return result;
    }
        
    
    private String getData(String str) {
        System.out.println(str);
        return scanner.nextLine();
    }
    
    private void printResult(String str) {
        System.out.println(str + "\n");
    }
    
}

/*
public class Main {

    public static void main(String[] args) {
        Convertor convertor = new Convertor();
        convertor.work();
    }
}
*/
