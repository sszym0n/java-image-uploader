package com.spbw.meteo.imageuploader.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImagePart {
    private int index;
    @JsonProperty("num_of_chunks")
    private int numOfChunks;
    @JsonProperty("station_name")
    private String stationName;
    private String data;
}
