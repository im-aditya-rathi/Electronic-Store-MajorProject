package com.asr.project.services.impl;

import com.asr.project.exceptions.BadApiResponseException;
import com.asr.project.exceptions.ResourceNotFoundException;
import com.asr.project.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    private List<String> imageExtension = List.of(".png", ".jpg", ".jpeg");

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        String originalFileName = file.getOriginalFilename();
        logger.info("Original File Name: {}", originalFileName);

        String originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString();
        String fileNameWithExtension = fileName + originalFileExtension;
        String fullPathWithFileName = path + File.separator + fileNameWithExtension;

        if (imageExtension.contains(originalFileExtension.toLowerCase())){

            File folder = new File(path);
            if(!folder.exists()) {
                folder.mkdirs();
            }
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));

            logger.info("File created at location: {}", fullPathWithFileName);
        } else {
            throw new BadApiResponseException("File with this [" + originalFileExtension +
                    "] not allowed !!");
        }

        return fileNameWithExtension;
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {

        String fullPath = path + File.separator + name;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fullPath);
        } catch (FileNotFoundException exception) {
            throw new ResourceNotFoundException("Image not found !!");
        }
        return fileInputStream;
    }

    @Override
    public void deleteFile(String filePath, String fileName) {

        String fullPathName = filePath + File.separator + fileName;
        try {
            Files.delete(Path.of(fullPathName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
