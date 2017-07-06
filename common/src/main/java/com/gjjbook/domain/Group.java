package com.gjjbook.domain;

import java.util.List;

public class Group implements Identified<Integer> {
    private int id;
    private String name;
    private String description;
    private List<Account> participants;

    public Group() {
    }

    public Group(String name, String description, List<Account> participants) {
        this.name = name;
        this.description = description;
        this.participants = participants;
    }

    public Integer getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Account> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Account> participants) {
        this.participants = participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        return name.equals(group.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public Integer getPK() {
        return getId();
    }
}
