package com.Blog.Blog;

import java.util.ArrayList;
import java.util.List;

interface TextFormatStrategy {
    String formatText(String text);
}

class UppercaseStrategy implements TextFormatStrategy {
    @Override
    public String formatText(String text) {
        return text.toUpperCase();
    }
}

class LowercaseStrategy implements TextFormatStrategy {
    @Override
    public String formatText(String text) {
        return text.toLowerCase();
    }
}
class CapitalizeStrategy implements TextFormatStrategy {
    @Override
    public String formatText(String text) {
        String[] words = text.split(" ");
        StringBuilder capitalizedText = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                capitalizedText.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return capitalizedText.toString().trim();
    }
}

class TextFormatter {
    private TextFormatStrategy formatStrategy;

    public void setFormatStrategy(
            TextFormatStrategy formatStrategy)
    {
        this.formatStrategy = formatStrategy;
    }

    public String format(String text) {
        return formatStrategy.formatText(text);
    }
}

class Client {
    public static void main(String[] args) {
        TextFormatter formatter = new TextFormatter();

        String text = "hello world from textopolis";

        // Using Uppercase formatting
        formatter.setFormatStrategy(new UppercaseStrategy());
        System.out.println(formatter.format(text));

        // Using Lowercase formatting
        formatter.setFormatStrategy(new LowercaseStrategy());
        System.out.println(formatter.format(text));

        // Using Capitalize Each Word formatting
        formatter.setFormatStrategy(new CapitalizeStrategy());
        System.out.println(formatter.format(text));
    }
}





