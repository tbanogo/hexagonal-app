package eu.happycoders.shop.application.service.cart;

import eu.happycoders.shop.application.port.out.persistence.CartRepository;
import eu.happycoders.shop.model.customer.CustomerId;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class EmptyCartServiceTest {

    private static final CustomerId TEST_CUSTOMER_ID = new CustomerId(61157);

    CartRepository cartRepository = mock(CartRepository.class);
    EmptyCartService emptyCartService =
            new EmptyCartService(cartRepository);

    @Test
    void givenExistingCart_whenDeletingCart_thenSucceeds() {
        emptyCartService.emptyCart(TEST_CUSTOMER_ID);

        verify(cartRepository).deleteById(TEST_CUSTOMER_ID);
    }
}
