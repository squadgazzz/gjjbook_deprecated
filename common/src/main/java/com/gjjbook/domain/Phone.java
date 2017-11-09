package com.gjjbook.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "phones")
public class Phone implements Identified<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonManagedReference
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "Account_id")
    private Account owner;
    @Enumerated(EnumType.STRING)
    private PhoneType type;
    private String number;

    public Phone() {
    }

    public Phone(Account owner, PhoneType type, String number) {
        this.owner = owner;
        this.type = type;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account getOwner() {
        return this.owner;
    }

    public void setOwner(Account account) {
        this.owner = account;
        if (!account.getPhones().contains(this)) { // warning this may cause performance issues if you have a large data set since this operation is O(n)
            account.getPhones().add(this);
        }
    }

    public PhoneType getType() {
        return type;
    }


    public void setType(PhoneType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Phone phone = (Phone) o;

        return number.equals(phone.number);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    @Override
    public Integer getPK() {
        return getId();
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", owner=" + owner +
                ", type=" + type +
                ", number='" + number + '\'' +
                '}';
    }
}
