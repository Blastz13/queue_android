package com.example.shop.Addresses;

import android.widget.EditText;

public class AddressesModel {


    private Boolean is_selected_address;
    private String city;
    private String street;
    private String house;
    private String index;
    private String flat;
    private String note;
    private String name;
    private String phone;

    public AddressesModel(Boolean is_selected_address, String city, String street, String house, String index, String flat, String note, String name, String phone) {
        this.is_selected_address = is_selected_address;
        this.city = city;
        this.street = street;
        this.house = house;
        this.index = index;
        this.flat = flat;
        this.note = note;
        this.name = name;
        this.phone = phone;
    }

    public String getAddress(){
        return city + " " + street + " " + house + " " + flat;
    }

    public Boolean getIs_selected_address() {
        return is_selected_address;
    }

    public void setIs_selected_address(Boolean is_selected_address) {
        this.is_selected_address = is_selected_address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
