package com.demo.dto;

import com.demo.annotation.Email;
import com.demo.model.Role;
import com.demo.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

public class UserDTO extends AbstractDTO implements UserDetails {
    private String phone;
    private String avatar;
    private int gender;
    private Collection<? extends GrantedAuthority> authorities;

    @JsonIgnore
    private ArrayList<Role> roles;

    @Email
    @NotBlank(message = "Email không được bỏ trống")
    private String email;

    @JsonIgnore
    @NotBlank(message = "Password không được bỏ trống")
    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private String birthday;

    public UserDTO build(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setRoles(new ArrayList<>(user.getRoles()));
        if(user.getRoles() != null && user.getRoles().size() > 0) {
            userDTO.setRoles(new ArrayList<>(user.getRoles()));
            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
            userDTO.setAuthorities(authorities);
        }
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public ArrayList<UserDTO> build(ArrayList<User> users) {
        ArrayList<UserDTO> dtos = new ArrayList<>();
        users.forEach(user -> dtos.add(new UserDTO().build(user)));
        return dtos;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.password;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDTO user = (UserDTO) o;
        return Objects.equals(id, user.id);
    }
}
