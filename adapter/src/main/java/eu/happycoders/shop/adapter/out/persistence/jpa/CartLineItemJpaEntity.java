package eu.happycoders.shop.adapter.out.persistence.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CartLineItem")
@Getter
@Setter
public class CartLineItemJpaEntity {

    @Id
    @GeneratedValue // permet la génération de clé automatiquement
    private Integer id;

    @ManyToOne
    //@JoinColumn(name = "cart_id")
    private CartJpaEntity cart;

    @ManyToOne
    private ProductJpaEntity product;

    private int quantity;
}
