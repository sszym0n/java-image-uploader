package com.spbw.meteo.imageuploader.image;

import java.io.File;

public interface ImageRepository {
    void saveImagePart(ImagePart part);
    File getImage(String name);
}
