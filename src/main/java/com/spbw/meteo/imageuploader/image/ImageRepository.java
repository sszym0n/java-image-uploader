package com.spbw.meteo.imageuploader.image;

import java.io.File;

public interface ImageRepository {
    boolean saveImagePart(ImagePart part);
    byte[] getImage(String name);
}
