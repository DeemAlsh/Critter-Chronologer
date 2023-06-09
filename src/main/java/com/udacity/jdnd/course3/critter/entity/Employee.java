package com.udacity.jdnd.course3.critter.entity;

import com.udacity.jdnd.course3.critter.dto.EmployeeSkill;
import com.udacity.jdnd.course3.critter.dto.EmployeeSkill;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    @Column
    private String name;

    @ElementCollection(targetClass = EmployeeSkill.class)
    @JoinTable(name = "employee_skills", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "skill", nullable = false)
    @Enumerated(EnumType.STRING)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<EmployeeSkill> skills;

    @ElementCollection
    @JoinTable(name = "employee_days", joinColumns = @JoinColumn(name = "employee_id"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<DayOfWeek> daysAvailable;

    @ManyToMany(mappedBy = "employees")
    private List<Schedule> schedules;


    public Employee() {
    }
}
