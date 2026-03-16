package com.diachenko.checker.integration.controller;


/*  health-checker
    04.03.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.integration.AbstractIntegrationTest;
import com.diachenko.checker.integration.IntegrationTestDataFactory;
import com.diachenko.checker.model.entity.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class HealthCheckControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IntegrationTestDataFactory integrationTestDataFactory;

    private AppUser appUser;

    @BeforeEach
    void setup() {
        integrationTestDataFactory.createDefaultUser();
        integrationTestDataFactory.createDefaultUrl();
        appUser = integrationTestDataFactory.getDefaultUser();
    }

    @Test
    void healthCheck_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/")
                        .with(user(appUser)))
                .andExpect(status().isOk())
                .andExpect(view().name("health"))
                .andExpect((result ->
                        assertTrue(result.getResponse()
                                .getContentAsString()
                                .contains("https://google.com"))));
    }

}
