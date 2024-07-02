package eu.happycoders.shop.application.service.cart;

import eu.happycoders.shop.application.port.in.cart.ProductNotFoundException;
import eu.happycoders.shop.application.port.out.persistence.CartRepository;
import eu.happycoders.shop.application.port.out.persistence.ProductRepository;
import eu.happycoders.shop.model.cart.Cart;
import eu.happycoders.shop.model.cart.NotEnoughItemsInStockException;
import eu.happycoders.shop.model.customer.CustomerId;
import eu.happycoders.shop.model.product.Product;
import eu.happycoders.shop.model.product.ProductId;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static eu.happycoders.shop.model.money.TestMoneyFactory.euros;
import static eu.happycoders.shop.model.product.TestProductFactory.createTestProduct;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JUnit crée une instance pour chaque méthode de Test
 * La raison d'utiliser des attributs static est que l'on veux
 * partager les attributs entre les tests.
 * En fonction de la configuration, les méthodes sont exécuter les une
 * après les autres de façon indépendant dans leur propre contexte.
 *
 * Dans ce cas précis, pour chaqun des 4 méthodes, une instance de AddToCartServiceTest est
 * créer ensuite initTestDoubles est appélé et enfin la méthode concernée.
 */
public class AddToCartServiceTest {

    private static final CustomerId TEST_CUSTOMER_ID = new CustomerId(61157);
    private static final Product TEST_PRODUCT_1 = createTestProduct(euros(19, 99));
    private static final Product TEST_PRODUCT_2 = createTestProduct(euros(25, 99));

    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final AddToCartService addToCartService =
            new AddToCartService(productRepository, cartRepository);

    @BeforeEach
    void initTestDoubles() {
        when(productRepository.findById(TEST_PRODUCT_1.id()))
                .thenReturn(Optional.of(TEST_PRODUCT_1));

        when(productRepository.findById(TEST_PRODUCT_2.id()))
                .thenReturn(Optional.of(TEST_PRODUCT_2));
    }

    @Test
    void givenExistingCart_whenAddingToCart_thenCartWithAddedProductIsSaveAndReturned()
        throws NotEnoughItemsInStockException, ProductNotFoundException {
        // Arrange : prépare l'état initial du test
        Cart persistedCart = new Cart(TEST_CUSTOMER_ID);
        persistedCart.addProduct(TEST_PRODUCT_1, 1);

        when(cartRepository.findByCustomerId(TEST_CUSTOMER_ID))
                .thenReturn(Optional.of(persistedCart));

        // Act : Exécute l'opération sous test
        Cart cart = addToCartService.addToCart(TEST_CUSTOMER_ID, TEST_PRODUCT_2.id(), 3);

        // Assert : Vérifie que l'état final correspond aux attentes
        verify(cartRepository).save(cart);

        assertThat(cart.lineItems()).hasSize(2);
        assertThat(cart.lineItems().get(0).product()).isEqualTo(TEST_PRODUCT_1);
        assertThat(cart.lineItems().get(0).quantity()).isEqualTo(1);
        assertThat(cart.lineItems().get(1).product()).isEqualTo(TEST_PRODUCT_2);
        assertThat(cart.lineItems().get(1).quantity()).isEqualTo(3);

        // More tests
    }

    @Test
    void givenNoExistingCart_whenAddingToCart_thenCartWithAddedProductIsSaveAndReturned()
        throws NotEnoughItemsInStockException, ProductNotFoundException{
        Cart cart = addToCartService.addToCart(TEST_CUSTOMER_ID, TEST_PRODUCT_1.id(), 2);

        verify(cartRepository).save(cart);

        assertThat(cart.lineItems()).hasSize(1);
        assertThat(cart.lineItems().get(0).product()).isEqualTo(TEST_PRODUCT_1);
        assertThat(cart.lineItems().get(0).quantity()).isEqualTo(2);
    }

    @Test
    void givenAnUnknowProductId_whenAddingToCart_thenThrowsException() {
        ProductId productId = ProductId.randomProductId();

        ThrowingCallable invocation =
                () -> addToCartService.addToCart(TEST_CUSTOMER_ID, productId, 1);

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(invocation);

        verify(cartRepository, never()).save(any());
    }

    @Test
    void givenQuantityLessThan1_whenAddingToCart_thenThrowsException() {
        int quantity = 0;

        ThrowingCallable invocation =
                () -> addToCartService.addToCart(TEST_CUSTOMER_ID, TEST_PRODUCT_1.id(), quantity);

        assertThatIllegalArgumentException()
                .isThrownBy(invocation);
        verify(cartRepository, never()).save(any());
    }
}
