package eu.happycoders.shop.model.money;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public record Money(Currency currency, BigDecimal amount) {

    /**
     * Constructeur compact utilisé pour ajouter des vérifications de validité lors
     * de la création d'une instance de Money
     * @param currency
     * @param amount
     */
    public Money {
        Objects.requireNonNull(currency, "'currency' must not be null");
        Objects.requireNonNull(amount, "'amount' must not be null");
        if(amount.scale() > currency.getDefaultFractionDigits()) {
            throw new IllegalArgumentException(
                    ("Scale of amount %s is greater "
                            + "than the number of fraction digits used with currency %s")
                            .formatted(amount, currency)
            );
        }
    }

    /**
     * Créer une instance de Money en utilisant des valeures entières (mayor pour les
     * chiffres avant la virgule et minor pour ceux après la virgule)
     * @param currency
     * @param mayor
     * @param minor
     * @return
     */
    public static Money of(Currency currency, int mayor, int minor) {
        int scale = currency.getDefaultFractionDigits();

        return new Money(
                currency,
                BigDecimal.valueOf(mayor).add(BigDecimal.valueOf(minor, scale))
        );
    }

    /**
     * Permte de concaténer deux montants
     * @param augend
     * @return
     */
    public Money add(Money augend) {
        if(!this.currency.equals(augend.currency)) {
            throw new IllegalArgumentException(
                    ("Currency %s of augend does not match this money's currency %s")
                            .formatted(augend.currency, this.currency)
            );
        }

        return new Money(currency, this.amount.add(augend.amount));
    }

    public Money multiply(int multiplicand) {
        return new Money(currency, amount.multiply(BigDecimal.valueOf(multiplicand)));
    }
}
