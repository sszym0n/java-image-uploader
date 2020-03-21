package com.spbw.meteo.imageuploader.image;

import com.spbw.meteo.imageuploader.file.ImageHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ImageRepositoryImplTest {
    @Autowired
    ImageRepository imageRepository;

    @MockBean
    ImageHandler imageHandler;

    @Test
    void shouldReturnFalseWhenImagePartIsNull() {
        assertFalse(imageRepository.saveImagePart(null));
    }

    @Test
    void shouldReturnFalseWhenHandlerFails() {
        when(imageHandler.writePart(any())).thenReturn(false);
        ImagePart part = ImagePart.builder().build();
        assertFalse(imageRepository.saveImagePart(part));
        verify(imageHandler, times(1)).writePart(part);
    }

    @Test
    void shouldReturnTrueWhenHandlerSucceeds() {
        when(imageHandler.writePart(any())).thenReturn(true);
        ImagePart part = ImagePart.builder().build();
        assertTrue(imageRepository.saveImagePart(part));
        verify(imageHandler, times(1)).writePart(part);
    }

    @Test
    void getImage() {
        assertNull(imageRepository.getImage("dummy"));
    }
}