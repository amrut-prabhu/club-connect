package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.club.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.club.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.club.logic.commands.CommandTestUtil.GROUP_DESC_AMY;
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
import static seedu.club.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.club.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.club.logic.commands.CommandTestUtil.USERNAME_DESC_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_GROUP_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_MATRIC_NUMBER_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.club.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.club.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.club.model.Model.PREDICATE_SHOW_ALL_MEMBERS;
import static seedu.club.testutil.TypicalIndexes.INDEX_FIRST_MEMBER;
import static seedu.club.testutil.TypicalMembers.AMY;
import static seedu.club.testutil.TypicalMembers.BOB;
import static seedu.club.testutil.TypicalMembers.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.club.commons.core.Messages;
import seedu.club.commons.core.index.Index;
import seedu.club.logic.commands.EditCommand;
import seedu.club.logic.commands.RedoCommand;
import seedu.club.logic.commands.UndoCommand;
import seedu.club.model.Model;
import seedu.club.model.group.Group;
import seedu.club.model.member.Email;
import seedu.club.model.member.MatricNumber;
import seedu.club.model.member.Member;
import seedu.club.model.member.Name;
import seedu.club.model.member.Phone;
import seedu.club.model.member.exceptions.DuplicateMemberException;
import seedu.club.model.member.exceptions.MemberNotFoundException;
import seedu.club.model.tag.Tag;
import seedu.club.testutil.MemberBuilder;
import seedu.club.testutil.MemberUtil;

public class EditCommandSystemTest extends ClubBookSystemTest {

    @Test
    public void edit() throws Exception {
        Model model = getModel();

        /* ----------------- Performing edit operation while an unfiltered list is being shown ---------------------- */

        /* Case: edit all fields, command with leading spaces, trailing spaces and multiple spaces between each field
         * -> edited
         */
        Index index = INDEX_FIRST_MEMBER;
        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  " + NAME_DESC_BOB + "  "
                + PHONE_DESC_BOB + " " + EMAIL_DESC_BOB + "  " + MATRIC_NUMBER_DESC_BOB + " "
                + GROUP_DESC_BOB + " " + TAG_DESC_HUSBAND + " " + USERNAME_DESC_BOB + " " + PASSWORD_DESC;
        Member editedMember = new MemberBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withMatricNumber(VALID_MATRIC_NUMBER_BOB).withGroup(VALID_GROUP_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        assertCommandSuccess(command, index, editedMember);

        /* Case: undo editing the last member in the list -> last member restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo editing the last member in the list -> last member edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.updateMember(
                getModel().getFilteredMemberList().get(INDEX_FIRST_MEMBER.getZeroBased()), editedMember);
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: edit a member with new values same as existing values -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + MATRIC_NUMBER_DESC_BOB + GROUP_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        assertCommandSuccess(command, index, BOB);

        /* Case: edit some fields -> edited */
        index = INDEX_FIRST_MEMBER;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + TAG_DESC_FRIEND;
        Member memberToEdit = getModel().getFilteredMemberList().get(index.getZeroBased());
        editedMember = new MemberBuilder(memberToEdit).withTags(VALID_TAG_FRIEND).build();
        assertCommandSuccess(command, index, editedMember);

        /* Case: clear tags -> cleared */
        index = INDEX_FIRST_MEMBER;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + PREFIX_TAG.getPrefix();
        editedMember = new MemberBuilder(memberToEdit).withTags().build();
        assertCommandSuccess(command, index, editedMember);

        /* ------------------ Performing edit operation while a filtered list is being shown ------------------------ */

        /* Case: filtered member list, edit index within bounds of club book and member list -> edited */
        showMembersWithName(KEYWORD_MATCHING_MEIER);
        index = INDEX_FIRST_MEMBER;
        assertTrue(index.getZeroBased() < getModel().getFilteredMemberList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + NAME_DESC_BOB;
        memberToEdit = getModel().getFilteredMemberList().get(index.getZeroBased());
        editedMember = new MemberBuilder(memberToEdit).withName(VALID_NAME_BOB).build();
        assertCommandSuccess(command, index, editedMember);

        /* Case: filtered member list, edit index within bounds of club book but out of bounds of member list
         * -> rejected
         */
        showMembersWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getClubBook().getMemberList().size();
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB,
                Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);

        /* --------------------- Performing edit operation while a member card is selected -------------------------- */

        /* Case: selects first card in the member list, edit a member -> edited, card selection remains unchanged but
         * browser url changes
         */
        showAllMembers();
        index = INDEX_FIRST_MEMBER;
        selectMember(index);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + MATRIC_NUMBER_DESC_AMY + GROUP_DESC_AMY + TAG_DESC_FRIEND;
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new member's name
        assertCommandSuccess(command, index, AMY, index);

        /* --------------------------------- Performing invalid edit operation -------------------------------------- */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredMemberList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB,
                Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_MEMBER.getOneBased(),
                EditCommand.MESSAGE_NOT_EDITED);

        /* Case: invalid name -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_MEMBER.getOneBased() + INVALID_NAME_DESC,
                Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid phone -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_MEMBER.getOneBased() + INVALID_PHONE_DESC,
                Phone.MESSAGE_PHONE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_MEMBER.getOneBased() + INVALID_EMAIL_DESC,
                Email.MESSAGE_EMAIL_CONSTRAINTS);

        /* Case: invalid matric number -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_MEMBER.getOneBased()
                        + INVALID_MATRIC_NUMBER_DESC,
                MatricNumber.MESSAGE_MATRIC_NUMBER_CONSTRAINTS);

        /* Case: invalid group -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_MEMBER.getOneBased() + INVALID_GROUP_DESC,
                Group.MESSAGE_GROUP_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_MEMBER.getOneBased() + INVALID_TAG_DESC,
                Tag.MESSAGE_TAG_CONSTRAINTS);

        /* Case: edit a member with new values same as another member's values -> rejected */
        executeCommand(MemberUtil.getAddCommand(BOB));
        assertTrue(getModel().getClubBook().getMemberList().contains(BOB));
        index = INDEX_FIRST_MEMBER;
        assertFalse(getModel().getFilteredMemberList().get(index.getZeroBased()).equals(BOB));
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + MATRIC_NUMBER_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_MEMBER);

        /* Case: edit a member with new values same as another member's values but with different tags -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + MATRIC_NUMBER_DESC_BOB + TAG_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_MEMBER);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, Member, Index)} except that
     * the browser url and selected card remain unchanged.
     *
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Member, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Member editedMember) {
        assertCommandSuccess(command, toEdit, editedMember, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the member at index {@code toEdit} being
     * updated to values specified {@code editedMember}.<br>
     *
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Member editedMember,
                                      Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        try {
            expectedModel.updateMember(
                    expectedModel.getFilteredMemberList().get(toEdit.getZeroBased()), editedMember);
            expectedModel.updateFilteredMemberList(PREDICATE_SHOW_ALL_MEMBERS);
        } catch (DuplicateMemberException | MemberNotFoundException e) {
            throw new IllegalArgumentException(
                    "editedMember is a duplicate in expectedModel, or it isn't found in the model.");
        }

        assertCommandSuccess(command, expectedModel,
                String.format(EditCommand.MESSAGE_EDIT_MEMBER_SUCCESS, editedMember), expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     *
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card update accordingly depending on the card at
     * {@code expectedSelectedCardIndex}.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code ClubBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see ClubBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see ClubBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
                                      Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredMemberList(PREDICATE_SHOW_ALL_MEMBERS);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 5. Asserts that the command box has the error style.<br>
     * Verifications 1 to 3 are performed by
     * {@code ClubBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
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
