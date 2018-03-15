package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.club.commons.core.Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX;
import static seedu.club.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.club.logic.commands.DeleteCommand.MESSAGE_DELETE_MEMBER_SUCCESS;
import static seedu.club.testutil.TestUtil.getLastIndex;
import static seedu.club.testutil.TestUtil.getMember;
import static seedu.club.testutil.TestUtil.getMidIndex;
import static seedu.club.testutil.TypicalIndexes.INDEX_FIRST_MEMBER;
import static seedu.club.testutil.TypicalMembers.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.club.commons.core.Messages;
import seedu.club.commons.core.index.Index;
import seedu.club.logic.commands.DeleteCommand;
import seedu.club.logic.commands.RedoCommand;
import seedu.club.logic.commands.UndoCommand;
import seedu.club.model.Model;
import seedu.club.model.member.Member;
import seedu.club.model.member.exceptions.MemberNotFoundException;

public class DeleteCommandSystemTest extends ClubBookSystemTest {

    private static final String MESSAGE_INVALID_DELETE_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);

    @Test
    public void delete() {
        /* ----------------- Performing delete operation while an unfiltered list is being shown -------------------- */

        /* Case: delete the first member in the list, command with leading spaces and trailing spaces -> deleted */
        Model expectedModel = getModel();
        String command = "     " + DeleteCommand.COMMAND_WORD + "      " + INDEX_FIRST_MEMBER.getOneBased() + "       ";
        Member deletedMember = removeMember(expectedModel, INDEX_FIRST_MEMBER);
        String expectedResultMessage = String.format(MESSAGE_DELETE_MEMBER_SUCCESS, deletedMember);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: delete the last member in the list -> deleted */
        Model modelBeforeDeletingLast = getModel();
        Index lastMemberIndex = getLastIndex(modelBeforeDeletingLast);
        assertCommandSuccess(lastMemberIndex);

        /* Case: undo deleting the last member in the list -> last member restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* Case: redo deleting the last member in the list -> last member deleted again */
        command = RedoCommand.COMMAND_WORD;
        removeMember(modelBeforeDeletingLast, lastMemberIndex);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* Case: delete the middle member in the list -> deleted */
        Index middleMemberIndex = getMidIndex(getModel());
        assertCommandSuccess(middleMemberIndex);

        /* ------------------ Performing delete operation while a filtered list is being shown ---------------------- */

        /* Case: filtered member list, delete index within bounds of club book and member list -> deleted */
        showMembersWithName(KEYWORD_MATCHING_MEIER);
        Index index = INDEX_FIRST_MEMBER;
        assertTrue(index.getZeroBased() < getModel().getFilteredMemberList().size());
        assertCommandSuccess(index);

        /* Case: filtered member list, delete index within bounds of club book but out of bounds of member list
         * -> rejected
         */
        showMembersWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getClubBook().getMemberList().size();
        command = DeleteCommand.COMMAND_WORD + " " + invalidIndex;
        assertCommandFailure(command, MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);

        /* --------------------- Performing delete operation while a member card is selected ------------------------ */

        /* Case: delete the selected member -> member list panel selects the member before the deleted member */
        showAllMembers();
        expectedModel = getModel();
        Index selectedIndex = getLastIndex(expectedModel);
        Index expectedIndex = Index.fromZeroBased(selectedIndex.getZeroBased() - 1);
        selectMember(selectedIndex);
        command = DeleteCommand.COMMAND_WORD + " " + selectedIndex.getOneBased();
        deletedMember = removeMember(expectedModel, selectedIndex);
        expectedResultMessage = String.format(MESSAGE_DELETE_MEMBER_SUCCESS, deletedMember);
        assertCommandSuccess(command, expectedModel, expectedResultMessage, expectedIndex);

        /* --------------------------------- Performing invalid delete operation ------------------------------------ */

        /* Case: invalid index (0) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " 0";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -1";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getClubBook().getMemberList().size() + 1);
        command = DeleteCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " abc", MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " 1 abc", MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("DelETE 1", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Removes the {@code Member} at the specified {@code index} in {@code model}'s club book.
     * @return the removed member
     */
    private Member removeMember(Model model, Index index) {
        Member targetMember = getMember(model, index);
        try {
            model.deleteMember(targetMember);
        } catch (MemberNotFoundException pnfe) {
            throw new AssertionError("targetMember is retrieved from model.");
        }
        return targetMember;
    }

    /**
     * Deletes the member at {@code toDelete} by creating a default {@code DeleteCommand} using {@code toDelete} and
     * performs the same verification as {@code assertCommandSuccess(String, Model, String)}.
     * @see DeleteCommandSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertCommandSuccess(Index toDelete) {
        Model expectedModel = getModel();
        Member deletedMember = removeMember(expectedModel, toDelete);
        String expectedResultMessage = String.format(MESSAGE_DELETE_MEMBER_SUCCESS, deletedMember);

        assertCommandSuccess(
                DeleteCommand.COMMAND_WORD + " " + toDelete.getOneBased(), expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card remains unchanged.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code ClubBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see ClubBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String)} except that the browser url
     * and selected card are expected to update accordingly depending on the card at {@code expectedSelectedCardIndex}.
     * @see DeleteCommandSystemTest#assertCommandSuccess(String, Model, String)
     * @see ClubBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
            Index expectedSelectedCardIndex) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }

        assertCommandBoxShowsDefaultStyle();
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
