package br.com.marinholab.keycloakxp.core.controller;

import br.com.marinholab.keycloakxp.core.model.AccessTokenDTO;
import br.com.marinholab.keycloakxp.core.model.UserDTO;
import br.com.marinholab.keycloakxp.core.model.operations.*;
import br.com.marinholab.keycloakxp.core.service.*;
import br.com.marinholab.keycloakxp.exception.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateUserService createUserService;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @MockitoBean
    private ChangeUserPasswordService changeUserPasswordService;

    @MockitoBean
    private LoginService loginService;

    @MockitoBean
    private LogoutService logoutService;

    @MockitoBean
    private DeactivateUserService deactivateUserService;

    @MockitoBean
    private GetUserService getUserService;

    private ObjectMapper objectMapper;

    private final String jwtTokenMock = "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI4MjQxOTJiNy01OWUzLTRhNTEtYmM0Mi0yOGU3OGVjNTMzMWYifQ.eyJleHAiOjE3NDA3ODQ4MzEsImlhdCI6MTc0MDc4NDIzMSwianRpIjoiNjZjY2I0YWUtY2ExMi00M2JlLTg2OTctMzIyZDQxYTBhMDI1IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgxL3JlYWxtcy9rZXljbG9ha3hwIiwiYXVkIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgxL3JlYWxtcy9rZXljbG9ha3hwIiwic3ViIjoiYmJkYjE2Y2ItYzI1OS00M2YxLWIzYTEtMTA1Yzk5OWM2MDcxIiwidHlwIjoiUmVmcmVzaCIsImF6cCI6ImtleWNsb2FreHAiLCJzaWQiOiI2OTYyMGQyMi05MjhiLTQwZTAtOTc3OS1hZmIzNTg0YThhNzQiLCJzY29wZSI6IndlYi1vcmlnaW5zIHNlcnZpY2VfYWNjb3VudCBhY3IgcHJvZmlsZSByb2xlcyBlbWFpbCBiYXNpYyJ9.5cnDP8Zy5uo9gHfTRZXJfPe23lkXuFXmL8gxaj9r2uTlJt0VFL--NtqGNGXLTfsL9vPaQa0SkuTEpleZxUCEWw";

    @BeforeEach
    void init() {
        this.objectMapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @DisplayName("Should throw an exception and status 400 when user provides incorrect information")
    void shouldThrowAnExceptionAndStatus400WhenUserProvidesIncorrectInformation() throws Exception {
        CreateUserForm form = new CreateUserForm(
                "test",
                "test@example.com",
                "firstName",
                "lastName",
                null
        );

        String formAsJson = this.objectMapper.writeValueAsString(form);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .content(formAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Should throw an exception and return status 409 when user information conflicts with that of an existing record")
    void shouldThrowAnExceptionAndReturnStatus409WhenUserInformationConflictsWithThatOfAnExistingRecord() throws Exception {
        CreateUserForm form = new CreateUserForm(
                "test",
                "test@example.com",
                "firstName",
                "lastName",
                "Str0ngP@ssword"
        );

        String formAsJson = this.objectMapper.writeValueAsString(form);

        CreateUserException createUserException = new CreateUserException(HttpStatus.CONFLICT, "root");

        doThrow(createUserException).when(this.createUserService).createUser(any(CreateUserForm.class));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .content(formAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("Should throw an exception and return status 500 when there is an internal error during user creation")
    void shouldThrowAnExceptionAndReturnStatus500WhenThereIsAnInternalErrorDuringUserCreation() throws Exception {
        CreateUserForm form = new CreateUserForm(
                "test",
                "test@example.com",
                "firstName",
                "lastName",
                "Str0ngP@ssword"
        );

        String formAsJson = this.objectMapper.writeValueAsString(form);

        CreateUserException createUserException = new CreateUserException("root");
        doThrow(createUserException).when(this.createUserService).createUser(any(CreateUserForm.class));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .content(formAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    @DisplayName("Should create and return a new user")
    void shouldCreateAndReturnNewUser() throws Exception {
        String id = UUID.randomUUID().toString();
        CreateUserForm form = new CreateUserForm(
                "test",
                "test@example.com",
                "firstName",
                "lastName",
                "Str0ngP@ssword"
        );

        UserDTO userDTO = mock(UserDTO.class);
        when(userDTO.id()).thenReturn(id);

        String formAsJson = this.objectMapper.writeValueAsString(form);

        when(this.createUserService.createUser(any())).thenReturn(userDTO);

        MockHttpServletResponse response = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .content(formAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(201, response.getStatus());
        assertTrue(response.containsHeader("Location"));
        assertNotNull(response.getHeader("Location"));
        assertTrue(Objects.requireNonNull(response.getHeader("Location")).endsWith(String.format("/user/%s", id)));
    }

    @Test
    @DisplayName("Should throw an exception and status 401 when user credentials are not valid on login")
    void shouldThrowAnExceptionAndStatus401WhenUserCredentialsAreNotValidOnLogin() throws Exception {
        UserLoginForm form = new UserLoginForm("root", "Str0ngP@ssword");

        String formAsJson = this.objectMapper.writeValueAsString(form);
        when(this.loginService.login(any(UserLoginForm.class))).thenThrow(new UserAuthenticationException(form.username()));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/login")
                        .header("Accept-Language", "en_US")
                        .content(formAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("Should authenticate the user and return session data")
    void shouldAuthenticateUserAndReturnSessionData() throws Exception {
        UserLoginForm form = new UserLoginForm("root", "Str0ngP@ssword");

        AccessTokenDTO accessTokenDTOAsMock = mock(AccessTokenDTO.class);
        when(this.loginService.login(any(UserLoginForm.class))).thenReturn(accessTokenDTOAsMock);

        String formAsJson = this.objectMapper.writeValueAsString(form);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/login")
                        .header("Accept-Language", "en_US")
                        .content(formAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Should throw an exception and status 400 when user token is invalid on logout")
    void shouldThrowAnExceptionAndStatus400WhenUserTokenIsInvalidOnLogout() throws Exception {
        UserLogoutForm form = new UserLogoutForm("refreshToken");
        String formAsJson = this.objectMapper.writeValueAsString(form);

        doThrow(new UserLogoutException("root")).when(this.logoutService).logout(any(UserLogoutForm.class), any(Jwt.class));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/logout")
                        .header("Accept-Language", "en_US")
                        .with(jwt().jwt(token -> token.claim("preferred_username", "root")))
                        .content(formAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Should disconnect user session in Keycloak and return status 204")
    void shouldDisconnectUserSessionInKeycloakAndReturnStatus204() throws Exception {
        UserLogoutForm form = new UserLogoutForm(this.jwtTokenMock);
        String formAsJson = this.objectMapper.writeValueAsString(form);

        doNothing().when(this.logoutService).logout(any(UserLogoutForm.class), any(Jwt.class));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/logout")
                        .header("Accept-Language", "en_US")
                        .with(jwt().jwt(token -> token.claim("preferred_username", "root")))
                        .content(formAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Should refresh user access token and return it with status 200")
    void shouldRefreshUserAccessTokenAndReturnItWithStatus200() throws Exception {
        RefreshTokenForm form = new RefreshTokenForm(this.jwtTokenMock);
        AccessTokenDTO accessTokenDTOAsMock = mock(AccessTokenDTO.class);
        String formAsJson = this.objectMapper.writeValueAsString(form);

        when(this.refreshTokenService.refreshToken(any(Jwt.class), any(RefreshTokenForm.class))).thenReturn(accessTokenDTOAsMock);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/refresh-token")
                        .header("Accept-Language", "en_US")
                        .with(jwt().jwt(token -> token.claim("preferred_username", "root")))
                        .content(formAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Should throw exception and return status 400 when provided refresh token is invalid on refresh token operation")
    void shouldThrowExceptionAndReturnStatus400WhenProvidedRefreshTokenIsInvalidOnRefreshTokenOperation() throws Exception {
        RefreshTokenForm form = new RefreshTokenForm(this.jwtTokenMock);
        RefreshTokenException exceptionAsMock = new RefreshTokenException(HttpStatus.BAD_REQUEST, "root");
        String formAsJson = this.objectMapper.writeValueAsString(form);

        doThrow(exceptionAsMock).when(this.refreshTokenService).refreshToken(any(Jwt.class), any(RefreshTokenForm.class));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/refresh-token")
                        .header("Accept-Language", "en_US")
                        .with(jwt().jwt(token -> token.claim("preferred_username", "root")))
                        .content(formAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Should throw exception and return status 500 when internal error occurs in refresh token operation")
    void shouldThrowExceptionAndReturnStatus500WhenInternalErrorOccursInRefreshTokenOperation() throws Exception {
        RefreshTokenForm form = new RefreshTokenForm(this.jwtTokenMock);
        RefreshTokenException exceptionAsMock = new RefreshTokenException("root");
        String formAsJson = this.objectMapper.writeValueAsString(form);

        doThrow(exceptionAsMock).when(this.refreshTokenService).refreshToken(any(Jwt.class), any(RefreshTokenForm.class));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/refresh-token")
                        .header("Accept-Language", "en_US")
                        .with(jwt().jwt(token -> token.claim("preferred_username", "root")))
                        .content(formAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    @DisplayName("Should throw an exception and status 400 when the new password does not follow the password rules")
    void shouldThrowExceptionAndStatus400WhenNewPasswordDoesNotFollowThePasswordRules() throws Exception {
        ChangePasswordForm form = new ChangePasswordForm("fragilePassword");
        String formAsJson = this.objectMapper.writeValueAsString(form);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/password")
                        .header("Accept-Language", "en_US")
                        .with(jwt().jwt(token -> token.claim("preferred_username", "root")))
                        .content(formAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Should throw an exception and status 404 when user is not found in Keycloak")
    void shouldThrowExceptionAndStatus404WhenUserIsNotFoundInKeycloak() throws Exception {
        ChangePasswordForm form = new ChangePasswordForm("Str0ngP@ssword");
        String formAsJson = this.objectMapper.writeValueAsString(form);

        UserNotFoundException exceptionAsMock = new UserNotFoundException("root");
        doThrow(exceptionAsMock).when(this.changeUserPasswordService).changePassword(anyString(), anyString());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/password")
                        .header("Accept-Language", "en_US")
                        .with(jwt().jwt(token -> token.claim("preferred_username", "root")))
                        .content(formAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Should return status 200 when the user's password change request is successful")
    void shouldReturnStatus200WhenTheUsersPassworChangeRequestIsSuccessful() throws Exception {
        ChangePasswordForm form = new ChangePasswordForm("Str0ngP@ssword");
        String formAsJson = this.objectMapper.writeValueAsString(form);

        doNothing().when(this.changeUserPasswordService).changePassword(anyString(), anyString());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/password")
                        .header("Accept-Language", "en_US")
                        .with(jwt().jwt(token -> token.claim("preferred_username", "root")))
                        .content(formAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Should return status 204 when the request to deactivate the user is executed successfully")
    void shouldReturnStatus204WhenTheRequestToDeactivateUserIsExecutedSuccessfully() throws Exception {
        doNothing().when(this.deactivateUserService).deactivate(anyString());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .patch("/user/deactivate")
                        .header("Accept-Language", "en_US")
                        .with(jwt().jwt(token -> token.claim("preferred_username", "root"))))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Should return status 404 when user is not found in Keycloak during account deactivation operation")
    void shouldReturnStatus404WhenUserIsNotFoundInKeycloakDuringAccountDeactivationOperation() throws Exception {
        UserNotFoundException exception = new UserNotFoundException("root");
        doThrow(exception).when(this.deactivateUserService).deactivate(anyString());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .patch("/user/deactivate")
                        .header("Accept-Language", "en_US")
                        .with(jwt().jwt(token -> token.claim("preferred_username", "root"))))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Should throw an exception and status 401 when user credentials are not valid on get current user")
    void shouldThrowAnExceptionAndStatus401WhenUserCredentialsAreNotValidOnGetCurrentUser() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/current")
                        .header("Accept-Language", "en_US")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("Should throw an exception and status 404 when user is not found on get current authenticated user")
    void shouldThrowAnExceptionAndStatus404WhenUserIsNotFoundOnGetCurrentAuthenticatedUser() throws Exception {
        UserNotFoundException exceptionAsMock = new UserNotFoundException("root");
        doThrow(exceptionAsMock).when(this.getUserService).searchUserAsDTOByUsername(anyString());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/current")
                        .with(jwt().jwt(token -> token.claim("preferred_username", "root")))
                        .header("Accept-Language", "en_US")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Should get current authenticated user")
    void shouldGetCurrentAuthenticatedUser() throws Exception {
        UserDTO userDTOAsMock = mock(UserDTO.class);
        doReturn(userDTOAsMock).when(this.getUserService).searchUserAsDTOByUsername(anyString());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/current")
                        .with(jwt().jwt(token -> token.claim("preferred_username", "root")))
                        .header("Accept-Language", "en_US")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}