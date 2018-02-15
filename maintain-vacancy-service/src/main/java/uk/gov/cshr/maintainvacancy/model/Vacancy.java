package uk.gov.cshr.maintainvacancy.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vacancies")
@SequenceGenerator(name = "vacancies_id_seq", sequenceName = "vacancies_id_seq", allocationSize = 1)
public class Vacancy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vacancies_id_seq")
    private Long id;

    @NonNull
    private Long identifier;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @Column(name = "shortdescription")
    private String shortDescription;

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

    @NonNull
    private Integer salaryMin;

    private Integer salaryMax;

    private Integer numberVacancies;

    /**
     * If a vacancy has no longitude ensure it is null not 0 (zero) since 0 is a
     * valid point in latitude
     */
    private Double longitude;

    /**
     * If a vacancy has no latitude ensure it is null not 0 (zero) since 0 is a
     * valid point in latitude
     */
    private Double latitude;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Department department;

    @Column(name = "displaycsccontent")
    private Boolean displayCscContent;

    @Column(name = "selectionprocessdetails")
    private String selectionProcessDetails;
}
