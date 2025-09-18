package com.rajreja.tracking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TrackingResponse {
    @JsonProperty("tracking_number")
    private final String trackingNumber;

    @JsonProperty("created_at")
    private final String createdAt;

    @JsonProperty("generator")
    private final String generator = "snowflake-base36";

    public TrackingResponse(String trackingNumber, String createdAt) {
        this.trackingNumber = trackingNumber;
        this.createdAt = createdAt;
    }
}
