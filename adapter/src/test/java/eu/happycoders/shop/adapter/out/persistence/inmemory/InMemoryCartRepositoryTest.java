package eu.happycoders.shop.adapter.out.persistence.inmemory;

import eu.happycoders.shop.adapter.out.persistence.AbstractCartRepositoryTest;
import eu.happycoders.shop.application.port.out.persistence.CartRepository;
import eu.happycoders.shop.application.port.out.persistence.ProductRepository;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class InMemoryCartRepositoryTest extends AbstractCartRepositoryTest<CartRepository, ProductRepository> { }
