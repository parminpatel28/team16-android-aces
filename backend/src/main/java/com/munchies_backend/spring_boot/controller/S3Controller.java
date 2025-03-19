package com.munchies_backend.spring_boot.controller;
import com.munchies_backend.spring_boot.services.S3Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {


    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/presigned-url")
    public Map<String, String> getPresignedUrl(@RequestParam String fileName, @RequestParam String fileType) {
        String presignedUrl = s3Service.generatePresignedUrl(fileName, fileType);
        Map<String, String> response = new HashMap<>();
        response.put("url", presignedUrl);
        return response;
    }

}
