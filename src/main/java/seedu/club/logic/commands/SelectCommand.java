package seedu.club.logic.commands;

import java.util.List;

import seedu.club.commons.core.EventsCenter;
import seedu.club.commons.core.Messages;
import seedu.club.commons.core.index.Index;
import seedu.club.commons.events.ui.JumpToListRequestEvent;
import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.member.Member;

/**
 * Selects a member identified using it's last displayed index from the club book.
 */
public class SelectCommand extends Command {

    public static final String COMMAND_WORD = "select";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the member identified by the index number used in the last member listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_MEMBER_SUCCESS = "Selected Member: %1$s";

    private final Index targetIndex;

    public SelectCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<Member> lastShownList = model.getFilteredMemberList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);
        }

        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex));
        return new CommandResult(String.format(MESSAGE_SELECT_MEMBER_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectCommand // instanceof handles nulls
                && this.targetIndex.equals(((SelectCommand) other).targetIndex)); // state check
    }
}
