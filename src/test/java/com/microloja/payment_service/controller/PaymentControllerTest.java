package com.microloja.payment_service.controller;

import com.microloja.payment_service.dto.PaymentResponseDTO;
import com.microloja.payment_service.exceptions.PaymentNotFoundException;
import com.microloja.payment_service.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(PaymentController.class)
@Import({PaymentControllerTest.MockedConfig.class, PaymentControllerTest.ValidationConfig.class})
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentService paymentService;

    @TestConfiguration
    static class MockedConfig {
        @Bean
        public PaymentService paymentService() {
            return Mockito.mock(PaymentService.class);
        }
    }
    @TestConfiguration
    static class ValidationConfig {
        @Bean
        public MethodValidationPostProcessor methodValidationPostProcessor() {
            return new MethodValidationPostProcessor();
        }
    }

    @Test
    void testGetAll_ShouldReturnList() throws Exception {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(1L);
        dto.setProduct("Produto Teste");
        dto.setAmount(new BigDecimal("99.99"));
        dto.setOrderId("5");
        dto.setProcessedAt(LocalDateTime.now());

        Mockito.when(paymentService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].product").value("Produto Teste"));
    }

    @Test
    void testGetById_WhenValidId_ShouldReturnDTO() throws Exception {
        Long id = 1L;
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(id);
        dto.setProduct("Produto XYZ");
        dto.setAmount(new BigDecimal("199.99"));
        dto.setOrderId("10");
        dto.setProcessedAt(LocalDateTime.now());

        Mockito.when(paymentService.findById(id)).thenReturn(dto);

        mockMvc.perform(get("/payments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.product").value("Produto XYZ"));
    }

    @Test
    void testGetById_WhenInvalidId_ShouldReturn404() throws Exception {
        Long invalidId = 99999999999999L;

        Mockito.when(paymentService.findById(invalidId))
                .thenThrow(new PaymentNotFoundException(invalidId));

        mockMvc.perform(get("/payments/{id}", invalidId))
                .andExpect(status().isNotFound());
    }
    @Test
    void testGetById_WhenNegativeId_ShouldReturn400() throws Exception {
        mockMvc.perform(get("/payments/{id}", -1L))
                .andExpect(status().isBadRequest());
    }
}
