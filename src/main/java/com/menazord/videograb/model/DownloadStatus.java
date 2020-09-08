package com.menazord.videograb.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadStatus {

    int progress;
    boolean finished;
    boolean failed;
}
