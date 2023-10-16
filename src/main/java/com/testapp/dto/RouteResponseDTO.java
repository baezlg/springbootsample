package com.testapp.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteResponseDTO {

    private Long routeId;
    private String routeName;
    private String origin;
    private String destination;
    private Double distance;
    private Boolean enabled;
}
