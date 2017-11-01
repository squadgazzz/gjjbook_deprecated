package com.gjjbook.domain.DTO;

import com.gjjbook.domain.PhoneType;

import javax.persistence.*;

@Entity
@Table(name = "phones")
public class PhoneDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private PhoneType type;
    private String number;

    public PhoneDTO(PhoneType type, String number) {
        this.type = type;
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneDTO phoneDTO = (PhoneDTO) o;

        return number.equals(phoneDTO.number);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    public PhoneDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
