package ru.artorium.configs.formats;

import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSON implements Format {
    @Override
    public void writeFile(File file, Map<String, Object> map) {
        final String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(map);

        try (final FileWriter writer = new FileWriter(file)) {
            writer.write(jsonString);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> readFile(File file) {
        final JSONParser parser = new JSONParser();

        if (file.length() == 0) {
            return new HashMap<>();
        }

        try {
            return new LinkedHashMap<>((JSONObject) parser.parse(new FileReader(file, StandardCharsets.UTF_8)));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
