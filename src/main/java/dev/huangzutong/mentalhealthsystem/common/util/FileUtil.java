package dev.huangzutong.mentalhealthsystem.common.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件工具类
 */
public class FileUtil {
    private static final String AVATAR_DIR = "avatar";

    /**
     * 保存文件到项目avatar目录
     * @param file 文件
     * @return 文件保存路径
     */
    public static String save(MultipartFile file){
        String extension = getString(file);
        // 生成唯一文件名（避免重名）
        String safeFileName = UUID.randomUUID().toString() + extension;
        // 构建完整路径
        Path filePath = Paths.get(AVATAR_DIR, safeFileName);
        try {
            // 保存文件
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("文件保存失败", e);
        }

        // 返回相对路径（或根据需求返回 URL）
        return filePath.toString(); // 例如：avatar/123e4567-e89b-12d3-a456-426614174000.png
    }

    /**
     * 获取文件扩展名
     * @param file 文件
     * @return 文件扩展名
     */
    private static String getString(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传的文件为空");
        }
        // 确保 avatar 目录存在
        File dir = new File(AVATAR_DIR);
        if (!dir.exists()) {
            dir.mkdirs(); // 创建多级目录

        }
        // 原始文件名（带扩展名）
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return extension;
    }
}
