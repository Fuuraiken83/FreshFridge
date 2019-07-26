package com.hfad.freshfridge;

public class Product {
    String label;
    String expirationDate = "expiration date: ";
    boolean isChecked;

    Product(String label, String expirationDate, boolean isChecked){
        this.label = label;
        this.expirationDate += expirationDate;
        this.isChecked = isChecked;
    }

}
