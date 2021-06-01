package com.demo.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utils {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static double calculateSalePrice(double discountRate, double price) {
        if (discountRate >= 1 || discountRate < 0) discountRate = 0;
        if (!Double.isNaN((1 / discountRate))) {
            return Math.ceil((1 - discountRate) * price);
        }
        return Math.ceil(price);
    }

    public static String toASCII(String text) {
        String noWhiteSpace = WHITESPACE.matcher(text).replaceAll("-");
        String normalized = Normalizer.normalize(noWhiteSpace, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH)
                .replace("-", " ");
    }
}
