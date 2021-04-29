/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict373_a2_33276203;

import java.io.Serializable;

public class Address implements Serializable {
    private int number = 0;
    private String street = "N/A";
    private String suburb = "N/A";
    private int postcode = 0;

    Address() {}

    Address(int number, String street, String suburb, int postcode) {
        this.number = number;
        this.street = street;
        this.suburb = suburb;
        this.postcode = postcode;
    }

    //Getters
    public int getNumber() { return number; }
    public String getStreet() { return street; }
    public String getSuburb() { return suburb; }
    public int getPostcode() { return postcode; }

    //Setters
    public void setNumber(int number) { this.number = number; }
    public void setStreet(String street) { this.street = street; }
    public void setSuburb(String suburb) { this.suburb = suburb; }
    public void setPostcode(int postcode) { this.postcode = postcode; }

    public static boolean isEqual(Address a1, Address a2) {
        if ( a1.getNumber() == a2.getNumber() &&
             a1.getPostcode() == a2.getPostcode() &&
             a1.getStreet().equals(a2.getStreet()) &&
             a1.getSuburb().equals(a2.getSuburb())) {
            return true;
        } else {
            return false;
        }
    }
}
