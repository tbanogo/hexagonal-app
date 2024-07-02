package eu.happycoders.shop.application.service.product;

import eu.happycoders.shop.application.port.out.persistence.ProductRepository;
import eu.happycoders.shop.model.product.Product;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static eu.happycoders.shop.model.money.TestMoneyFactory.euros;
import static eu.happycoders.shop.model.product.TestProductFactory.createTestProduct;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FindProductsServiceTest {

    private static final Product TEST_PRODUCT_1 = createTestProduct(euros(11, 99));
    private static final Product TEST_PRODUCT_2 = createTestProduct(euros(23, 99));

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final FindProductsService findProductsService =
            new FindProductsService(productRepository);

    @Test
    void givenASearchQuery_whenFindingByNameOrDescription_thenReturnsTheProducts() {
        when(productRepository.findByNameOrDescription("one")).thenReturn(List.of(TEST_PRODUCT_1));
        when(productRepository.findByNameOrDescription("two")).thenReturn(List.of(TEST_PRODUCT_2));
        when(productRepository.findByNameOrDescription("one-two")).thenReturn(List.of(TEST_PRODUCT_1, TEST_PRODUCT_2));
        when(productRepository.findByNameOrDescription("empty")).thenReturn(List.of(null));
    }

    @Test
    void givenTooSearchQuery_whenFindingByNameOrDescription_thenThrowsException() {
        String searchQuery = "x";

        ThrowingCallable invocation = () -> findProductsService.findByNameOrDescription(searchQuery);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }
}