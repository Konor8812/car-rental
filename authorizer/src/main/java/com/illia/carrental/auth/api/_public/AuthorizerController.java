package com.illia.carrental.auth.api._public;

import com.illia.carrental.auth.dto.request.LoginUserRequest;
import com.illia.carrental.auth.dto.request.RegisterUserRequest;
import com.illia.carrental.auth.dto.response.AuthenticationResponse;
import com.illia.carrental.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthorizerController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginUserRequest loginUserRequest) {
        return ResponseEntity.ok(authenticationService.login(loginUserRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterUserRequest registerUserRequest) {
        return ResponseEntity.ok(authenticationService.createUser(registerUserRequest));
    }

    // todo: not delete but set inactive
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorizationToken) {
        authenticationService.logout(authorizationToken);
        return ResponseEntity.noContent().build();
    }

}
