package com.nyc.googlenowfeed.models2;

/**
 * Created by D on 12/16/17.
 */

public class SpaceStationModels {

    private String message;
    private String timestamp;
    private Iss_Position iss_position;

    public SpaceStationModels(){

    }

    public SpaceStationModels(String message, String timestamp, Iss_Position iss_position) {
        this.message = message;
        this.timestamp = timestamp;
        this.iss_position = iss_position;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Iss_Position iss_position() {
        return iss_position;
    }
}
