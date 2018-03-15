package seedu.club.logic.parser;

import static seedu.club.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.club.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.club.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.club.logic.commands.CommandTestUtil.GROUP_DESC_BOB;
import static seedu.club.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.club.logic.commands.CommandTestUtil.INVALID_GROUP_DESC;
import static seedu.club.logic.commands.CommandTestUtil.INVALID_MATRIC_NUMBER_DESC;
import static seedu.club.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.club.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.club.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.club.logic.commands.CommandTestUtil.MATRIC_NUMBER_DESC_AMY;
import static seedu.club.logic.commands.CommandTestUtil.MATRIC_NUMBER_DESC_BOB;
import static seedu.club.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.club.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.club.logic.commands.CommandTestUtil.PASSWORD_DESC;
import static seedu.club.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.club.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.club.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.club.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.club.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.club.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.club.logic.commands.CommandTestUtil.USERNAME_DESC_AMY;
import static seedu.club.logic.commands.CommandTestUtil.USERNAME_DESC_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.club.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_GROUP_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_MATRIC_NUMBER_AMY;
import static seedu.club.logic.commands.CommandTestUtil.VALID_MATRIC_NUMBER_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.club.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.club.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.club.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.club.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.club.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.club.logic.commands.AddCommand;
import seedu.club.model.group.Group;
import seedu.club.model.member.Email;
import seedu.club.model.member.MatricNumber;
import seedu.club.model.member.Member;
import seedu.club.model.member.Name;
import seedu.club.model.member.Phone;
import seedu.club.model.tag.Tag;
import seedu.club.testutil.MemberBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Member expectedMember = new MemberBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withMatricNumber(VALID_MATRIC_NUMBER_BOB)
                .withGroup(VALID_GROUP_BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + MATRIC_NUMBER_DESC_BOB + GROUP_DESC_BOB + TAG_DESC_FRIEND
                + USERNAME_DESC_BOB + PASSWORD_DESC, new AddCommand(expectedMember));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + MATRIC_NUMBER_DESC_BOB + GROUP_DESC_BOB + TAG_DESC_FRIEND
                + USERNAME_DESC_BOB + PASSWORD_DESC, new AddCommand(expectedMember));

        // multiple phones - last phone accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_AMY + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + MATRIC_NUMBER_DESC_BOB + GROUP_DESC_BOB + TAG_DESC_FRIEND
                + USERNAME_DESC_BOB + PASSWORD_DESC, new AddCommand(expectedMember));

        // multiple emails - last email accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_AMY + EMAIL_DESC_BOB
                + MATRIC_NUMBER_DESC_BOB + GROUP_DESC_BOB + TAG_DESC_FRIEND
                + USERNAME_DESC_BOB + PASSWORD_DESC, new AddCommand(expectedMember));

        // multiple addresses - last club accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + MATRIC_NUMBER_DESC_AMY
                + MATRIC_NUMBER_DESC_BOB + GROUP_DESC_BOB + TAG_DESC_FRIEND
                + USERNAME_DESC_BOB + PASSWORD_DESC, new AddCommand(expectedMember));

        // multiple tags - all accepted
        Member expectedMemberMultipleTags = new MemberBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withMatricNumber(VALID_MATRIC_NUMBER_BOB)
                .withGroup(VALID_GROUP_BOB).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND).build();
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + MATRIC_NUMBER_DESC_BOB
                + GROUP_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND
                + USERNAME_DESC_BOB + PASSWORD_DESC, new AddCommand(expectedMemberMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Member expectedMemberAmy = new MemberBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_AMY).withMatricNumber(VALID_MATRIC_NUMBER_AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + MATRIC_NUMBER_DESC_AMY
                        + USERNAME_DESC_AMY + PASSWORD_DESC,
                new AddCommand(expectedMemberAmy));

        // no group
        Member expectedMemberBob = new MemberBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withMatricNumber(VALID_MATRIC_NUMBER_BOB).withGroup().withTags().build();
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + MATRIC_NUMBER_DESC_BOB
                        + USERNAME_DESC_BOB + PASSWORD_DESC,
                new AddCommand(expectedMemberBob));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + MATRIC_NUMBER_DESC_BOB
                + GROUP_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + MATRIC_NUMBER_DESC_BOB
                + GROUP_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + MATRIC_NUMBER_DESC_BOB,
                expectedMessage);

        // missing club prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_MATRIC_NUMBER_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_MATRIC_NUMBER_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + MATRIC_NUMBER_DESC_BOB
                + GROUP_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + MATRIC_NUMBER_DESC_BOB
                + GROUP_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Phone.MESSAGE_PHONE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + MATRIC_NUMBER_DESC_BOB
                + GROUP_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Email.MESSAGE_EMAIL_CONSTRAINTS);

        // invalid club
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_MATRIC_NUMBER_DESC
                + GROUP_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, MatricNumber.MESSAGE_MATRIC_NUMBER_CONSTRAINTS);

        //invalid group
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + MATRIC_NUMBER_DESC_BOB
                + INVALID_GROUP_DESC + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Group.MESSAGE_GROUP_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + MATRIC_NUMBER_DESC_BOB
                + GROUP_DESC_BOB + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_TAG_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_MATRIC_NUMBER_DESC
                + GROUP_DESC_BOB,
                Name.MESSAGE_NAME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + MATRIC_NUMBER_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
