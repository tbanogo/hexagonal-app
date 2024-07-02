package eu.happycoders.shop.application.service.cart;

import eu.happycoders.shop.application.port.in.cart.AddToCartUseCase;
import eu.happycoders.shop.application.port.in.cart.ProductNotFoundException;
import eu.happycoders.shop.application.port.out.persistence.CartRepository;
import eu.happycoders.shop.application.port.out.persistence.ProductRepository;
import eu.happycoders.shop.model.cart.Cart;
import eu.happycoders.shop.model.cart.NotEnoughItemsInStockException;
import eu.happycoders.shop.model.customer.CustomerId;
import eu.happycoders.shop.model.product.Product;
import eu.happycoders.shop.model.product.ProductId;

import java.util.Objects;

public class AddToCartService implements AddToCartUseCase {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public AddToCartService(
            ProductRepository productRepository,
            CartRepository cartRepository
    ) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart addToCart(
            CustomerId customerId,
            ProductId productId,
            int quantity) throws
            ProductNotFoundException, NotEnoughItemsInStockException {
        Objects.requireNonNull(customerId, "'customerId' must not be null");
        Objects.requireNonNull(productId, "'productId' must not be null");
        if(quantity < 1) {
            throw new IllegalArgumentException("'quantity' must be greater than 0");
        }

        Product product = this.productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        Cart cart = this.cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> new Cart(customerId));

        cart.addProduct(product, quantity);

        cartRepository.save(cart);

        return cart;
    }
}
