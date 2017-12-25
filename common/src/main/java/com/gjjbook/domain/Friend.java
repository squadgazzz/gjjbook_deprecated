package com.gjjbook.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "friends")
@IdClass(FriendPk.class)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Friend implements Identified<FriendPk> {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_one_id")
    @JsonManagedReference
    @Getter
    @Setter
    private Account accountOne;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_two_id")
    @JsonManagedReference
    @Getter
    @Setter
    private Account accountTwo;
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private FriendShipStatus status; // done: 11.11.2017 сделать энум для статуса
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_account_id")
    @JsonManagedReference
    @Getter
    @Setter
    private Account actionAccount;

    @Override
    public FriendPk getPK() {
        return new FriendPk(accountOne, accountTwo);
    }
}
