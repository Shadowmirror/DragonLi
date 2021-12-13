package miao.kmirror.dragonli.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Kmirror
 */
public class RangePasswordUtils {

    public static final int ONLY_NUMBER = 1;
    public static final int ONLY_LETTER = 2;
    public static final int LETTER_NUMBER = 3;
    public static final int ONLY_SYMBOL = 4;
    public static final int SYMBOL_NUMBER = 5;
    public static final int SYMBOL_LETTER = 6;
    public static final int ALL = 7;

    private static final String letterStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String symbolStr = "~!@#$%^&*()_+/-=[]{};:'<>?.";
    private static final String numberStr = "0123456789";

    // 随机获取字符串字符
    private static char getRandomChar(String str) {
        SecureRandom random = new SecureRandom();
        return str.charAt(random.nextInt(str.length()));
    }

    // 随机获取字母
    private static char getLetterChar() {
        return getRandomChar(letterStr);
    }


    // 随机获取数字字符
    private static char getNumberChar() {
        return getRandomChar(numberStr);
    }

    // 随机获取特殊字符
    private static char getSymbolChar() {
        return getRandomChar(symbolStr);
    }

    public static String rangePassword(int rangeType, int length) {
        StringBuilder pwd = new StringBuilder("");
        switch (rangeType) {
            case ONLY_NUMBER:
                for (int i = 0; i < length; i++) {
                    pwd.append(getNumberChar());
                }
                break;
            case ONLY_LETTER:
                for (int i = 0; i < length; i++) {
                    pwd.append(getLetterChar());
                }
                break;
            case LETTER_NUMBER:
                for (int i = 0; i < length; i++) {
                    pwd.append(getRandomChar(letterStr + numberStr));
                }
                break;
            case ONLY_SYMBOL:
                for (int i = 0; i < length; i++) {
                    pwd.append(getSymbolChar());
                }
                break;
            case SYMBOL_NUMBER:
                for (int i = 0; i < length; i++) {
                    pwd.append(getRandomChar(symbolStr + numberStr));
                }
                break;
            case SYMBOL_LETTER:
                for (int i = 0; i < length; i++) {
                    pwd.append(getRandomChar(symbolStr + letterStr));
                }
                break;
            case ALL:
                for (int i = 0; i < length; i++) {
                    pwd.append(getRandomChar(symbolStr + letterStr + numberStr));
                }
                break;
            default:
        }

        return pwd.toString();
    }
}
