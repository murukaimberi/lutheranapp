package com.lutheran.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lutheran.app.domain.enumeration.ContributionType;
import com.lutheran.app.domain.enumeration.Frequency;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contribution.
 */
@Entity
@Table(name = "contribution")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contribution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "contribution_type", nullable = false)
    private ContributionType contributionType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", nullable = false)
    private Frequency frequency;

    @NotNull
    @Column(name = "month", nullable = false)
    private LocalDate month;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "marriageHistory", "baptismHistory", "user", "dependencies", "addresses", "contributions", "posts", "leagues" },
        allowSetters = true
    )
    private Congregant congregant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contribution id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContributionType getContributionType() {
        return this.contributionType;
    }

    public Contribution contributionType(ContributionType contributionType) {
        this.setContributionType(contributionType);
        return this;
    }

    public void setContributionType(ContributionType contributionType) {
        this.contributionType = contributionType;
    }

    public Frequency getFrequency() {
        return this.frequency;
    }

    public Contribution frequency(Frequency frequency) {
        this.setFrequency(frequency);
        return this;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public LocalDate getMonth() {
        return this.month;
    }

    public Contribution month(LocalDate month) {
        this.setMonth(month);
        return this;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Contribution amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Congregant getCongregant() {
        return this.congregant;
    }

    public void setCongregant(Congregant congregant) {
        this.congregant = congregant;
    }

    public Contribution congregant(Congregant congregant) {
        this.setCongregant(congregant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contribution)) {
            return false;
        }
        return id != null && id.equals(((Contribution) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contribution{" +
            "id=" + getId() +
            ", contributionType='" + getContributionType() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", month='" + getMonth() + "'" +
            ", amount=" + getAmount() +
            "}";
    }
}
