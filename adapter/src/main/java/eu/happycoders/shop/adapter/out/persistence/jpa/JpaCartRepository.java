package eu.happycoders.shop.adapter.out.persistence.jpa;

import eu.happycoders.shop.application.port.out.persistence.CartRepository;
import eu.happycoders.shop.model.cart.Cart;
import eu.happycoders.shop.model.customer.CustomerId;
import io.quarkus.arc.lookup.LookupIfProperty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;

@LookupIfProperty(name = "persistence", stringValue = "mysql")
@ApplicationScoped
public class JpaCartRepository implements CartRepository {

    private final JpaCartPanacheRepository jpaCartPanacheRepository;

    public JpaCartRepository(JpaCartPanacheRepository jpaCartPanacheRepository) {
        this.jpaCartPanacheRepository = jpaCartPanacheRepository;
    }

    @Override
    @Transactional
    public void save(Cart cart) {
        jpaCartPanacheRepository.getEntityManager().merge(CartMapper.toJpaEntity(cart));
    }

    @Override
    @Transactional
    public void deleteById(CustomerId customerId) {
        jpaCartPanacheRepository.deleteById(customerId.value());
    }

    @Override
    @Transactional
    public Optional<Cart> findByCustomerId(CustomerId customerId) {
        CartJpaEntity cartJpaEntity = jpaCartPanacheRepository.findById(customerId.value());
        return CartMapper.toModelEntityOptional(cartJpaEntity);
    }

}
