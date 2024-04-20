package ru.artorium.configs.serialization;

import java.lang.reflect.Field;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public class TypeReference {

    private final @NonNull Class<?> clazz;
    private final @Nullable Field field;

    public TypeReference(@NotNull Class<?> clazz) {
        this.clazz = clazz;
        this.field = null;
    }

    public TypeReference(Field field) {
        this.clazz = field.getType();
        this.field = field;
    }

    public TypeReference(Object object) {
        this.clazz = object.getClass();
        this.field = null;
    }
}
