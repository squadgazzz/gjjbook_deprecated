package com.gjjbook.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@EqualsAndHashCode(of = "email")
@ToString
public class Account implements Identified<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;
    @Lob
    @Getter
    @Setter
    private byte[] avatar;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String middleName;
    @Getter
    @Setter
    private String surName;
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private Gender gender;
    @Getter
    @Setter
    private LocalDate birthDate;
    @JsonBackReference
    @OneToMany
            (
                    mappedBy = "owner",
                    cascade = CascadeType.ALL,
                    orphanRemoval = true
            )
    @Getter
    @Setter
    private List<Phone> phones;
    @Getter
    @Setter
    private String homeAddress;
    @Getter
    @Setter
    private String workAddress;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String icq;
    @Getter
    @Setter
    private String skype;
    @Getter
    @Setter
    private String additionalInfo;
    @Transient
    @Getter
    @Setter
    private List<Account> friendList;
    @Getter
    @Setter
    private String password;

    public void addPhone(Phone phone) {
        if (phone == null) {
            return;
        }

        if (this.phones == null) {
            this.phones = new ArrayList<>();
        }
        this.phones.add(phone);
        if (phone.getOwner() != this) {
            phone.setOwner(this);
        }
    }

    public void removePhone(Phone phone) {
        if (phone == null) {
            return;
        }

        phones.remove(phone);
        phone.setOwner(null);
    }

    public Account(byte[] avatar, String name, String middleName, String surName, Gender gender, LocalDate birthDate, List<Phone> phones, String homeAddress, String workAddress, String email, String icq, String skype, String additionalInfo, List<Account> friendList, String password) {
        this.avatar = avatar;
        this.name = name;
        this.middleName = middleName;
        this.surName = surName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phones = phones;
        this.homeAddress = homeAddress;
        this.workAddress = workAddress;
        this.email = email;
        this.icq = icq;
        this.skype = skype;
        this.additionalInfo = additionalInfo;
        this.friendList = friendList;
        this.password = password;
    }

    public String getStringAvatar() {
        if (avatar == null) {
            return null;
        }

        return Base64.getEncoder().encodeToString(avatar);
    }

    @Override
    public Integer getPK() {
        return getId();
    }
}
