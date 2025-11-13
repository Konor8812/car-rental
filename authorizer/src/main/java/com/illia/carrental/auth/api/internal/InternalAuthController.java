package com.illia.carrental.auth.api.internal;

import com.illia.carrental.auth.dto.UserDTO;
import com.illia.carrental.auth.dto.request.AuthenticateUserRequest;
import com.illia.carrental.auth.service.AuthenticationService;
import com.illia.carrental.auth.service.ClientSecretValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/internal")
@RequiredArgsConstructor
public class InternalAuthController {

    private final AuthenticationService authenticationService;
    private final ClientSecretValidator clientSecretValidator;

    @PostMapping("/auth")
    public ResponseEntity<UserDTO> authenticateUser(@RequestHeader("client-secret") String clientSecret,
                                                    @RequestBody AuthenticateUserRequest authenticateUserRequest) {
        clientSecretValidator.validate(clientSecret);
        var auth = authenticationService.authenticate(authenticateUserRequest);
        return ResponseEntity.ok(auth);
    }

}
