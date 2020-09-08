package com.menazord.videograb.providers;

import com.menazord.videograb.model.DownloadStatus;
import com.menazord.videograb.model.Video;
import com.menazord.videograb.model.VideoFormat;

public interface VideoService {

    String TOKEN = "##";

    Video getDetails(String url) throws VideoServiceException;

    DownloadStatus downloadVideo (VideoFormat format, String outputFile) throws VideoServiceException;

}
