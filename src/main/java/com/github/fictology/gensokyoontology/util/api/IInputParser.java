package com.github.fictology.gensokyoontology.util.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface IInputParser {
    default int parseInt(String input) {
        String pattern = "-?\\d+";
        Matcher matcher = Pattern.compile(pattern).matcher(input);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) sb.append(matcher.group());
        return sb.toString().isEmpty() ? 0 : toValidStringAndGetInt(sb.toString());
    }

    default float parseFloat(String input) {
        String pattern = "-?\\d*\\.?\\d+(?:[eE][-+]?\\d+)?";
        Matcher matcher = Pattern.compile(pattern).matcher(input);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) sb.append(matcher.group());
        return sb.toString().isEmpty() ? 0F : Float.parseFloat(sb.toString());
    }

    default double parseDouble(String input) {
        String pattern = "-?\\d*\\.?\\d+(?:[eE][-+]?\\d+)?";
        Matcher matcher = Pattern.compile(pattern).matcher(input);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) sb.append(matcher.group());
        return sb.toString().isEmpty() ? 0D : Double.parseDouble(sb.toString());
    }

    default long parseLong(String input) {
        String pattern = "-?\\d+";
        Matcher matcher = Pattern.compile(pattern).matcher(input);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) sb.append(matcher.group());
        return sb.toString().isEmpty() ? 0L : Long.parseLong(sb.toString());
    }

    default boolean parseBoolean(String input) {
        String pattern = "(?i)(?:true|false)|^(?!0(?:\\.0+)?$)(?:[1-9]\\d*|0)(?:\\.\\d+)?";
        Matcher matcher = Pattern.compile(pattern).matcher(input);
        StringBuilder sb = new StringBuilder();
        if (matcher.find()) {
            sb.append(matcher.group());
            return Boolean.parseBoolean(sb.toString());
        }
        while (matcher.find()) sb.append(matcher.group());
        return !sb.toString().isEmpty() && Boolean.parseBoolean(sb.toString());
    }

    default int toValidStringAndGetInt(String input) {
        String pattern = "-?\\d+";
        String substring = input.substring(0, Math.min(input.length(), 9));
        Matcher matcher = Pattern.compile(pattern).matcher(substring);
        if (matcher.matches()) {
            return Integer.parseInt(matcher.group());
        }
        return 0;
    }

    default int hasPosOrNegSymbol(String parsedStr) {
        if (parsedStr.contains("+") || parsedStr.contains("-")) return 1;
        else return 0;
    }


}
