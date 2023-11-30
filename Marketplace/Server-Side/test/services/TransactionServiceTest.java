package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        transactionService = new TransactionService();
    }

    @Test
    void addToCart() {
        assertTrue(transactionService.addToCart("PztsCpytYcTxgAKCJVb", "3452345klj52345234", "2034vas2345234",1));
    }

    @Test
    void removeFromCart() {
        assertTrue(transactionService.removeFromCart("PztsCpytYcTxgAKCJVb", "3452345klj52345234",1));
    }

    @Test
    void clearCart() {
        assertTrue(transactionService.clearCart("PztsCpytYcTxgAKCJVb"));
    }

    @Test
    void getCartContents() {
        System.out.println(transactionService.getCartContents("PztsCpytYcTxgAKCJVb"));

    }


}