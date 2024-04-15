package ru.artorium.configs.formats;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class YAML implements Format {

    @Override
    public void writeFile(File file, Map<String, Object> map) {
        try (final FileWriter writer = new FileWriter(file)) {
            final String yamlString = new YAMLMapper().writeValueAsString(map);
            writer.write(yamlString);
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
