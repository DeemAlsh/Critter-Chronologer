package com.udacity.jdnd.course3.critter.controller;
import com.udacity.jdnd.course3.critter.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PetService petService;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        List<Employee> employees = employeeService.findByIdIn(scheduleDTO.getEmployeeIds());
        List<Pet> pets = petService.findAllById(scheduleDTO.getPetIds());

        Schedule schedule = new Schedule();

        BeanUtils.copyProperties(scheduleDTO, schedule);
        schedule.setEmployees(employees);
        schedule.setPets(pets);

        Schedule savedSchedule = scheduleService.save(schedule);

        employees.stream().forEach(employee -> {
            if(employee.getSchedules() == null)
                employee.setSchedules(new ArrayList<>());

            employee.getSchedules().add(savedSchedule);
        });

        pets.stream().forEach(pet -> {
            if(pet.getSchedules() == null)
                pet.setSchedules(new ArrayList<>());

            pet.getSchedules().add(savedSchedule);
        });


        return scheduleDTO;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.findAll();

        return convertToDto(schedules);

    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {

        List<Schedule> schedules = petService.findById(petId).getSchedules();
        return convertToDto(schedules);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.findById(employeeId);
        if(employee.getSchedules() == null)
            return null;

        List<Schedule> schedules = employee.getSchedules();
        return convertToDto(schedules);
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {

        List<Pet> pets =customerService.findById(customerId).getPets();
        HashMap<Long, Schedule> map = new HashMap<>();

        pets.stream().forEach(pet -> {
            pet.getSchedules().stream().forEach(schedule -> {
                map.put(schedule.getId(), schedule);
            });
        });
        return convertToDto(new ArrayList<>(map.values()));
    }

    private List<ScheduleDTO> convertToDto(List<Schedule> schedules){
        return schedules.stream().map(schedule -> {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            BeanUtils.copyProperties(schedule, scheduleDTO);

            scheduleDTO.setEmployeeIds(schedule.getEmployees().stream().map(employee -> {return employee.getId();}).collect(Collectors.toList()));
            scheduleDTO.setPetIds(schedule.getPets().stream().map(pet -> {return pet.getId();}).collect(Collectors.toList()));

            return scheduleDTO;

        }).collect(Collectors.toList());
    }
}
