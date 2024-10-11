package fr.zwartkat.itemcreator.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatColorUtil {

    public static String convert(String input) {
        if (input != null) {
            // Expressions régulières pour les balises hexadécimales
            Pattern patternHexStart = Pattern.compile("<#([0-9A-Fa-f]{6})>");
            Pattern patternHexEnd = Pattern.compile("</#([0-9A-Fa-f]{6})>");

            // Matcher pour les balises de fin
            Matcher matcherHexEnd = patternHexEnd.matcher(input);
            StringBuffer result = new StringBuffer();
            int lastEnd = 0; // La position après le dernier match

            while (matcherHexEnd.find()) {
                String endColor = matcherHexEnd.group(1);
                int end = matcherHexEnd.start();

                // Trouver la balise de début avant la balise de fin
                Matcher matcherHexStart = patternHexStart.matcher(input.substring(0, end));
                if (matcherHexStart.find()) {
                    String startColor = matcherHexStart.group(1);
                    int start = matcherHexStart.end();
                    // Extraire le texte à colorer
                    String text = input.substring(start, end);

                    // Appliquer le dégradé sur le texte
                    String gradientText = GradientTextUtil.applyGradient(text, startColor, endColor);

                    // Ajouter le texte modifié au résultat
                    result.append(input, lastEnd, matcherHexStart.start())
                            .append(gradientText);

                    lastEnd = matcherHexEnd.end(); // Mettre à jour la position après le dernier match
                } else {
                    result.append(input, lastEnd, matcherHexEnd.end());
                    lastEnd = matcherHexEnd.end();
                }
            }

            // Ajouter le reste du texte après la dernière balise de fin, s'il y en a
            result.append(input.substring(lastEnd));

            // Traiter les balises hexadécimales sans balises de fin
            Matcher matcherHexStart = patternHexStart.matcher(result.toString());
            StringBuffer finalResult = new StringBuffer();
            while (matcherHexStart.find()) {
                matcherHexStart.appendReplacement(finalResult, convertToMinecraftColor(matcherHexStart.group(1)));
            }
            matcherHexStart.appendTail(finalResult);

            input = finalResult.toString();

            // Convertir les codes de couleur alternatifs manuellement
            input = input.replace("&0", "§0").replace("&1", "§1").replace("&2", "§2")
                    .replace("&3", "§3").replace("&4", "§4").replace("&5", "§5")
                    .replace("&6", "§6").replace("&7", "§7").replace("&8", "§8")
                    .replace("&9", "§9").replace("&a", "§a").replace("&b", "§b")
                    .replace("&c", "§c").replace("&d", "§d").replace("&e", "§e")
                    .replace("&f", "§f").replace("&k", "§k").replace("&l", "§l")
                    .replace("&m", "§m").replace("&n", "§n").replace("&o", "§o")
                    .replace("&r", "§r");
        }

        return input;
    }

    private static String convertToMinecraftColor(String color){

        return String.format("§x§%c§%c§%c§%c§%c§%c",
                color.charAt(0), color.charAt(1),
                color.charAt(2), color.charAt(3),
                color.charAt(4), color.charAt(5));



    }

}
