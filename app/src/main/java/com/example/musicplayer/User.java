package com.example.musicplayer;

public class User {
    private String name;
    private String password;
    private String email;
    public User(){

    }
    public User(String n,String p,String e){
        this.name=n;
        password=p;
        email=e;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
