package com.lutheran.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MarriageHistory.
 */
@Entity
@Table(name = "marriage_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MarriageHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "marriage_date", nullable = false)
    private LocalDate marriageDate;

    @NotNull
    @Column(name = "parish_married_at", nullable = false)
    private String parishMarriedAt;

    @JsonIgnoreProperties(
        value = { "marriageHistory", "baptismHistory", "user", "dependencies", "addresses", "contributions", "posts", "leagues" },
        allowSetters = true
    )
    @OneToOne(mappedBy = "marriageHistory")
    private Congregant congregant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MarriageHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getMarriageDate() {
        return this.marriageDate;
    }

    public MarriageHistory marriageDate(LocalDate marriageDate) {
        this.setMarriageDate(marriageDate);
        return this;
    }

    public void setMarriageDate(LocalDate marriageDate) {
        this.marriageDate = marriageDate;
    }

    public String getParishMarriedAt() {
        return this.parishMarriedAt;
    }

    public MarriageHistory parishMarriedAt(String parishMarriedAt) {
        this.setParishMarriedAt(parishMarriedAt);
        return this;
    }

    public void setParishMarriedAt(String parishMarriedAt) {
        this.parishMarriedAt = parishMarriedAt;
    }

    public Congregant getCongregant() {
        return this.congregant;
    }

    public void setCongregant(Congregant congregant) {
        if (this.congregant != null) {
            this.congregant.setMarriageHistory(null);
        }
        if (congregant != null) {
            congregant.setMarriageHistory(this);
        }
        this.congregant = congregant;
    }

    public MarriageHistory congregant(Congregant congregant) {
        this.setCongregant(congregant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarriageHistory)) {
            return false;
        }
        return id != null && id.equals(((MarriageHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarriageHistory{" +
            "id=" + getId() +
            ", marriageDate='" + getMarriageDate() + "'" +
            ", parishMarriedAt='" + getParishMarriedAt() + "'" +
            "}";
    }
}
