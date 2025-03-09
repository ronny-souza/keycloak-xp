package br.com.marinholab.keycloakxp.core.controller;

import br.com.marinholab.keycloakxp.core.model.AccessTokenDTO;
import br.com.marinholab.keycloakxp.core.model.UserDTO;
import br.com.marinholab.keycloakxp.core.model.common.JwtProperties;
import br.com.marinholab.keycloakxp.core.model.operations.*;
import br.com.marinholab.keycloakxp.core.service.*;
import br.com.marinholab.keycloakxp.exception.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
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
    private final RefreshTokenService refreshTokenService;
    private final ChangeUserPasswordService changeUserPasswordService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final GetUserService getUserService;
    private final DeactivateUserService deactivateUserService;

    public UserController(CreateUserService createUserService,
                          RefreshTokenService refreshTokenService,
                          ChangeUserPasswordService changeUserPasswordService,
                          LoginService loginService,
                          LogoutService logoutService,
                          GetUserService getUserService,
                          DeactivateUserService deactivateUserService) {
        this.createUserService = createUserService;
        this.refreshTokenService = refreshTokenService;
        this.changeUserPasswordService = changeUserPasswordService;
        this.loginService = loginService;
        this.logoutService = logoutService;
        this.getUserService = getUserService;
        this.deactivateUserService = deactivateUserService;
    }

    @Operation(summary = "Create a user in Keycloak.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The user has been created and is available in Keycloak.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "The information sent to the user may be incorrect.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "The user entered a username or email already existing in the database.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "The user could not be created in Keycloak due to some internal error.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
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
                    description = "The user has been successfully authenticated in Keycloak.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AccessTokenDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The user's credentials are invalid and Keycloak could not authenticate the user.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
    })
    @PostMapping("/login")
    public ResponseEntity<AccessTokenDTO> login(@Valid @RequestBody UserLoginForm form) throws UserAuthenticationException {
        AccessTokenDTO response = this.loginService.login(form);
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
                    description = "The user's session could not be terminated because the required information is invalid.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Logout could not be requested in Keycloak due to some internal error.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Jwt jwt,
                                       @Valid @RequestBody UserLogoutForm form) throws UserLogoutException {
        this.logoutService.logout(form, jwt);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Renews the user's access token.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The operation was successfully performed on Keycloak and the renewed access token will be returned.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AccessTokenDTO.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "The refresh token provided is invalid and renewal could not be performed.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "An internal error occurred and the operation could not be completed.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<AccessTokenDTO> refreshToken(@AuthenticationPrincipal Jwt jwt,
                                                       @Valid @RequestBody RefreshTokenForm form) throws RefreshTokenException {
        return ResponseEntity.ok(this.refreshTokenService.refreshToken(jwt, form));
    }

    @Operation(summary = "Changes the user's password in Keycloak. The user can only change his own password.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The user password change operation was successful."
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "The new password provisioned by the user does not meet the required standards.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "User not found in Keycloak.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
    })
    @PutMapping("/password")
    public ResponseEntity<Void> changeUserPassword(@AuthenticationPrincipal Jwt jwt,
                                                   @Valid @RequestBody ChangePasswordForm form) throws UserNotFoundException {
        this.changeUserPasswordService.changePassword(jwt.getClaimAsString(JwtProperties.PREFERRED_USERNAME), form.password());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Disables the account of the currently authenticated user. This operation is currently not reversible.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "The user account has been disabled in Keycloak."
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "User not found in Keycloak.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
    })
    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivateUser(@AuthenticationPrincipal Jwt jwt) throws UserNotFoundException {
        this.deactivateUserService.deactivate(jwt.getClaimAsString(JwtProperties.PREFERRED_USERNAME));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retrieves basic information of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The user has a valid session and their information could be recovered.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDTO.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "401",
                    description = "The user's session is invalid and your information could not be retrieved.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "User not found in Keycloak.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
    })
    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrentAuthenticatedUser(@AuthenticationPrincipal Jwt jwt) throws UserNotFoundException {
        return ResponseEntity.ok(
                this.getUserService.searchUserAsDTOByUsername(jwt.getClaimAsString(JwtProperties.PREFERRED_USERNAME))
        );
    }
}
