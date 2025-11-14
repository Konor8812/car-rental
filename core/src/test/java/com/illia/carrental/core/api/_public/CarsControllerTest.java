package com.illia.carrental.core.api._public;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.illia.carrental.core.commons.annotation.CurrentUser;
import com.illia.carrental.core.model.dto.*;
import com.illia.carrental.core.model.dto.response.*;
import com.illia.carrental.core.service.CarRentalService;
import com.illia.carrental.core.service.AuthenticationService;
import com.illia.carrental.core.commons.interceptor.UserInterceptor;
import com.illia.carrental.core.commons.annotation.resolver.CurrentUserArgumentResolver;
import com.illia.carrental.core.commons.context.RequestUserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarsController.class)
@Import({UserInterceptor.class, CurrentUserArgumentResolver.class, RequestUserContext.class})
class CarsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarRentalService carRentalService;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Autowired
    private RequestUserContext userContext;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO testUser;

    @BeforeEach
    void setup() {
        testUser = new UserDTO(1L, "john@doe.com", "Doe");
    }

    // --- getAllCars ---
    @Test
    void getAllCars_shouldReturnPagedCars() throws Exception {
        var car = new CarDTO(1L, "SUV", "Toyota", "RAV4", 2022,
                new RentalDetailsDTO(BigDecimal.valueOf(100)),
                List.of(new CarReviewDTO(5, "Great car!")));

        var response = new GetAllCarsResponse(List.of(car), 0, 10, 1L);

        when(carRentalService.getAllCars(0, 10)).thenReturn(response);

        mockMvc.perform(get("/v1/public/cars")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cars[0].manufacturer").value("Toyota"))
                .andExpect(jsonPath("$.cars[0].rentalDetails.rate").value(100))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(carRentalService).getAllCars(0, 10);
    }

    // --- getCarInfo ---
    @Test
    void getCarInfo_shouldReturnCarDetails() throws Exception {
        var car = new CarDTO(2L, "Sedan", "BMW", "3 Series", 2021,
                new RentalDetailsDTO(BigDecimal.valueOf(200)), List.of());
        var response = new GetCarInfoResponse(car, true);

        when(carRentalService.getCarInfo(2L)).thenReturn(response);

        mockMvc.perform(get("/v1/public/cars/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.car.model").value("3 Series"))
                .andExpect(jsonPath("$.available").value(true));

        verify(carRentalService).getCarInfo(2L);
    }

    // --- getCurrentRentRate ---
    @Test
    void getCurrentRentRate_shouldReturnRate() throws Exception {
        var rateResponse = new GetRateResponse(BigDecimal.valueOf(150));

        when(carRentalService.getCurrentRentRate(3L)).thenReturn(rateResponse);

        mockMvc.perform(post("/v1/public/cars/3/rate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(150));

        verify(carRentalService).getCurrentRentRate(3L);
    }

    // --- addReview ---
    @Test
    void addReview_shouldAddReviewWithResolvedUser() throws Exception {
        // Simulate token-based user resolution
        when(authenticationService.authUserByHeader("Bearer token")).thenReturn(testUser);

        var request = new AddReviewRequest(5, "Excellent ride!");

        mockMvc.perform(post("/v1/public/cars/5/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer token")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(authenticationService).authUserByHeader("Bearer token");
        verify(carRentalService).addReview(eq(testUser), eq(5L), any(AddReviewRequest.class));
    }
}
