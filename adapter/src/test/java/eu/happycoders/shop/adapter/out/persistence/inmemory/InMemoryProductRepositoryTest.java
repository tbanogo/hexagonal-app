package eu.happycoders.shop.adapter.out.persistence.inmemory;

import eu.happycoders.shop.adapter.out.persistence.AbstractProductRepositoryTest;

public class InMemoryProductRepositoryTest extends AbstractProductRepositoryTest<InMemoryProductRepository> {

    @Override
    protected InMemoryProductRepository createProductRepository() {
        return new InMemoryProductRepository();
    }
}
