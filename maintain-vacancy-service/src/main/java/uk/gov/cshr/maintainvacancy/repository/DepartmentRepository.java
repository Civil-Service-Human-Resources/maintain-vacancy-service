package uk.gov.cshr.maintainvacancy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.cshr.maintainvacancy.model.Department;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Long> {

}
