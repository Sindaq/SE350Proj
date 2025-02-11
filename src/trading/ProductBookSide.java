package trading;

import GlobalConstants.BookSide;
import exceptions.InvalidPriceException;
import interfaces.Tradable;
import interfaces.TradableDTO;
import prices.Price;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class ProductBookSide {

    BookSide side;
    final TreeMap<Price, ArrayList<Tradable>> bookEntries;

    public ProductBookSide(BookSide side) {

        setSide(side);
        if (side == BookSide.BUY) {
            this.bookEntries = new TreeMap<>(Collections.reverseOrder());
        } else {
            this.bookEntries = new TreeMap<>();
        }
    }

    private void setSide(BookSide side) {

        if (side == null) {
            throw new IllegalArgumentException("Side object can not be null!");
        }
        this.side = side;
    }

    public TradableDTO add(Tradable o) {
        if (!bookEntries.containsKey(o.getPrice())) { // if the key doesn't exist

            bookEntries.put(o.getPrice(), new ArrayList<Tradable>()); // create a new key with the tradable's price
        }
        bookEntries.get(o.getPrice()).add(o); // then add the tradable into the array list

        return o.makeTradableDTO();
    }

    public TradableDTO cancel(String tradableId) {

        if (tradableId == null) {
            throw new IllegalArgumentException("tradableId can not be null!");
        }
        for (Price key : bookEntries.keySet()) {

            for (Tradable t : bookEntries.get(key)) {

                if (t.getId().equals(tradableId)) {
                    System.out.println("**CANCEL: " + t);
                    bookEntries.get(key).remove(t); // remove the tradable from the array list. ArrayList = bookEntries.get(key) and removal is .remove(object)

                    t.setCancelledVolume(t.getCancelledVolume() + t.getRemainingVolume()); // add the tradable’s remaining volume to the tradable’s cancelled volume
                    t.setRemainingVolume(0); // then set the remaining volume to zero

                    if (bookEntries.get(key).isEmpty()) { // if the arraylist is empty after removing the tradable

                        bookEntries.remove(key); // remove the price and the empty array list from bookEntries

                    }
                    return t.makeTradableDTO();
                }
            }
        }

        return null; // if the tradable is not found, return null
    }

    public TradableDTO removeQuotesForUser(String userName) {

        if (userName == null) {
            throw new IllegalArgumentException("userName can not be null!");
        }

        for (Price key : bookEntries.keySet()) {

            for (Tradable t : bookEntries.get(key)) {

                if (t.getUser().equals(userName)) {

                    TradableDTO c = cancel(t.getId()); // save the tradableDTO received from cancel

                    if (bookEntries.get(key) == null) {

                        bookEntries.remove(key);

                    }

                    return c;
                }
            }
        }

        return null; // return null if no quote is found
    }

    public Price topOfBookPrice() {

        if (bookEntries.isEmpty()) {
            return null;
        }

        return bookEntries.firstKey();
    }

    public int topOfBookVolume() {

        if (bookEntries.isEmpty()) {
            return 0;
        }

        int totalTradableVol = 0;

        ArrayList<Tradable> tradables = bookEntries.get(topOfBookPrice());

        for (Tradable t: tradables) {

            totalTradableVol += t.getRemainingVolume();
        }

        return totalTradableVol;
    }

    public void tradeOut(Price price, int volToTrade) throws InvalidPriceException {

        if (topOfBookPrice() == null) {

            return;
        } else if (topOfBookPrice().greaterThan(price)) {

            return;
        }

        ArrayList<Tradable> atPrice = this.bookEntries.get(topOfBookPrice());
        int totalVolAtPrice = 0;

        for (Tradable t : atPrice) {

            totalVolAtPrice += t.getRemainingVolume();
        }

        if (volToTrade >= totalVolAtPrice) {

            for (Tradable t : atPrice) {

                int rv = t.getRemainingVolume();
                t.setFilledVolume(t.getOriginalVolume());
                t.setRemainingVolume(0);
                System.out.println("\t\tFULL FILL: (" + this.side + " " + t.getOriginalVolume() + ") " + t);
            }

            bookEntries.remove(topOfBookPrice());
        } else {

            int remainder = volToTrade;

            for (Tradable t : atPrice) {

                double ratio = (double) t.getRemainingVolume() / (double) totalVolAtPrice;
                int toTrade = (int) Math.ceil(volToTrade * ratio);

                toTrade = Math.min(toTrade, remainder);

                t.setFilledVolume(t.getFilledVolume() + toTrade);
                t.setRemainingVolume(t.getRemainingVolume() - toTrade);

                System.out.println("\t\tPARTIAL FILL: (" + this.side + " " + t.getFilledVolume() + ") " + t);

                remainder -= toTrade;

            }

            return;
        }
    }

    @Override
    public String toString() {

        String productBookSide;

        if (this.side == BookSide.BUY) {

            productBookSide = "Side: BUY\n";
        } else {

            productBookSide = "Side: SELL\n";
        }

        if (!this.bookEntries.isEmpty()) {
            for (Price p : this.bookEntries.keySet()) {

                productBookSide += "\t\t" + p.toString() + ":\n";


                for (Tradable t : this.bookEntries.get(p)) {
                    productBookSide += "\t\t\t" + t + "\n";
                }
            }
        } else {

            productBookSide += ("\t\t<Empty>\n");
//            productBookSide += ("     " + this.bookEntries.toString() + "\n");
        }



        return productBookSide;
    }
}
