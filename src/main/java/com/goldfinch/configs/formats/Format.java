package com.goldfinch.configs.formats;

import java.io.File;
import java.util.Map;

public interface Format {

    void writeFile(File file, Map<String, Object> map);

    Map<String, Object> readFile(File file);
}
