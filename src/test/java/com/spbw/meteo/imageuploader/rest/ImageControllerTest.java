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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
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
}