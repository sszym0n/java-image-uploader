package com.spbw.meteo.imageuploader.image;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ImageRepositoryImpl implements ImageRepository {
    @Override
    public void saveImagePart(ImagePart part) {
    }

    @Override
    public File getImage(String name) {
        return null;
    }
}
