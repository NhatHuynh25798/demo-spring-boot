package com.demo.service;

import com.demo.dao.UserDAO;
import com.demo.dto.PagingDTO;
import com.demo.dto.UserDTO;
import com.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserDTO getUserByEmail(String email) {
        return new UserDTO().build(userDAO.getUserByEmailAddress(email));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User res = userDAO.getUserByEmailAddress(email);
        return new UserDTO().build(res);
    }

    public PagingDTO<UserDTO> getUsersInPage(int page, int limit) {
        return userDAO.getUsersInPage(page, limit);
    }
}
