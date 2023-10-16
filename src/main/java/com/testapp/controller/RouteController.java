package com.testapp.controller;

import com.testapp.dto.APIResponse;
import com.testapp.dto.RouteRequestDTO;
import com.testapp.dto.RouteResponseDTO;
import com.testapp.service.RouteService;
import com.testapp.util.ValueMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
@AllArgsConstructor
@Slf4j
public class RouteController {

    public static final String SUCCESS = "Success";
    private RouteService routeService;

    @PostMapping
    public ResponseEntity<APIResponse> createNewRoute(@RequestBody @Valid RouteRequestDTO routeRequestDTO) {

        log.info("RouteController::createNewRoute request body {}", ValueMapper.jsonAsString(routeRequestDTO));

        RouteResponseDTO routeResponseDTO = routeService.createNewRoute(routeRequestDTO);

        APIResponse<RouteResponseDTO> responseDTO = APIResponse
                .<RouteResponseDTO>builder()
                .status(SUCCESS)
                .results(routeResponseDTO)
                .build();

        log.info("RouteController::createNewRoute response {}", ValueMapper.jsonAsString(responseDTO));

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIResponse> getRoutes() {

        List<RouteResponseDTO> routes = routeService.getRoutes();
        APIResponse<List<RouteResponseDTO>> responseDTO = APIResponse
                .<List<RouteResponseDTO>>builder()
                .status(SUCCESS)
                .results(routes)
                .build();

        log.info("RouteController::getRoutes response {}", ValueMapper.jsonAsString(responseDTO));

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<?> getRoute(@PathVariable long routeId) {

        log.info("RouteController::getRoute by id  {}", routeId);

        RouteResponseDTO routeResponseDTO = routeService.getRouteById(routeId);
        APIResponse<RouteResponseDTO> responseDTO = APIResponse
                .<RouteResponseDTO>builder()
                .status(SUCCESS)
                .results(routeResponseDTO)
                .build();

        log.info("RouteController::getRoute by id  {} response {}", routeId,ValueMapper
                .jsonAsString(routeResponseDTO));

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/{routeId}")
    public ResponseEntity<APIResponse> updateRoute(
            @PathVariable Long routeId,
            @RequestBody @Valid RouteRequestDTO routeRequestDTO
    ) {
        log.info("RouteController::updateRoute called for routeId: {}", routeId);

            RouteResponseDTO updatedRoute = routeService.updateRoute(routeId, routeRequestDTO);

            APIResponse<RouteResponseDTO> responseDTO = APIResponse
                    .<RouteResponseDTO>builder()
                    .status(SUCCESS)
                    .results(updatedRoute)
                    .build();

            log.info("RouteController::updateRoute response: {}", ValueMapper.jsonAsString(updatedRoute));

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{routeId}")
    public ResponseEntity<APIResponse> deleteRoute(@PathVariable Long routeId) {
            log.info("RouteController::deleteRoute called for routeId: {}", routeId);
            routeService.deleteRoute(routeId);
            APIResponse<String> responseDTO = APIResponse
                    .<String>builder()
                    .status(SUCCESS)
                    .results("Route deleted successfully")
                    .build();
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
