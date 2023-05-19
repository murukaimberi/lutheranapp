package com.lutheran.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BaptismHistory.
 */
@Entity
@Table(name = "baptism_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BaptismHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "parish_name", length = 50, nullable = false)
    private String parishName;

    @NotNull
    @Column(name = "baptism_date", nullable = false)
    private LocalDate baptismDate;

    @NotNull
    @Column(name = "confirmed_date", nullable = false)
    private LocalDate confirmedDate;

    @NotNull
    @Size(max = 100)
    @Column(name = "parish_baptised_at", length = 100, nullable = false)
    private String parishBaptisedAt;

    @NotNull
    @Size(max = 100)
    @Column(name = "prished_confirmed_at", length = 100, nullable = false)
    private String prishedConfirmedAt;

    @JsonIgnoreProperties(
        value = { "marriageHistory", "baptismHistory", "user", "dependencies", "addresses", "contributions", "posts", "leagues" },
        allowSetters = true
    )
    @OneToOne(mappedBy = "baptismHistory")
    private Congregant congragant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BaptismHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParishName() {
        return this.parishName;
    }

    public BaptismHistory parishName(String parishName) {
        this.setParishName(parishName);
        return this;
    }

    public void setParishName(String parishName) {
        this.parishName = parishName;
    }

    public LocalDate getBaptismDate() {
        return this.baptismDate;
    }

    public BaptismHistory baptismDate(LocalDate baptismDate) {
        this.setBaptismDate(baptismDate);
        return this;
    }

    public void setBaptismDate(LocalDate baptismDate) {
        this.baptismDate = baptismDate;
    }

    public LocalDate getConfirmedDate() {
        return this.confirmedDate;
    }

    public BaptismHistory confirmedDate(LocalDate confirmedDate) {
        this.setConfirmedDate(confirmedDate);
        return this;
    }

    public void setConfirmedDate(LocalDate confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public String getParishBaptisedAt() {
        return this.parishBaptisedAt;
    }

    public BaptismHistory parishBaptisedAt(String parishBaptisedAt) {
        this.setParishBaptisedAt(parishBaptisedAt);
        return this;
    }

    public void setParishBaptisedAt(String parishBaptisedAt) {
        this.parishBaptisedAt = parishBaptisedAt;
    }

    public String getPrishedConfirmedAt() {
        return this.prishedConfirmedAt;
    }

    public BaptismHistory prishedConfirmedAt(String prishedConfirmedAt) {
        this.setPrishedConfirmedAt(prishedConfirmedAt);
        return this;
    }

    public void setPrishedConfirmedAt(String prishedConfirmedAt) {
        this.prishedConfirmedAt = prishedConfirmedAt;
    }

    public Congregant getCongragant() {
        return this.congragant;
    }

    public void setCongragant(Congregant congregant) {
        if (this.congragant != null) {
            this.congragant.setBaptismHistory(null);
        }
        if (congregant != null) {
            congregant.setBaptismHistory(this);
        }
        this.congragant = congregant;
    }

    public BaptismHistory congragant(Congregant congregant) {
        this.setCongragant(congregant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaptismHistory)) {
            return false;
        }
        return id != null && id.equals(((BaptismHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BaptismHistory{" +
            "id=" + getId() +
            ", parishName='" + getParishName() + "'" +
            ", baptismDate='" + getBaptismDate() + "'" +
            ", confirmedDate='" + getConfirmedDate() + "'" +
            ", parishBaptisedAt='" + getParishBaptisedAt() + "'" +
            ", prishedConfirmedAt='" + getPrishedConfirmedAt() + "'" +
            "}";
    }
}
