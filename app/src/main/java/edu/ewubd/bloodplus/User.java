package edu.ewubd.bloodplus;

public class User {
    private String name, email, phone, country, city, password, bloodType;

    public User(){

    }

    public User(String name, String email, String phone, String country, String city, String password, String bloodType) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.country = country;
        this.city = city;
        this.password = password;
        this.bloodType = bloodType;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getPassword() {
        return password;
    }

    public String getBloodType() {
        return bloodType;
    }
}
