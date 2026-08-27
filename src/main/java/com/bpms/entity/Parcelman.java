package com.bpms.entity;

import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "parcelman")
public class Parcelman {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long parcelmanID;

    @Column(name = "name", nullable = false)
    private String parcelmanName;

    @Column(name = "account", nullable = false, unique = true)
    private String parcelmanAccount;

    /**
     * Diagram field: parcelmanPassword. Despite the name (matching the
     * diagram exactly), this still stores a bcrypt hash, never plaintext -
     * see verifyIdentity() below, which compares against it via a
     * PasswordEncoder rather than doing a raw string comparison.
     */
    @Column(name = "password_hash", nullable = false)
    private String parcelmanPassword;

    /**
     * Diagram method: verifyIdentity(): Boolean.
     * The diagram shows no parameters, but checking identity inherently
     * needs the raw password to check and a way to compare it against the
     * stored hash. A PasswordEncoder is passed in (rather than injected)
     * since this is a JPA entity and should not hold a Spring-managed
     * dependency.
     */
    public boolean verifyIdentity(String rawPassword, PasswordEncoder encoder) {
        return rawPassword != null && parcelmanPassword != null && encoder.matches(rawPassword, parcelmanPassword);
    }

    public Long getParcelmanID() { return parcelmanID; }
    public void setParcelmanID(Long parcelmanID) { this.parcelmanID = parcelmanID; }
    public String getParcelmanName() { return parcelmanName; }
    public void setParcelmanName(String parcelmanName) { this.parcelmanName = parcelmanName; }
    public String getParcelmanAccount() { return parcelmanAccount; }
    public void setParcelmanAccount(String parcelmanAccount) { this.parcelmanAccount = parcelmanAccount; }
    public String getParcelmanPassword() { return parcelmanPassword; }
    public void setParcelmanPassword(String parcelmanPassword) { this.parcelmanPassword = parcelmanPassword; }
}
