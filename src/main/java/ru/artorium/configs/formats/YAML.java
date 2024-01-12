package ru.artorium.configs.formats;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.yaml.snakeyaml.Yaml;

public class YAML implements Format {

    @Override
    public void writeFile(File file, Map<String, Object> map) {
        try (final FileWriter writer = new FileWriter(file)) {
            writer.write(new YAMLMapper().writeValueAsString(map));
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> readFile(File file) {
        if (file.length() == 0) {
            return new HashMap<>();
        }

        try (final FileReader reader = new FileReader(file)) {
            final Yaml yaml = new Yaml();
            return yaml.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
