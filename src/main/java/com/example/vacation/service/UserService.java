package com.example.vacation.service;

import com.example.vacation.domain.model.service.UserServiceModel;
import java.util.List;

public interface UserService {

    UserServiceModel register(UserServiceModel userServiceModel);

    UserServiceModel findByUsername(String username);

    boolean passwordsAreEqual(String password,String username);

    List<String> getAllUserNames(String name);

    UserServiceModel editUserProfile(UserServiceModel userServiceModel, String oldPassword);

    List<UserServiceModel> getAllUsers();

    List<String> getAllUsernamesStrings(List<UserServiceModel> users);

    UserServiceModel getById(String id);

}
