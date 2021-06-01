package com.demo.dto;

import com.demo.model.AbstractModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public class AbstractDTO {
    protected int id;
    protected String name;
    protected int createdBy;
    protected int updatedBy;
    protected boolean display;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    protected Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    protected Date updatedAt;

    public void build(AbstractDTO abstractDTO, AbstractModel model) {
        abstractDTO.setId(model.getId());
        if(model.getName() != null) abstractDTO.setName(model.getName());
        if(model.getCreatedAt() != null) abstractDTO.setCreatedAt(model.getCreatedAt());
        if(model.getUpdatedAt() != null) abstractDTO.setUpdatedAt(model.getUpdatedAt());
        if(model.getCreatedBy() != null) abstractDTO.setCreatedBy(model.getCreatedBy());
        if(model.getUpdatedBy() != null)abstractDTO.setUpdatedBy(model.getUpdatedBy());
        if(model.getDisplayToUser() != null) abstractDTO.setDisplay(model.getDisplayToUser());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }
}
