package prices;

import interfaces.Tradable;
import interfaces.TradableDTO;
import GlobalConstants.BookSide;

public class QuoteSide implements Tradable {

    private String user;
    private String product;
    private Price price;
    private BookSide side;
    private int originalVolume;
    private int remainingVolume;
    private int cancelledVolume;
    private int filledVolume;
    private String id;

    public QuoteSide(String newUser, String newProduct, Price newPrice, BookSide side, int newVolume) {

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

    private void setVolume(int newVolume) {
        if (newVolume <= 0 || newVolume >= 10000) {
            throw new IllegalArgumentException("Volume has to be between 0 and 10000!");
        }
        this.originalVolume = newVolume;
    }

    private void setSide(BookSide side) {
        if (side == null) {
            throw new IllegalArgumentException("BookSide cannot be null!");
        }
        this.side = side;
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

        this.cancelledVolume = newVol;
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
        return this.user + " " + this.side + " side quote for " + this.product + ": " + this.price + ", Orig Vol: " + this.originalVolume +
                ", Rem Vol: " + this.remainingVolume + ", Fill Vol: " + this.filledVolume + ", CXL Vol: " + this.cancelledVolume + ", ID: " + this.id;
    }
}
