package uk.gov.cshr.maintainvacancy.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.cshr.maintainvacancy.model.Vacancy;

@Repository
public interface VacancyRepository extends CrudRepository<Vacancy, Long> {

    default Optional<Vacancy> findById(Long id) {
        return Optional.ofNullable(this.findOne(id));
    }
}
