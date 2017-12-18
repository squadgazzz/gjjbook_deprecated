package com.gjjbook.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "friends")
@IdClass(FriendPk.class)
public class Friend implements Identified<FriendPk> {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_one_id")
    @JsonManagedReference
    private Account accountOne;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_two_id")
    @JsonManagedReference
    private Account accountTwo;
    @Enumerated(EnumType.STRING)
    private FriendShipStatus status; // done: 11.11.2017 сделать энум для статуса
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_account_id")
    @JsonManagedReference
    private Account actionAccount;

    public Friend() {
    }

    public Friend(Account accountOne, Account accountTwo, FriendShipStatus status, Account actionAccount) {
        this.accountOne = accountOne;
        this.accountTwo = accountTwo;
        this.status = status;
        this.actionAccount = actionAccount;
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

    public FriendShipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendShipStatus status) {
        this.status = status;
    }

    public Account getActionAccount() {
        return actionAccount;
    }

    public void setActionAccount(Account actionAccount) {
        this.actionAccount = actionAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Friend friend = (Friend) o;

        if (!accountOne.equals(friend.accountOne)) return false;
        if (!accountTwo.equals(friend.accountTwo)) return false;
        if (status != friend.status) return false;
        return actionAccount.equals(friend.actionAccount);
    }

    @Override
    public int hashCode() {
        int result = accountOne.hashCode();
        result = 31 * result + accountTwo.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + actionAccount.hashCode();
        return result;
    }

    @Override
    public FriendPk getPK() {
        return new FriendPk(accountOne, accountTwo);
    }
}
