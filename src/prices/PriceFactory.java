package prices;

import exceptions.InvalidPriceException;

public abstract class PriceFactory {

    public static Price makePrice(int value) {
        return new Price(value);
    }

    public static Price makePrice(String stringValueIn) throws InvalidPriceException{

        if (stringValueIn.isEmpty()) {
            throw new InvalidPriceException("Invalid price String value: " + stringValueIn);
        }
        String stringValue = stringValueIn.replace("$", "");
        stringValue = stringValue.replace(",", "");

        if (stringValue.contains(".")) {

            String validDecimal = stringValue.substring(stringValue.indexOf("."));

            try { // After removing '$' or ',', it will throw a NumberFormatException if there are two decimals or any letters
                double number = Double.parseDouble(stringValue);
            } catch (NumberFormatException e) {
                throw new InvalidPriceException("Invalid price String value: " + stringValue);
            }

            if (validDecimal.length() == 2) { // .# is 1 digit after decimal
                throw new InvalidPriceException("Invalid price String value: " + stringValue);
            } else if (validDecimal.length() > 3) {// .## is 2 digits after decimal
                throw new InvalidPriceException("Invalid price String value: " + stringValue);
            }

            stringValue = stringValue.replace(".", "");

            return validDecimal.length() == 1 ? new Price(Integer.parseInt(stringValue)*100) : new Price(Integer.parseInt(stringValue));

        } else {
            return new Price(Integer.parseInt(stringValue) * 100);
        }




//        else if (validDecimal.substring(validDecimal.indexOf(".") + 1).contains(".")) { // checks the string 1 index past the decimal if there's another decimal
//            throw new InvalidPriceException("Too many decimal points!");
//        }
        //check for another decimal


    }
}
