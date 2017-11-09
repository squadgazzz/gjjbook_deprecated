package com.gjjbook.domain;

import java.io.Serializable;

public class FriendPk implements Serializable {
    protected Account accountOne;
    protected Account accountTwo;

    public FriendPk() {
    }

    public FriendPk(Account accountOne, Account accountTwo) {
        if (accountOne.getId() > accountTwo.getId()) {
            this.accountOne = accountTwo;
            this.accountTwo = accountOne;
        } else {
            this.accountOne = accountOne;
            this.accountTwo = accountTwo;
        }
    }

    public Account getAccountOne() {
        return accountOne;
    }

    public void setAccountOne(Account accountOne) {
        this.accountOne = accountOne;
    }

    public Account getAccountTwo() {
        return accountTwo;
    }

    public void setAccountTwo(Account accountTwo) {
        this.accountTwo = accountTwo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendPk friendPk = (FriendPk) o;

        if (!accountOne.equals(friendPk.accountOne)) return false;
        return accountTwo.equals(friendPk.accountTwo);
    }

    @Override
    public int hashCode() {
        int result = accountOne.hashCode();
        result = 31 * result + accountTwo.hashCode();
        return result;
    }
}
