package fr.zwartkat.itemcreator.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.List;

public class GradientTextUtil {

    public static String applyGradient(String text, String startColorHex, String endColorHex) {
        StringBuilder gradientText = new StringBuilder();
        int length = text.replaceAll("&([0-9A-Fa-f])", "").length();
        List<Character> format = new ArrayList<>();

        for (int i = 0; i < text.length(); i++) {
            if(text.charAt(i) != '&' && (i == 0 || text.charAt(i-1) != '&')){
                double ratio = (double) i / (length - 1);
                String colorHex = interpolateColor(startColorHex, endColorHex, ratio);
                gradientText.append("§x§").append(colorHex.charAt(1)).append("§").append(colorHex.charAt(2))
                        .append("§").append(colorHex.charAt(3)).append("§").append(colorHex.charAt(4))
                        .append("§").append(colorHex.charAt(5)).append("§").append(colorHex.charAt(6));
                for(char formatChar : format){
                    gradientText.append("§"+formatChar);
                }

                gradientText.append(text.charAt(i));
            }
            else {
                char formatChar = text.charAt(i);
                if( formatChar != '&' && !format.contains(formatChar)){
                    if(formatChar == 'r'){
                        format.clear();
                    }
                    else {
                        format.add(formatChar);
                    }
                }
            }

        }

        return gradientText.toString() + "&r";
    }

    private static String interpolateColor(String startColorHex, String endColorHex, double ratio) {
        int startRed = Integer.parseInt(startColorHex.substring(0, 2), 16);
        int startGreen = Integer.parseInt(startColorHex.substring(2, 4), 16);
        int startBlue = Integer.parseInt(startColorHex.substring(4, 6), 16);

        int endRed = Integer.parseInt(endColorHex.substring(0, 2), 16);
        int endGreen = Integer.parseInt(endColorHex.substring(2, 4), 16);
        int endBlue = Integer.parseInt(endColorHex.substring(4, 6), 16);

        int red = (int) (startRed * (1 - ratio) + endRed * ratio);
        int green = (int) (startGreen * (1 - ratio) + endGreen * ratio);
        int blue = (int) (startBlue * (1 - ratio) + endBlue * ratio);

        return String.format("#%02x%02x%02x", red, green, blue);
    }
}
