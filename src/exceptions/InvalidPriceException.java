package exceptions;

public class InvalidPriceException extends Exception{

    String message = "InvalidPriceException";

    public InvalidPriceException(String message) {
        super(message);
    }
}
