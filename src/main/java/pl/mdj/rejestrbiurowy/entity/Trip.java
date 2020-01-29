package pl.mdj.rejestrbiurowy.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.mdj.rejestrbiurowy.entity.interfaces.MyEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Trip extends MyEntity {

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    private LocalDate date;
    private String description;
    private Long mileage;
/*
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Long getMileage() {
        return mileage;
    }

    public void setMileage(Long mileage) {
        this.mileage = mileage;
    }

    public void setDescription(String description, Long mileage) {
        this.description = description;
        this.mileage = mileage;
    }

    public Trip() {
    }

    public Trip(Long id, String description) {
        this.id = id;
        this.description = description;
    }*/
}


