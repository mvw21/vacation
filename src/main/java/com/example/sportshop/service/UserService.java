package com.example.sportshop.service;

import com.example.sportshop.domain.model.service.UserServiceModel;
import java.util.List;

public interface UserService {

    UserServiceModel register(UserServiceModel userServiceModel);

    UserServiceModel findByUsername(String username);

    boolean passwordsAreEqual(String password,String username);

    List<String> getAllUserNames(String name);

    UserServiceModel editUserProfile(UserServiceModel userServiceModel, String oldPassword);

    List<UserServiceModel> getAllUsers();

}
