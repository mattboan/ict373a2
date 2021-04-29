/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict373_a2_33276203;

import java.io.Serializable;
import java.util.ArrayList;

public class Person implements Serializable {
    //Constants
    public static String MALE = "Male";
    public static String FEMALE = "Female";
    public static int MAX_PARENTS = 2;

    private String fname = "";
    private String lname = "";
    private String lnameAfter = "";
    private String gender = "";
    private String bio = "";
    private Address address;
    private Person[] parents = new Person[MAX_PARENTS];
    private ArrayList<Person> children = new ArrayList<Person>();
    private Person spouse;


    //Getters
    public String getFname() { return fname; }
    public String getLname() { return lname; }
    public String getLnameAfter() { return lnameAfter; }
    public String getGender() { return gender; }
    public Person getParent(int index) { return parents[index]; }
    public String getBio() { return bio; }
    public Person[] getParents() { return parents; }
    public Person getChild(int index) { return children.get(index); }
    public ArrayList<Person> getChildren() { return children; }
    public Person getSpouse() { return spouse; }
    public Address getAddress() { return address; }

    //Setters
    public void setFname(String fname) { this.fname = fname; }
    public void setLname(String lname) { this.lname = lname; }
    public void setLnameAfter(String lnameAfter) { this.lnameAfter = lnameAfter; }
    public void setGender(String gender) { this.gender = gender; }
    public void addChild(Person child) { children.add(child); }
    public void addSpouse(Person spouse) { this.spouse = spouse; }
    public void setBio(String bio) { this.bio = bio; }
    public void setAddress(Address address) { this.address = address; }
    public void setAddress(int number, String street, String suburb, int postcode) {
        address = new Address(number, street, suburb, postcode);
    }

    /**
     * This adds a parent into the array of parents, max number of parents is set with the static constant MAX_PARENTS.
     * This function also checks to see if that parent has already been added to the parents array, it will return false
     * if the parent already exists in the array.
     * @param parent - Person object to add as a parent
     * @return - Returns true if insertion is successful
     */
    public boolean addParent(Person parent) {
        for ( int i = 0; i < MAX_PARENTS; i++ ) {
            if (parents[i] == null) {
                parents[i] = parent;
                return true;
            }/* else {
                if (Person.isEqual(parents[i], parent)) {
                    return false;
                }
            }*/
        }
        return false;
    }

    /**
     * This tests the equality between two people.
     * @param p1 - Person 1
     * @param p2 - Person 2
     * @return - True if the two people are the same
     */
    public static boolean isEqual(Person p1, Person p2) {
        boolean flag = false;

        if ( p1.getFname().equals(p2.getFname()) &&
             p1.getLname().equals(p2.getLname()) &&
             p1.getGender().equals(p2.getGender()) &&
             p1.getSpouse() == p2.getSpouse()) {

            flag = true;

            for (int i = 0; i < p1.children.size(); i++) {
                if (p1.getChild(i) != p2.getChild(i)) {
                    flag = false;
                }
            }

            for ( int i = 0; i < MAX_PARENTS; i++ ) {
                if (p1.getParent(i) != p2.getParent(i)) {
                    flag = false;
                }
            }

            //Check addresses
            if (!Address.isEqual(p1.getAddress(), p2.getAddress())) {
                flag = false;
            }
        }

        return flag;
    }

    @Override
    public String toString() {
        return fname;
    }
}
