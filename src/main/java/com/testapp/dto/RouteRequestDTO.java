package com.testapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RouteRequestDTO {

    private String routeName;

    private String origin;

    private String destination;

    private Double distance;

    private Boolean enabled;
}
