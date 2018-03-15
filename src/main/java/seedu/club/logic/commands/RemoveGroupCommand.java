package seedu.club.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.club.logic.parser.CliSyntax.PREFIX_GROUP;

import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.group.Group;
import seedu.club.model.group.exceptions.GroupCannotBeRemovedException;
import seedu.club.model.group.exceptions.GroupNotFoundException;

/**
 * Removes a group from the Club Book
 */
public class RemoveGroupCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "removegroup";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes a Group from the Club Book. "
            + "Parameters: "
            + PREFIX_GROUP + "GROUP";

    public static final String MESSAGE_SUCCESS = "Group deleted from Club Book: %1$s";
    public static final String MESSAGE_NON_EXISTENT_GROUP = "This group does not exist in the Club Book";
    public static final String MESSAGE_MANDATORY_GROUP = "This group cannot be deleted as it is a mandatory group.";

    private final Group toRemove;

    /**
     * Creates an AddCommand to add the specified {@code Member}
     */
    public RemoveGroupCommand(Group group) {
        requireNonNull(group);
        toRemove = group;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.removeGroup(toRemove);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toRemove));
        } catch (GroupNotFoundException gnfe) {
            throw new CommandException(MESSAGE_NON_EXISTENT_GROUP);
        } catch (GroupCannotBeRemovedException gcbre) {
            throw new CommandException(MESSAGE_MANDATORY_GROUP);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RemoveGroupCommand // instanceof handles nulls
                && toRemove.equals(((RemoveGroupCommand) other).toRemove));
    }
}
