package prices;

import exceptions.InvalidPriceException;

import java.util.Objects;

public class Price implements Comparable<Price>{

    private final int CENTS;

    public Price(int cents) {

        this.CENTS = cents;
    }

    public boolean isNegative() {
        return CENTS < 0;
    }

    public Price add(Price p) throws InvalidPriceException {
        if (p == null) { throw new InvalidPriceException("Cannot add null to a Price object"); }

        return new Price(p.CENTS + this.CENTS);
    }

    public Price subtract(Price p) throws InvalidPriceException {
        if (p == null) { throw new InvalidPriceException("Cannot add null to a Price object"); }

        return new Price(this.CENTS - p.CENTS);
    }

    public Price multiply(int n) {
        return new Price(this.CENTS * n);
    }

    public boolean greaterOrEqual(Price p) throws InvalidPriceException {
        if (p == null) { throw new InvalidPriceException("Cannot check greater than or equal with a null"); }

        return this.CENTS >= p.CENTS;
    }

    public boolean lessOrEqual(Price p) throws InvalidPriceException {
        if (p == null) { throw new InvalidPriceException("Cannot check less than or equal with a null"); }

        return this.CENTS <= p.CENTS;
    }

    public boolean greaterThan(Price p) throws InvalidPriceException {
        if (p == null) { throw new InvalidPriceException("Cannot check greater than with a null"); }

        return this.CENTS > p.CENTS;
    }

    public boolean lessThan(Price p) throws InvalidPriceException {
        if (p == null) { throw new InvalidPriceException("Cannot check less than with a null"); }

        return this.CENTS < p.CENTS;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return CENTS == price.CENTS;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(CENTS);
    }

    @Override
    public int compareTo(Price o) { //1:08:00
        if (o == null) {
            return -1;
        }

        return this.CENTS - o.CENTS;
    }

    @Override
    public String toString() {
        return String.format("$%,.2f", CENTS / 100.0);
    }
}
