package com.menazord.videograb.providers.youtube;

import com.github.kiulian.downloader.OnYoutubeDownloadListener;
import com.menazord.videograb.model.DownloadStatus;

import java.io.File;

public class YouTubeDownloadListener extends DownloadStatus implements OnYoutubeDownloadListener {

    @Override
    public void onDownloading(int i) {
        setProgress(i);
    }

    @Override
    public void onFinished(File file) {
        setFinished(true);
    }

    @Override
    public void onError(Throwable throwable) {
        setFailed(true);
    }
}
