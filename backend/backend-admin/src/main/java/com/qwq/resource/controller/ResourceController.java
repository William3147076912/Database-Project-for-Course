package com.qwq.resource.controller;

import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qwq.common.config.RuoYiConfig;
import com.qwq.common.constant.Constants;
import com.qwq.resource.domain.Video;
import com.qwq.resource.domain.vo.ResourceVO;
import com.qwq.resource.mapper.VideoMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qwq.common.annotation.Log;
import com.qwq.common.core.controller.BaseController;
import com.qwq.common.core.domain.AjaxResult;
import com.qwq.common.enums.BusinessType;
import com.qwq.resource.domain.Resource;
import com.qwq.resource.service.IResourceService;
import com.qwq.common.utils.poi.ExcelUtil;
import com.qwq.common.core.page.TableDataInfo;

/**
 * 教学资源Controller
 *
 * @author william
 * @date 2025-03-09
 */
@RestController
@RequestMapping("/resource/resource")
public class ResourceController extends BaseController {
    @Autowired
    private IResourceService resourceService;

    /**
     * 查询教学资源列表
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:list')")
    @GetMapping("/list")
    public TableDataInfo list(Resource resource) {
        startPage();
        List<ResourceVO> list = resourceService.selectResourceList(resource);
        return getDataTable(list);
    }

    /**
     * 导出教学资源列表
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:export')")
    @Log(title = "教学资源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Resource resource) {
        List<ResourceVO> list = resourceService.selectResourceList(resource);
        ExcelUtil<ResourceVO> util = new ExcelUtil<>(ResourceVO.class);
        util.exportExcel(response, list, "教学资源数据");
    }

    /**
     * 获取教学资源详细信息
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:query')")
    @GetMapping(value = "/{resourceId}")
    public AjaxResult getInfo(@PathVariable("resourceId") Long resourceId) {
        return success(resourceService.selectResourceByResourceId(resourceId));
    }
    @javax.annotation.Resource
    @Autowired
    com.qwq.resource.mapper.VideoMapper VideoMapper;
    @GetMapping("/video/{videoId}")
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
//            log.info("start:" + startByte + ",end:" + endByte);

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


    /**
     * 新增教学资源
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:add')")
    @Log(title = "教学资源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Resource resource) {
        return toAjax(resourceService.insertResource(resource));
    }

    /**
     * 修改教学资源
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:edit')")
    @Log(title = "教学资源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Resource resource) {
        return toAjax(resourceService.updateResource(resource));
    }

    /**
     * 删除教学资源
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:remove')")
    @Log(title = "教学资源", businessType = BusinessType.DELETE)
    @DeleteMapping("/{resourceIds}")
    public AjaxResult remove(@PathVariable Long[] resourceIds) {
        return toAjax(resourceService.deleteResourceByResourceIds(resourceIds));
    }
}
