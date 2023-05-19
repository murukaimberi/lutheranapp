package com.lutheran.app.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.lutheran.app.domain.BaptismHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BaptismHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String parishName;

    @NotNull
    private LocalDate baptismDate;

    @NotNull
    private LocalDate confirmedDate;

    @NotNull
    @Size(max = 100)
    private String parishBaptisedAt;

    @NotNull
    @Size(max = 100)
    private String prishedConfirmedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParishName() {
        return parishName;
    }

    public void setParishName(String parishName) {
        this.parishName = parishName;
    }

    public LocalDate getBaptismDate() {
        return baptismDate;
    }

    public void setBaptismDate(LocalDate baptismDate) {
        this.baptismDate = baptismDate;
    }

    public LocalDate getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(LocalDate confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public String getParishBaptisedAt() {
        return parishBaptisedAt;
    }

    public void setParishBaptisedAt(String parishBaptisedAt) {
        this.parishBaptisedAt = parishBaptisedAt;
    }

    public String getPrishedConfirmedAt() {
        return prishedConfirmedAt;
    }

    public void setPrishedConfirmedAt(String prishedConfirmedAt) {
        this.prishedConfirmedAt = prishedConfirmedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaptismHistoryDTO)) {
            return false;
        }

        BaptismHistoryDTO baptismHistoryDTO = (BaptismHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, baptismHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BaptismHistoryDTO{" +
            "id=" + getId() +
            ", parishName='" + getParishName() + "'" +
            ", baptismDate='" + getBaptismDate() + "'" +
            ", confirmedDate='" + getConfirmedDate() + "'" +
            ", parishBaptisedAt='" + getParishBaptisedAt() + "'" +
            ", prishedConfirmedAt='" + getPrishedConfirmedAt() + "'" +
            "}";
    }
}
