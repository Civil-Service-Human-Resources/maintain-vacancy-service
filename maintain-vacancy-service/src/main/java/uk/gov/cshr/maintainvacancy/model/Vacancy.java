package uk.gov.cshr.maintainvacancy.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vacancies")
@SequenceGenerator(name = "vacancies_sequence", sequenceName = "vacancies_sequence", allocationSize = 1)
public class Vacancy implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vacancies_sequence")
    private long id;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    private String location;

    @NonNull
    private String grade;

    @NonNull
    private String role;

    @NonNull
    private String responsibilities;

    @NonNull
    private String workingHours;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @NonNull
    private Timestamp closingDate;

    @NonNull
    private String contactName;

    @NonNull
    private String contactDepartment;

    @NonNull
    private String contactEmail;

    @NonNull
    private String contactTelephone;

    @NonNull
    private String eligibility;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Column(name = "government_opening_date")
    private Timestamp governmentOpeningDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Column(name = "internal_opening_date")
    private Timestamp internalOpeningDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Column(name = "public_opening_date")
    private Timestamp publicOpeningDate;

    private int salaryMin;

    private int salaryMax;

    private int numberVacancies;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Department department;
}
