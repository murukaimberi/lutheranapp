package com.lutheran.app.service.dto;

import com.lutheran.app.domain.enumeration.Gender;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.lutheran.app.domain.Dependant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DependantDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 250)
    private String fullNames;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private Gender gender;

    private CongregantDTO congregant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullNames() {
        return fullNames;
    }

    public void setFullNames(String fullNames) {
        this.fullNames = fullNames;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public CongregantDTO getCongregant() {
        return congregant;
    }

    public void setCongregant(CongregantDTO congregant) {
        this.congregant = congregant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DependantDTO)) {
            return false;
        }

        DependantDTO dependantDTO = (DependantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dependantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DependantDTO{" +
            "id=" + getId() +
            ", fullNames='" + getFullNames() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", gender='" + getGender() + "'" +
            ", congregant=" + getCongregant() +
            "}";
    }
}
