package uk.gov.cshr.maintainvacancy.controller;

import static java.lang.Math.toIntExact;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import org.assertj.core.api.Assertions;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import uk.gov.cshr.maintainvacancy.MaintainVacancyApplication;
import uk.gov.cshr.maintainvacancy.model.Department;
import uk.gov.cshr.maintainvacancy.model.Vacancy;
import uk.gov.cshr.maintainvacancy.repository.DepartmentRepository;
import uk.gov.cshr.maintainvacancy.repository.VacancyRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = MaintainVacancyApplication.class)
@ContextConfiguration
@WebAppConfiguration
public class VacancyControllerTest extends AbstractTestNGSpringContextTests {

    private static final SimpleDateFormat ISO_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final Timestamp THIRTY_DAYS_FROM_NOW = getTime(30);

    @Inject
    private WebApplicationContext webApplicationContext;

    @Inject
    private VacancyRepository vacancyRepository;

    @Inject
    private DepartmentRepository departmentRepository;

    private MockMvc mvc;

    final private MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private final Vacancy requestBodyVacancy = Vacancy.builder()
            .title("testTitle")
            .description("testDescription")
            .location("testLocation")
            .grade("testGrade")
            .role("testRole")
            .responsibilities("testResponsibilities")
            .workingHours("testWorkingHours")
            .closingDate(THIRTY_DAYS_FROM_NOW)
            .contactName("testContactName")
            .contactDepartment("testContactDepartment")
            .contactEmail("testContactEmail")
            .contactTelephone("testContactTelephone")
            .eligibility("testEligibility")
            .salaryMin(10)
            .salaryMax(100)
            .numberVacancies(1)
            .build();

    private final String requestBody = "{"
            +            "\"title\":\"" + requestBodyVacancy.getTitle() + "\"," +
            "\"description\":\"" + requestBodyVacancy.getDescription() + "\"," +
            "\"location\":\"" + requestBodyVacancy.getLocation() + "\"," +
            "\"grade\":\"" + requestBodyVacancy.getGrade() + "\"," +
            "\"role\":\"" + requestBodyVacancy.getRole() + "\"," +
            "\"responsibilities\":\"" + requestBodyVacancy.getResponsibilities() + "\"," +
            "\"workingHours\":\"" + requestBodyVacancy.getWorkingHours() + "\"," +
            "\"closingDate\":\"" + ISO_DATEFORMAT.format(requestBodyVacancy.getClosingDate()) + "\"," +
            "\"contactName\":\"" + requestBodyVacancy.getContactName() + "\"," +
            "\"contactDepartment\":\"" + requestBodyVacancy.getContactDepartment() + "\"," +
            "\"contactEmail\":\"" + requestBodyVacancy.getContactEmail() + "\"," +
            "\"contactTelephone\":\"" + requestBodyVacancy.getContactTelephone() + "\"," +
            "\"eligibility\":\"" + requestBodyVacancy.getEligibility() + "\"," +
            "\"salaryMin\":" + requestBodyVacancy.getSalaryMin() + "," +
            "\"salaryMax\":" + requestBodyVacancy.getSalaryMax() + "," +
            "\"numberVacancies\":" + requestBodyVacancy.getNumberVacancies() + "" +
            "}";

    private final Vacancy vacancy1 = Vacancy.builder()
            .id(1)
            .title("testTile1 SearchQueryTitle")
            .description("testDescription1 SearchQueryDescription")
            .location("testLocation1 SearchQueryLocation")
            .grade("testGrade1 SearchQueryGrade")
            .role("testRole1")
            .responsibilities("testResponsibilities1")
            .workingHours("testWorkingHours1")
            .closingDate(THIRTY_DAYS_FROM_NOW)
            .contactName("testContactName1")
            .contactDepartment("testContactDepartment1")
            .contactEmail("testContactEmail1")
            .contactTelephone("testContactTelephone1")
            .eligibility("testEligibility1")
            .salaryMin(0)
            .salaryMax(10)
            .numberVacancies(1)
            .build();

    private final Vacancy vacancy2 = Vacancy.builder()
            .id(2)
            .title("testTitle2")
            .description("testDescription2")
            .location("testLocation2")
            .grade("testGrade2")
            .role("testRole2")
            .responsibilities("testResponsibilities2")
            .workingHours("testWorkingHours2")
            .closingDate(THIRTY_DAYS_FROM_NOW)
            .contactName("testContactName2")
            .contactDepartment("testContactDepartment2")
            .contactEmail("testContactEmail2")
            .contactTelephone("testContactTelephone2")
            .eligibility("testEligibility2")
            .salaryMin(0)
            .salaryMax(10)
            .numberVacancies(2)
            .build();

    private final Vacancy vacancy3 = Vacancy.builder()
            .id(3)
            .title("testTitle3")
            .description("testDescription3")
            .location("testLocation3")
            .grade("testGrade3")
            .role("testRole3")
            .responsibilities("testResponsibilities3")
            .workingHours("testWorkingHours2")
            .closingDate(THIRTY_DAYS_FROM_NOW)
            .contactName("testContactName3")
            .contactDepartment("testContactDepartment3")
            .contactEmail("testContactEmail3")
            .contactTelephone("testContactTelephone3")
            .eligibility("testEligibility3")
            .salaryMin(0)
            .salaryMax(10)
            .numberVacancies(2)
            .build();

    @BeforeMethod
    void setup() {

        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Department department1 = departmentRepository.save(Department.builder().id(1L).name("Department One").build());
        Department department2 = departmentRepository.save(Department.builder().id(2L).name("Department Two").build());
        Department department3 = departmentRepository.save(Department.builder().id(3L).name("Department Three").build());

        vacancy1.setDepartment(department1);
        Vacancy savedVacancy1 = this.vacancyRepository.save(vacancy1);
        vacancy1.setId(savedVacancy1.getId());

        vacancy2.setDepartment(department2);
        Vacancy savedVacancy2 = this.vacancyRepository.save(vacancy2);
        vacancy2.setId(savedVacancy2.getId());

        vacancy3.setDepartment(department3);
        Vacancy savedVacancy3 = this.vacancyRepository.save(vacancy3);
        vacancy3.setId(savedVacancy3.getId());
    }

    @AfterMethod
    void tearDown() {
        this.vacancyRepository.deleteAll();
    }

    @Test
    public void testFindAll() throws Exception {

        // Given
        String path = "/vacancy";

        // When
        ResultActions sendRequest = mvc.perform(get(path));

        // Then
        sendRequest
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.totalElements", is(3)))
                .andExpect(jsonPath("$.content[0].id", is(toIntExact(this.vacancy1.getId()))))
                .andExpect(jsonPath("$.content[0].description", is(this.vacancy1.getDescription())))
                .andExpect(jsonPath("$.content[0].location", is(this.vacancy1.getLocation())))
                .andExpect(jsonPath("$.content[0].grade", is(this.vacancy1.getGrade())))
                .andExpect(jsonPath("$.content[0].role", is(this.vacancy1.getRole())))
                .andExpect(jsonPath("$.content[0].responsibilities", is(this.vacancy1.getResponsibilities())))
                .andExpect(jsonPath("$.content[0].workingHours", is(this.vacancy1.getWorkingHours())))
                .andExpect(jsonPath("$.content[0].closingDate", is(ISO_DATEFORMAT.format(requestBodyVacancy.getClosingDate()))))
                .andExpect(jsonPath("$.content[0].contactName", is(this.vacancy1.getContactName())))
                .andExpect(jsonPath("$.content[0].contactDepartment", is(this.vacancy1.getContactDepartment())))
                .andExpect(jsonPath("$.content[0].contactEmail", is(this.vacancy1.getContactEmail())))
                .andExpect(jsonPath("$.content[0].contactTelephone", is(this.vacancy1.getContactTelephone())))
                .andExpect(jsonPath("$.content[0].eligibility", is(this.vacancy1.getEligibility())))
                .andExpect(jsonPath("$.content[0].salaryMin", is(this.vacancy1.getSalaryMin())))
                .andExpect(jsonPath("$.content[0].salaryMax", is(this.vacancy1.getSalaryMax())))
                .andExpect(jsonPath("$.content[0].numberVacancies", is(this.vacancy1.getNumberVacancies())))
                .andExpect(jsonPath("$.content[1].id", is(toIntExact(this.vacancy2.getId()))))
                .andExpect(jsonPath("$.content[1].description", is(this.vacancy2.getDescription())))
                .andExpect(jsonPath("$.content[1].location", is(this.vacancy2.getLocation())))
                .andExpect(jsonPath("$.content[1].grade", is(this.vacancy2.getGrade())))
                .andExpect(jsonPath("$.content[1].role", is(this.vacancy2.getRole())))
                .andExpect(jsonPath("$.content[1].responsibilities", is(this.vacancy2.getResponsibilities())))
                .andExpect(jsonPath("$.content[1].workingHours", is(this.vacancy2.getWorkingHours())))
                .andExpect(jsonPath("$.content[1].closingDate", is(ISO_DATEFORMAT.format(requestBodyVacancy.getClosingDate()))))
                .andExpect(jsonPath("$.content[1].contactName", is(this.vacancy2.getContactName())))
                .andExpect(jsonPath("$.content[1].contactDepartment", is(this.vacancy2.getContactDepartment())))
                .andExpect(jsonPath("$.content[1].contactEmail", is(this.vacancy2.getContactEmail())))
                .andExpect(jsonPath("$.content[1].contactTelephone", is(this.vacancy2.getContactTelephone())))
                .andExpect(jsonPath("$.content[1].eligibility", is(this.vacancy2.getEligibility())))
                .andExpect(jsonPath("$.content[1].salaryMin", is(this.vacancy2.getSalaryMin())))
                .andExpect(jsonPath("$.content[1].salaryMax", is(this.vacancy2.getSalaryMax())))
                .andExpect(jsonPath("$.content[1].numberVacancies", is(this.vacancy2.getNumberVacancies())))
                .andExpect(jsonPath("$.content[2].id", is(toIntExact(this.vacancy3.getId()))))
                .andExpect(jsonPath("$.content[2].description", is(this.vacancy3.getDescription())))
                .andExpect(jsonPath("$.content[2].location", is(this.vacancy3.getLocation())))
                .andExpect(jsonPath("$.content[2].grade", is(this.vacancy3.getGrade())))
                .andExpect(jsonPath("$.content[2].role", is(this.vacancy3.getRole())))
                .andExpect(jsonPath("$.content[2].responsibilities", is(this.vacancy3.getResponsibilities())))
                .andExpect(jsonPath("$.content[2].workingHours", is(this.vacancy3.getWorkingHours())))
                .andExpect(jsonPath("$.content[2].closingDate", is(ISO_DATEFORMAT.format(requestBodyVacancy.getClosingDate()))))
                .andExpect(jsonPath("$.content[2].contactName", is(this.vacancy3.getContactName())))
                .andExpect(jsonPath("$.content[2].contactDepartment", is(this.vacancy3.getContactDepartment())))
                .andExpect(jsonPath("$.content[2].contactEmail", is(this.vacancy3.getContactEmail())))
                .andExpect(jsonPath("$.content[2].contactTelephone", is(this.vacancy3.getContactTelephone())))
                .andExpect(jsonPath("$.content[2].eligibility", is(this.vacancy3.getEligibility())))
                .andExpect(jsonPath("$.content[2].salaryMin", is(this.vacancy3.getSalaryMin())))
                .andExpect(jsonPath("$.content[2].salaryMax", is(this.vacancy3.getSalaryMax())))
                .andExpect(jsonPath("$.content[2].numberVacancies", is(this.vacancy3.getNumberVacancies())));
    }

    @Test
    public void testFindById() throws Exception {
        // Given
        String path = "/vacancy/" + vacancy1.getId();

        // When
        ResultActions sendRequest = mvc.perform(get(path));

        // Then
        sendRequest
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.description", is(this.vacancy1.getDescription())))
                .andExpect(jsonPath("$.location", is(this.vacancy1.getLocation())))
                .andExpect(jsonPath("$.grade", is(this.vacancy1.getGrade())))
                .andExpect(jsonPath("$.role", is(this.vacancy1.getRole())))
                .andExpect(jsonPath("$.responsibilities", is(this.vacancy1.getResponsibilities())))
                .andExpect(jsonPath("$.workingHours", is(this.vacancy1.getWorkingHours())))
                .andExpect(jsonPath("$.closingDate", is(ISO_DATEFORMAT.format(requestBodyVacancy.getClosingDate()))))
                .andExpect(jsonPath("$.contactName", is(this.vacancy1.getContactName())))
                .andExpect(jsonPath("$.contactDepartment", is(this.vacancy1.getContactDepartment())))
                .andExpect(jsonPath("$.contactEmail", is(this.vacancy1.getContactEmail())))
                .andExpect(jsonPath("$.contactTelephone", is(this.vacancy1.getContactTelephone())))
                .andExpect(jsonPath("$.eligibility", is(this.vacancy1.getEligibility())))
                .andExpect(jsonPath("$.salaryMin", is(this.vacancy1.getSalaryMin())))
                .andExpect(jsonPath("$.salaryMax", is(this.vacancy1.getSalaryMax())))
                .andExpect(jsonPath("$.numberVacancies", is(this.vacancy1.getNumberVacancies())));
    }

    @Test
    public void testFindByIdNotFound() throws Exception {
        // Given
        String path = "/vacancy/-1";

        // When
        ResultActions sendRequest = mvc.perform(get(path));

        // Then
        sendRequest.andExpect(status().isNotFound());

    }

    @Test
    public void testCreate() throws Exception {
        // Given
        String path = "/vacancy";

        // When
        ResultActions sendRequest = mvc.perform(post(path).contentType(APPLICATION_JSON_UTF8).content(requestBody));

        MvcResult sendRequestResult = sendRequest.andReturn();

        String returnedLocation = sendRequestResult.getResponse().getRedirectedUrl();

        // Then
        sendRequest
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8));

        Long createdVacancyId = getResourceIdFromUrl(returnedLocation);

        Vacancy storedVacancy = vacancyRepository.findOne(createdVacancyId);

        Assertions.assertThat(storedVacancy).isEqualToIgnoringGivenFields(requestBodyVacancy, "id");
    }


    @Test
    public void testUpdate() throws Exception {
        // Given
        String path = "/vacancy/" + vacancy1.getId();

        // When
        ResultActions sendRequest = mvc.perform(put(path).contentType(APPLICATION_JSON_UTF8).content(requestBody));

        // Then
        sendRequest
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(toIntExact(this.vacancy1.getId()))))
                .andExpect(jsonPath("$.description", is(this.requestBodyVacancy.getDescription())))
                .andExpect(jsonPath("$.location", is(this.requestBodyVacancy.getLocation())))
                .andExpect(jsonPath("$.grade", is(this.requestBodyVacancy.getGrade())))
                .andExpect(jsonPath("$.role", is(this.requestBodyVacancy.getRole())))
                .andExpect(jsonPath("$.responsibilities", is(this.requestBodyVacancy.getResponsibilities())))
                .andExpect(jsonPath("$.workingHours", is(this.requestBodyVacancy.getWorkingHours())))
                .andExpect(jsonPath("$.closingDate", is(ISO_DATEFORMAT.format(requestBodyVacancy.getClosingDate()))))
                .andExpect(jsonPath("$.contactName", is(this.requestBodyVacancy.getContactName())))
                .andExpect(jsonPath("$.contactDepartment", is(this.requestBodyVacancy.getContactDepartment())))
                .andExpect(jsonPath("$.contactEmail", is(this.requestBodyVacancy.getContactEmail())))
                .andExpect(jsonPath("$.contactTelephone", is(this.requestBodyVacancy.getContactTelephone())))
                .andExpect(jsonPath("$.eligibility", is(this.requestBodyVacancy.getEligibility())))
                .andExpect(jsonPath("$.salaryMin", is(this.requestBodyVacancy.getSalaryMin())))
                .andExpect(jsonPath("$.salaryMax", is(this.requestBodyVacancy.getSalaryMax())))
                .andExpect(jsonPath("$.numberVacancies", is(this.requestBodyVacancy.getNumberVacancies())));
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        // Given
        String path = "/vacancy/-1";

        // When
        ResultActions sendRequest = mvc.perform(put(path).contentType(APPLICATION_JSON_UTF8).content(requestBody));

        // Then
        sendRequest.andExpect(status().isNotFound());

    }

    private long getResourceIdFromUrl(String locationUrl) {
        String[] parts = locationUrl.split("/");
        return Long.valueOf(parts[parts.length - 1]);
    }

    @Test
    public void testDelete() throws Exception {
        // Given
        String path = "/vacancy/" + vacancy1.getId();
        // When
        ResultActions sendRequest = mvc.perform(delete(path));

        Iterable<Vacancy> vacancies = vacancyRepository.findAll();

        // Then
        sendRequest.andExpect(status().isNoContent());
        Assertions.assertThat(vacancies).hasSize(2);
        Vacancy vacancy = vacancies.iterator().next();
        Set<Long> validIds = new HashSet<>();
        validIds.add(vacancy2.getId());
        validIds.add(vacancy3.getId());
        Assertions.assertThat(validIds.contains(vacancy.getId()));
    }

    @Test
    public void testFindAllPaginated() throws Exception {
        // Given
        String path = "/vacancy/?page=0&size=1";

        // When
        ResultActions sendRequest = mvc.perform(get(path));

        // Then
        sendRequest
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements", is(3)))
                .andExpect(jsonPath("$.totalPages", is(3)))
                .andExpect(jsonPath("$.size", is(1)))
                .andExpect(jsonPath("$.numberOfElements", is(1)));
    }

    private static Timestamp getTime(int numberOfDaysFromNow) {
        Date date = Date.from(LocalDateTime.now().plusDays(numberOfDaysFromNow).atZone(ZoneId.systemDefault()).toInstant());
        return new Timestamp(date.getTime());
    }
}
