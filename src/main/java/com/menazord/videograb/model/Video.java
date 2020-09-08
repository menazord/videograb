package com.menazord.videograb.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Video {

    String title;
    List<VideoFormat> formats;

    public Video() {
        this.formats = new ArrayList<>();
    }

    public void addFormat(VideoFormat e){
        this.formats.add(e);
    }
}
