package eu.happycoders.shop.adapter.out.persistence.jpa;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JpaCartPanacheRepository implements PanacheRepositoryBase<CartJpaEntity, Integer> { }
