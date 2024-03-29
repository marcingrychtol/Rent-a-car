package com.rentacar.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;
import com.rentacar.model.entity.enums.CarCategory;
import com.rentacar.model.entity.enums.CarFuel;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private CarCategory carCategory = CarCategory.COMPANY;
    private String brand;
    private String model;
    private String registration;
    @Enumerated(EnumType.STRING)
    private CarFuel fuel;
    private Boolean cancelled = false;

    private LocalDate insuranceExpiration;
    private LocalDate inspectionExpiration;
    @Nullable
    private Long mileage;

    @Lob
    private byte[] image;
    private String imageName;
    private String fileType;

    @OneToMany(mappedBy = "car")
    private List<Trip> trips;

    public Car(String brand, String registration) {
        this.brand = brand;
        this.registration = registration;
    }
}
