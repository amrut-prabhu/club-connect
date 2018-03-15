package systemtests;

import static org.junit.Assert.assertEquals;
import static seedu.club.logic.commands.CommandTestUtil.INVALID_TAG;
import static seedu.club.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.club.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.club.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.club.logic.commands.CommandTestUtil.VALID_TAG_UNUSED;
import static seedu.club.logic.commands.CommandTestUtil.VALID_TAG_UNUSED_DESC;
import static seedu.club.logic.commands.DeleteTagCommand.MESSAGE_DELETE_TAG_SUCCESS;
import static seedu.club.logic.commands.DeleteTagCommand.MESSAGE_NON_EXISTENT_TAG;

import org.junit.Test;

import seedu.club.logic.commands.DeleteTagCommand;
import seedu.club.logic.commands.RedoCommand;
import seedu.club.logic.commands.UndoCommand;
import seedu.club.model.Model;
import seedu.club.model.tag.Tag;
import seedu.club.model.tag.exceptions.TagNotFoundException;

public class DeleteTagCommandSystemTest extends ClubBookSystemTest {

    @Test
    public void deleteTag() {
        Model expectedModel = getModel();
        Model modelBeforeDeletingGroup = getModel();
        Tag deletedTag;
        String command;

        /* ------------------------ Perform deleteTag operations on the shown unfiltered list -------------------- */

        /* Case: delete a valid tag which is present in the club book */
        command = DeleteTagCommand.COMMAND_WORD + TAG_DESC_FRIEND;
        deletedTag = deleteTagFromModel(expectedModel, VALID_TAG_FRIEND);
        String expectedResultMessage = String.format(MESSAGE_DELETE_TAG_SUCCESS, deletedTag);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: undo deleting the tag -> tag restored in relevant members */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingGroup, expectedResultMessage);

        /*Case: redo deleting the tag -> deleted */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: delete an invalid tag */
        command = DeleteTagCommand.COMMAND_WORD + INVALID_TAG_DESC;
        deletedTag = deleteTagFromModel(expectedModel, INVALID_TAG);
        assertEquals(null, deletedTag);
        assertCommandFailure(command, Tag.MESSAGE_TAG_CONSTRAINTS);

        /* Case: delete a non-existent tag */
        command = DeleteTagCommand.COMMAND_WORD + VALID_TAG_UNUSED_DESC;
        deletedTag = deleteTagFromModel(expectedModel, VALID_TAG_UNUSED);
        assertEquals(null, deletedTag);
        assertCommandFailure(command, MESSAGE_NON_EXISTENT_TAG);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code ClubBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see ClubBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model model, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, model);

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the command box has the error style.<br>
     * Verifications 1 to 3 are performed by
     * {@code ClubBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see ClubBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        executeCommand(command);
        Model expectedModel = getModel();
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }


    /**
     * Removes the tag from model
     * @param model expected model
     * @param tag new Tag object to be created with this string
     * @return either a valid Tag object if the group has been deleted; null otherwise
     */
    private Tag deleteTagFromModel(Model model, String tag) {
        if (Tag.isValidTagName(tag)) {
            try {
                model.deleteTag(new Tag(tag));
            } catch (TagNotFoundException tnfe) {
                return null;
            }
            return new Tag(tag);
        }
        return null;
    }
}
