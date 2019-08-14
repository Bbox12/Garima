package com.garima.garima.Model;

public class main {


    private String Name,Photo,Open_time,Closing_time,Minimum_orders,CURL,Address;
    private double Price,Discount,Final_Price;
    private int ID,NoofItems;

    public int getNoofItems(int position) {
        return NoofItems;
    }

    public void setNoofItems(int noofItems) {
        NoofItems = noofItems;
    }

    public int getID(int position) {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName(int position) {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoto(int position) {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getOpen_time(int position) {
        return Open_time;
    }

    public void setOpen_time(String open_time) {
        Open_time = open_time;
    }

    public String getClosing_time(int position) {
        return Closing_time;
    }

    public void setClosing_time(String closing_time) {
        Closing_time = closing_time;
    }

    public String getMinimum_orders(int position) {
        return Minimum_orders;
    }

    public void setMinimum_orders(String minimum_orders) {
        Minimum_orders = minimum_orders;
    }

    public String getCURL(int position) {
        return CURL;
    }

    public void setCURL(String CURL) {
        this.CURL = CURL;
    }

    public String getAddress(int position) {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public double getPrice(int position) {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getDiscount(int position) {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    public double getFinal_Price(int position) {
        return Final_Price;
    }

    public void setFinal_Price(double final_Price) {
        Final_Price = final_Price;
    }
}
