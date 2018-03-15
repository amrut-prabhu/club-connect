package seedu.club.model.member;

/**
 * Represent a member's password
 */
public class Password {
    public final String value;

    public Password(String password) {
        this.value = password;
    }

    @Override
    public String toString() {
        return value;
    }
}
