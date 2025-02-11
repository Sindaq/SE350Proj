package trading;

import GlobalConstants.BookSide;
import interfaces.Tradable;
import interfaces.TradableDTO;
import prices.Price;

/*
The Order class represents an order to buy or sell a volume (i.e., quantity) of stock at a certain price. The order will
also maintain its processing state, detailed below.
 */
public class Order implements Tradable {

    private String user;
    private String product;
    private Price price;
    private BookSide side;
    private int originalVolume;
    private int remainingVolume;
    private int cancelledVolume;
    private int filledVolume;
    private String id;


    public Order(String newUser, String newProduct, Price newPrice, int newVolume, BookSide side) {

        setUser(newUser);
        setProduct(newProduct);
        setPrice(newPrice);
        setSide(side);
        setVolume(newVolume);
        this.remainingVolume = this.originalVolume;
        setCancelledVolume(0);
        setFilledVolume(0);
        this.id = this.user + this.product + this.price + System.nanoTime();

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

    private void setPrice(Price newPrice) {
        if (newPrice == null) {
            throw new IllegalArgumentException("Price object cannot be null!");
        }
        this.price = newPrice;
    }

    private void setSide(BookSide side) {
        if (side == null) {
            throw new IllegalArgumentException("BookSide cannot be null!");
        }
        this.side = side;
    }

    private void setVolume(int newVolume) {
        if (newVolume <= 0 || newVolume >= 10000) {
            throw new IllegalArgumentException("Volume has to be between 0 and 10000!");
        }
        this.originalVolume = newVolume;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getRemainingVolume() {
        return this.remainingVolume;
    }

    @Override
    public void setCancelledVolume(int newVol) {
        this.cancelledVolume = 0;
    }

    @Override
    public int getCancelledVolume() {
        return this.cancelledVolume;
    }

    @Override
    public void setRemainingVolume(int newVol) {
        this.remainingVolume = newVol;
    }

    @Override
    public TradableDTO makeTradableDTO() {
        return new TradableDTO(this);
    }

    @Override
    public Price getPrice() {
        return this.price;
    }

    @Override
    public void setFilledVolume(int newVol) {
        this.filledVolume = newVol;
    }

    @Override
    public int getFilledVolume() {
        return this.filledVolume;
    }

    @Override
    public BookSide getSide() {
        return this.side;
    }

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public String getProduct() {
        return this.product;
    }

    @Override
    public int getOriginalVolume() {
        return this.originalVolume;
    }

    @Override
    public String toString() {
        return this.user + " " + this.side + " order: " + this.product + " at " + this.price + ", Orig Vol: " + this.originalVolume +
                ", Rem Vol: " + this.remainingVolume + ", Fill Vol: " + this.filledVolume + ", CXL Vol: " + this.cancelledVolume + ", ID: " + this.id;
    }
}
