package com.example.vacation.service.implementations;

import com.example.vacation.domain.entity.User;
import com.example.vacation.domain.entity.Vacation;
import com.example.vacation.domain.model.service.UserServiceModel;
import com.example.vacation.error.Constants;
import com.example.vacation.error.UserAlreadyExistException;
import com.example.vacation.repository.UsersRepository;
import com.example.vacation.repository.VacationRepository;
import com.example.vacation.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final VacationRepository vacationRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UsersRepository usersRepository, VacationRepository vacationRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.vacationRepository = vacationRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserServiceModel register(UserServiceModel userServiceModel) {

        User user = this.usersRepository
                .findByUsername(userServiceModel.getUsername())
                .orElse(null);

        if (user != null) {
            throw new UserAlreadyExistException("User already exists");
        }
        user = modelMapper.map(userServiceModel, User.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(userServiceModel.getPassword()));

        user.setRole(userServiceModel.getRole());

        this.usersRepository.saveAndFlush(user);
        return this.modelMapper.map(user, UserServiceModel.class);

    }

    @Override
    public UserServiceModel findByUsername(String username) {
        return this.usersRepository.findByUsername(username)
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .orElse(null);
    }
    @Override
    public boolean passwordsAreEqual (String password,String username){
        User user = this.usersRepository.findByUsername(username).orElse(null);
        assert user != null;
        return bCryptPasswordEncoder.matches(password, user.getPassword());
    }

    @Override
    public List<String> getAllUserNames(String name) {
        return this.usersRepository
                .findAll()
                .stream()
                .map(User::getUsername)
                .filter(username -> !username.equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public UserServiceModel editUserProfile(UserServiceModel userServiceModel, String oldPassword) {
        User user = this.usersRepository.findByUsername(userServiceModel.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException(Constants.USER_ID_NOT_FOUND));

        if (!this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException(Constants.PASSWORD_IS_INCORRECT);
        }

        user.setPassword(!"".equals(userServiceModel.getPassword()) ?
                this.bCryptPasswordEncoder.encode(userServiceModel.getPassword()) :
                user.getPassword());
        user.setEmail(userServiceModel.getEmail());

//        Vacation vacation = new Vacation(userServiceModel.getStartDate(),userServiceModel.getEndDate(),userServiceModel.getUsername());
        Vacation vacation = new Vacation(userServiceModel.getStartDate(),userServiceModel.getEndDate(),userServiceModel.getUsername());
        user.setVacations(userServiceModel.getVacations());
//        user.setStartDate(userServiceModel.getStartDate());
//        user.setEndDate(userServiceModel.getEndDate());
        this.vacationRepository.saveAndFlush(vacation);
        return this.modelMapper.map(this.usersRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> getAllUsers() {
        return this.usersRepository.findAll()
                .stream()
                .map(e-> this.modelMapper.map(e,UserServiceModel.class))
                .collect(Collectors.toList());

    }

    @Override
    public List<String> getAllUsernamesStrings(List<UserServiceModel> users) {
        List<String> usernames = new ArrayList<>();
        for(UserServiceModel u : users){
            usernames.add(u.getUsername());
        }
        return usernames;
    }

    @Override
    public UserServiceModel getById(String id) {
        User user = this.usersRepository.findById(id).orElse(null);
        assert user != null;
        List<Vacation> vacationsOfUser = this.vacationRepository.findAllByUsername(user.getUsername());
        UserServiceModel userServiceModel = this.modelMapper.map(user,UserServiceModel.class);
        userServiceModel.setVacations(vacationsOfUser);
        return userServiceModel;
    }


}
