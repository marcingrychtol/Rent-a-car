package pl.mdj.rejestrbiurowy.model.entity;

import lombok.*;
import org.springframework.lang.Nullable;
import pl.mdj.rejestrbiurowy.model.entity.enums.CarCategory;
import pl.mdj.rejestrbiurowy.model.entity.enums.CarFuel;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private CarCategory carCategory = CarCategory.COMPANY;
    private String name;
    private String registration;
    @Enumerated(EnumType.STRING)
    private CarFuel fuel;

    private LocalDate insuranceExpiration;
    private LocalDate inspectionExpiration;
    @Nullable
    private Long mileage;

    public Car(String name, String registration) {
        this.name = name;
        this.registration = registration;
    }
}
