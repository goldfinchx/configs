package com.goldfinch.configs.formats;

import java.io.File;
import java.util.Map;

public interface Format {

    /**
     * Write the map to the file
     * @see #readFile(File)
     *
     * @param file File
     * @param map Key-value map to be written to the file
     */
    void writeFile(File file, Map<String, Object> map);

    /**
     * Read the file and return the key-value map
     * @see #writeFile(File, Map)
     *
     * @param file File
     * @return deserialized file in a form of key-value map
     */
    Map<String, Object> readFile(File file);

    static FormatFactory factory(){
        return FormatFactory.getFactory();
    }
}
