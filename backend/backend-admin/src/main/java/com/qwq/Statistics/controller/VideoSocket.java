package com.qwq.Statistics.controller;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/video",configurator = GetHttpSessionConfigurator.class)
public class VideoSocket {
    private static final Set<Session> activeSessions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static final int MAX_FRAME_SIZE = 1024 * 1024; // 1MB单帧大小
    private static final int FPS = 25; // 帧率控制

    private Session session;
    private VideoStreamProcessor streamProcessor;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        activeSessions.add(session);
        System.out.println("新连接: " + session.getId());

        // 初始化视频流处理器（示例使用本地文件流）
        streamProcessor = new VideoStreamProcessor("src/main/resources/videos/视频_20250312225536A001.mp4", FPS);
        new Thread(this::startStreaming).start();
    }

    private void startStreaming() {
        try {
            while (session.isOpen()) {
                byte[] frame = streamProcessor.getNextFrame();
                if (frame != null) {
                    sendFrame(frame);
                }
                Thread.sleep(1000 / FPS); // 控制帧率
            }
        } catch (InterruptedException e) {
            System.err.println("流传输中断: " + e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(byte[] message, Session session) {
        // 处理前端控制消息（如暂停/恢复）
        if (message.length == 1 && message[0] == 0x01) {
            streamProcessor.pause();
        } else if (message.length == 1 && message[0] == 0x02) {
            streamProcessor.resume();
        }
    }

    private void sendFrame(byte[] frame) {
        if (session.isOpen() && frame.length <= MAX_FRAME_SIZE) {
            try {
                session.getBasicRemote().sendBinary(ByteBuffer.wrap(frame));
            } catch (IOException e) {
                System.err.println("发送帧失败: " + e.getMessage());
                closeSession();
            }
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        activeSessions.remove(session);
        System.out.println("连接关闭: " + session.getId() + " 原因: " + closeReason.getReasonPhrase());
        streamProcessor.stop();
    }

    @OnError
    public void onError(Throwable error) {
        System.err.println("WebSocket错误: " + error.getMessage());
        closeSession();
    }

    private void closeSession() {
        if (session != null && session.isOpen()) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "连接异常关闭"));
            } catch (IOException e) {
                // 静默处理
            }
        }
    }

    // 视频流处理器（示例实现）
    static class VideoStreamProcessor {
        private final String videoPath;
        private final int fps;
        private boolean isPaused = false;
        private Process ffmpegProcess;

        public VideoStreamProcessor(String videoPath, int fps) {
            this.videoPath = videoPath;
            this.fps = fps;
            initFFmpeg();
        }

        private void initFFmpeg() {
            try {
                String[] command = {
                        "ffmpeg",
                        "-i", videoPath,
                        "-vf", "fps=" + fps,
                        "-f", "image2pipe",
                        "-vcodec", "rawvideo",
                        "-pix_fmt", "yuv420p",
                        "-"
                };
                ffmpegProcess = new ProcessBuilder(command).start();
            } catch (IOException e) {
                throw new RuntimeException("FFmpeg初始化失败", e);
            }
        }

        public byte[] getNextFrame() {
            while (!isPaused) {
                byte[] buffer = new byte[1920 * 1080 * 3 / 2]; // 1080p YUV420P大小
                try {
                    int read = ffmpegProcess.getInputStream().read(buffer);
                    if (read == -1) return null;
                    return java.util.Arrays.copyOf(buffer, read);
                } catch (IOException e) {
                    System.err.println("读取视频帧失败: " + e.getMessage());
                    return null;
                }
            }
            return null;
        }

        public void pause() {
            isPaused = true;
        }

        public void resume() {
            isPaused = false;
        }

        public void stop() {
            if (ffmpegProcess != null) {
                ffmpegProcess.destroy();
            }
        }
    }
}
