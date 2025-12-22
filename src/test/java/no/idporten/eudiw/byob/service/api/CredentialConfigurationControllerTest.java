package no.idporten.eudiw.byob.service.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.idporten.eudiw.byob.service.model.ByobInput;
import no.idporten.eudiw.byob.service.serviceClasses.CredentialConfigurationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("When POST-ing and GETing data to the BYOB")
@AutoConfigureMockMvc
@ActiveProfiles("junit")
@SpringBootTest
class CredentialConfigurationControllerTest {

    private static final Logger log = LoggerFactory.getLogger(CredentialConfigurationControllerTest.class);
    private final String example = """
            {
            "vct": "foomittbevisheisenleisensen",
            "format": "dc+sd-jwt",
            "example_credential_metadata": {
        "foo": "bar"
    },
            "credential_metadata": {
        "display": [
        {
            "name": "MinID PID",
                "locale": "no",
                "background_color": "#afcee9",
                "text_color": "#002c54"
        }
    ],
        "claims": [
        {
            "path": "foo",
                "mandatory": true,
                "display": {
            "name": "MinID PID",
                "locale": "no",
                "background_color": "#afcee9",
                "text_color": "#002c54"
        }
        }
    ]
    }
}""";

    private MockMvc mockMvc;

    @MockitoSpyBean
    private CredentialConfigurationService service;

    private CredentialConfigurationController controller;

    private Map<String, ByobInput> persistenceLayer;

    @Autowired
    public CredentialConfigurationControllerTest(MockMvc mockMvc, CredentialConfigurationService service) {
        this.mockMvc = mockMvc;
        this.service = service;
        this.controller = new CredentialConfigurationController(service);
    }

    @BeforeEach
    void setUp() {
        this.persistenceLayer = new HashMap<>();
    }

    @DisplayName("That the response is formatted correctly when POSTing correct data to the BYOB")
    @Test
    void postRequestTest() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        ByobInput input = mapper.readValue(example, ByobInput.class);
        Map<String, ByobInput> testMap = new HashMap<>();
        List<String> vcts = new ArrayList<>();
        vcts.add("mockvctthisisempty");
        String id = service.buildVct(vcts, input.vct());
        testMap.put(id, input);
        Mockito.when(service.getResponseModel(id, input)).thenReturn(testMap);
        Mockito.when(service.buildVct(ArgumentMatchers.anyList(), eq(input.vct()))).thenReturn(id);
        mockMvc.perform(post("/v1/credential-configurations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testMap)));
    }
}
