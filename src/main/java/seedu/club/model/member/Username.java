package seedu.club.model.member;

/**
 * Represent a member's username
 */
public class Username {
    public final String value;

    public Username(String name) {
        this.value = name;
    }

    @Override
    public String toString() {
        return value;
    }
}
