package com.example.shesafe;

public class Contact_model {
    private int id;
    private String name;
//    private int phone;
    private String phone;

//    public Contact_model(int id, String name, int phone) {
    public Contact_model(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public Contact_model() {
    }

    @Override
    /*public String toString() {
        return "Contact_model{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone=" + phone +
                '}';
    }*/

    public String toString() {
        return " Name: " + name +
                " | Phone: " + phone;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public int getPhone() {
//        return phone;
//    }

    public String getPhone() {
        return phone;
    }

//    public void setPhone(int phone) {
//        this.phone = phone;
//    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
