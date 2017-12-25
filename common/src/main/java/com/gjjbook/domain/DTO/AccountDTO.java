package com.gjjbook.domain.DTO;

import com.gjjbook.domain.Identified;
import lombok.*;

import javax.persistence.*;
import java.util.Base64;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class AccountDTO implements Identified<Integer> {

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

    public AccountDTO(int id, byte[] avatar, String name, String middleName, String surName) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.middleName = middleName;
        this.surName = surName;
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
