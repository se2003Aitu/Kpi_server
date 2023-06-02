package com.program.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.program.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.program.model.role.ERole;
import com.program.model.role.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.*;
import java.util.stream.Collectors;

//@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class User {
    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;


    @NotEmpty(message = "{user.email.not.empty}")
    @Column(unique = true)
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email is not valid")
    private String email;
    @NotEmpty(message = "{user.name.not.empty}")
    private String name;
    @NotEmpty(message = "{user.password.not.empty}")
    @Length(min = 5, message = "{user.password.length}")
    private String password;



//    @ManyToOne(cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "statusId")
//    private Status status;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
//    private List<Status> statuses =new ArrayList<>();


    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @JsonIgnore
    public boolean isAdmin() {
        ERole adminRole = ERole.ROLE_ADMIN;
        return roles.stream().map(Role::getName).anyMatch(adminRole::equals);
    }

    @JsonIgnore
    public boolean isTeacher() {
        ERole teacherRole = ERole.ROLE_TEACHER;
        return roles.stream().map(Role::getName).anyMatch(teacherRole::equals);
    }

    @JsonIgnore
    public boolean isObserver() {
        ERole observerRole = ERole.ROLE_OBSERVER;
        return roles.stream().map(Role::getName).anyMatch(observerRole::equals);
    }

//    public List<Event> getUserEvents(String categoryName, String statusName) {
//        if (Objects.equals(this.categoryName, categoryName) && Objects.equals(this.statusName, statusName)){
//            User userEventId =
//        }
//    }

    @JsonIgnore
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public Set<Role> getRoles() {
        return roles;
    }

    @JsonProperty("roles")
    public List<ERole> getRolesByString(){
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) &&
                email.equals(user.email) &&
                name.equals(user.name) &&
                password.equals(user.password) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email, name, password, roles);
    }



}
