package br.com.marinholab.keycloakxp.core.controller;

import br.com.marinholab.keycloakxp.core.model.UserDTO;
import br.com.marinholab.keycloakxp.core.model.operations.CreateUserForm;
import br.com.marinholab.keycloakxp.core.service.CreateUserService;
import br.com.marinholab.keycloakxp.exception.CreateUserException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller", description = "Controller for managing all user related operations.")
public class UserController {

    private final CreateUserService createUserService;

    public UserController(CreateUserService createUserService) {
        this.createUserService = createUserService;
    }

    @Operation(summary = "Create a user in Keycloak")
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
}
