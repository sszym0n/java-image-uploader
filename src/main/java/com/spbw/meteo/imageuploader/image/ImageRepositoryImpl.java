package com.spbw.meteo.imageuploader.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;

@Repository
public class ImageRepositoryImpl implements ImageRepository {
    Logger logger = LoggerFactory.getLogger(ImageRepositoryImpl.class);

    @Override
    public boolean saveImagePart(ImagePart part) {
        return false;
    }

    @Override
    public File getImage(String name) {
        return null;
    }
}
