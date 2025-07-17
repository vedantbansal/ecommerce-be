package com.example.ecommerce.service;

import com.example.ecommerce.constants.ProductConstants;
import com.example.ecommerce.exceptions.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uploadImage(String filePath, MultipartFile file){
        File folder = new File(filePath);
        if(!folder.exists()){
            if(!folder.mkdir()){
                throw new ApiException("Unable to create folder");
            }
        }
        String randomId = UUID.randomUUID().toString();
        String originalFileName = file.getOriginalFilename();
        String fileName =  randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));
        String destinationPath = filePath + File.separator + fileName;
        try(InputStream in = file.getInputStream()) {
            Files.copy(in, Paths.get(destinationPath));
        }catch (Exception e){
            fileName = ProductConstants.DEFAULT_PRODUCT_IMAGE_NAME;
        }
        return fileName;
    }
}
