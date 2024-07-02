package eu.happycoders.shop.model.customer;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CustomerIdTest {

    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void givenAValueLessThan1_whenCreatingCustomerId_thenThrowsException(int value) {
        ThrowableAssert.ThrowingCallable invocation = () -> new CustomerId(value);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 8_765, 2_000_000_000})
    void givenAValueGreatThanOrEqualTo1_whenCreatingCustomerId_thenSucceeds(int value) {
        CustomerId customerId = new CustomerId(value);

        assertThat(customerId.value()).isEqualTo(value);
    }

}
