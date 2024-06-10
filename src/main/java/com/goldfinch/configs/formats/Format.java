package com.goldfinch.configs.formats;

import java.io.File;
import java.util.Map;

public interface Format {

    /**
     * Write the map to the file
     *
     * @see #readFile(File)
     *
     * @param file File
     * @param map Map
     */
    void writeFile(File file, Map<String, Object> map);

    /**
     * You shouldn't deserialize the file. Return a string-object map.
     * @param file File
     * @return Key-value map. The value can be primitive
     * types (number, string, boolean, etc.), list, and
     * submap. A submap can also have key-values. Looks like recursion
     */
    Map<String, Object> readFile(File file);

    static FormatFactory factory(){
        return FormatFactory.getFactory();
    }
}
