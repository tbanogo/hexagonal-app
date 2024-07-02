package eu.happycoders.shop.model.cart;

import eu.happycoders.shop.model.product.Product;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static eu.happycoders.shop.model.cart.TestCartFactory.emptyCartForRandomCustomer;
import static eu.happycoders.shop.model.money.TestMoneyFactory.euros;
import static eu.happycoders.shop.model.product.TestProductFactory.createTestProduct;
import static org.assertj.core.api.Assertions.*;

public class CartTest {

    @Test
    void givenEmptyCart_whenAddingTwoProducts_thenProductsAreInCart() throws NotEnoughItemsInStockException {
        Cart cart = emptyCartForRandomCustomer();

        Product product1 = createTestProduct(euros(12, 99));
        Product product2 = createTestProduct(euros(5, 97));

        cart.addProduct(product1, 3);
        cart.addProduct(product2, 5);

        assertThat(cart.lineItems()).hasSize(2);
        assertThat(cart.lineItems().get(0).product()).isEqualTo(product1);
        assertThat(cart.lineItems().get(0).quantity()).isEqualTo(3);
        assertThat(cart.lineItems().get(1).product()).isEqualTo(product2);
        assertThat(cart.lineItems().get(1).quantity()).isEqualTo(5);
    }

    @Test
    void givenEmptyCart_whenAddingTwoProducts_thenNumberOfItemsAndSubTotalIsCalculatedCorrectly() throws NotEnoughItemsInStockException {
        Cart cart = emptyCartForRandomCustomer();

        Product product1 = createTestProduct(euros(12, 99));
        Product product2 = createTestProduct(euros(5, 97));

        cart.addProduct(product1, 3);
        cart.addProduct(product2, 5);

        assertThat(cart.numberOfItems()).isEqualTo(8);
        assertThat(cart.subTotal()).isEqualTo(euros(68, 82));
    }

    @Test
    void givenCartWithAFewProductsAvailable_whenAddingMoreProductsThanAvailableInCart_thenThrowsNotEnoughItemsInStockException() {
        Cart cart = emptyCartForRandomCustomer();

        Product product = createTestProduct(euros(9, 97), 3);

        ThrowingCallable invocation = () -> cart.addProduct(product, 4);

        assertThatExceptionOfType(NotEnoughItemsInStockException.class)
                .isThrownBy(invocation)
                .satisfies(ex -> assertThat(ex.itemsInStock()).isEqualTo(product.itemsInStock()));
    }

    @Test
    void givenCartWithAFewProductsAvailable_whenAddingAllAvailableProductsToTheCart_thenSucceeds() {
        Cart cart = emptyCartForRandomCustomer();

        Product product = createTestProduct(euros(9, 97), 3);

        ThrowingCallable invocation = () -> cart.addProduct(product, 3);

        assertThatNoException().isThrownBy(invocation);
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void givenEmptyCart_whenAddingLessThanOneProductInCart_thenThrowsIllegalArgumentException(int quantity) {
        Cart cart = emptyCartForRandomCustomer();

        Product product = createTestProduct(euros(1, 49));

        ThrowingCallable invocation = () -> cart.addProduct(product, quantity);

        assertThatIllegalArgumentException()
                .isThrownBy(invocation);
    }
}
