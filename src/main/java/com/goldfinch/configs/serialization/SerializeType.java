package com.goldfinch.configs.serialization;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SerializeType extends TypeReference {
    private List<SerializeType> subTypes;

    public SerializeType(Class<?> owner, List<SerializeType> subTypes) {
        super(owner);
        this.subTypes = subTypes;
    }

    public SerializeType(Class<?> owner) {
        super(owner);
    }
}
