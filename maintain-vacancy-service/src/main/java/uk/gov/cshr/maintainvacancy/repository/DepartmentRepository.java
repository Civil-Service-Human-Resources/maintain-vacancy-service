package uk.gov.cshr.maintainvacancy.repository;

import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import uk.gov.cshr.maintainvacancy.model.Department;

@Repository
public interface DepartmentRepository extends PagingAndSortingRepository<Department, Long> {

    default Optional<Department> findById(Long id) {
        return Optional.ofNullable(this.findOne(id));
    }
}
