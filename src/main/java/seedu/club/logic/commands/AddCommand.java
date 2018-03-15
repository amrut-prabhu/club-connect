package seedu.club.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.club.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.club.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.club.logic.parser.CliSyntax.PREFIX_MATRIC_NUMBER;
import static seedu.club.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.club.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.club.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.club.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.club.logic.parser.CliSyntax.PREFIX_USERNAME;

import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.member.Member;
import seedu.club.model.member.exceptions.DuplicateMemberException;

/**
 * Adds a member to the club book.
 */
public class AddCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a member to the club book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_MATRIC_NUMBER + "MATRIC NUMBER "
            + "[" + PREFIX_GROUP + "GROUP] "
            + "[" + PREFIX_TAG + "TAG]... "
            + PREFIX_USERNAME + "username "
            + PREFIX_PASSWORD + "password\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_MATRIC_NUMBER + "A0123456H "
            + PREFIX_GROUP + "logistics "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney "
            + PREFIX_USERNAME + "JohnDoe "
            + PREFIX_PASSWORD + "password";

    public static final String MESSAGE_SUCCESS = "New member added: %1$s";
    public static final String MESSAGE_DUPLICATE_MEMBER = "This member already exists in the club book";

    private final Member toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Member}
     */
    public AddCommand(Member member) {
        requireNonNull(member);
        toAdd = member;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.addMember(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateMemberException e) {
            throw new CommandException(MESSAGE_DUPLICATE_MEMBER);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
