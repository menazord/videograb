package com.menazord.videograb;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.model.VideoDetails;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioVideoFormat;
import com.github.kiulian.downloader.model.formats.Format;
import com.github.kiulian.downloader.model.formats.VideoFormat;
import com.github.kiulian.downloader.model.quality.VideoQuality;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {

        String url = "https://www.youtube.com/watch?v=5G5fzf7dyhU";
        String downloadDir = "/Users/cristian/";

        try {
            Map<String, String> urlParams = splitQuery(new URL(url));
            String videoId = urlParams.get("v");

            YoutubeDownloader downloader = new YoutubeDownloader();
            YoutubeVideo video = downloader.getVideo(videoId);

            VideoDetails details = video.details();
            System.out.println(details.title());

            List<AudioVideoFormat> videoWithAudioFormats = video.videoWithAudioFormats();
            videoWithAudioFormats.forEach(it -> {
                System.out.println(it.audioQuality() + "/" + it.videoQuality() + " : " + it.url());
            });

            File outputDir = new File(downloadDir);
            Format format = videoWithAudioFormats.get(videoWithAudioFormats.size() - 1);

            // sync downloading
            File file = video.download(format, outputDir);

        } catch (Exception e) {
            e.printStackTrace();
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
