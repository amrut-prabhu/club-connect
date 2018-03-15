package seedu.club.logic.parser;

import static seedu.club.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.club.logic.commands.FindByCommand;
import seedu.club.logic.parser.exceptions.ParseException;
import seedu.club.model.member.FieldContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindByCommand object
 */
public class FindByCommandParser implements Parser<FindByCommand> {

    private static final String[] fieldTypes = {"name", "email", "phone", "matric", "tag", "group"};

    /**
     * Parses the given {@code String} of arguments in the context of the FindByCommand
     * and returns an FindByCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindByCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindByCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        if (nameKeywords.length < 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindByCommand.MESSAGE_USAGE));
        }

        String fieldType = nameKeywords[0].toLowerCase();
        if (!Arrays.asList(fieldTypes).contains(fieldType)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindByCommand.MESSAGE_USAGE));
        }

        return new FindByCommand(new FieldContainsKeywordsPredicate(
                Arrays.asList(nameKeywords).subList(1, nameKeywords.length), fieldType));
    }
}
