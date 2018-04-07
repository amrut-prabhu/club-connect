package seedu.club.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;

import seedu.club.logic.CommandHistory;
import seedu.club.logic.UndoRedoStack;
import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.Model;


/**
 * Logs a member out of the clubbook
 */
public class LogOutCommand extends Command {
    public static final String COMMAND_WORD = "logout";
    public static final ArrayList<String> COMMAND_ALIASES = new ArrayList<>(
            Arrays.asList(COMMAND_WORD, "signout")
    );

    public static final String MESSAGE_SUCCESS = "Logout successful.";

    public LogOutCommand() {
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);
        requireToLogIn();

        model.logOutMember();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public void setData(Model model, CommandHistory history, UndoRedoStack undoRedoStack) {
        super.setData(model, history, undoRedoStack);
    }
}
