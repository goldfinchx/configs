package com.goldfinch.configs.formats;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;

public final class FormatFactory {
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
    private static FormatFactory factory;

    private FormatFactory() {
        this.formats = new HashMap<>();
        this.registered = new HashMap<>();
    }

    public static FormatFactory getFactory() {
        if (factory != null) {
            return factory;
        }
        factory = new FormatFactory();

        /* Register build-in formats */
        factory.register(JSON.class, "application/json", "json");
        factory.register(YAML.class, "application/yaml", "yml", "yaml");

        return factory;
    }

    /**
     * @see #register(Class, String, List)
     */
    public void register(Class<? extends Format> format, String mime, String... fileFormats) {
        this.register(format, mime, Arrays.asList(fileFormats));
    }

    /**
     * Register a new file format. Please note that if such
     * a format already exists, it will be replaced.
     * @param format Class of {@link Format}
     * @param mime <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types">MIME</a>
     * @param fileFormats Array of file formats (f.e.: "yml", "json")
     */
    public void register(Class<? extends Format> format, String mime, List<String> fileFormats) {
        Objects.requireNonNull(format);
        Objects.requireNonNull(mime);
        Objects.requireNonNull(fileFormats);

        if (fileFormats.isEmpty()) {
            throw new IllegalArgumentException("File format array shouldn't be empty");
        }

        if (!this.verify(format)) {
            throw new IllegalArgumentException(format.getName() + " should contain constructor with no parameters");
        }

        for (String fileFormat : fileFormats) {
            while (fileFormat.startsWith(".")) {
                fileFormat = fileFormat.substring(1);
            }

            this.formats.put(fileFormat, mime);
        }

        this.registered.put(mime, format);
    }

    private boolean verify(Class<? extends Format> format) {
        try {
            format.getConstructor();
        } catch (NoSuchMethodException e) {
            return false;
        }

        return true;
    }

    /**
     * Unregister file format
     */
    public void unregister(Class<? extends Format> format) {
        if (format == null) {
            return;
        }

        String mime = null;

        for (final Map.Entry<String, Class<? extends Format>> entry : this.registered.entrySet()) {
            if (!entry.getValue().equals(format)) {
                continue;
            }

            mime = entry.getKey();
        }

        if (mime == null) {
            return;
        }

        this.registered.remove(mime);
        this.getFileFormatsFromMime(mime).forEach(this.formats::remove);
    }

    private List<String> getFileFormatsFromMime(String mime) {
        final List<String> fileFormats = new ArrayList<>();
        for (final Map.Entry<String, String> entry : this.formats.entrySet()) {
            if (!entry.getValue().equals(mime)) {
                continue;
            }

            fileFormats.add(entry.getKey());
        }
        return fileFormats;
    }

    /**
     * Unregister by MIME
     */
    public void unregister(String mime) {
        if (mime == null) {
            return;
        }

        this.getFileFormatsFromMime(mime).forEach(this.formats::remove);
        this.registered.remove(mime);
    }

    /**
     * Get a format from a file name
     * @param filename File name (f.e.: "config.yml", "messages.json")
     */
    @Nullable
    public Format getFormatFromFileName(String filename) {
        if (filename == null) {
            return null;
        }

        final String[] parts = filename.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException("The file name does not contain type");
        }

        final String filetype = parts[parts.length - 1];
        return this.getFormatFromFileType(filetype);
    }

    /**
     * Get a format from a file type
     * @param fileType File type (f.e.: "yml", "json")
     */
    @Nullable
    public Format getFormatFromFileType(String fileType) {
        if (fileType == null) {
            return null;
        }

        final String mime = this.formats.get(fileType);
        return this.getFormatFromMimeType(mime);
    }

    /**
     * Get a format from a mime type
     * @param mime MIME (f.e.: "application/yaml", "application/json")
     */
    @Nullable
    public Format getFormatFromMimeType(String mime) {
        if (mime == null) {
            return null;
        }

        final Class<? extends Format> formatClass = this.registered.get(mime);
        try {
            final Constructor<? extends Format> constructor = formatClass.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }
    }
}
