package com.example.ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadImage(String filePath, MultipartFile file);
}
