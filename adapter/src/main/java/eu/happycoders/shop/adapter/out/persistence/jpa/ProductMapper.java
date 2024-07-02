package eu.happycoders.shop.adapter.out.persistence.jpa;

import eu.happycoders.shop.model.money.Money;
import eu.happycoders.shop.model.product.Product;
import eu.happycoders.shop.model.product.ProductId;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

final class ProductMapper {

    private ProductMapper() {}

    static ProductJpaEntity toJpaEntity(Product product) {
        ProductJpaEntity productJpa = new ProductJpaEntity();

        productJpa.setId(product.id().value());
        productJpa.setName(product.name());
        productJpa.setDescription(product.description());
        productJpa.setPriceAmount(product.price().amount());
        productJpa.setPriceCurrency(product.price().currency().getCurrencyCode());
        productJpa.setItemsInStock(product.itemsInStock());

        return productJpa;
    }

    static Optional<Product> toModelEntityOptional(ProductJpaEntity productJpa) {
        return Optional.ofNullable(productJpa).map(ProductMapper::toModelEntity);
    }

    static Product toModelEntity(ProductJpaEntity productJpa) {
        return new Product(
                new ProductId(productJpa.getId()),
                productJpa.getName(),
                productJpa.getDescription(),
                new Money(
                        Currency.getInstance(productJpa.getPriceCurrency()),
                        productJpa.getPriceAmount()),
                productJpa.getItemsInStock()
        );
    }

    static List<Product> toModelEntities(List<ProductJpaEntity> productJpaEntities) {
        return productJpaEntities.stream().map(ProductMapper::toModelEntity).toList();
    }
}
