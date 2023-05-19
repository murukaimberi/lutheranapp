package com.lutheran.app.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.lutheran.app.domain.League} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeagueDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @Lob
    private String description;

    @NotNull
    private LocalDate createdDate;

    private Set<CongregantDTO> congregants = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<CongregantDTO> getCongregants() {
        return congregants;
    }

    public void setCongregants(Set<CongregantDTO> congregants) {
        this.congregants = congregants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeagueDTO)) {
            return false;
        }

        LeagueDTO leagueDTO = (LeagueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leagueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeagueDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", congregants=" + getCongregants() +
            "}";
    }
}
