package com.testapp.service;

import com.testapp.dto.RouteRequestDTO;
import com.testapp.dto.RouteResponseDTO;
import com.testapp.entity.Route;
import com.testapp.exception.RouteNotFoundException;
import com.testapp.exception.RouteServiceBusinessException;
import com.testapp.repository.RouteRepository;
import com.testapp.util.ValueMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class RouteService {

    private RouteRepository routeRepository;
    private CacheManager cacheManager;

    public RouteResponseDTO createNewRoute(RouteRequestDTO routeRequestDTO) throws RouteServiceBusinessException {

        RouteResponseDTO routeResponseDTO;

        try{
            log.info("RouteService:createNewRoute execution started.");
            Route route = ValueMapper.convertToEntity(routeRequestDTO);
            log.debug("RouteService:createNewRoute request parameters {}", ValueMapper.jsonAsString(routeRequestDTO));

            Route routeResults = routeRepository.save(route);
            routeResponseDTO = ValueMapper.convertToDTO(routeResults);
            log.debug("RouteService:createNewRoute received response from Database {}", ValueMapper.jsonAsString(routeRequestDTO));

        }catch (Exception ex){
            log.error("Exception occurred while persisting route to database , Exception message {}", ex.getMessage());
            throw new RouteServiceBusinessException("Exception occurred while create a new route");
        }

        return routeResponseDTO;
    }

    @Cacheable(value = "route")
    public List<RouteResponseDTO> getRoutes() throws RouteServiceBusinessException {
        List<RouteResponseDTO> routeResponseDTOS = null;

        try {
            log.info("RouteService:getRoutes execution started.");

            List<Route> routeList = routeRepository.findAll();

            if (!routeList.isEmpty()) {
                routeResponseDTOS = routeList.stream()
                        .map(ValueMapper::convertToDTO)
                        .collect(Collectors.toList());
            } else {
                routeResponseDTOS = Collections.emptyList();
            }

            log.debug("RouteService:getRoutes retrieving routes from database  {}", ValueMapper.jsonAsString(routeResponseDTOS));

        } catch (Exception ex) {
            log.error("Exception occurred while retrieving routes from database , Exception message {}", ex.getMessage());
            throw new RouteServiceBusinessException("Exception occurred while fetch all routes from Database");
        }

        log.info("RouteService:getRoutes execution ended.");
        return routeResponseDTOS;
    }

    @Cacheable(value = "route", key = "#routeId")
    public RouteResponseDTO getRouteById(long routeId) {
        RouteResponseDTO routeResponseDTO;

        try {
            log.info("RouteService:getRouteById execution started.");

            Route route = routeRepository.findById(routeId)
                    .orElseThrow(() -> new RouteNotFoundException("Route not found with id " + routeId));
            routeResponseDTO = ValueMapper.convertToDTO(route);

            log.debug("RouteService:getRouteById retrieving route from database for id {} {}", routeId, ValueMapper.jsonAsString(routeResponseDTO));

        } catch (Exception ex) {
            log.error("Exception occurred while retrieving route {} from database , Exception message {}", routeId, ex.getMessage());
            throw new RouteServiceBusinessException("Exception occurred while fetch route from Database " + routeId);
        }

        log.info("RouteService:getRouteById execution ended.");
        return routeResponseDTO;
    }

    public RouteResponseDTO updateRoute(Long routeId, RouteRequestDTO routeRequestDTO) throws RouteServiceBusinessException {
        RouteResponseDTO routeResponseDTO;

        try {
            log.info("RouteService:updateRoute execution started for routeId: {}", routeId);

            Route existingRoute = routeRepository.findById(routeId)
                    .orElseThrow(() -> new RouteServiceBusinessException("Route not found for routeId: " + routeId));

            existingRoute.setRouteName(routeRequestDTO.getRouteName());
            existingRoute.setOrigin(routeRequestDTO.getOrigin());
            existingRoute.setDestination(routeRequestDTO.getDestination());
            existingRoute.setDistance(routeRequestDTO.getDistance());
            existingRoute.setEnabled(routeRequestDTO.getEnabled());

            Route updatedRoute = routeRepository.save(existingRoute);
            routeResponseDTO = ValueMapper.convertToDTO(updatedRoute);

            evictCache("route");

            log.debug("RouteService:updateRoute updated route with routeId: {}", routeId);

        } catch (Exception ex) {
            log.error("Exception occurred while updating route, routeId: {}, Exception message: {}", routeId, ex.getMessage());
            throw new RouteServiceBusinessException("Exception occurred while updating route");
        }

        return routeResponseDTO;
    }

    public void deleteRoute(Long routeId) throws RouteServiceBusinessException {
        try {
            log.info("RouteService:deleteRoute execution started for routeId: {}", routeId);

            Route existingRoute = routeRepository.findById(routeId)
                    .orElseThrow(() -> new RouteServiceBusinessException("Route not found for routeId: " + routeId));

            routeRepository.delete(existingRoute);

            log.debug("RouteService:deleteRoute deleted route with routeId: {}", routeId);

        } catch (Exception ex) {
            log.error("Exception occurred while deleting route, routeId: {}, Exception message: {}", routeId, ex.getMessage());
            throw new RouteServiceBusinessException("Exception occurred while deleting route");
        }
    }

    private void evictCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
            log.debug("Cache evicted: {}", cacheName);
        }
    }

}
