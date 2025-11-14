package com.illia.carrental.core.api._public;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.illia.carrental.core.commons.annotation.resolver.CurrentUserArgumentResolver;
import com.illia.carrental.core.model.dto.UserDTO;
import com.illia.carrental.core.model.dto.request.CreateReservationRequest;
import com.illia.carrental.core.model.dto.response.GetReservationsResponse;
import com.illia.carrental.core.model.dto.response.ReserveCarResponse;
import com.illia.carrental.core.service.CarReservationService;
import com.illia.carrental.core.commons.context.RequestUserContext;
import com.illia.carrental.core.commons.interceptor.UserInterceptor;
import com.illia.carrental.core.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReservationsController.class)
@Import({CurrentUserArgumentResolver.class, UserInterceptor.class, RequestUserContext.class})
class ReservationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RequestUserContext userContext;

    @MockitoBean
    private CarReservationService carReservationService;

    @MockitoBean
    private AuthenticationService authenticationService;

    private UserDTO mockUser;

    @BeforeEach
    void setup() {
        mockUser = new UserDTO(
                10L,
                "john.doe@test.com",
                "John"
        );

        // Interceptor behavior
        Mockito.when(authenticationService.authUserByHeader(anyString()))
                .thenReturn(mockUser);

        // Put into context so @CurrentUser resolves correctly
        Mockito.when(userContext.getUser()).thenReturn(mockUser);
    }

    // -----------------------------------------------------
    // GET /v1/public/reservations
    // -----------------------------------------------------
    @Test
    void testGetReservations() throws Exception {

        GetReservationsResponse response = new GetReservationsResponse(
                Collections.emptyList()
        );

        Mockito.when(carReservationService.getReservationsByUser(any()))
                .thenReturn(response);

        mockMvc.perform(get("/v1/public/reservations")
                        .header("Authorization", "Bearer token123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservations").isArray());
    }

    // -----------------------------------------------------
    // POST /v1/public/reservations
    // -----------------------------------------------------
    @Test
    void testCreateReservation() throws Exception {

        CreateReservationRequest req = new CreateReservationRequest(
                5L,
                2,
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(10)
        );

        ReserveCarResponse response = new ReserveCarResponse("https://payment.link", "awaits_payment");

        Mockito.when(carReservationService.createReservation(any(), any()))
                .thenReturn(response);

        mockMvc.perform(post("/v1/public/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer token123")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentLink").value("https://payment.link"));
    }

    // -----------------------------------------------------
    // POST /v1/public/reservations/{id}/release
    // -----------------------------------------------------
    @Test
    void testReleaseReservation() throws Exception {

        mockMvc.perform(post("/v1/public/reservations/42/release")
                        .header("Authorization", "Bearer token123"))
                .andExpect(status().isNoContent());

        Mockito.verify(carReservationService)
                .releaseReservation(eq(42L), eq(10L));
    }
}
