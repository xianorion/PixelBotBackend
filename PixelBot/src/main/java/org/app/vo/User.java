package org.app.vo;

import jakarta.persistence.*;

//By default, the name is the classname (similar cases i.e User)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column
    private String username;
    @Column
    private int age;
    @Column
    private String preferredVoice;
    @Column
    private String email;

    public User(){

    }
    public User(String username, int age, String preferredVoice, String email) {
        this.username = username;
        this.age = age;
        this.preferredVoice = preferredVoice;
        this.email = email;
    }

    public User(Integer id, String preferredVoice) {
        this.id = id;
        this.preferredVoice = preferredVoice;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPreferredVoice() {
        return preferredVoice;
    }

    public void setPreferredVoice(String preferredVoice) {
        this.preferredVoice = preferredVoice;
    }
}
