package com.spbw.meteo.imageuploader.rest;

import com.spbw.meteo.imageuploader.image.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    Logger logger = LoggerFactory.getLogger(ImageController.class);

    @GetMapping("/hello")
    public String helloWorld() {
        logger.debug("Got request from");
        return "SPBW image uploader app";
    }

}
