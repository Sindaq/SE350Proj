package trading;

import GlobalConstants.BookSide;
import exceptions.InvalidPriceException;
import interfaces.Tradable;
import interfaces.TradableDTO;
import prices.Price;
import prices.Quote;

public class ProductBook {

    String product;
    ProductBookSide buySide;
    ProductBookSide sellSide;

    public ProductBook(String newProduct) {


        setProduct(newProduct);
        this.buySide = new ProductBookSide(BookSide.BUY);
        this.sellSide = new ProductBookSide(BookSide.SELL);
    }

    private void setProduct(String newProduct) {
        if (!newProduct.matches("^[A-Z0-9.]+$") || newProduct.length() > 5 || newProduct.isEmpty()) {
            throw new IllegalArgumentException(("must from 1 to 5 letters/numbers, wise, -no spaces, no special characters"));
        }


        this.product = newProduct;
    }

    public TradableDTO add(Tradable t) throws InvalidPriceException {

        if (t == null) {
            throw new IllegalArgumentException("Tradable object can not be null!");
        }

        System.out.println("**ADD: " + t);


        TradableDTO newT = t.getSide() == BookSide.BUY ? this.buySide.add(t) : this.sellSide.add(t);

        tryTrade();

        return t.makeTradableDTO();
    }

    public TradableDTO[] add(Quote qte) throws InvalidPriceException {

        if (qte == null) {
            throw new IllegalArgumentException("Quote object can not be null!");
        }

        removeQuotesForUser(qte.getUser());

        System.out.println("**ADD: " + qte.getQuoteSide(BookSide.BUY));
        System.out.println("**ADD: " + qte.getQuoteSide(BookSide.SELL));
        TradableDTO buy = this.buySide.add(qte.getQuoteSide(BookSide.BUY));
        TradableDTO sell = this.sellSide.add(qte.getQuoteSide(BookSide.SELL));

        tryTrade();
        return new TradableDTO[]{buy, sell};
    }

    public TradableDTO cancel(BookSide side, String orderId) {
        if (side == BookSide.BUY) {
            return this.buySide.cancel(orderId);
        } else {
            return this.sellSide.cancel(orderId);
        }
    }

    public TradableDTO[] removeQuotesForUser(String userName) {

        return new TradableDTO[]{this.buySide.removeQuotesForUser(userName), this.sellSide.removeQuotesForUser(userName)};
    }

    public void tryTrade() throws InvalidPriceException {

        Price topBuy = this.buySide.topOfBookPrice();
        Price topSell = this.sellSide.topOfBookPrice();

        int totalToTrade;

        if (topBuy == null || topSell == null) {
            return;
        }

        totalToTrade = Math.max(this.buySide.topOfBookVolume(), this.sellSide.topOfBookVolume());

        while (totalToTrade > 0) {


            topBuy = this.buySide.topOfBookPrice();
            topSell = this.sellSide.topOfBookPrice();

            if (topBuy == null || topSell == null || topSell.greaterThan(topBuy)) {
                return;
            }

            int toTrade = Math.min(this.buySide.topOfBookVolume(), this.sellSide.topOfBookVolume());

            this.buySide.tradeOut(topBuy, toTrade);
            this.sellSide.tradeOut(topSell, toTrade);

            totalToTrade -= toTrade;
        }

        return;

    }

    public String getTopOfBookString(BookSide side) {

        Price topOfBook = side == BookSide.BUY ? this.buySide.topOfBookPrice() : this.sellSide.topOfBookPrice();
        int topOfVolume = side == BookSide.BUY ? this.buySide.topOfBookVolume() : this.sellSide.topOfBookVolume();


        return topOfBook == null ? "Top of " + side + " book: $0.00 x " + topOfVolume : "Top of " + side + " book: " + topOfBook + " * " + topOfVolume;

    }

    @Override
    public String toString() {


        return "--------------------------------------------\n" +
                "Product Book: " + this.product + "\n" +
                this.buySide.toString() +
                this.sellSide.toString() +
                "--------------------------------------------";
    }
}
