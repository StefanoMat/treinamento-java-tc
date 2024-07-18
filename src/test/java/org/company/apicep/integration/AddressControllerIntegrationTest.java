package org.company.apicep.integration;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.company.apicep.repository.AddressRepository;
import org.company.apicep.service.StepAddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    public void setUp() {
        listAppender = new ListAppender<>();
        listAppender.start();
        Logger logger = (Logger) LoggerFactory.getLogger(StepAddressService.class);
        logger.addAppender(listAppender);
        listAppender.list.clear();
    }

    @Test
    void testCreateAddressAsyncBulk() throws Exception {
        String requestBody = """
                [
                  {
                    "cep": "12345-001",
                    "logradouro": "Rua Exemplo 1",
                    "complemento": "Apt 101",
                    "bairro": "Centro",
                    "localidade": "São Paulo",
                    "uf": "SP",
                    "ddd": "11"
                  },
                  {
                    "cep": "12345-002",
                    "logradouro": "Rua Exemplo 2",
                    "complemento": "Apt 102",
                    "bairro": "Centro",
                    "localidade": "São Paulo",
                    "uf": "SP",
                    "ddd": "11"
                  }
                ]
                """;
        mockMvc.perform(post("/api/address/cep/async/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isAccepted());
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            List<ILoggingEvent> logEvents = listAppender.list;
            assertNotNull(logEvents);
            assertEquals(2, logEvents.size());

            assertEquals("address processed: 1", logEvents.get(0).getFormattedMessage());
            assertEquals("address processed: 2", logEvents.get(1).getFormattedMessage());
        });

    }
}
