package com.example.sportshop.service.implementations;

import com.example.sportshop.domain.entity.Vacation;
import com.example.sportshop.domain.model.service.VacationServiceModel;
import com.example.sportshop.domain.model.view.VacationViewModel;
import com.example.sportshop.repository.VacationRepository;
import com.example.sportshop.service.VacationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacationServiceImpl implements VacationService {
    private final VacationRepository vacationRepository;
    private final ModelMapper modelMapper;

    public VacationServiceImpl(VacationRepository vacationRepository, ModelMapper modelMapper) {
        this.vacationRepository = vacationRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<VacationServiceModel> getAllVacations() {
        return this.vacationRepository.findAll()
                .stream()
                .map(e-> this.modelMapper.map(e, VacationServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public VacationViewModel findById(String id) {
        return this.vacationRepository
                .findById(id)
                .map(vacation -> {
                    VacationViewModel vacationViewModel = this.modelMapper
                            .map(vacation, VacationViewModel.class);
                    return vacationViewModel;
                })
                .orElse(null);
    }

    @Override
    public VacationServiceModel getByUsername(String username) {
        return this.modelMapper
                .map(this.vacationRepository.findByUsername(username),VacationServiceModel.class);
    }

    @Override
    public void addVacation(VacationServiceModel vacationServiceModel) {
        Vacation vacation = this.modelMapper.map(vacationServiceModel, Vacation.class);
        this.vacationRepository.saveAndFlush(vacation);
    }

    @Override
    public void delete(String id) {
        this.vacationRepository.deleteById(id);
    }

    @Override
    public VacationServiceModel editVacation(VacationServiceModel vacationServiceModel) {

    Vacation vacation = this.vacationRepository.findByUsername(vacationServiceModel.getUsername());
        assert vacation != null;
        vacation.setStartDate(vacationServiceModel.getStartDate());
        vacation.setEndDate(vacationServiceModel.getEndDate());

    this.vacationRepository.save(vacation);

//        this.vacationRepository.saveAndFlush(vacation);
   return this.modelMapper.map(this.vacationRepository.findById(vacation.getId()),VacationServiceModel.class);
    }
}
