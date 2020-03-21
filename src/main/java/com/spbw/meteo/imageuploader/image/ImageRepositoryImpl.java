package com.spbw.meteo.imageuploader.image;

import com.spbw.meteo.imageuploader.file.ImageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;

@Repository
public class ImageRepositoryImpl implements ImageRepository {

    @Autowired
    ImageHandler imageHandler;

    @Override
    public boolean saveImagePart(ImagePart part) {
        return null != part && imageHandler.writePart(part);
    }

    @Override
    public File getImage(String name) {
        return null;
    }
}
