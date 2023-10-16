package com.testapp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testapp.dto.RouteRequestDTO;
import com.testapp.dto.RouteResponseDTO;
import com.testapp.entity.Route;

public class ValueMapper {

    public static Route convertToEntity(RouteRequestDTO routeRequestDTO){
        Route route = new Route();
        route.setRouteName(routeRequestDTO.getRouteName());
        route.setOrigin(routeRequestDTO.getOrigin());
        route.setDestination(routeRequestDTO.getDestination());
        route.setDistance(routeRequestDTO.getDistance());
        route.setEnabled(routeRequestDTO.getEnabled());
        return route;
    }

    public static RouteResponseDTO convertToDTO(Route route){
        RouteResponseDTO routeResponseDTO = new RouteResponseDTO();
        routeResponseDTO.setRouteId(route.getRouteId());
        routeResponseDTO.setRouteName(route.getRouteName());
        routeResponseDTO.setOrigin(route.getOrigin());
        routeResponseDTO.setDestination(route.getDestination());
        routeResponseDTO.setDistance(route.getDistance());
        routeResponseDTO.setEnabled(route.getEnabled());
        return routeResponseDTO;
    }

    public static String jsonAsString(Object obj){
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
