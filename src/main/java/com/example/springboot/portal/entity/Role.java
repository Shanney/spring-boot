package com.example.springboot.portal.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "portal", name = "role")
public class Role {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    private String id;

    private String roleName;

    private String version;

    private String createUser;

    private String createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
