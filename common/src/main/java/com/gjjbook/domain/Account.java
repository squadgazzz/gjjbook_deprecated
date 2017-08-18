package com.gjjbook.domain;

import java.time.LocalDate;
import java.util.List;

public class Account implements Identified<Integer> {
    private int id;
    private String name;
    private String middleName;
    private String surName;
    private Sex sex;
    private LocalDate birthDate;
    private List<Phone> phones;
    private String homeAddress;
    private String workAddress;
    private String email;
    private String icq;
    private String skype;
    private String additionalInfo;
    private List<Account> friendList;
    private List<Group> groupList;

    public Account() {
    }

    public Account(String name, String middleName, String surName, Sex sex, LocalDate birthDate, List<Phone> phones, String homeAddress, String workAddress, String email, String icq, String skype, String additionalInfo, List<Account> friendList, List<Group> groupList) {
        this.name = name;
        this.middleName = middleName;
        this.surName = surName;
        this.sex = sex;
        this.birthDate = birthDate;
        this.phones = phones;
        this.homeAddress = homeAddress;
        this.workAddress = workAddress;
        this.email = email;
        this.icq = icq;
        this.skype = skype;
        this.additionalInfo = additionalInfo;
        this.friendList = friendList;
        this.groupList = groupList;
    }

    public Integer getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
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

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
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

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
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
