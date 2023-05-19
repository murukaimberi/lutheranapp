package com.lutheran.app.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.lutheran.app.domain.MarriageHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MarriageHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate marriageDate;

    @NotNull
    private String parishMarriedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getMarriageDate() {
        return marriageDate;
    }

    public void setMarriageDate(LocalDate marriageDate) {
        this.marriageDate = marriageDate;
    }

    public String getParishMarriedAt() {
        return parishMarriedAt;
    }

    public void setParishMarriedAt(String parishMarriedAt) {
        this.parishMarriedAt = parishMarriedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarriageHistoryDTO)) {
            return false;
        }

        MarriageHistoryDTO marriageHistoryDTO = (MarriageHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, marriageHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarriageHistoryDTO{" +
            "id=" + getId() +
            ", marriageDate='" + getMarriageDate() + "'" +
            ", parishMarriedAt='" + getParishMarriedAt() + "'" +
            "}";
    }
}
