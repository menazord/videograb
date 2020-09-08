package com.menazord.videograb.providers.youtube;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.model.VideoDetails;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioVideoFormat;
import com.menazord.videograb.model.DownloadStatus;
import com.menazord.videograb.model.Video;
import com.menazord.videograb.model.VideoFormat;
import com.menazord.videograb.providers.VideoService;
import com.menazord.videograb.providers.VideoServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("YOUTUBE")
public class YouTubeVideoService implements VideoService {

    private YoutubeDownloader downloader = new YoutubeDownloader();

    @Override
    public Video getDetails(String url) throws VideoServiceException {

        try {
            Map<String, String> urlParams = splitQuery(new URL(url));
            String videoId = urlParams.get("v");

            log.info("Downloading video details for YouTube video id {}", videoId);

            YouTubeVideo videoDetails = new YouTubeVideo();
            videoDetails.setVideoId(videoId);

            YoutubeVideo video = downloader.getVideo(videoId);

            VideoDetails details = video.details();
            videoDetails.setTitle(details.title());

            List<AudioVideoFormat> videoWithAudioFormats = video.videoWithAudioFormats();
            videoWithAudioFormats.forEach(it -> {

                YouTubeVideoFormat videoFormat = new YouTubeVideoFormat();
                videoFormat.setAudioQuality(it.audioQuality().toString());
                videoFormat.setVideoQuality(it.videoQuality().toString());
                videoFormat.setVideoId(videoId);
                videoFormat.setFormat(it);

                videoDetails.addFormat(videoFormat);
            });

            log.info("Details downloaded successfully");

            return videoDetails;

        } catch (Exception e) {
            throw new VideoServiceException("Error while getting Youtube video details.", e);
        }
    }

    @Override
    public DownloadStatus downloadVideo(VideoFormat videoFormat, String outputFile) throws VideoServiceException {

        try {
            if (videoFormat instanceof YouTubeVideoFormat) {

                log.info("Starting download for format " + videoFormat);

                YouTubeVideoFormat youTubeVideoFormat = (YouTubeVideoFormat) videoFormat;
                File outputDir = new File(outputFile);

                YoutubeVideo video = downloader.getVideo(youTubeVideoFormat.getVideoId());
                log.info("Downloading {} bytes", youTubeVideoFormat.getFormat().contentLength());

                YouTubeDownloadListener status = new YouTubeDownloadListener();
                video.downloadAsync(youTubeVideoFormat.getFormat(), outputDir, status);

                log.info("Async download task started.");

                return status;

            } else {
                throw new VideoServiceException("Invalid format argument");
            }

        } catch (YoutubeException e) {
            log.error("YouTube service error while downloading video", e);
            throw new VideoServiceException("YouTube service error while downloading video", e);
        } catch (IOException e) {
            log.error("I/O error while downloading video", e);
            throw new VideoServiceException("I/O error while downloading video", e);
        } finally {

        }
    }


    public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8),
                    URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
        }
        return query_pairs;
    }

}
