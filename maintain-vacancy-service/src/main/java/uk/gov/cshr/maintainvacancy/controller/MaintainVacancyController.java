package uk.gov.cshr.maintainvacancy.controller;

import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.gov.cshr.maintainvacancy.model.Vacancy;
import uk.gov.cshr.maintainvacancy.repository.VacancyRepository;

@RestController
@RequestMapping(value = "/vacancy", produces = MediaType.APPLICATION_JSON_VALUE)
public class MaintainVacancyController {

	private static final Logger log = LoggerFactory.getLogger(MaintainVacancyController.class);

    private final VacancyRepository vacancyRepository;

    @Autowired
    MaintainVacancyController(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Find all vacancies", nickname = "findAll")
    public ResponseEntity<Page<Vacancy>> findAll(Pageable pageable) {
        Page<Vacancy> vacancies = vacancyRepository.findAll(pageable);
        return ResponseEntity.ok().body(vacancies);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{vacancyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find a specific vacancy", nickname = "findById")
    public ResponseEntity<Vacancy> findById(@PathVariable Long vacancyId) {

        Optional<Vacancy> foundVacancy = vacancyRepository.findById(vacancyId);

        if (!foundVacancy.isPresent()) {
            log.debug("No vacancy found for id " + vacancyId);
		}

        ResponseEntity<Vacancy> notFound = ResponseEntity.notFound().build();

        return foundVacancy.map(v -> ResponseEntity.ok().body(v)).orElse(notFound);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Vacancy> create(@RequestBody Vacancy vacancy) {

        Vacancy savedVacancy = vacancyRepository.save(vacancy);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedVacancy.getId()).toUri();

        return ResponseEntity.created(location).body(savedVacancy);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{vacancyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vacancy> update(@PathVariable Long vacancyId, @RequestBody Vacancy vacancyUpdate) {

        Optional<Vacancy> foundVacancy = vacancyRepository.findById(vacancyId);

        if (!foundVacancy.isPresent()) {
            log.error("No vacancy found for id " + vacancyId);
		}

        ResponseEntity<Vacancy> notFound = ResponseEntity.notFound().build();

        return foundVacancy.map((Vacancy vacancy) -> {
            // Attention, mutable state on the argument
            vacancyUpdate.setId(vacancy.getId());
            vacancyRepository.save(vacancyUpdate);
            return ResponseEntity.ok().body(vacancy);
        }).orElse(notFound);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{vacancyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vacancy> deleteById(@PathVariable Long vacancyId) {

        vacancyRepository.delete(vacancyId);
        return ResponseEntity.noContent().build();
    }
}
