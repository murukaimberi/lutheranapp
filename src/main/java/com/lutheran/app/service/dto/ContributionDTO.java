package com.lutheran.app.service.dto;

import com.lutheran.app.domain.enumeration.ContributionType;
import com.lutheran.app.domain.enumeration.Frequency;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.lutheran.app.domain.Contribution} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContributionDTO implements Serializable {

    private Long id;

    @NotNull
    private ContributionType contributionType;

    @NotNull
    private Frequency frequency;

    @NotNull
    private LocalDate month;

    @NotNull
    private BigDecimal amount;

    private CongregantDTO congregant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContributionType getContributionType() {
        return contributionType;
    }

    public void setContributionType(ContributionType contributionType) {
        this.contributionType = contributionType;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public LocalDate getMonth() {
        return month;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
        if (!(o instanceof ContributionDTO)) {
            return false;
        }

        ContributionDTO contributionDTO = (ContributionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contributionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContributionDTO{" +
            "id=" + getId() +
            ", contributionType='" + getContributionType() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", month='" + getMonth() + "'" +
            ", amount=" + getAmount() +
            ", congregant=" + getCongregant() +
            "}";
    }
}
