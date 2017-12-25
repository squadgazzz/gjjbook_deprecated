package com.gjjbook.domain;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class FriendPk implements Serializable {
    @Getter
    @Setter
    protected Account accountOne;
    @Getter
    @Setter
    protected Account accountTwo;

    public FriendPk(Account accountOne, Account accountTwo) {
        if (accountOne.getId() > accountTwo.getId()) {
            this.accountOne = accountTwo;
            this.accountTwo = accountOne;
        } else {
            this.accountOne = accountOne;
            this.accountTwo = accountTwo;
        }
    }
}
