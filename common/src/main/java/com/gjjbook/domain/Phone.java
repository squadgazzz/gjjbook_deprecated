package com.gjjbook.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "phones")
@NoArgsConstructor
@EqualsAndHashCode(of = "number")
@ToString
public class Phone implements Identified<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;
    @JsonManagedReference
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "Account_id")
    @Getter
    private Account owner;
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private PhoneType type;
    @Getter
    @Setter
    private String number;

    public Phone(Account owner, PhoneType type, String number) {
        this.owner = owner;
        this.type = type;
        this.number = number;
    }

    public void setOwner(Account account) {
        this.owner = account;
        if (!account.getPhones().contains(this)) { // warning this may cause performance issues if you have a large data set since this operation is O(n)
            account.getPhones().add(this);
        }
    }

    @Override
    public Integer getPK() {
        return getId();
    }
}
