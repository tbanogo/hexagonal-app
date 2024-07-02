package eu.happycoders.shop.model.money;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

import static eu.happycoders.shop.model.money.TestMoneyFactory.euros;
import static eu.happycoders.shop.model.money.TestMoneyFactory.usDollars;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class MoneyTest {

    private static final Currency EUR = Currency.getInstance("EUR");

    @Test
    void givenAmountWithInvalidScale_whenCreatingMoney_thenThrowsIllegalArgumentException() {
        BigDecimal amountWithScale3 = new BigDecimal(BigInteger.valueOf(12999), 3);

        ThrowingCallable invocation = () -> new Money(EUR, amountWithScale3);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }

    @Test
    void givenAEuroAmount_whenAddDollarMoney_thenThrowsIllegalArgumentException() {
        Money euros = euros(11, 99);
        Money dollars = usDollars(11, 99);

        ThrowingCallable invocation = () -> euros.add(dollars);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }
}
