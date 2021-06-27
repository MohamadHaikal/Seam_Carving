package sample.model;
import sample.controller.RLEController;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RLE_Algorithm {

    public String compression(String source) {

        StringBuilder buff = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            int partLength = 1;
            if (i + 1 < source.length() && source.charAt(i) == source.charAt(i + 1)) {
                while (i + 1 < source.length() && source.charAt(i) == source.charAt(i + 1)) {
                    partLength++;
                    i++;
                }
                buff.append(partLength);
                buff.append(":");
                buff.append(source.charAt(i));

            } else {

                StringBuilder buff2 = new StringBuilder();
                partLength = 1;
                if (((i + 1 < source.length() || i == source.length() - 1) && source.charAt(i) != source.charAt(i + 1))) {
                    buff.append("[");
                    while (i + 1 < source.length()
                            && source.charAt(i) != source.charAt(i + 1)) {
                        partLength++;
                        buff2.append(source.charAt(i));
                        i++;
                    }
                    buff.append(buff2);
                    buff.append("]");
                }
                if (i != source.length() - 1) i--;
            }
        }
        return buff.toString();
    }

    public void CompressionToFile() throws IOException {
        FileWriter writer = new FileWriter("compression.txt");
        List<String> lines = Files.readAllLines(Paths.get(RLEController.path), StandardCharsets.UTF_8);
        for (String line : lines) {
            writer.write(compression(line));
        }
        writer.close();
    }

    public String decompression(String source) {

        StringBuilder buff = new StringBuilder();
        Pattern pattern = Pattern.compile("(\\d+):(\\w|\\W)|(?:\\[(\\w+)\\])");
        Matcher matcher = pattern.matcher(source);


        while (matcher.find()) {

            if (matcher.group(1) != null && matcher.group(2) != null) {
                int number = Integer.parseInt(matcher.group(1));
                while (number-- != 0) {
                    buff.append(matcher.group(2));
                }
            } else if (matcher.group(3) != null) {
                buff.append(matcher.group(3));
            }
        }
        return buff.toString();
    }

    public void decompressionToFile() throws IOException {
        FileWriter writer = new FileWriter("decompression.txt");
        List<String> lines = Files.readAllLines(Paths.get("compression.txt"), StandardCharsets.UTF_8);
        for (String line : lines) {
            writer.write(decompression(line));
        }
        writer.close();
    }


}

