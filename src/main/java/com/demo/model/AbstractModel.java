package com.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @JsonProperty(value = "name")
    @Column(name = "name")
    protected String name;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "created_at")
    protected Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "updated_at")
    protected Date updatedAt;

    @JsonProperty(value = "userId")
    @Column(name = "created_by")
    protected Integer createdBy;

    @JsonProperty(value = "userUpdateId")
    @Column(name = "updated_by")
    protected Integer updatedBy;

    @JsonProperty(value = "displayToUser")
    @Column(name = "display")
    protected Boolean displayToUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Boolean getDisplayToUser() {
        return displayToUser;
    }

    public void setDisplayToUser(Boolean displayToUser) {
        this.displayToUser = displayToUser;
    }
}
