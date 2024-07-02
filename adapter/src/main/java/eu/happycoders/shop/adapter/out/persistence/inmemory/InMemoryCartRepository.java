package eu.happycoders.shop.adapter.out.persistence.inmemory;

import eu.happycoders.shop.application.port.out.persistence.CartRepository;
import eu.happycoders.shop.model.cart.Cart;
import eu.happycoders.shop.model.customer.CustomerId;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCartRepository implements CartRepository {

    private final Map<CustomerId, Cart> carts = new ConcurrentHashMap<>();

    public InMemoryCartRepository() {
        this.save(new Cart(new CustomerId(61157)));
    }

    @Override
    public void save(Cart cart) {
        carts.put(cart.id(), cart);
    }

    @Override
    public void deleteById(CustomerId customerId) {
        carts.remove(customerId);
    }

    @Override
    public Optional<Cart> findByCustomerId(CustomerId customerId) {
        return Optional.of(carts.get(customerId));
    }
}
