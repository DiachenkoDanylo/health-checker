package com.diachenko.checker.web;


/*  health-checker
    05.03.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.TestDataFactory;
import com.diachenko.checker.controller.HealthCheckController;
import com.diachenko.checker.model.entity.AppUser;
import com.diachenko.checker.service.MonitoredUrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"standalone", "h2"})
class HealthCheckControllerTest {

    AppUser appUser;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MonitoredUrlService monitoredUrlService;

    @BeforeEach
    void setUp() {
        appUser = TestDataFactory.getAppUserWithUrl();
    }

    @Test
    void healthCheck_shouldReturnHealthPage() throws Exception {

        when(monitoredUrlService.getUserUrlsByUsername("johndoe"))
                .thenReturn(appUser.getMonitoredUrls());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/")
                        .with(user(appUser)))
                .andExpect(status().isOk())
                .andExpect(view().name("health"))
                .andExpect(model().attributeExists("urls"));
    }
}
