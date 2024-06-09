package com.goldfinch.configs.formats;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class FormatFactory {
    private static FormatFactory factory;

    public static FormatFactory getFactory(){
        if(factory != null) return factory;
        factory = new FormatFactory();

        /* Register build-in formats */
        factory.register(JSON.class, "application/json", "json");
        factory.register(YAML.class, "application/yaml", "yml", "yaml");

        return factory;
    }

    /**
     * Key - File format
     * Value - MIME type
     */
    private final Map<String, String> formats;

    /**
     * Key - MIME type
     * Value - Format class
     */
    private final Map<String, Class<? extends Format>> registered;

    private FormatFactory() {
        formats = new HashMap<>();
        registered = new HashMap<>();
    }

    /**
     * @see #register(Class, String, List)
     */
    public void register(Class<? extends Format> format, String mime, String... fileFormats){
        register(format, mime, Arrays.asList(fileFormats));
    }

    /**
     * Register a new file format. Please note that if such
     * a format already exists, it will be replaced.
     * @param format Class of {@link Format}
     * @param mime <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types">MIME</a>
     * @param fileFormats
     */
    public void register(Class<? extends Format> format, String mime, List<String> fileFormats){
        Objects.requireNonNull(format);
        Objects.requireNonNull(mime);
        Objects.requireNonNull(fileFormats);

        if(fileFormats.isEmpty()){
            throw new IllegalArgumentException("File format array shouldn't be empty");
        }

        if(!this.verify(format)){
            throw new IllegalArgumentException(format.getName()+" should contain constructor with no parameters");
        }

        for (String fileFormat : fileFormats) {
            while (fileFormat.startsWith(".")){
                fileFormat = fileFormat.substring(1);
            }
            this.formats.put(fileFormat, mime);
        }
        this.registered.put(mime, format);
    }

    /**
     * Unregister file format
     */
    public void unregister(Class<? extends Format> format){
        if(format == null) return;

        String mime = null;

        for (Map.Entry<String, Class<? extends Format>> entry : this.registered.entrySet()) {
            if(entry.getValue().equals(format)){
                mime = entry.getKey();
            }
        }

        if(mime == null) return;

        this.registered.remove(mime);

        getFileFormatsFromMime(mime).forEach(this.formats::remove);
    }

    /**
     * Unregister by MIME
     */
    public void unregister(String mime){
        if(mime == null) return;
        getFileFormatsFromMime(mime).forEach(this.formats::remove);
        this.registered.remove(mime);
    }

    /**
     * Get a format from a file name
     * @param filename File name (f.e.: "config.yml", "messages.json")
     */
    @Nullable
    public Format getFormatFromFileName(String filename){
        if(filename == null) return null;
        String[] parts = filename.split("\\.");
        if(parts.length < 2){
            throw new IllegalArgumentException("The file name does not contain type");
        }
        String filetype = parts[parts.length-1];
        return getFormatFromFileType(filetype);
    }

    /**
     * Get a format from a file type
     * @param fileType File type (f.e.: "yml", "json")
     */
    @Nullable
    public Format getFormatFromFileType(String fileType){
        if(fileType == null) return null;
        String mime = this.formats.get(fileType);
        return getFormatFromMimeType(mime);
    }

    /**
     * Get a format from a mime type
     * @param mime MIME (f.e.: "application/yaml", "application/json")
     */
    @Nullable
    public Format getFormatFromMimeType(String mime){
        if(mime == null) return null;
        Class<? extends Format> formatClass = this.registered.get(mime);
        try {
            Constructor<? extends Format> constructor = formatClass.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }
    }

    private List<String> getFileFormatsFromMime(String mime) {
        List<String> fileFormats = new ArrayList<>();
        for (Map.Entry<String, String> entry : this.formats.entrySet()) {
            if(entry.getValue().equals(mime)){
                fileFormats.add(entry.getKey());
            }
        }
        return fileFormats;
    }

    private boolean verify(Class<? extends Format> format){
        try {
            format.getConstructor();
        } catch (NoSuchMethodException e) {
            return false;
        }
        return true;
    }
}
