package com.gjjbook.domain.DTO;

import com.gjjbook.domain.Identified;

public class AccountDTO implements Identified<Integer> {
    private int id;
    private byte[] avatar;
    private String name;
    private String middleName;
    private String surName;

    public AccountDTO() {
    }

    public AccountDTO(int id, byte[] avatar, String name, String middleName, String surName) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.middleName = middleName;
        this.surName = surName;
    }

    public int getId() {
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


    @Override
    public Integer getPK() {
        return getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountDTO that = (AccountDTO) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
