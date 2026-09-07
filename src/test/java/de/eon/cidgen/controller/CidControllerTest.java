package de.eon.cidgen.controller;

import de.eon.cidgen.entity.CidCounter;
import de.eon.cidgen.service.CidService;
import de.eon.cidgen.support.TestRepositories;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CidControllerTest {

    @Test
    void postCidsReturnsGeneratedCid() throws Exception {
        MockMvc mockMvc = mockMvc();

        mockMvc.perform(post("/api/cids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hrSystem\":\"SAP\",\"eonAccountingAreaID\":\"DE01\",\"employeeNumber\":\"12345678\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cid").value("CD0000"));
    }

    @Test
    void postCidsRejectsBlankRequiredFields() throws Exception {
        mockMvc().perform(post("/api/cids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hrSystem\":\"SAP\",\"eonAccountingAreaID\":\"DE01\",\"employeeNumber\":\"  \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("employeeNumber must not be empty"));
    }

    @Test
    void oldKidEndpointDoesNotExist() throws Exception {
        mockMvc().perform(post("/api/kids").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isNotFound());
    }

    private MockMvc mockMvc() {
        CidService cidService = new CidService(
                TestRepositories.counterRepository(new CidCounter("D", 0)),
                TestRepositories.generatedCidRepository(new ArrayList<>()), "D");
        return MockMvcBuilders.standaloneSetup(new CidController(cidService))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }
}
