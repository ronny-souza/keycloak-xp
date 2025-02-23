package br.com.marinholab.keycloakxp.core.controller;

import br.com.marinholab.keycloakxp.core.model.UserDTO;
import br.com.marinholab.keycloakxp.core.model.operations.CreateUserForm;
import br.com.marinholab.keycloakxp.core.service.CreateUserService;
import br.com.marinholab.keycloakxp.exception.CreateUserException;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateUserService createUserService;

    private ObjectMapper objectMapper;

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
    @DisplayName("Should throw an exception and status 500 when an internal error prevents user creation")
    void shouldThrowAnExceptionAndStatus500WhenAnInternalErrorPreventsUserCreation() throws Exception {
        CreateUserForm form = new CreateUserForm(
                "test",
                "test@example.com",
                "firstName",
                "lastName",
                "Str0ngP@ssword"
        );

        String formAsJson = this.objectMapper.writeValueAsString(form);

        when(this.createUserService.createUser(any())).thenThrow(CreateUserException.class);

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
}