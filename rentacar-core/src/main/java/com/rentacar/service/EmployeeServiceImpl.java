package com.rentacar.service;

import com.rentacar.exceptions.EntityConflictException;
import com.rentacar.model.dto.EmployeeDto;
import com.rentacar.model.entity.Employee;
import com.rentacar.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rentacar.exceptions.CannotFindEntityException;
import com.rentacar.exceptions.WrongInputDataException;
import com.rentacar.model.mappers.EmployeeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    EmployeeRepository employeeRepository;
    EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public List<EmployeeDto> findAll() {
        return employeeMapper.mapToDto(employeeRepository.findAllByOrderBySecondName());
    }

    @Override
    public List<EmployeeDto> findAllActive() {
        List<EmployeeDto> dtoList = findAll();
        List<EmployeeDto> dtoListActive = new ArrayList<>();
        for (EmployeeDto emp :
                dtoList) {
            if (!emp.isCancelled()) {
                dtoListActive.add(emp);
            }
        }
        return dtoListActive;
//        return getAll().stream()
//                .filter(emp -> !emp.getCancelled())
//                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto findById(Long id) throws CannotFindEntityException {
        Optional<Employee> optional = employeeRepository.findById(id);
        if (optional.isPresent()) {
            return employeeMapper.mapToDto(optional.get());
        } else {
            throw new CannotFindEntityException("Cannot find employee of id: " + id);
        }
    }

    @Override
    public Long addOne(EmployeeDto employeeDto) throws WrongInputDataException, EntityConflictException {
        checkInputLengthData(employeeDto); // throws WIDE
        checkDuplicates(employeeDto); // throws ECE
        Employee employee = employeeMapper.mapToEntity(employeeDto);
        employeeRepository.save(employee);
        return employee.getId();
    }

    @Override
    public void cancelByDto(EmployeeDto employeeDto) throws CannotFindEntityException {
        Optional<Employee> empOptional = employeeRepository.findById(employeeDto.getId());
        if (!empOptional.isPresent()){
            throw new CannotFindEntityException("Pracownik nie istnieje, wystąpił błąd! (jednoczesna edycja z innego stanowiska)");
        }
        Employee employee = empOptional.get();
        employee.setCancelled(true);
        employeeRepository.save(employee);
    }

    @Override
    public void deleteByDto(EmployeeDto employeeDto) throws WrongInputDataException, CannotFindEntityException, DataIntegrityViolationException {
        Optional<Employee> empOptional = employeeRepository.findById(employeeDto.getId());
        if (!empOptional.isPresent()){
            throw new CannotFindEntityException("Pracownik nie istnieje, wystąpił błąd! (jednoczesna edycja z innego stanowiska)");
        }
        if (!empOptional.get().getEmail().equals(employeeDto.getEmail())) {
            throw new WrongInputDataException("Niepoprawne dane, nie można usunąć pracownika!");
        }
        employeeRepository.deleteById(employeeDto.getId());
    }

    @Override
    public void enableByDto(EmployeeDto employeeDto) throws CannotFindEntityException {
        Optional<Employee> empOptional = employeeRepository.findById(employeeDto.getId());
        if (!empOptional.isPresent()){
            throw new CannotFindEntityException("Pracownik nie istnieje, wystąpił błąd! (jednoczesna edycja z innego stanowiska)");
        }
        Employee employee = empOptional.get();
        employee.setCancelled(false);
        employeeRepository.save(employee);
    }

    @Override
    public void update(EmployeeDto employeeDto) throws EntityConflictException, WrongInputDataException, CannotFindEntityException {

        checkInputLengthData(employeeDto);
        checkDuplicates(employeeDto);

        Optional<Employee> empOptional = employeeRepository.findById(employeeDto.getId());
        if (empOptional.isPresent()) {
            empOptional.get().setName(employeeDto.getName());
            empOptional.get().setSecondName(employeeDto.getSecondName());
            empOptional.get().setEmail(employeeDto.getEmail());
            empOptional.get().setPhoneNumber(employeeDto.getPhoneNumber());
            employeeRepository.save(empOptional.get());
        } else {
            throw new CannotFindEntityException(
                    "Nie można wprowadzić danych, pracownik nie istnieje lub jest nieaktywny. " +
                            "Prawdopodobnie ktoś zmienił dane w międzyczasie");
        }

    }


    private void checkInputLengthData(EmployeeDto employeeDto) throws WrongInputDataException {
        if (
                employeeDto.getEmail().length() < 5
                        || employeeDto.getName().length() < 2
                        || employeeDto.getSecondName().length() < 3
                        || employeeDto.getPhoneNumber().length() < 9
        ) {
            throw new WrongInputDataException("Weźże wprowadź dane dłuższe niż 3 znaki...");
        }
    }

    private void checkDuplicates(EmployeeDto employeeDto) throws EntityConflictException {
        Optional<Employee> empConflictTest = employeeRepository.findByEmailEquals(employeeDto.getEmail());
        if (empConflictTest.isPresent() && !empConflictTest.get().getId().equals(employeeDto.getId())) {
            throw new EntityConflictException(
                    "Pracownik o emailu "
                            + employeeDto.getEmail()
                            + " już istnieje! Jest to "
                            + empConflictTest.get().getName()
                            + " "
                            + empConflictTest.get().getSecondName()
            );
        }
    }

}
