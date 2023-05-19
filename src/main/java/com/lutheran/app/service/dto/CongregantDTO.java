package com.lutheran.app.service.dto;

import com.lutheran.app.domain.enumeration.Gender;
import com.lutheran.app.domain.enumeration.MaritalStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.lutheran.app.domain.Congregant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CongregantDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String title;

    @NotNull
    @Size(max = 50)
    private String firstNames;

    @NotNull
    @Size(max = 50)
    private String surname;

    @NotNull
    @Size(max = 100)
    private String email;

    @NotNull
    private LocalDate dob;

    private Gender gender;

    @Size(max = 100)
    private String profession;

    @NotNull
    private MaritalStatus maritalStatus;

    @Lob
    private byte[] profilePicture;

    private String profilePictureContentType;
    private MarriageHistoryDTO marriageHistory;

    private BaptismHistoryDTO baptismHistory;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePictureContentType() {
        return profilePictureContentType;
    }

    public void setProfilePictureContentType(String profilePictureContentType) {
        this.profilePictureContentType = profilePictureContentType;
    }

    public MarriageHistoryDTO getMarriageHistory() {
        return marriageHistory;
    }

    public void setMarriageHistory(MarriageHistoryDTO marriageHistory) {
        this.marriageHistory = marriageHistory;
    }

    public BaptismHistoryDTO getBaptismHistory() {
        return baptismHistory;
    }

    public void setBaptismHistory(BaptismHistoryDTO baptismHistory) {
        this.baptismHistory = baptismHistory;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CongregantDTO)) {
            return false;
        }

        CongregantDTO congregantDTO = (CongregantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, congregantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CongregantDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", firstNames='" + getFirstNames() + "'" +
            ", surname='" + getSurname() + "'" +
            ", email='" + getEmail() + "'" +
            ", dob='" + getDob() + "'" +
            ", gender='" + getGender() + "'" +
            ", profession='" + getProfession() + "'" +
            ", maritalStatus='" + getMaritalStatus() + "'" +
            ", profilePicture='" + getProfilePicture() + "'" +
            ", marriageHistory=" + getMarriageHistory() +
            ", baptismHistory=" + getBaptismHistory() +
            ", user=" + getUser() +
            "}";
    }
}
