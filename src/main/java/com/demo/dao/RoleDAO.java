package com.demo.dao;

import com.demo.model.Role;
import org.springframework.stereotype.Repository;

@Repository(value = "roleDAO")
public class RoleDAO extends AbstractDAO<Role> {
}
