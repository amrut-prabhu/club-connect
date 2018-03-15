package seedu.club.testutil;

import static seedu.club.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.club.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.club.logic.parser.CliSyntax.PREFIX_MATRIC_NUMBER;
import static seedu.club.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.club.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.club.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.club.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.club.logic.parser.CliSyntax.PREFIX_USERNAME;

import seedu.club.logic.commands.AddCommand;
import seedu.club.model.member.Member;

/**
 * A utility class for Member.
 */
public class MemberUtil {

    /**
     * Returns an add command string for adding the {@code member}.
     */
    public static String getAddCommand(Member member) {
        return AddCommand.COMMAND_WORD + " " + getMemberDetails(member);
    }

    /**
     * Returns the part of command string for the given {@code member}'s details.
     */
    public static String getMemberDetails(Member member) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + member.getName().fullName + " ");
        sb.append(PREFIX_PHONE + member.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + member.getEmail().value + " ");
        sb.append(PREFIX_MATRIC_NUMBER + member.getMatricNumber().value + " ");
        sb.append(PREFIX_GROUP + member.getGroup().groupName + " ");
        member.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        sb.append(PREFIX_USERNAME + member.getUsername().value + " ");
        sb.append(PREFIX_PASSWORD + member.getPassword().value + " ");
        return sb.toString();
    }
}
