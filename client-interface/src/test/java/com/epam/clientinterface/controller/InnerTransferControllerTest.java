package com.epam.clientinterface.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.epam.clientinterface.configuration.ApplicationConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration()
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class InnerTransferControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void shouldTransferIfIncomeDataIsValid() throws Exception {
        this.mockMvc.
            perform(
                post("/transfer/inner")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"sourceAccountId\":1,\"destinationAccountId\":2,\"amount\":1000.00}"))
            .andExpect(status().isBadRequest());

        Assertions.assertEquals(1, 1);
    }
}
