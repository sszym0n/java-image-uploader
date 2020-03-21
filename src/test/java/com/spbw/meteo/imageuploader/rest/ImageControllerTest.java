package com.spbw.meteo.imageuploader.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spbw.meteo.imageuploader.image.ImagePart;
import com.spbw.meteo.imageuploader.image.ImageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
public class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ImageRepository imageRepository;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/hello")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("SPBW File Uploader")));
    }

    @Test
    public void shouldReturnOkWhenJsonCorrect() throws Exception {
        ImagePart part = ImagePart.builder().index(1).numOfChunks(5).stationName("station").data("data").build();
        when(imageRepository.saveImagePart(part)).thenReturn(true);

        this.mockMvc.perform(
                post("/json")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(part)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnErrorWhenJsonIncorrect() throws Exception {
        ImagePart part = ImagePart.builder().index(1).numOfChunks(5).stationName("station").data("data").build();
        when(imageRepository.saveImagePart(part)).thenReturn(true);

        this.mockMvc.perform(
                post("/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("not a json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnErrorWhenImagePartIncorrect() throws Exception {
        ImagePart part = ImagePart.builder().index(1).numOfChunks(5).stationName("station").data("data").build();
        when(imageRepository.saveImagePart(part)).thenReturn(false);

        this.mockMvc.perform(
                post("/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(part)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnErrorForPut() throws Exception {
        this.mockMvc.perform(put("/json")).andDo(print()).andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void shouldReturnErrorForMissingStation() throws Exception {
        this.mockMvc.perform(get("/image")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnErrorForUnknownStation() throws Exception {
        when(imageRepository.getImage(any())).thenReturn(null);
        this.mockMvc.perform(get("/image/none")).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("No image for station")));
    }

    @Test
    public void shouldReturnBytesForExistingStation() throws Exception {
        when(imageRepository.getImage("some")).thenReturn("abcdef".getBytes());
        this.mockMvc.perform(get("/image/some")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().bytes("abcdef".getBytes()));
        verify(imageRepository, times(1)).getImage("some");
    }
}