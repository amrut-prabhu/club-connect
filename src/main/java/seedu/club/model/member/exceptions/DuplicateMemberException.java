package seedu.club.model.member.exceptions;

import seedu.club.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate Member objects.
 */
public class DuplicateMemberException extends DuplicateDataException {
    public DuplicateMemberException() {
        super("Operation would result in duplicate persons");
    }
}
