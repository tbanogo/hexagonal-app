package eu.happycoders.shop.adapter.in.rest.cart;

import eu.happycoders.shop.application.port.in.cart.AddToCartUseCase;
import eu.happycoders.shop.application.port.in.cart.EmptyCartUseCase;
import eu.happycoders.shop.application.port.in.cart.GetCartUseCase;
import eu.happycoders.shop.application.port.in.cart.ProductNotFoundException;
import eu.happycoders.shop.model.cart.Cart;
import eu.happycoders.shop.model.cart.NotEnoughItemsInStockException;
import eu.happycoders.shop.model.customer.CustomerId;
import eu.happycoders.shop.model.product.Product;
import eu.happycoders.shop.model.product.ProductId;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static eu.happycoders.shop.adapter.in.rest.HttpTestCommons.assertThatResponseIsError;
import static eu.happycoders.shop.adapter.in.rest.cart.CartsControllerAssertions.assertThatResponseIsCart;
import static eu.happycoders.shop.model.money.TestMoneyFactory.euros;
import static eu.happycoders.shop.model.product.TestProductFactory.createTestProduct;
import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;
import static org.mockito.Mockito.*;

@QuarkusTest
public class CartsControllerTest {

    private static final CustomerId TEST_CUSTOMER_ID = new CustomerId(61157);
    private static final Product TEST_PRODUCT_1 = createTestProduct(euros(19, 99));
    private static final Product TEST_PRODUCT_2 = createTestProduct(euros(25, 99));

    @InjectMock
    AddToCartUseCase addToCartUseCase;

    @InjectMock
    GetCartUseCase getCartUseCase;

    @InjectMock
    EmptyCartUseCase emptyCartUseCase;

    @Test
    void givenAnInvalidCustomerId_whenGetCart_thenReturnedAnError() {
        String customerId = "foo";

        Response response =
                given().get("/carts/" + customerId).then().extract().response();

        assertThatResponseIsError(response, BAD_REQUEST, "Invalid 'customerId'");
    }

    @Test
    void givenAValidCustomerIdAndACart_whenGetCart_thenReturnedCart()
            throws NotEnoughItemsInStockException {
        CustomerId customerId = TEST_CUSTOMER_ID;

        Cart cart = new Cart(customerId);
        cart.addProduct(TEST_PRODUCT_1, 3);
        cart.addProduct(TEST_PRODUCT_2, 5);

        when(getCartUseCase.getCart(customerId)).thenReturn(cart);

        Response response =
                given().get("/carts/" + customerId.value()).then().extract().response();

        assertThatResponseIsCart(response, cart);
    }

    @Test
    void givenSomeTestData_whenAddLineItem_thenReturnsUpdatedCart()
            throws NotEnoughItemsInStockException, ProductNotFoundException {
        // Arrange
        CustomerId customerId = TEST_CUSTOMER_ID;
        ProductId productId = TEST_PRODUCT_1.id();
        int quantity = 5;

        Cart cart = new Cart(customerId);
        cart.addProduct(TEST_PRODUCT_1, quantity);

        when(addToCartUseCase.addToCart(customerId, productId, quantity)).thenReturn(cart);

        // Act
        Response response =
                given()
                        .queryParam("productId", productId.value())
                        .queryParam("quantity", quantity)
                        .post("/carts/" + customerId.value() + "/line-items")
                        .then()
                        .extract()
                        .response();

        // Assert
        assertThatResponseIsCart(response, cart);
    }

    @Test
    void givenAnInvalidCustomerId_whenAddLineItem_thenReturnsAnError() {
        String customerId = "foo";
        ProductId productId = TEST_PRODUCT_1.id();
        int quantity = 5;

        Response response =
                given()
                        .queryParam("productId", productId)
                        .queryParam("quantity", quantity)
                        .post("/carts/" + customerId + "/line-items")
                        .then()
                        .extract()
                        .response();

        assertThatResponseIsError(response, BAD_REQUEST, "Invalid 'customerId'");
    }

    @Test
    void givenAnInvalidProductId_whenAddLineItem_thenReturnsAnError() {
        CustomerId customerId = TEST_CUSTOMER_ID;
        String productId = "";
        int quantity = 5;

        Response response =
                given()
                        .queryParam("productId", productId)
                        .queryParam("quantity", quantity)
                        .post("/carts/" + customerId.value() + "/line-items")
                        .then()
                        .extract()
                        .response();

        assertThatResponseIsError(response, BAD_REQUEST, "Invalid 'productId'");
    }

    @Test
    void givenProductNotFound_whenAddLineItem_thenReturnsProductNotFoundException()
            throws NotEnoughItemsInStockException, ProductNotFoundException {
        CustomerId customerId = TEST_CUSTOMER_ID;
        ProductId productId = TEST_PRODUCT_1.id();
        int quantity = 5;

        when(addToCartUseCase.addToCart(customerId, productId, quantity))
                .thenThrow(new ProductNotFoundException());

        Response response =
                given()
                        .queryParam("productId", productId)
                        .queryParam("quantity", quantity)
                        .post("/carts/" + customerId.value() + "/line-items")
                        .then()
                        .extract()
                        .response();

        assertThatResponseIsError(response, BAD_REQUEST, "The request product does not exist");
    }

    @Test
    void givenNotEnoughItemsInStock_whenAddLineItem_thenReturnsNotEnoughItemsInStockException()
            throws NotEnoughItemsInStockException, ProductNotFoundException {
        CustomerId customerId = TEST_CUSTOMER_ID;
        ProductId productId = TEST_PRODUCT_1.id();
        int quantity = 5;

        when(addToCartUseCase.addToCart(customerId, productId, quantity))
                .thenThrow(new NotEnoughItemsInStockException("Not enough items in stock", 2));

        Response response =
                given()
                        .queryParam("productId", productId)
                        .queryParam("quantity", quantity)
                        .post("/carts/" + customerId.value() + "/line-items")
                        .then()
                        .extract()
                        .response();

        assertThatResponseIsError(response, BAD_REQUEST, "Only 2 items in stock");
    }

    @Test
    void givenAnInvalidCustomerId_whenEmptyCart_thenReturnedAnError() {
        String customerId = "foo";

        Response response =
                given().delete("/carts/" + customerId).then().extract().response();

        assertThatResponseIsError(response, BAD_REQUEST, "Invalid 'customerId'");
    }

    @Test
    void givenAValidCustomerId_whenEmptyCart_thenReturnedCart() {
        CustomerId customerId = TEST_CUSTOMER_ID;

        given()
                .delete("/carts/" + customerId.value())
                .then()
                .statusCode(NO_CONTENT.getStatusCode());

        verify(emptyCartUseCase).emptyCart(customerId);
    }
}
