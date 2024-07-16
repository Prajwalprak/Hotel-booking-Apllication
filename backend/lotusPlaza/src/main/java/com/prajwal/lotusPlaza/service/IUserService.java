package com.prajwal.lotusPlaza.service;

import java.util.List;

import com.prajwal.lotusPlaza.model.User;

public interface IUserService {
	User registerUser(User user);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);
}
