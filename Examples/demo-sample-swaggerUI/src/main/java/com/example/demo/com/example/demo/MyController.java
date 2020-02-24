package com.example.demo.com.example.demo;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
    public class MyController {

        @GetMapping(value = "/upload", consumes = "multipart/form-data")
        public String upload(@RequestPart("file") MultipartFile file, 
        		@RequestPart("user") User user) {
            return user + "\n" + file.getOriginalFilename() + "\n" + file.getSize();
        }

    }