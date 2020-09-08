package com.menazord.videograb.providers;

public enum Provider {

    YOUTUBE("YouTube");

    String friendlyName;

    Provider(String name) {
        this.friendlyName = name;
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}
