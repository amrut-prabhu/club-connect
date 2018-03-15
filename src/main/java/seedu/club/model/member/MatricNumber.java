package seedu.club.model.member;

import static java.util.Objects.requireNonNull;
import static seedu.club.commons.util.AppUtil.checkArgument;

/**
 * Represents a Member's matric number in the club book.
 * Guarantees: immutable; is valid as declared in {@link #isValidMatricNumber(String)}
 */
public class MatricNumber {

    public static final String MESSAGE_MATRIC_NUMBER_CONSTRAINTS =
            "Member matric number must begin with one letter, 7 digits in the middle and a letter at the end";

    public static final String MATRIC_NUMBER_VALIDATION_REGEX = "^[aA]\\d{7}[a-zA-Z]$";

    public final String value;

    /**
     * Constructs a {@code MatricNumber}.
     *
     * @param matricNumber A valid matric number.
     */
    public MatricNumber(String matricNumber) {
        requireNonNull(matricNumber);
        checkArgument(isValidMatricNumber(matricNumber), MESSAGE_MATRIC_NUMBER_CONSTRAINTS);
        this.value = matricNumber.toUpperCase();
    }

    /**
     * Returns true if a given string is a valid member email.
     */
    public static boolean isValidMatricNumber(String test) {
        return test.matches(MATRIC_NUMBER_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MatricNumber // instanceof handles nulls
                && this.value.equals(((MatricNumber) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
