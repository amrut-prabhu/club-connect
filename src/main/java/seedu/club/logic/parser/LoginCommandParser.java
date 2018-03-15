package seedu.club.logic.parser;

import static seedu.club.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.club.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.club.logic.parser.CliSyntax.PREFIX_USERNAME;

import java.util.stream.Stream;

import seedu.club.commons.exceptions.IllegalValueException;
import seedu.club.logic.commands.LogInCommand;
import seedu.club.logic.parser.exceptions.ParseException;
import seedu.club.model.member.Password;
import seedu.club.model.member.Username;

/**
 * Parses input arguments and creates a new LogInCommand object
 */
public class LoginCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the LogInCommand
     * and returns an LogInCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public LogInCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_USERNAME, PREFIX_PASSWORD);

        if (!arePrefixesPresent(argMultimap, PREFIX_USERNAME, PREFIX_PASSWORD)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, LogInCommand.MESSAGE_USAGE));
        }

        try {
            Username username = ParserUtil.parseUsername(argMultimap.getValue(PREFIX_USERNAME)).get();
            Password password = ParserUtil.parsePassword(argMultimap.getValue(PREFIX_PASSWORD)).get();

            return new LogInCommand(username, password);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
