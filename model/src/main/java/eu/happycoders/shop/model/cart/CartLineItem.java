package eu.happycoders.shop.model.cart;

import eu.happycoders.shop.model.money.Money;
import eu.happycoders.shop.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class CartLineItem {

    private final Product product;
    private int quantity;

    public void increaseQuantityBy(int augend, int itemsInStock)
            throws NotEnoughItemsInStockException {
        if(augend < 1) {
            throw new IllegalArgumentException("You must add at least one item");
        }

        int newQuantity = this.quantity + augend;

        if(itemsInStock < newQuantity) {
            throw new NotEnoughItemsInStockException(
                    ("Product %s has less items in stock %d "
                            + " thant the requested total quantity (%d)")
                            .formatted(this.product.id(), product.itemsInStock(), newQuantity),
                    product.itemsInStock());
        }

        this.quantity = newQuantity;
    }

    public Money subTotal() {
        return this.product.price().multiply(quantity);
    }
}
