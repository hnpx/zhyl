package com.px.modulars.device.entity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "经纬度")
public class ScopeRequest {

    private Double longitude;

    private Double latitude;

    public ScopeRequest() {
    }

    public ScopeRequest(Double longitude, Double latitude) {

        this.longitude = longitude;

        this.latitude = latitude;

    }
}
