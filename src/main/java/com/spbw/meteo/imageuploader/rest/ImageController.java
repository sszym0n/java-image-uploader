package com.spbw.meteo.imageuploader.rest;

import com.spbw.meteo.imageuploader.image.ImagePart;
import com.spbw.meteo.imageuploader.image.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@ConfigurationProperties
public class ImageController {
    @Value("${spring.application.name}")
    String applicationName;

    @Autowired
    private ImageRepository imageRepository;

    Logger logger = LoggerFactory.getLogger(ImageController.class);

    @GetMapping("/hello")
    public String helloWorld(HttpServletRequest request) {
        logger.debug(String.format("Got request from: %s", request.getRemoteHost()));
        return applicationName;
    }

    @PostMapping("/json")
    public ResponseEntity<?> uploadImage(@RequestBody ImagePart imagePart) {
        if (imageRepository.saveImagePart(imagePart)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
