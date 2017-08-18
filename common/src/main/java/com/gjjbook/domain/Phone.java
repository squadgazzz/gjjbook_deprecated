package com.gjjbook.domain;

public class Phone implements Identified<Integer> {
    private int id;
    private int ownerId;
    private PhoneType type;
    private String number;

    public Phone() {
    }

    public Phone(PhoneType type, String number) {
        this.type = type;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
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
}
