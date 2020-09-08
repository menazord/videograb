package com.menazord.videograb.model;

public class ProviderModel {

    String key;
    String value;

    public ProviderModel(String name, String key) {
        this.key = key;
        this.value = name;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
