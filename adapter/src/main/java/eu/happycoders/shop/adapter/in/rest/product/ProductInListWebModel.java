package eu.happycoders.shop.adapter.in.rest.product;

import eu.happycoders.shop.model.money.Money;
import eu.happycoders.shop.model.product.Product;

public record ProductInListWebModel(
        String id, String name, String description, Money price, int itemsInStock) {

    public static ProductInListWebModel fromDomainModel(Product product) {
        return new ProductInListWebModel(product.id().value(), product.name(), product.description(),
                product.price(), product.itemsInStock());
    }
}
