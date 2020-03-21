package com.spbw.meteo.imageuploader.image;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ImageRepositoryImplTest {
    @Autowired
    ImageRepository imageRepository;

    @Test
    void saveImagePart() {
        assertFalse(imageRepository.saveImagePart(ImagePart.builder().build()));
    }

    @Test
    void getImage() {
        assertNull(imageRepository.getImage("dummy"));
    }
}