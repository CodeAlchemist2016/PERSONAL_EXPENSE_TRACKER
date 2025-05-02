package com.medvedev.backend.service;

import com.medvedev.backend.entity.PaymentMethod;
import com.medvedev.backend.repository.PaymentMethodRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    // Retrieve all payment methods
    public Page<PaymentMethod> getAllPaymentMethods(Pageable pageable) {
        return paymentMethodRepository.findAll(pageable);
    }

    // Retrieve a specific payment method by ID
    public PaymentMethod getPaymentMethodById(Integer id) {
        return paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found for ID: " + id));
    }

    // Retrieve a payment method by name (case insensitive)
    public PaymentMethod getPaymentMethodByName(String methodName) {
        return paymentMethodRepository.findByMethodNameIgnoreCase(methodName)
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found: " + methodName));
    }

    // Create or save a new payment method
    @Transactional
    public PaymentMethod createPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod.getMethodName() == null || paymentMethod.getMethodName().isEmpty()) {
            throw new IllegalArgumentException("Payment method name cannot be null or empty");
        }
        return paymentMethodRepository.save(paymentMethod);
    }

    // Update an existing payment method
    @Transactional
    public PaymentMethod updatePaymentMethod(Integer id, PaymentMethod newPaymentMethodDetails) {
        PaymentMethod existingMethod = paymentMethodRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment method not found for ID: " + id));

        if (newPaymentMethodDetails.getMethodName() != null) {
            existingMethod.setMethodName(newPaymentMethodDetails.getMethodName());
        }

        return paymentMethodRepository.save(existingMethod);
    }

    // Delete a payment method by ID
    @Transactional
    public void deletePaymentMethodById(Integer id) {
        if (!paymentMethodRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment method not found for ID: " + id);
        }
        paymentMethodRepository.deleteById(id);
    }
}
