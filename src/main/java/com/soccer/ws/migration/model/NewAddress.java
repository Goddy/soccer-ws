package com.soccer.ws.migration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * User: Tom De Dobbeleer
 * Date: 1/17/14
 * Time: 9:47 PM
 * Remarks: none
 */

/**
 * @NamedQueries({
 * @NamedQuery(name = "findAddressById", query = "from address where id = :id")
 * })
 */
@Entity
@Table(name = "address")
public class NewAddress extends NewBaseClass {
    private int postalCode;
    private String address;
    private String city;
    private String googleLink;

    public NewAddress(int postalCode, String address, String city, String googleLink) {
        this.setPostalCode(postalCode);
    }

    public NewAddress() {
    }

    @NotNull
    @Column(name = "postal_code")
    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    @NotNull
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NotNull
    @Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "google_link")
    public String getGoogleLink() {
        return googleLink;
    }

    public void setGoogleLink(String googleLink) {
        this.googleLink = googleLink;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s %s", address, postalCode, city);
    }
}