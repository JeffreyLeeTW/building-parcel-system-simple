package com.bpms.entity;

import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "concierge")
public class Concierge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long conciergeID;

    @Column(name = "name", nullable = false)
    private String conciergeName;

    @Column(name = "account", nullable = false, unique = true)
    private String conciergeAccount;

    /**
     * Diagram field: parcelmanPassword (Parcelman renamed to Concierge).
     * Despite the name (matching the diagram exactly), this still stores a
     * bcrypt hash, never plaintext - see verifyIdentity() below, which
     * compares against it via a PasswordEncoder rather than doing a raw
     * string comparison.
     */
    @Column(name = "password_hash", nullable = false)
    private String conciergePassword;

    /**
     * Diagram method: verifyIdentity(): Boolean.
     * The diagram shows no parameters, but checking identity inherently
     * needs the raw password to check and a way to compare it against the
     * stored hash. A PasswordEncoder is passed in (rather than injected)
     * since this is a JPA entity and should not hold a Spring-managed
     * dependency.
     */
    public boolean verifyIdentity(String rawPassword, PasswordEncoder encoder) {
        return rawPassword != null && conciergePassword != null && encoder.matches(rawPassword, conciergePassword);
    }

    public Long getConciergeID() { return conciergeID; }
    public void setConciergeID(Long conciergeID) { this.conciergeID = conciergeID; }
    public String getConciergeName() { return conciergeName; }
    public void setConciergeName(String conciergeName) { this.conciergeName = conciergeName; }
    public String getConciergeAccount() { return conciergeAccount; }
    public void setConciergeAccount(String conciergeAccount) { this.conciergeAccount = conciergeAccount; }
    public String getConciergePassword() { return conciergePassword; }
    public void setConciergePassword(String conciergePassword) { this.conciergePassword = conciergePassword; }
}
