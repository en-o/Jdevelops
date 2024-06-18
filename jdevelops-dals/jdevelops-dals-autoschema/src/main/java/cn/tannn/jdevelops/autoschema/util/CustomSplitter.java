package cn.tannn.jdevelops.autoschema.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CustomSplitter {
    private final String delimiter;
    private final Pattern pattern;
    private final int fixedLength;
    private boolean omitEmptyStrings = false;
    private boolean trimResults = false;

    private CustomSplitter(String delimiter) {
        this.delimiter = delimiter;
        this.pattern = null;
        this.fixedLength = -1;
    }

    private CustomSplitter(Pattern pattern) {
        this.delimiter = null;
        this.pattern = pattern;
        this.fixedLength = -1;
    }

    private CustomSplitter(int fixedLength) {
        this.delimiter = null;
        this.pattern = null;
        this.fixedLength = fixedLength;
    }

    public static CustomSplitter on(String delimiter) {
        return new CustomSplitter(delimiter);
    }

    public static CustomSplitter onPattern(String pattern) {
        return new CustomSplitter(Pattern.compile(pattern));
    }

    public static CustomSplitter fixedLength(int length) {
        return new CustomSplitter(length);
    }

    public CustomSplitter omitEmptyStrings() {
        this.omitEmptyStrings = true;
        return this;
    }

    public CustomSplitter trimResults() {
        this.trimResults = true;
        return this;
    }

    public Iterable<String> split(String input) {
        return splitToList(input);
    }

    public List<String> splitToList(String input) {
        List<String> results = new ArrayList<>();
        String[] parts;

        if (fixedLength > 0) {
            parts = splitByFixedLength(input, fixedLength);
        } else if (pattern != null) {
            parts = pattern.split(input);
        } else {
            parts = input.split(Pattern.quote(delimiter));
        }

        for (String part : parts) {
            if (trimResults) {
                part = part.trim();
            }
            if (!omitEmptyStrings || !part.isEmpty()) {
                results.add(part);
            }
        }

        return results;
    }

    private String[] splitByFixedLength(String input, int length) {
        int arrayLength = (int) Math.ceil((double) input.length() / length);
        String[] result = new String[arrayLength];
        int index = 0;
        for (int i = 0; i < input.length(); i += length) {
            result[index++] = input.substring(i, Math.min(input.length(), i + length));
        }
        return result;
    }

//    public static void main(String[] args) {
//        CustomSplitter splitter = CustomSplitter.on(",")
//                .omitEmptyStrings()
//                .trimResults();
//
//        String str = "apple, banana, , orange, , grape";
//        List<String> result = splitter.splitToList(str);
//
//        System.out.println("Result:");
//        for (String part : result) {
//            System.out.println(part);
//        }
//
//        // Fixed length example
//        CustomSplitter fixedLengthSplitter = CustomSplitter.fixedLength(2);
//        String fixedLengthStr = "1234567890";
//        List<String> fixedLengthResult = fixedLengthSplitter.splitToList(fixedLengthStr);
//
//        System.out.println("Fixed Length Result:");
//        for (String part : fixedLengthResult) {
//            System.out.println(part);
//        }
//
//        // Regex example
//        CustomSplitter regexSplitter = CustomSplitter.onPattern("\\d+");
//        String regexStr = "apple123banana456orange789grape";
//        List<String> regexResult = regexSplitter.splitToList(regexStr);
//
//        System.out.println("Regex Result:");
//        for (String part : regexResult) {
//            System.out.println(part);
//        }
//    }
}
