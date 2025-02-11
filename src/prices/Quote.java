package prices;

import GlobalConstants.BookSide;

public class Quote {

    private String user;
    private String product;
    private QuoteSide buySide;
    private QuoteSide sellSide;

    public Quote(String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume, String userName) {

        this.user = userName;
        this.product = symbol;
        this.buySide = new QuoteSide(userName, symbol, buyPrice, BookSide.BUY, buyVolume);
        this.sellSide = new QuoteSide(userName, symbol, sellPrice, BookSide.SELL, sellVolume);
    }

    private void setUser(String newUser) {
        if (!newUser.matches("^[A-Z]+$") || newUser.length() != 3) {
            throw new IllegalArgumentException("User must be 3 letters, no spaces, no numbers, no special characters ");
        }

        this.user = newUser;
    }

    private void setProduct(String newProduct) {
        if (!newProduct.matches("^[A-Z0-9.]+$") || newProduct.length() > 5 || newProduct.isEmpty()) {
            throw new IllegalArgumentException(("must from 1 to 5 letters/numbers, wise, -no spaces, no special characters"));
        }
        this.product = newProduct;
    }

    public QuoteSide getQuoteSide(BookSide sideIn) {
        if (sideIn == BookSide.BUY) {
            return this.buySide;
        } else {
            return this.sellSide;
        }
    }

    public String getSymbol() {
        return this.product;
    }

    public String getUser() {
        return this.user;
    }
}
