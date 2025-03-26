package com.qwq.resource.mapper;
import com.qwq.resource.domain.Video;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface VideoMapper {
    Video SelectVideoById(Integer videoId);
}
