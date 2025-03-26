package com.qwq.resource.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

//视频数据库实体类
@Data
public class Video{
    private int VideoId;
    private String videoName;
    private String videoUrl;
}

