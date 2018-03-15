package seedu.club.model.group;

import static java.util.Objects.requireNonNull;
import static seedu.club.commons.util.AppUtil.checkArgument;

/**
 * Represents a Member's Group in the club book
 * Guarantees: immutable; is valid as declared in {@link #isValidGroup(String)}
 */
public class Group {
    public static final String MESSAGE_GROUP_CONSTRAINTS =
            "Member groups should only contain alphanumeric characters, and it should not be blank";

    /*
     * The first character of the group must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String GROUP_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum}]*";

    public static final String DEFAULT_GROUP = "member";

    public final String groupName;

    /**
     * Constructs a {@code Group}.
     *
     * @param group A valid group.
     */
    public Group(String group) {
        requireNonNull(group);
        checkArgument(isValidGroup(group), MESSAGE_GROUP_CONSTRAINTS);
        this.groupName = group;
    }

    /**
     * Returns true if given string is a valid member group.
     */
    public static Boolean isValidGroup(String test) {
        return test.matches(GROUP_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return groupName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this    // short circuit if same object
                || (other instanceof Group  // instanceof handles nulls
                && this.groupName.equalsIgnoreCase(((Group) other).groupName));   // state check
    }

    @Override
    public int hashCode() {
        return groupName.hashCode();
    }
}
