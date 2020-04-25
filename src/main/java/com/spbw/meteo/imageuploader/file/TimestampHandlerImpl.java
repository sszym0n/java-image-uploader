package com.spbw.meteo.imageuploader.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TimestampHandlerImpl implements TimestampHandler {
    @Value("${spring.application.images.timestamp.format}")
    String DATE_TIME_FORMAT;
    @Value("${spring.application.images.timestamp.timezone}")
    String STATION_TIMEZONE;
    @Value("${spring.application.images.timestamp.font.type}")
    String TIMESTAMP_FONT;
    @Value("${spring.application.images.timestamp.font.size}")
    int TIMESTAMP_SIZE;

    Logger logger = LoggerFactory.getLogger(TimestampHandlerImpl.class);

    @Override
    public boolean addTimestamp(File file) {
            String timestamp = DateTimeFormatter
                    .ofPattern(DATE_TIME_FORMAT)
                    .format(ZonedDateTime.now(ZoneId.of(STATION_TIMEZONE)));
            logger.info("Adding timestamp {} to file {}", timestamp, file);
            try {
                BufferedImage image = ImageIO.read(file);
                Graphics g = image.getGraphics();
                g.setColor(Color.RED);
                g.setFont(new Font(TIMESTAMP_FONT, Font.BOLD, TIMESTAMP_SIZE));
                g.drawString(timestamp, 20, 10 + TIMESTAMP_SIZE);
                g.dispose();

                return ImageIO.write(image, "jpg", file);
            } catch (Exception e) {
                logger.error("Something went wrong", e);
            }
            return false;
        }
}