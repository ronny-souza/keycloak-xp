package br.com.marinholab.keycloakxp.core.controller;

import br.com.marinholab.keycloakxp.core.model.LoginResponseDTO;
import br.com.marinholab.keycloakxp.core.model.UserDTO;
import br.com.marinholab.keycloakxp.core.model.operations.CreateUserForm;
import br.com.marinholab.keycloakxp.core.model.operations.UserLoginForm;
import br.com.marinholab.keycloakxp.core.model.operations.UserLogoutForm;
import br.com.marinholab.keycloakxp.core.service.AuthenticationService;
import br.com.marinholab.keycloakxp.core.service.CreateUserService;
import br.com.marinholab.keycloakxp.exception.CreateUserException;
import br.com.marinholab.keycloakxp.exception.UserAuthenticationException;
import br.com.marinholab.keycloakxp.exception.UserLogoutException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller", description = "Controller for managing all user related operations.")
public class UserController {

    private final CreateUserService createUserService;
    private final AuthenticationService authenticationService;

    public UserController(CreateUserService createUserService, AuthenticationService authenticationService) {
        this.createUserService = createUserService;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Create a user in Keycloak.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The user has been created and is available in Keycloak."
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "The information sent to the user may be incorrect."
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "The user could not be created in Keycloak due to some internal error."
            )
    })
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserForm form,
                                              UriComponentsBuilder uriComponentsBuilder) throws CreateUserException {
        UserDTO response = this.createUserService.createUser(form);
        return ResponseEntity
                .created(uriComponentsBuilder.path("/user/{id}").buildAndExpand(response.id()).toUri())
                .body(response);
    }

    @Operation(summary = "Authenticate a user in Keycloak.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The user has been successfully authenticated in Keycloak."
            ),

            @ApiResponse(
                    responseCode = "401",
                    description = "The user's credentials are invalid and Keycloak could not authenticate the user."
            ),
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody UserLoginForm form) throws UserAuthenticationException {
        LoginResponseDTO response = this.authenticationService.login(form);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Ends a user's session in Keycloak.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "The user session was successfully disconnected from Keycloak."
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Unable to log out the user from Keycloak. This could be an internal issue or invalid credentials."
            ),
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Jwt jwt,
                                       @Valid @RequestBody UserLogoutForm form) throws UserLogoutException {
        this.authenticationService.logout(form, jwt);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retrieves basic information of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The user has a valid session and their information could be recovered."
            ),

            @ApiResponse(
                    responseCode = "401",
                    description = "The user's session is invalid and your information could not be retrieved."
            ),
    })
    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrentAuthenticatedUser(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(new UserDTO(jwt));
    }
}
