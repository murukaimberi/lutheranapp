package com.lutheran.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lutheran.app.domain.enumeration.Gender;
import com.lutheran.app.domain.enumeration.MaritalStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Congregant.
 */
@Entity
@Table(name = "congregant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Congregant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @NotNull
    @Size(max = 50)
    @Column(name = "first_names", length = 50, nullable = false)
    private String firstNames;

    @NotNull
    @Size(max = 50)
    @Column(name = "surname", length = 50, nullable = false)
    private String surname;

    @NotNull
    @Size(max = 100)
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @NotNull
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Size(max = 100)
    @Column(name = "profession", length = 100)
    private String profession;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status", nullable = false)
    private MaritalStatus maritalStatus;

    @Lob
    @Column(name = "profile_picture")
    private byte[] profilePicture;

    @Column(name = "profile_picture_content_type")
    private String profilePictureContentType;

    @JsonIgnoreProperties(value = { "congregant" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private MarriageHistory marriageHistory;

    @JsonIgnoreProperties(value = { "congragant" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private BaptismHistory baptismHistory;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "congregant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "congregant" }, allowSetters = true)
    private Set<Dependant> dependencies = new HashSet<>();

    @OneToMany(mappedBy = "congregant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "country", "congregant" }, allowSetters = true)
    private Set<Location> addresses = new HashSet<>();

    @OneToMany(mappedBy = "congregant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "congregant" }, allowSetters = true)
    private Set<Contribution> contributions = new HashSet<>();

    @OneToMany(mappedBy = "congregant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "congregant" }, allowSetters = true)
    private Set<Post> posts = new HashSet<>();

    @ManyToMany(mappedBy = "congregants")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "congregants" }, allowSetters = true)
    private Set<League> leagues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Congregant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Congregant title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstNames() {
        return this.firstNames;
    }

    public Congregant firstNames(String firstNames) {
        this.setFirstNames(firstNames);
        return this;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public String getSurname() {
        return this.surname;
    }

    public Congregant surname(String surname) {
        this.setSurname(surname);
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return this.email;
    }

    public Congregant email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public Congregant dob(LocalDate dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Congregant gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getProfession() {
        return this.profession;
    }

    public Congregant profession(String profession) {
        this.setProfession(profession);
        return this;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public MaritalStatus getMaritalStatus() {
        return this.maritalStatus;
    }

    public Congregant maritalStatus(MaritalStatus maritalStatus) {
        this.setMaritalStatus(maritalStatus);
        return this;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public byte[] getProfilePicture() {
        return this.profilePicture;
    }

    public Congregant profilePicture(byte[] profilePicture) {
        this.setProfilePicture(profilePicture);
        return this;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePictureContentType() {
        return this.profilePictureContentType;
    }

    public Congregant profilePictureContentType(String profilePictureContentType) {
        this.profilePictureContentType = profilePictureContentType;
        return this;
    }

    public void setProfilePictureContentType(String profilePictureContentType) {
        this.profilePictureContentType = profilePictureContentType;
    }

    public MarriageHistory getMarriageHistory() {
        return this.marriageHistory;
    }

    public void setMarriageHistory(MarriageHistory marriageHistory) {
        this.marriageHistory = marriageHistory;
    }

    public Congregant marriageHistory(MarriageHistory marriageHistory) {
        this.setMarriageHistory(marriageHistory);
        return this;
    }

    public BaptismHistory getBaptismHistory() {
        return this.baptismHistory;
    }

    public void setBaptismHistory(BaptismHistory baptismHistory) {
        this.baptismHistory = baptismHistory;
    }

    public Congregant baptismHistory(BaptismHistory baptismHistory) {
        this.setBaptismHistory(baptismHistory);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Congregant user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Dependant> getDependencies() {
        return this.dependencies;
    }

    public void setDependencies(Set<Dependant> dependants) {
        if (this.dependencies != null) {
            this.dependencies.forEach(i -> i.setCongregant(null));
        }
        if (dependants != null) {
            dependants.forEach(i -> i.setCongregant(this));
        }
        this.dependencies = dependants;
    }

    public Congregant dependencies(Set<Dependant> dependants) {
        this.setDependencies(dependants);
        return this;
    }

    public Congregant addDependencies(Dependant dependant) {
        this.dependencies.add(dependant);
        dependant.setCongregant(this);
        return this;
    }

    public Congregant removeDependencies(Dependant dependant) {
        this.dependencies.remove(dependant);
        dependant.setCongregant(null);
        return this;
    }

    public Set<Location> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Location> locations) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setCongregant(null));
        }
        if (locations != null) {
            locations.forEach(i -> i.setCongregant(this));
        }
        this.addresses = locations;
    }

    public Congregant addresses(Set<Location> locations) {
        this.setAddresses(locations);
        return this;
    }

    public Congregant addAddresses(Location location) {
        this.addresses.add(location);
        location.setCongregant(this);
        return this;
    }

    public Congregant removeAddresses(Location location) {
        this.addresses.remove(location);
        location.setCongregant(null);
        return this;
    }

    public Set<Contribution> getContributions() {
        return this.contributions;
    }

    public void setContributions(Set<Contribution> contributions) {
        if (this.contributions != null) {
            this.contributions.forEach(i -> i.setCongregant(null));
        }
        if (contributions != null) {
            contributions.forEach(i -> i.setCongregant(this));
        }
        this.contributions = contributions;
    }

    public Congregant contributions(Set<Contribution> contributions) {
        this.setContributions(contributions);
        return this;
    }

    public Congregant addContributions(Contribution contribution) {
        this.contributions.add(contribution);
        contribution.setCongregant(this);
        return this;
    }

    public Congregant removeContributions(Contribution contribution) {
        this.contributions.remove(contribution);
        contribution.setCongregant(null);
        return this;
    }

    public Set<Post> getPosts() {
        return this.posts;
    }

    public void setPosts(Set<Post> posts) {
        if (this.posts != null) {
            this.posts.forEach(i -> i.setCongregant(null));
        }
        if (posts != null) {
            posts.forEach(i -> i.setCongregant(this));
        }
        this.posts = posts;
    }

    public Congregant posts(Set<Post> posts) {
        this.setPosts(posts);
        return this;
    }

    public Congregant addPosts(Post post) {
        this.posts.add(post);
        post.setCongregant(this);
        return this;
    }

    public Congregant removePosts(Post post) {
        this.posts.remove(post);
        post.setCongregant(null);
        return this;
    }

    public Set<League> getLeagues() {
        return this.leagues;
    }

    public void setLeagues(Set<League> leagues) {
        if (this.leagues != null) {
            this.leagues.forEach(i -> i.removeCongregants(this));
        }
        if (leagues != null) {
            leagues.forEach(i -> i.addCongregants(this));
        }
        this.leagues = leagues;
    }

    public Congregant leagues(Set<League> leagues) {
        this.setLeagues(leagues);
        return this;
    }

    public Congregant addLeagues(League league) {
        this.leagues.add(league);
        league.getCongregants().add(this);
        return this;
    }

    public Congregant removeLeagues(League league) {
        this.leagues.remove(league);
        league.getCongregants().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Congregant)) {
            return false;
        }
        return id != null && id.equals(((Congregant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Congregant{" +
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
            ", profilePictureContentType='" + getProfilePictureContentType() + "'" +
            "}";
    }
}
