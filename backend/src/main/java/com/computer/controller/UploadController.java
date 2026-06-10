package com.computer.controller;

import com.computer.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Value("${file.upload-path}")
    private String uploadPath;
    
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp");

    @PostMapping("")
    public Result upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件为空");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.contains(".")) {
            return Result.error("请上传有效的图片文件");
        }

        String ext = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
        
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            return Result.error("请上传有效的图片文件");
        }

        String fileName = UUID.randomUUID().toString().replace("-", "") + ext;
        Path basePath = Paths.get(uploadPath).toAbsolutePath().normalize();
        File dest = basePath.resolve(fileName).toFile();
        File parent = dest.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            return Result.error("上传失败: 无法创建上传目录");
        }

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            return Result.error("上传失败: " + e.getMessage());
        }
        return Result.success("/uploads/" + fileName);
    }
}
