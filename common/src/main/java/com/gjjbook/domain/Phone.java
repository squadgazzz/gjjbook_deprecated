package com.gjjbook.domain;

public class Phone implements Identified<Integer> {
    private int id;
    private int ownerId;
    private PhoneType type;
    private int countryCode;
    private long number;

    public Phone() {
    }

    public Phone(PhoneType type, int countryCode, long number) {
        this.type = type;
        this.countryCode = countryCode;
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

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Phone phone = (Phone) o;

        if (countryCode != phone.countryCode) return false;
        return number == phone.number;
    }

    @Override
    public int hashCode() {
        int result = countryCode;
        result = 31 * result + (int) (number ^ (number >>> 32));
        return result;
    }

    @Override
    public Integer getPK() {
        return getId();
    }
}
