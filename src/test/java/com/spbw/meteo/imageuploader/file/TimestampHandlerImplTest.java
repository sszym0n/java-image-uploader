package com.spbw.meteo.imageuploader.file;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TimestampHandlerImplTest {
    @Autowired
    TimestampHandler timestampHandler;

    @Test
    void shouldReturnFalseWhenFileNotExist() {
        File tempFile = Path.of("dummy.file").toFile();
        assertFalse(timestampHandler.addTimestamp(tempFile));
    }

    @Test
    void shouldReturnFalseWhenFileIsNotImage() throws IOException {
        File tempFile = File.createTempFile("test", "image");
        assertFalse(timestampHandler.addTimestamp(tempFile));
        tempFile.delete();
    }

    @Test
    void shouldReturnTrueWhenFileIsImage() throws IOException {
        File tempFile = File.createTempFile("test", "image");
        Files.copy(getClass().getClassLoader().getResourceAsStream("blank.jpg"), tempFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        assertTrue(timestampHandler.addTimestamp(tempFile));
        tempFile.delete();
    }
}