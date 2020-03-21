package com.spbw.meteo.imageuploader.file;

import com.spbw.meteo.imageuploader.image.ImagePart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.Base64;

import static org.springframework.util.StringUtils.hasText;

@Service
public class ImageHandler {
    private Logger logger = LoggerFactory.getLogger(ImageHandler.class);

    @Value("${spring.application.images.location}")
    String imagesLocation;

    private final String JPG_SUFFIX = ".jpg";
    private final String TMP_SUFFIX = ".tmp";


    public boolean writePart(ImagePart imagePart) {
        logger.debug("Writing part: {}", imagePart);
        if (imagePart.getIndex() < 0 || imagePart.getIndex() > imagePart.getNumOfChunks()) {
            logger.error("Incorrect data: index({}), chunks({})", imagePart.getIndex(), imagePart.getNumOfChunks());
            return false;
        }
        if (!hasText(imagePart.getStationName())) {
            logger.error("Station name is missing");
            return false;
        }
        if (!hasText(imagePart.getData())) {
            logger.error("Data is missing");
            return false;
        }
        File tmpFile = getTempFile(imagePart.getStationName());
        if (!createImagesDirectory()) {
            return false;
        }
        try (FileOutputStream fos  = new FileOutputStream(tmpFile, true)) {
            logger.debug("Writing file content");
            fos.write(Base64.getDecoder().decode(imagePart.getData()));
        } catch (IOException e) {
            logger.error("File handling problem", e);
            return false;
        }
        if (imagePart.getIndex() == imagePart.getNumOfChunks()) {
            File dstFile = getJpgFile(imagePart.getStationName());
            logger.info("Moving {} to {}", tmpFile, dstFile);
            if (dstFile.exists()) {
                logger.info("Destination file exists, removing it");
                if (!dstFile.delete())
                    logger.error("Removing {} failed!", dstFile);
            }
            if (!tmpFile.renameTo(dstFile)) {
                logger.error("Moving of {} to {} failed!", tmpFile, dstFile);
            }
        }
        return true;
    }

    public byte[] readImage(String stationName) {
        logger.debug("Retrieving image for station {}", stationName);
        byte[] result = null;

        if (StringUtils.hasText(stationName)) {
            File imageFile = getJpgFile(stationName);
            logger.debug("Look for image {}", imageFile);
            if (imageFile.exists()) {
                try (InputStream fis = new FileInputStream(imageFile)) {
                    result = fis.readAllBytes();
                } catch (IOException e) {
                    logger.error("Error reading file", e);
                }
            }
        }
        return result;
    }

    private boolean createImagesDirectory() {
        File dir = Path.of(imagesLocation).toFile();
        if (!dir.exists()) {
            logger.info("Creating {} directory", dir);
            if (!dir.mkdir()) {
                logger.error("Directory creation failed!");
                return false;
            }
        }
        return true;
    }

    private File getTempFile(String stationName) {
        return Path.of(imagesLocation, File.separator, stationName + TMP_SUFFIX).toFile();
    }

    private File getJpgFile(String stationName) {
        return Path.of(imagesLocation, File.separator, stationName + JPG_SUFFIX).toFile();
    }
}
