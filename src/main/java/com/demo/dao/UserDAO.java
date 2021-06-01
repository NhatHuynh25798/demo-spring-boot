package com.demo.dao;

import com.demo.dto.PagingDTO;
import com.demo.dto.UserDTO;
import com.demo.model.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository(value = "userDAO")
public class UserDAO extends AbstractDAO<User> {

    public boolean isEmailAddressUsed(String emailAddress) {
        return isStringUsed(User.class, "email", emailAddress);
    }

    @SuppressWarnings("unchecked")
    public PagingDTO<UserDTO> getUsersInPage(int page, int limit) {
        return new PagingDTO<>(new UserDTO().build((ArrayList<User>) getInPage(User.class, page, limit)), getRowCount(User.class), page, limit);
    }

    public User getUserByEmailAddress(String emailAddress) {
        try {
            DetachedCriteria c = DetachedCriteria.forClass(User.class).setProjection(Projections.rowCount())
                    .add(Restrictions.eq("email", emailAddress));
            return (User) findByCriteria(c).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
