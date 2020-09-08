package com.menazord.videograb.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoFormat {

    String audioQuality;
    String videoQuality;

    @Override
    public String toString() {
        return String.join("/", videoQuality, audioQuality);
    }
}
