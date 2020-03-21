package com.spbw.meteo.imageuploader.file;

import com.spbw.meteo.imageuploader.image.ImagePart;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ImageHandlerTest {
    @Autowired
    private ImageHandler imageHandler;

    @MockBean
    private TimestampHandler timestampHandler;

    private List<ImagePart> imageParts;

    @BeforeEach
    public void setUpTest() throws IOException {
        imageParts = new ArrayList<>();
        imageParts.add(ImagePart.builder().index(0).numOfChunks(3).stationName("test").data("00000000").build());
        imageParts.add(ImagePart.builder().index(1).numOfChunks(3).stationName("test").data("11111111").build());
        imageParts.add(ImagePart.builder().index(2).numOfChunks(3).stationName("test").data("22222222").build());
        imageParts.add(ImagePart.builder().index(3).numOfChunks(3).stationName("test").data("33333333").build());

        FileSystemUtils.deleteRecursively(Path.of(imageHandler.IMAGES_LOCATION));
    }

    @AfterEach
    public void tearDown() throws IOException {
        FileSystemUtils.deleteRecursively(Path.of(imageHandler.IMAGES_LOCATION));
    }

    @Test
    void shouldReturnFalseWithNegativeIndex() {
        assertFalse(imageHandler.writePart(ImagePart.builder().index(-1).numOfChunks(1).build()));
    }

    @Test
    void shouldReturnFalseWhenIndexLargerThanNumOfChunks() {
        assertFalse(imageHandler.writePart(ImagePart.builder().index(2).numOfChunks(1).build()));
    }

    @Test
    void shouldReturnFalseWhenStationNameIsEmptyOrNull() {
        assertFalse(imageHandler.writePart(ImagePart.builder().index(0).numOfChunks(1).stationName(null).build()));
        assertFalse(imageHandler.writePart(ImagePart.builder().index(0).numOfChunks(1).stationName("").build()));
    }

    @Test
    void shouldReturnFalseWhenDataIsEmptyOrNull() {
        assertFalse(imageHandler.writePart(ImagePart.builder().index(0).numOfChunks(1)
                .stationName("test").data(null).build()));
        assertFalse(imageHandler.writePart(ImagePart.builder().index(0).numOfChunks(1)
                .stationName("test").data("").build()));
    }

    @Test
    void shouldWriteFirstPart() {
        assertTrue(imageHandler.writePart(imageParts.get(0)));
        File testFile = Path.of(imageHandler.IMAGES_LOCATION, File.separator, "test.tmp").toFile();
        assertTrue(testFile.exists());
        assertEquals(6, testFile.length());
    }

    @Test
    void shouldWriteTwoParts() {
        assertTrue(imageHandler.writePart(imageParts.get(0)));
        assertTrue(imageHandler.writePart(imageParts.get(1)));
        File testFile = Path.of(imageHandler.IMAGES_LOCATION, File.separator, "test.tmp").toFile();
        assertEquals(12, testFile.length());
    }

    @Test
    void shouldWriteAllParts() {
        when(timestampHandler.addTimestamp(any())).thenReturn(true);
        assertTrue(imageHandler.writePart(imageParts.get(0)));
        assertTrue(imageHandler.writePart(imageParts.get(1)));
        assertTrue(imageHandler.writePart(imageParts.get(2)));
        assertTrue(imageHandler.writePart(imageParts.get(3)));
        File testTempFile = Path.of(imageHandler.IMAGES_LOCATION, File.separator, "test.tmp").toFile();
        File testDestinationFile = Path.of(imageHandler.IMAGES_LOCATION, File.separator, "test.jpg").toFile();
        assertFalse(testTempFile.exists());
        assertEquals(24, testDestinationFile.length());
        verify(timestampHandler, times(1)).addTimestamp(any());
    }

    @Test
    void shouldWriteAllPartsWhenDestinationFileExists() throws IOException {
        when(timestampHandler.addTimestamp(any())).thenReturn(true);
        File testTempFile = Path.of(imageHandler.IMAGES_LOCATION, File.separator, "test.tmp").toFile();
        File testDestinationFile = Path.of(imageHandler.IMAGES_LOCATION, File.separator, "test.jpg").toFile();
        createDummyFile(testDestinationFile.getName(), "dummy".getBytes());
        assertTrue(imageHandler.writePart(imageParts.get(0)));
        assertTrue(imageHandler.writePart(imageParts.get(1)));
        assertTrue(imageHandler.writePart(imageParts.get(2)));
        assertTrue(imageHandler.writePart(imageParts.get(3)));
        assertFalse(testTempFile.exists());
        assertEquals(24, testDestinationFile.length());
        verify(timestampHandler, times(1)).addTimestamp(any());
    }

    @Test
    void shouldReturnNullForEmptyStationName() {
        assertNull(imageHandler.readImage(null));
        assertNull(imageHandler.readImage(""));
    }

    @Test
    void shouldReturnNullForUnknownStation() {
        assertNull(imageHandler.readImage("unknown"));
    }

    @Test
    void shouldReturnBytesForExistingStation() throws IOException {
        byte[] bytes = "dummy text".getBytes();
        createDummyFile("known.jpg", bytes);
        assertArrayEquals(imageHandler.readImage("known"), bytes);
    }

    private void createDummyFile(String fileName, byte[] bytes) throws IOException {
        Files.createDirectory(Path.of(imageHandler.IMAGES_LOCATION));
        Files.write(Path.of(imageHandler.IMAGES_LOCATION, File.separator, fileName), bytes);
    }
}