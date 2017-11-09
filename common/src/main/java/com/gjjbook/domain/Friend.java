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
    /**
     * 0 - Pending<br>
     * 1 - Accepted<br>
     * 2 - Declined<br>
     * 3 - Blocked
     */
    private int status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_account_id")
    @JsonManagedReference
    private Account actionAccount;

    public Friend() {
    }

    public Friend(Account accountOne, Account accountTwo, int status, Account actionAccount) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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

        if (status != friend.status) return false;
        if (!accountOne.equals(friend.accountOne)) return false;
        if (accountTwo != null ? !accountTwo.equals(friend.accountTwo) : friend.accountTwo != null) return false;
        return actionAccount != null ? actionAccount.equals(friend.actionAccount) : friend.actionAccount == null;
    }

    @Override
    public int hashCode() {
        int result = accountOne.hashCode();
        result = 31 * result + (accountTwo != null ? accountTwo.hashCode() : 0);
        result = 31 * result + status;
        result = 31 * result + (actionAccount != null ? actionAccount.hashCode() : 0);
        return result;
    }

    @Override
    public FriendPk getPK() {
        return new FriendPk(accountOne, accountTwo);
    }
}
