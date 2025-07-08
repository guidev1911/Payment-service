package com.microloja.payment_service.service;

import com.microloja.payment_service.dto.PaymentResponseDTO;
import com.microloja.payment_service.exceptions.PaymentNotFoundException;
import com.microloja.payment_service.model.Payment;
import com.microloja.payment_service.model.PaymentStatus;
import com.microloja.payment_service.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository repository;
    private PaymentService service;

    @BeforeEach
    void setup() {
        service = new PaymentService(repository);
    }

    @Test
    void testFindAll_ShouldReturnListOfDTOs() {
        Payment payment = new Payment(
                1L,
                "10",
                "Produto A",
                2,
                new BigDecimal("200.00"),
                LocalDateTime.now(),
                new BigDecimal("100.00"),
                PaymentStatus.APPROVED
        );

        when(repository.findAll()).thenReturn(List.of(payment));

        List<PaymentResponseDTO> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("Produto A", result.get(0).getProduct());
    }

    @Test
    void testFindById_WhenFound_ShouldReturnDTO() {
        Long id = 1L;

        Payment payment = new Payment(
                id,
                "10",
                "Produto B",
                3,
                new BigDecimal("300.00"),
                LocalDateTime.now(),
                new BigDecimal("150.00"),
                PaymentStatus.APPROVED
        );

        when(repository.findById(id)).thenReturn(Optional.of(payment));

        PaymentResponseDTO dto = service.findById(id);

        assertEquals("Produto B", dto.getProduct());
        assertEquals(id, dto.getId());
    }

    @Test
    void testFindById_WhenNotFound_ShouldThrowException() {
        Long id = 2L;
        lenient().when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class, () -> service.findById(id));
    }

}
