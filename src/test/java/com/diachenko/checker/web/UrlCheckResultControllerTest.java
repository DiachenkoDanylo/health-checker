package com.diachenko.checker.web;


/*  health-checker
    11.03.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.TestDataFactory;
import com.diachenko.checker.model.entity.AppUser;
import com.diachenko.checker.model.entity.UrlCheckResult;
import com.diachenko.checker.service.MonitoredUrlService;
import com.diachenko.checker.service.UrlCheckResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"standalone", "h2"})
class UrlCheckResultControllerTest {

    AppUser appUser;
    List<UrlCheckResult>  urlCheckResultList;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MonitoredUrlService monitoredUrlService;

    @MockitoBean
    private UrlCheckResultService urlCheckResultService;

    @BeforeEach
    void setUp() {
        appUser = TestDataFactory.getAppUserWithUrl();
        urlCheckResultList = TestDataFactory.getUrlCheckResultList();
    }

    @Test
    void getHistoryById_shouldReturnHistoryPage() throws Exception {
        Long id = appUser.getMonitoredUrls().iterator().next().getId();
        when(monitoredUrlService.getById(id))
                .thenReturn(appUser.getMonitoredUrls().iterator().next());

        when(urlCheckResultService.getAllUrlCheckResultsByUrlId(id)).thenReturn(urlCheckResultList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/history/{id}", id)
                        .with(user(appUser)))
                .andExpect(status().isOk())
                .andExpect(view().name("history"))
                .andExpect(model().attributeExists("monitoredUrl"))
                .andExpect(model().attributeExists("historyCheksList"));
    }

}
