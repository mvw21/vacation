package com.example.sportshop.service;

import com.example.sportshop.domain.model.service.VacationServiceModel;
import com.example.sportshop.domain.model.view.VacationViewModel;

import java.util.List;

public interface VacationService {
    List<VacationServiceModel> getAllVacations();

    VacationViewModel findById(String id);

    VacationServiceModel getByUsername(String username);

    void addVacation(VacationServiceModel vacationServiceModel);

    void delete(String id);

    VacationServiceModel editVacation(VacationServiceModel vacationServiceModel);
}
