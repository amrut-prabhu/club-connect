package systemtests;

import static org.junit.Assert.assertEquals;
import static seedu.club.logic.commands.CommandTestUtil.GROUP_DESC_BOB;
import static seedu.club.logic.commands.CommandTestUtil.INVALID_GROUP;
import static seedu.club.logic.commands.CommandTestUtil.INVALID_GROUP_DESC;
import static seedu.club.logic.commands.CommandTestUtil.MANDATORY_GROUP;
import static seedu.club.logic.commands.CommandTestUtil.MANDATORY_GROUP_DESC;
import static seedu.club.logic.commands.CommandTestUtil.NON_EXISTENT_GROUP;
import static seedu.club.logic.commands.CommandTestUtil.NON_EXISTENT_GROUP_DESC;
import static seedu.club.logic.commands.CommandTestUtil.VALID_GROUP_BOB;
import static seedu.club.logic.commands.RemoveGroupCommand.MESSAGE_MANDATORY_GROUP;
import static seedu.club.logic.commands.RemoveGroupCommand.MESSAGE_NON_EXISTENT_GROUP;
import static seedu.club.logic.commands.RemoveGroupCommand.MESSAGE_SUCCESS;

import org.junit.Test;

import seedu.club.logic.commands.RedoCommand;
import seedu.club.logic.commands.RemoveGroupCommand;
import seedu.club.logic.commands.UndoCommand;
import seedu.club.model.Model;
import seedu.club.model.group.Group;
import seedu.club.model.group.exceptions.GroupCannotBeRemovedException;
import seedu.club.model.group.exceptions.GroupNotFoundException;

public class RemoveGroupCommandSystemTest extends ClubBookSystemTest {

    @Test
    public void removeGroup() {
        Model expectedModel = getModel();
        Model modelBeforeDeletingGroup = getModel();
        Group deletedGroup;
        String command;
        /* ------------------------ Perform removegroup operations on the shown unfiltered list -------------------- */

        /* Case: delete a valid group which is present in the club book */
        command = " " + RemoveGroupCommand.COMMAND_WORD + " " + GROUP_DESC_BOB + " ";
        deletedGroup = deleteGroup(expectedModel, VALID_GROUP_BOB);
        String expectedResultMessage = String.format(MESSAGE_SUCCESS, deletedGroup);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: undo deleting the group -> group restored in relevant members */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingGroup, expectedResultMessage);

        /*Case: redo deleting the group -> deleted */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: delete an invalid group */
        command = " " + RemoveGroupCommand.COMMAND_WORD + " " + INVALID_GROUP_DESC + " ";
        deletedGroup = deleteGroup(expectedModel, INVALID_GROUP);
        assertEquals(null, deletedGroup);
        assertCommandFailure(command, Group.MESSAGE_GROUP_CONSTRAINTS);

        /* Case: delete a mandatory group */
        command = " " + RemoveGroupCommand.COMMAND_WORD + " " + MANDATORY_GROUP_DESC + " ";
        deletedGroup = deleteGroup(expectedModel, MANDATORY_GROUP);
        assertEquals(null, deletedGroup);
        assertCommandFailure(command, MESSAGE_MANDATORY_GROUP);

        /* Case: delete a non-existent group */
        command = " " + RemoveGroupCommand.COMMAND_WORD + " " + NON_EXISTENT_GROUP_DESC + " ";
        deletedGroup = deleteGroup(expectedModel, NON_EXISTENT_GROUP);
        assertEquals(null, deletedGroup);
        assertCommandFailure(command, MESSAGE_NON_EXISTENT_GROUP);
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
     * Removes the group from model
     * @param model expected model
     * @param group new Group object to be created with this string
     * @return either a valid Group object if the group has been deleted; null otherwise
     */
    private Group deleteGroup(Model model, String group) {
        if (Group.isValidGroup(group)) {
            try {
                model.removeGroup(new Group(group));
            } catch (GroupNotFoundException gnfe) {
                return null;
            } catch (GroupCannotBeRemovedException e) {
                return null;
            }

            return new Group(group);
        }
        return null;
    }
}
