package com.medvedev.backend.service;

import jakarta.transaction.Transactional;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class PaymentMethodServiceTest {
//    @Autowired
//    private PaymentMethodService paymentMethodService;
//
//    @Autowired
//    private PaymentMethodRepository paymentMethodRepository;
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    @Test
//    public void testGetPaymentMethodByName() {
//        // Save a sample payment method
//        PaymentMethod paymentMethod = new PaymentMethod(null, "Bank Transfer");
//        paymentMethodService.createPaymentMethod(paymentMethod);
//
//        // Fetch it by name
//        PaymentMethod fetchedMethod = paymentMethodService.getPaymentMethodByName("Bank Transfer");
//        assertNotNull(fetchedMethod);
//        assertEquals("Bank Transfer", fetchedMethod.getMethodName());
//    }
//
//    @Test
//    public void testUpdatePaymentMethod() {
//        // Save a new payment method
//        PaymentMethod paymentMethod = new PaymentMethod(null, "Old Name");
//        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);
//
//        // Update the payment method
//        PaymentMethod updateDetails = new PaymentMethod();
//        updateDetails.setMethodName("Updated Name");
//
//        PaymentMethod updatedMethod = paymentMethodService
//                .updatePaymentMethod(savedPaymentMethod.getId(), updateDetails);
//
//        // Assert the changes
//        assertNotNull(updatedMethod);
//        assertEquals("Updated Name", updatedMethod.getMethodName());
//    }
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Test
//    public void testFindByType() {
//        CategoryType type = CategoryType.EXPENSE;
//        List<Category> categories = categoryRepository.findByType(type);
//        assertNotNull(categories);
//        categories.forEach(category -> System.out.println(category.getName()));
//    }
//
//    @Test
//    public void testTransactionRelationships() {
//        Transaction transaction = transactionRepository.findById(1).orElseThrow();
//        assertNotNull(transaction.getAccount());
//        assertNotNull(transaction.getCategory());
//        assertNotNull(transaction.getPaymentMethod());
//        assertNotNull(transaction.getUser());
//    }
//
//    @Test
//    void testInvalidCreateTransactionRequest() {
//        CreateTransactionRequest request = CreateTransactionRequest.builder()
//                .userId(null)
//                .build();
//
//        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
//
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream()
//                .anyMatch(v -> v.getMessage().equals("User ID is required")));
//    }
//


}
