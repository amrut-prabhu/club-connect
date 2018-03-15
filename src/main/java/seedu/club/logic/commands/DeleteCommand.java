package seedu.club.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.club.commons.core.Messages;
import seedu.club.commons.core.index.Index;
import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.member.Member;
import seedu.club.model.member.exceptions.MemberNotFoundException;

/**
 * Deletes a member identified using it's last displayed index from the club book.
 */
public class DeleteCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the member identified by the index number used in the last member listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_MEMBER_SUCCESS = "Deleted Member: %1$s";

    private final Index targetIndex;

    private Member memberToDelete;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(memberToDelete);
        try {
            model.deleteMember(memberToDelete);
        } catch (MemberNotFoundException pnfe) {
            throw new AssertionError("The target member cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_MEMBER_SUCCESS, memberToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Member> lastShownList = model.getFilteredMemberList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);
        }

        memberToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteCommand) other).targetIndex) // state check
                && Objects.equals(this.memberToDelete, ((DeleteCommand) other).memberToDelete));
    }
}
