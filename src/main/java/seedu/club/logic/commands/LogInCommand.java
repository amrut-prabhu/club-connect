package seedu.club.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.club.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.club.logic.parser.CliSyntax.PREFIX_USERNAME;

import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.member.Password;
import seedu.club.model.member.Username;

/**
 * Logs in a Member to ClubConnect
 */
public class LogInCommand extends Command {

    public static final String COMMAND_WORD = "login";
    public static final String MESSAGE_SUCCESS = "login successful!";
    public static final String MESSAGE_FAILURE = "login unsuccessful!";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Logs in a member to ClubConnect. "
            + "Parameters: "
            + PREFIX_USERNAME + "username "
            + PREFIX_PASSWORD + "password\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_USERNAME + "JohnDoe" + " "
            + PREFIX_PASSWORD + "password";
    private final Username username;
    private final Password password;

    public LogInCommand(Username username, Password password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);
        if (model.logInMemberSuccessful(username.value, password.value)) {
            return new CommandResult(MESSAGE_SUCCESS);
        }
        return new CommandResult(MESSAGE_FAILURE);
    }
}
