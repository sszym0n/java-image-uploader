package com.spbw.meteo.imageuploader.rest;

import com.spbw.meteo.imageuploader.image.ImagePart;
import com.spbw.meteo.imageuploader.image.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        logger.info("Got hello request from: {}", request.getRemoteHost());
        return applicationName;
    }

    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadImage(@RequestBody ImagePart imagePart, HttpServletRequest request) {
        logger.info("Got JSON POST request from: {}:{}. Station: {}, part {} of {}", request.getRemoteHost(),
                request.getRemotePort(), imagePart.getStationName(), imagePart.getIndex(), imagePart.getNumOfChunks());
        if (imageRepository.saveImagePart(imagePart)) {
            logger.debug("Returning OK");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        logger.debug("Something went wrong, returning error");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/image/{station}")
    public ResponseEntity<?> getImage(@PathVariable String station, HttpServletRequest request) {
        logger.info("Got GET request from {}:{} for station {}", request.getRemoteHost(),
                request.getRemotePort(), station);
        byte[] bytes = imageRepository.getImage(station);
        if (bytes != null) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        } else {
            return ResponseEntity.badRequest().body("No image for station");
        }
    }
}
