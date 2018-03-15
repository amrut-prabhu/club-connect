package systemtests;

import static seedu.club.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.club.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.club.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.club.logic.commands.CommandTestUtil.GROUP_DESC_AMY;
import static seedu.club.logic.commands.CommandTestUtil.GROUP_DESC_BOB;
import static seedu.club.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
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
import static seedu.club.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.club.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.club.logic.commands.CommandTestUtil.USERNAME_DESC_AMY;
import static seedu.club.logic.commands.CommandTestUtil.USERNAME_DESC_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.club.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_GROUP_AMY;
import static seedu.club.logic.commands.CommandTestUtil.VALID_MATRIC_NUMBER_AMY;
import static seedu.club.logic.commands.CommandTestUtil.VALID_MATRIC_NUMBER_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.club.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.club.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.club.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.club.testutil.TypicalMembers.ALICE;
import static seedu.club.testutil.TypicalMembers.AMY;
import static seedu.club.testutil.TypicalMembers.BOB;
import static seedu.club.testutil.TypicalMembers.CARL;
import static seedu.club.testutil.TypicalMembers.HOON;
import static seedu.club.testutil.TypicalMembers.IDA;
import static seedu.club.testutil.TypicalMembers.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.club.commons.core.Messages;
import seedu.club.commons.core.index.Index;
import seedu.club.logic.commands.AddCommand;
import seedu.club.logic.commands.RedoCommand;
import seedu.club.logic.commands.UndoCommand;
import seedu.club.model.Model;
import seedu.club.model.member.Email;
import seedu.club.model.member.MatricNumber;
import seedu.club.model.member.Member;
import seedu.club.model.member.Name;
import seedu.club.model.member.Phone;
import seedu.club.model.member.exceptions.DuplicateMemberException;
import seedu.club.model.tag.Tag;
import seedu.club.testutil.MemberBuilder;
import seedu.club.testutil.MemberUtil;

public class AddCommandSystemTest extends ClubBookSystemTest {

    @Test
    public void add() throws Exception {
        Model model = getModel();

        /* ------------------------ Perform add operations on the shown unfiltered list ----------------------------- */

        /* Case: add a member without tags to a non-empty club book, command with leading spaces and trailing spaces
         * -> added
         */
        Member toAdd = AMY;
        String command = "   " + AddCommand.COMMAND_WORD + "  " + NAME_DESC_AMY + "  " + PHONE_DESC_AMY + " "
                + EMAIL_DESC_AMY + "   " + MATRIC_NUMBER_DESC_AMY + "   "
                + GROUP_DESC_AMY + "   " + TAG_DESC_FRIEND + " " + " " + USERNAME_DESC_AMY + " " + PASSWORD_DESC;
        assertCommandSuccess(command, toAdd);

        /* Case: undo adding Amy to the list -> Amy deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding Amy to the list -> Amy added again */
        command = RedoCommand.COMMAND_WORD;
        model.addMember(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add a member with all fields same as another member in the club book except name -> added */
        toAdd = new MemberBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withMatricNumber(VALID_MATRIC_NUMBER_AMY).withGroup(VALID_GROUP_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_AMY + EMAIL_DESC_AMY + MATRIC_NUMBER_DESC_AMY
                + GROUP_DESC_AMY + TAG_DESC_FRIEND + USERNAME_DESC_BOB + PASSWORD_DESC;
        assertCommandSuccess(command, toAdd);

        /* Case: add a member with all fields same as another member in the club book except phone -> added */
        toAdd = new MemberBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY)
                .withMatricNumber(VALID_MATRIC_NUMBER_AMY).withGroup(VALID_GROUP_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_BOB + EMAIL_DESC_AMY + MATRIC_NUMBER_DESC_AMY
                + GROUP_DESC_AMY + TAG_DESC_FRIEND + USERNAME_DESC_AMY + PASSWORD_DESC;
        assertCommandSuccess(command, toAdd);

        /* Case: add a member with all fields same as another member in the club book except email -> added */
        toAdd = new MemberBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_BOB)
                .withMatricNumber(VALID_MATRIC_NUMBER_AMY).withGroup(VALID_GROUP_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_BOB + MATRIC_NUMBER_DESC_AMY
                + GROUP_DESC_AMY + TAG_DESC_FRIEND + USERNAME_DESC_AMY + PASSWORD_DESC;
        assertCommandSuccess(command, toAdd);

        /* Case: add a member with all fields same as another member in the club book
         * except matric number -> added
         */
        toAdd = new MemberBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withMatricNumber(VALID_MATRIC_NUMBER_BOB).withGroup(VALID_GROUP_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + MATRIC_NUMBER_DESC_BOB
                + GROUP_DESC_AMY + TAG_DESC_FRIEND + USERNAME_DESC_AMY + PASSWORD_DESC;
        assertCommandSuccess(command, toAdd);

        /* Case: add to empty club book -> added */
        deleteAllMembers();
        assertCommandSuccess(ALICE);

        /* Case: add a member with tags, command with parameters in random order -> added */
        toAdd = BOB;
        command = AddCommand.COMMAND_WORD + TAG_DESC_FRIEND + PHONE_DESC_BOB + MATRIC_NUMBER_DESC_BOB + NAME_DESC_BOB
                + GROUP_DESC_BOB + TAG_DESC_HUSBAND + EMAIL_DESC_BOB + USERNAME_DESC_BOB + PASSWORD_DESC;
        assertCommandSuccess(command, toAdd);

        /* Case: add a member, missing tags -> added */
        assertCommandSuccess(HOON);

        /* -------------------------- Perform add operation on the shown filtered list ------------------------------ */

        /* Case: filters the member list before adding -> added */
        showMembersWithName(KEYWORD_MATCHING_MEIER);
        assertCommandSuccess(IDA);

        /* ------------------------ Perform add operation while a member card is selected --------------------------- */

        /* Case: selects first card in the member list, add a member -> added, card selection remains unchanged */
        selectMember(Index.fromOneBased(1));
        assertCommandSuccess(CARL);

        /* ----------------------------------- Perform invalid add operations --------------------------------------- */

        /* Case: add a duplicate member -> rejected */
        command = MemberUtil.getAddCommand(HOON);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_MEMBER);

        /* Case: add a duplicate member except with different tags -> rejected */
        // "friends" is an existing tag used in the default model, see TypicalMembers#ALICE
        // This test will fail if a new tag that is not in the model is used, see the bug documented in
        // ClubBook#addMember(Member)
        command = MemberUtil.getAddCommand(HOON) + " " + PREFIX_TAG.getPrefix() + "friends";
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_MEMBER);

        /* Case: missing name -> rejected */
        command = AddCommand.COMMAND_WORD + PHONE_DESC_AMY + EMAIL_DESC_AMY + MATRIC_NUMBER_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing phone -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + EMAIL_DESC_AMY + MATRIC_NUMBER_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing email -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + MATRIC_NUMBER_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing matric number -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "adds " + MemberUtil.getMemberDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid name -> rejected */
        command = AddCommand.COMMAND_WORD + INVALID_NAME_DESC + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + MATRIC_NUMBER_DESC_AMY;
        assertCommandFailure(command, Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid phone -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + INVALID_PHONE_DESC
                + EMAIL_DESC_AMY + MATRIC_NUMBER_DESC_AMY;
        assertCommandFailure(command, Phone.MESSAGE_PHONE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + INVALID_EMAIL_DESC + MATRIC_NUMBER_DESC_AMY;
        assertCommandFailure(command, Email.MESSAGE_EMAIL_CONSTRAINTS);

        /* Case: invalid matric number -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + INVALID_MATRIC_NUMBER_DESC;
        assertCommandFailure(command, MatricNumber.MESSAGE_MATRIC_NUMBER_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + MATRIC_NUMBER_DESC_AMY
                + INVALID_TAG_DESC;
        assertCommandFailure(command, Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Model}, {@code Storage} and {@code MemberListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code ClubBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see ClubBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Member toAdd) {
        assertCommandSuccess(MemberUtil.getAddCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Member)}. Executes {@code command}
     * instead.
     * @see AddCommandSystemTest#assertCommandSuccess(Member)
     */
    private void assertCommandSuccess(String command, Member toAdd) {
        Model expectedModel = getModel();
        try {
            expectedModel.addMember(toAdd);
        } catch (DuplicateMemberException dpe) {
            throw new IllegalArgumentException("toAdd already exists in the model.");
        }
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS, toAdd);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Member)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Model}, {@code Storage} and {@code MemberListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see AddCommandSystemTest#assertCommandSuccess(String, Member)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage} and {@code MemberListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code ClubBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see ClubBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
