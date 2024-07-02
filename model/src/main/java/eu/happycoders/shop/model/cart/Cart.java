package eu.happycoders.shop.model.cart;

import eu.happycoders.shop.model.customer.CustomerId;
import eu.happycoders.shop.model.money.Money;
import eu.happycoders.shop.model.product.Product;
import eu.happycoders.shop.model.product.ProductId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Accessors(fluent = true)
// Créer un constructeur avec les attributs finaux
@RequiredArgsConstructor
public class Cart {

    @Getter
    private final CustomerId id;
    private final Map<ProductId, CartLineItem> lineItems = new LinkedHashMap<>();

    /**
     * ComputeIfPresent renvoi CartLineItem dans tout les cas,
     * si product.id() existe dans la map, c'est la liste associée qui est renvoyé
     * sinon ignore -> new CartLineItem(product) est créer et associé à product.id()
     * pour être ajouté dans la map
     * @param product
     * @param quantity
     * @throws NotEnoughItemsInStockException
     */
    public void addProduct(Product product, int quantity) throws NotEnoughItemsInStockException {
        lineItems.computeIfAbsent(product.id(), ignore -> new CartLineItem(product))
                .increaseQuantityBy(quantity, product.itemsInStock());
    }

    /**
     * Retourne une liste immuable de la liste des produits
     * @return
     */
    public List<CartLineItem> lineItems() {
        return List.copyOf(lineItems.values());
    }

    /**
     * Retourne la quantité totale d'elements dans le panier
     * @return
     */
    public int numberOfItems() {
        return lineItems.values().stream().mapToInt(CartLineItem::quantity).sum();
    }

    public Money subTotal() {
        return  lineItems.values().stream()
                .map(CartLineItem::subTotal)
                .reduce(Money::add)
                .orElse(null);
    }

    // Use only for reconstituting a Cart entity from the database
    public void putProductIgnoringNotEnoughItemsInStock(Product product, int quantity) {
        lineItems.put(product.id(), new CartLineItem(product, quantity));
    }
}
