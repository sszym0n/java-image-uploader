package com.spbw.meteo.imageuploader.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Data
@JsonDeserialize(builder = ImagePart.ImageBuilder.class)
@Builder(builderClassName = "ImageBuilder", toBuilder = true)
public class ImagePart {
    private int index;
    @JsonProperty("num_of_chunks")
    private int numOfChunks;
    @JsonProperty("station_name")
    private String stationName;
    private String data;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ImageBuilder {
    }
}
