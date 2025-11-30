package dev.huangzutong.mentalhealthsystem.controller;

import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.common.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件相关接口
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {
    /**
     * 上传头像接口
     * @param avatar 头像
     * @return 头像URL
     */
    @PostMapping("/upload-avatar")
    public Result<Map<String, String>> uploadAvatar(MultipartFile avatar) {
        String url = FileUtil.save(avatar);
        return Result.success(Map.of("url", url));
    }
}
