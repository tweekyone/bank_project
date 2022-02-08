package com.epam.clientinterface.controller;

import static com.epam.clientinterface.util.UserTestData.user;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.config.MockConfig;
import com.epam.clientinterface.configuration.ApplicationConfiguration;
import com.epam.clientinterface.repository.UserRepository;
import com.epam.clientinterface.service.AccountService;
import com.epam.clientinterface.service.CardService;
import com.epam.clientinterface.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringJUnitWebConfig({ApplicationConfiguration.class, MockConfig.class})
public abstract class AbstractControllerTest {

    static final String LOGIN = "/login";

    @Mock
    UserRepository userRepositoryMock;

    @Mock
    UserService userServiceMock;

    @Mock
    AccountService accountServiceMock;

    @Mock
    CardService cardServiceMock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        // https://stackoverflow.com/a/31843799/13721689
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();
        this.userRepositoryMock = (UserRepository) webApplicationContext.getBean("userRepositoryMock");
        this.userServiceMock = (UserService) webApplicationContext.getBean("userServiceMock");
        this.accountServiceMock = (AccountService) webApplicationContext.getBean("accountServiceMock");
        this.cardServiceMock = (CardService) webApplicationContext.getBean("cardServiceMock");
    }

    @AfterEach
    void tearDown() {
        reset(userRepositoryMock);
        reset(userServiceMock);
        reset(accountServiceMock);
        reset(cardServiceMock);
    }

    public ResultActions send(MediaType mediaType, String requestBody, HttpMethod method, String uri) throws Exception {
        when(userServiceMock.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepositoryMock.findByEmailWithRoles(user.getEmail())).thenReturn(Optional.of(user));

        MvcResult result = mockMvc.perform(get(LOGIN).servletPath(LOGIN)
                .header("Authorization", "Basic YWFAZW1haWwuY29tOnBhc3M="))
            .andExpect(status().isOk()).andReturn();
        String token = result.getResponse().getHeader("Authorization");

        ResultActions resultActions;

        switch (method) {
            case PATCH:
                resultActions = mockMvc.perform(patch(uri)
                    .header("Authorization", String.format("Bearer %s", token))
                    .contentType(mediaType));
                break;

            case POST:
                resultActions = mockMvc.perform(post(uri)
                    .header("Authorization", String.format("Bearer %s", token))
                    .contentType(mediaType).content(requestBody));
                break;

            case DELETE:
                resultActions = mockMvc.perform(delete(uri)
                    .header("Authorization", String.format("Bearer %s", token))
                    .contentType(mediaType));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + method);
        }
        return resultActions;
    }
}
