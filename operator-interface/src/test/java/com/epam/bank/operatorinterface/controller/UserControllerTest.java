package com.epam.bank.operatorinterface.controller;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.bank.operatorinterface.config.WithMockAdmin;
import com.epam.bank.operatorinterface.configuration.security.util.JwtUtil;
import com.epam.bank.operatorinterface.controller.mapper.UserMapper;
import com.epam.bank.operatorinterface.exception.ValidationException;
import com.epam.bank.operatorinterface.service.UserDetailsServiceImpl;
import com.epam.bank.operatorinterface.service.UserService;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import util.TestDataFactory;

@WebMvcTest(UserController.class)
@WithMockAdmin
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userServiceMock;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserMapper responseMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    public void shouldReturnCreatedIfValidRequestBodyIsProvided_createEndpoint() throws Exception {
        var userFixture = TestDataFactory.getUser();
        var userResponseFixture = TestDataFactory.getUserResponse();

        when(userServiceMock.create(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
            .thenReturn(userFixture);
        when(responseMapper.map(userFixture)).thenReturn(userResponseFixture);

        sendCreate(getCreateRequestBody())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(userResponseFixture.getId()))
            .andExpect(jsonPath("$.name").value(userResponseFixture.getName()))
            .andExpect(jsonPath("$.surname").value(userResponseFixture.getSurname()))
            .andExpect(jsonPath("$.phoneNumber").value(userResponseFixture.getPhoneNumber()))
            .andExpect(jsonPath("$.username").value(userResponseFixture.getUsername()))
            .andExpect(jsonPath("$.email").value(userResponseFixture.getEmail()))
            .andExpect(jsonPath("$.accounts").value(is(userResponseFixture.getAccounts())))
            .andExpect(jsonPath("$.enabled").value(userResponseFixture.isEnabled()))
            .andExpect(jsonPath("$.failedLoginAttempts").value(userResponseFixture.getFailedLoginAttempts()));
    }

    @Test
    public void shouldReturnUnprocessableEntityIfServiceThrowsValidationException_createEndpoint() throws Exception {
        var propertyPathMock = mock(Path.class);
        var constraintDescriptorMock = mock(ConstraintDescriptor.class);
        var annotationMock = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        var violationMock = mock(ConstraintViolation.class);

        var errField = "error field";
        var errType = annotationMock.annotationType().getName();
        var errMessage = "error message";

        when(propertyPathMock.toString()).thenReturn(errField);
        when(violationMock.getPropertyPath()).thenReturn(propertyPathMock);
        when(constraintDescriptorMock.getAnnotation()).thenReturn(annotationMock);
        when(violationMock.getConstraintDescriptor()).thenReturn(constraintDescriptorMock);
        when(violationMock.getMessage()).thenReturn(errMessage);

        when(userServiceMock.create(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
            .thenThrow(new ValidationException(Set.of(violationMock)));

        sendCreate(getCreateRequestBody())
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.type").value("validation"))
            .andExpect(jsonPath("$.status").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
            .andExpect(jsonPath("$.errors[*].field").value(hasItems(errField)))
            .andExpect(jsonPath(String.format("$.errors[?(@.field=='%s')].type", errField), hasItems(errType)))
            .andExpect(jsonPath(String.format("$.errors[?(@.field=='%s')].error", errField), hasItems(errMessage)));
    }

    @Test
    public void shouldReturnBadRequestIfRequestBodyIsEmpty_createEndpoint() throws Exception {
        sendCreate("")
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type").value(HttpMessageNotReadableException.class.getName()));
    }

    @Test
    public void shouldReturnBadRequestIfRequestBodyIsInvalid_createEndpoint() throws Exception {
        sendCreate("{invalidRequest")
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type").value(HttpMessageNotReadableException.class.getName()));
    }

    @Test
    public void shouldReturnUnprocessableEntityIfRequestBodyIsEmptyJson_createEndpoint() throws Exception {
        var fields = new String[]{"name", "surname", "phoneNumber", "username", "email", "rawPassword"};

        var nameErrTypes = new String[]{"NotNull", "NotBlank"};
        var surnameErrTypes = new String[]{"NotNull", "NotBlank"};
        var phoneErrTypes = new String[]{"NotNull", "NotBlank"};
        var usernameErrTypes = new String[]{"NotNull", "NotBlank"};
        var emailErrTypes = new String[]{"NotNull", "NotBlank"};
        var passwordErrTypes = new String[]{"NotNull", "NotBlank"};

        var nameErrMsgs = new String[]{"must not be null", "must not be blank"};
        var surnameErrMsgs = new String[]{"must not be null", "must not be blank"};
        var phoneErrMsgs = new String[]{"must not be null", "must not be blank"};
        var usernameErrMsgs = new String[]{"must not be null", "must not be blank"};
        var emailErrMsgs = new String[]{"must not be null", "must not be blank"};
        var passwordErrMsgs = new String[]{"must not be null", "must not be blank"};

        sendCreate("{}")
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.type").value("validation"))
            .andExpect(jsonPath("$.errors[*].field").value(hasItems(fields)))
            .andExpect(jsonPath("$.errors[?(@.field=='name')].type").value(hasItems(nameErrTypes)))
            .andExpect(jsonPath("$.errors[?(@.field=='surname')].type").value(hasItems(surnameErrTypes)))
            .andExpect(jsonPath("$.errors[?(@.field=='phoneNumber')].type").value(hasItems(phoneErrTypes)))
            .andExpect(jsonPath("$.errors[?(@.field=='username')].type").value(hasItems(usernameErrTypes)))
            .andExpect(jsonPath("$.errors[?(@.field=='email')].type").value(hasItems(emailErrTypes)))
            .andExpect(jsonPath("$.errors[?(@.field=='rawPassword')].type").value(hasItems(passwordErrTypes)))
            .andExpect(jsonPath("$.errors[?(@.field=='name')].error").value(hasItems(nameErrMsgs)))
            .andExpect(jsonPath("$.errors[?(@.field=='surname')].error").value(hasItems(surnameErrMsgs)))
            .andExpect(jsonPath("$.errors[?(@.field=='phoneNumber')].error").value(hasItems(phoneErrMsgs)))
            .andExpect(jsonPath("$.errors[?(@.field=='username')].error").value(hasItems(usernameErrMsgs)))
            .andExpect(jsonPath("$.errors[?(@.field=='email')].error").value(hasItems(emailErrMsgs)))
            .andExpect(jsonPath("$.errors[?(@.field=='rawPassword')].error").value(hasItems(passwordErrMsgs)));
    }

    @Test
    public void shouldReturnUnprocessableEntityIfRequestBodyHasInvalidEmail_createEndpoint() throws Exception {
        var field = "email";
        var fieldErrType = "Email";
        var fieldErrMsg = "must be a well-formed email address";

        sendCreate("{\"email\": \"invalidEmail\"}")
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.type").value("validation"))
            .andExpect(jsonPath("$.errors[*].field").value(hasItems(field)))
            .andExpect(jsonPath("$.errors[?(@.field=='email')].type").value(hasItems(fieldErrType)))
            .andExpect(jsonPath("$.errors[?(@.field=='email')].error").value(hasItems(fieldErrMsg)));
    }

    @Test
    public void shouldReturnCreatedIfValidRequestBodyIsProvided_createClientEndpoint() throws Exception {
        var userFixture = TestDataFactory.getUser();
        var userResponseFixture = TestDataFactory.getUserWithAccountResponse();
        var accountFixture = userResponseFixture.getAccounts().get(0);

        when(userServiceMock.createClient(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
            .thenReturn(userFixture);
        when(responseMapper.map(userFixture)).thenReturn(userResponseFixture);

        sendCreateClient(getCreateRequestBody())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(userResponseFixture.getId()))
            .andExpect(jsonPath("$.name").value(userResponseFixture.getName()))
            .andExpect(jsonPath("$.surname").value(userResponseFixture.getSurname()))
            .andExpect(jsonPath("$.phoneNumber").value(userResponseFixture.getPhoneNumber()))
            .andExpect(jsonPath("$.username").value(userResponseFixture.getUsername()))
            .andExpect(jsonPath("$.email").value(userResponseFixture.getEmail()))
            .andExpect(jsonPath("$.accounts[0].id").value(accountFixture.getId()))
            .andExpect(jsonPath("$.accounts[0].number").value(accountFixture.getNumber()))
            .andExpect(jsonPath("$.accounts[0].plan").value(accountFixture.getPlan()))
            .andExpect(jsonPath("$.accounts[0].amount").value(is(accountFixture.getAmount()), Double.class))
            .andExpect(jsonPath("$.accounts[0].userId").value(accountFixture.getUserId()))
            .andExpect(jsonPath("$.accounts[0].cards").value(is(accountFixture.getCards())))
            .andExpect(jsonPath("$.accounts[0].closedAt").value(accountFixture.getClosedAt()))
            .andExpect(jsonPath("$.accounts[0].default").value(accountFixture.isDefault()))
            .andExpect(jsonPath("$.enabled").value(userResponseFixture.isEnabled()))
            .andExpect(jsonPath("$.failedLoginAttempts").value(userResponseFixture.getFailedLoginAttempts()));
    }

    @Test
    public void shouldReturnUnprocessableEntityIfServiceThrowsValidationException_createClientEndpoint()
        throws Exception {

        var propertyPathMock = mock(Path.class);
        var constraintDescriptorMock = mock(ConstraintDescriptor.class);
        var annotationMock = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        var violationMock = mock(ConstraintViolation.class);

        var errField = "error field";
        var errType = annotationMock.annotationType().getName();
        var errMessage = "error message";

        when(propertyPathMock.toString()).thenReturn(errField);
        when(violationMock.getPropertyPath()).thenReturn(propertyPathMock);
        when(constraintDescriptorMock.getAnnotation()).thenReturn(annotationMock);
        when(violationMock.getConstraintDescriptor()).thenReturn(constraintDescriptorMock);
        when(violationMock.getMessage()).thenReturn(errMessage);

        when(userServiceMock.createClient(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
            .thenThrow(new ValidationException(Set.of(violationMock)));

        sendCreateClient(getCreateRequestBody())
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.type").value("validation"))
            .andExpect(jsonPath("$.status").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
            .andExpect(jsonPath("$.errors[*].field").value(hasItems(errField)))
            .andExpect(jsonPath(String.format("$.errors[?(@.field=='%s')].type", errField), hasItems(errType)))
            .andExpect(jsonPath(String.format("$.errors[?(@.field=='%s')].error", errField), hasItems(errMessage)));
    }

    @Test
    public void shouldReturnBadRequestIfRequestBodyIsEmpty_createClientEndpoint() throws Exception {
        sendCreateClient("")
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type").value(HttpMessageNotReadableException.class.getName()));
    }

    @Test
    public void shouldReturnBadRequestIfRequestBodyIsInvalid_createClientEndpoint() throws Exception {
        sendCreateClient("{invalidRequest")
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type").value(HttpMessageNotReadableException.class.getName()));
    }

    @Test
    public void shouldReturnUnprocessableEntityIfRequestBodyIsEmptyJson_createClientEndpoint() throws Exception {
        var fields = new String[]{"name", "surname", "phoneNumber", "username", "email", "rawPassword"};

        var nameErrTypes = new String[]{"NotNull", "NotBlank"};
        var surnameErrTypes = new String[]{"NotNull", "NotBlank"};
        var phoneErrTypes = new String[]{"NotNull", "NotBlank"};
        var usernameErrTypes = new String[]{"NotNull", "NotBlank"};
        var emailErrTypes = new String[]{"NotNull", "NotBlank"};
        var passwordErrTypes = new String[]{"NotNull", "NotBlank"};

        var nameErrMsgs = new String[]{"must not be null", "must not be blank"};
        var surnameErrMsgs = new String[]{"must not be null", "must not be blank"};
        var phoneErrMsgs = new String[]{"must not be null", "must not be blank"};
        var usernameErrMsgs = new String[]{"must not be null", "must not be blank"};
        var emailErrMsgs = new String[]{"must not be null", "must not be blank"};
        var passwordErrMsgs = new String[]{"must not be null", "must not be blank"};

        sendCreateClient("{}")
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.type").value("validation"))
            .andExpect(jsonPath("$.errors[*].field").value(hasItems(fields)))
            .andExpect(jsonPath("$.errors[?(@.field=='name')].type").value(hasItems(nameErrTypes)))
            .andExpect(jsonPath("$.errors[?(@.field=='surname')].type").value(hasItems(surnameErrTypes)))
            .andExpect(jsonPath("$.errors[?(@.field=='phoneNumber')].type").value(hasItems(phoneErrTypes)))
            .andExpect(jsonPath("$.errors[?(@.field=='username')].type").value(hasItems(usernameErrTypes)))
            .andExpect(jsonPath("$.errors[?(@.field=='email')].type").value(hasItems(emailErrTypes)))
            .andExpect(jsonPath("$.errors[?(@.field=='rawPassword')].type").value(hasItems(passwordErrTypes)))
            .andExpect(jsonPath("$.errors[?(@.field=='name')].error").value(hasItems(nameErrMsgs)))
            .andExpect(jsonPath("$.errors[?(@.field=='surname')].error").value(hasItems(surnameErrMsgs)))
            .andExpect(jsonPath("$.errors[?(@.field=='phoneNumber')].error").value(hasItems(phoneErrMsgs)))
            .andExpect(jsonPath("$.errors[?(@.field=='username')].error").value(hasItems(usernameErrMsgs)))
            .andExpect(jsonPath("$.errors[?(@.field=='email')].error").value(hasItems(emailErrMsgs)))
            .andExpect(jsonPath("$.errors[?(@.field=='rawPassword')].error").value(hasItems(passwordErrMsgs)));
    }

    @Test
    public void shouldReturnUnprocessableEntityIfRequestBodyHasInvalidEmail_createClientEndpoint() throws Exception {
        var field = "email";
        var fieldErrType = "Email";
        var fieldErrMsg = "must be a well-formed email address";

        sendCreateClient("{\"email\": \"invalidEmail\"}")
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.type").value("validation"))
            .andExpect(jsonPath("$.errors[*].field").value(hasItems(field)))
            .andExpect(jsonPath("$.errors[?(@.field=='email')].type").value(hasItems(fieldErrType)))
            .andExpect(jsonPath("$.errors[?(@.field=='email')].error").value(hasItems(fieldErrMsg)));
    }

    public String getCreateRequestBody() {
        return getCreateRequestBody(
            RandomStringUtils.randomAlphabetic(6),
            RandomStringUtils.randomAlphabetic(6),
            RandomStringUtils.randomAlphabetic(6),
            RandomStringUtils.randomAlphabetic(6),
            RandomStringUtils.randomAlphabetic(6).concat("@gmail.com"),
            RandomStringUtils.randomAlphabetic(6)
        );
    }

    public String getCreateRequestBody(
        String name, String surname, String phone, String username, String email, String rawPassword
    ) {
        return JSONObject.toJSONString(Map.of(
            "name", name,
            "surname", surname,
            "phoneNumber", phone,
            "username", username,
            "email", email,
            "rawPassword", rawPassword
        ));
    }

    private ResultActions sendCreate(String requestBody) throws Exception {
        return mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(requestBody));
    }

    private ResultActions sendCreateClient(String requestBody) throws Exception {
        return mockMvc.perform(post("/users/client").contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));
    }
}
