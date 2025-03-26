package com.qwq.resource.controller;


import com.qwq.common.config.RuoYiConfig;
import com.qwq.common.constant.Constants;
import com.qwq.resource.domain.Video;
import com.qwq.resource.mapper.VideoMapper;

import com.qwq.resource.service.NonStaticResourceHttpRequestHandler;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//前端获取后端视频流
@RestController
@RequestMapping("/SelectVideo")
@AllArgsConstructor
@Slf4j
public class VideoController
{
    //引入返回视频流的组件
    private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;
    //把后端链接数据库接口引入进来
    @Resource
    @Autowired
    VideoMapper VideoMapper;

    @GetMapping("/getVideoSizeById/{videoId}")
    public long getVideoSizeById(@PathVariable("videoId") Integer videoId) throws IOException
    {
        Video videoPathList = VideoMapper.SelectVideoById(videoId);
        String videoPathUrl = videoPathList.getVideoUrl();
        Path filePath = Paths.get(videoPathUrl);

        if (Files.exists(filePath))
        {
            return Files.size(filePath);
        }
        return 0L;
    }

    //查询视频流的接口
    @GetMapping("/{videoId}")
    public void videoPreview(HttpServletRequest request, HttpServletResponse response, @PathVariable("videoId") String videoId) throws Exception
    {


//        if (!FileUtils.checkAllowDownload(resource))
//        {
//            throw new Exception(com.qwq.common.utils.StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
//        }
        // 本地资源路径
        String localPath = RuoYiConfig.getProfile();
        Video videoPathList = VideoMapper.SelectVideoById(Integer.parseInt(videoId));
        String videoPathUrl = videoPathList.getVideoUrl();
        String downloadPath = localPath + com.qwq.common.utils.StringUtils.substringAfter(videoPathUrl, Constants.RESOURCE_PREFIX);

        Path filePath = Paths.get(downloadPath);

        if (Files.exists(filePath))
        {
            String mimeType = Files.probeContentType(filePath);
            if (StringUtils.hasText(mimeType))
            {
                response.setContentType(mimeType);
            }

            // 设置支持部分请求（范围请求）的 'Accept-Ranges' 响应头
            response.setHeader("Accept-Ranges", "bytes");

            // 从请求头中获取请求的视频片段的范围（如果提供）
            long startByte = 0;
            long endByte = Files.size(filePath) - 1;
            String rangeHeader = request.getHeader("Range");
            // System.out.println("rangeHeader:" + rangeHeader);
            if (rangeHeader != null && rangeHeader.startsWith("bytes="))
            {
                String[] range = rangeHeader.substring(6).split("-");
                startByte = Long.parseLong(range[0]);
                if (range.length == 2)
                {
                    endByte = Long.parseLong(range[1]);
                }
            }

            // System.out.println("start:" + startByte + ",end:" + endByte);
            log.info("start:" + startByte + ",end:" + endByte);

            // 设置 'Content-Length' 响应头，指示正在发送的视频片段的大小
            long contentLength = endByte - startByte + 1;
            response.setHeader("Content-Length", String.valueOf(contentLength));

            // 设置 'Content-Range' 响应头，指示正在发送的视频片段的范围
            response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + Files.size(filePath));

            // 设置响应状态为 '206 Partial Content'
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

            // 使用 'RangeFileChannel' 进行视频片段的传输，以高效地只读取文件的请求部分
            ServletOutputStream outputStream = response.getOutputStream();
            try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r"); FileChannel fileChannel = file.getChannel())
            {
                fileChannel.transferTo(startByte, contentLength, Channels.newChannel(outputStream));
            } finally
            {
                outputStream.close();
            }
        } else
        {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

}


