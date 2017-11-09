package com.gjjbook.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account implements Identified<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Lob
    private byte[] avatar;
    private String name;
    private String middleName;
    private String surName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate birthDate;
    @JsonBackReference
    @OneToMany
            (
                    mappedBy = "owner",
                    cascade = CascadeType.ALL,
                    orphanRemoval = true
            )
    private List<Phone> phones;
    private String homeAddress;
    private String workAddress;
    private String email;
    private String icq;
    private String skype;
    private String additionalInfo;
    @Transient
    private List<Account> friendList;
    private String password;

    public Account() {
    }

    public void addPhone(Phone phone) {
        if (phone == null) {
            return;
        }

        if (this.phones == null) {
            this.phones = new ArrayList<>();
        }
        this.phones.add(phone);
        if (phone.getOwner() != this) {
            phone.setOwner(this);
        }
    }

    public void removePhone(Phone phone) {
        if (phone == null) {
            return;
        }

        phones.remove(phone);
        phone.setOwner(null);
    }

    public Account(byte[] avatar, String name, String middleName, String surName, Gender gender, LocalDate birthDate, List<Phone> phones, String homeAddress, String workAddress, String email, String icq, String skype, String additionalInfo, List<Account> friendList, String password) {
        this.avatar = avatar;
        this.name = name;
        this.middleName = middleName;
        this.surName = surName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phones = phones;
        this.homeAddress = homeAddress;
        this.workAddress = workAddress;
        this.email = email;
        this.icq = icq;
        this.skype = skype;
        this.additionalInfo = additionalInfo;
        this.friendList = friendList;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcq() {
        return icq;
    }

    public void setIcq(String icq) {
        this.icq = icq;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public List<Account> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Account> friendList) {
        this.friendList = friendList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStringAvatar() {
        if (avatar == null) {
            return null;
        }

        return Base64.getEncoder().encodeToString(avatar);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (email == null && account.email == null) {
            return true;
        }

        return email.equals(account.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public Integer getPK() {
        return getId();
    }
}
