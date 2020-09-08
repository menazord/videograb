package com.menazord.videograb.providers.youtube;

import com.github.kiulian.downloader.model.formats.Format;
import com.menazord.videograb.model.VideoFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YouTubeVideoFormat extends VideoFormat {

    String videoId;
    Format format;
}
