package com.udacity.jdnd.course3.critter.entity;

import com.udacity.jdnd.course3.critter.dto.PetType;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    private PetType type;

    private LocalDate birthDate;

    @Column
    private String notes;

    @ManyToOne
    private Customer owner;

    @ManyToMany(mappedBy = "pets")
    private List<Schedule> schedules;

    public Pet() {
    }

}
