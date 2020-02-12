package pl.mdj.rejestrbiurowy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mdj.rejestrbiurowy.entity.CarCategory;

public interface CarCategoryRepository extends JpaRepository<CarCategory, Long> {
}