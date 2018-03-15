package seedu.club.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.club.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.club.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.club.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.club.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.club.logic.commands.CommandTestUtil.showMemberAtIndex;
import static seedu.club.testutil.TypicalIndexes.INDEX_FIRST_MEMBER;
import static seedu.club.testutil.TypicalIndexes.INDEX_SECOND_MEMBER;
import static seedu.club.testutil.TypicalMembers.getTypicalClubBook;

import org.junit.Test;

import seedu.club.commons.core.Messages;
import seedu.club.commons.core.index.Index;
import seedu.club.logic.CommandHistory;
import seedu.club.logic.UndoRedoStack;
import seedu.club.model.Model;
import seedu.club.model.ModelManager;
import seedu.club.model.UserPrefs;
import seedu.club.model.member.Member;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalClubBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        Member memberToDelete = model.getFilteredMemberList().get(INDEX_FIRST_MEMBER.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_MEMBER);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_MEMBER_SUCCESS, memberToDelete);

        ModelManager expectedModel = new ModelManager(model.getClubBook(), new UserPrefs());
        expectedModel.deleteMember(memberToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredMemberList().size() + 1);
        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showMemberAtIndex(model, INDEX_FIRST_MEMBER);

        Member memberToDelete = model.getFilteredMemberList().get(INDEX_FIRST_MEMBER.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_MEMBER);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_MEMBER_SUCCESS, memberToDelete);

        Model expectedModel = new ModelManager(model.getClubBook(), new UserPrefs());
        expectedModel.deleteMember(memberToDelete);
        showNoMember(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showMemberAtIndex(model, INDEX_FIRST_MEMBER);

        Index outOfBoundIndex = INDEX_SECOND_MEMBER;
        // ensures that outOfBoundIndex is still in bounds of club book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getClubBook().getMemberList().size());

        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Member memberToDelete = model.getFilteredMemberList().get(INDEX_FIRST_MEMBER.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_MEMBER);
        Model expectedModel = new ModelManager(model.getClubBook(), new UserPrefs());

        // delete -> first member deleted
        deleteCommand.execute();
        undoRedoStack.push(deleteCommand);

        // undo -> reverts addressbook back to previous state and filtered member list to show all members
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first member deleted again
        expectedModel.deleteMember(memberToDelete);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredMemberList().size() + 1);
        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        // execution failed -> deleteCommand not pushed into undoRedoStack
        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Deletes a {@code Member} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted member in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the member object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameMemberDeleted() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_MEMBER);
        Model expectedModel = new ModelManager(model.getClubBook(), new UserPrefs());

        showMemberAtIndex(model, INDEX_SECOND_MEMBER);
        Member memberToDelete = model.getFilteredMemberList().get(INDEX_FIRST_MEMBER.getZeroBased());
        // delete -> deletes second member in unfiltered member list / first member in filtered member list
        deleteCommand.execute();
        undoRedoStack.push(deleteCommand);

        // undo -> reverts addressbook back to previous state and filtered member list to show all members
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.deleteMember(memberToDelete);
        assertNotEquals(memberToDelete, model.getFilteredMemberList().get(INDEX_FIRST_MEMBER.getZeroBased()));
        // redo -> deletes same second member in unfiltered member list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        DeleteCommand deleteFirstCommand = prepareCommand(INDEX_FIRST_MEMBER);
        DeleteCommand deleteSecondCommand = prepareCommand(INDEX_SECOND_MEMBER);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = prepareCommand(INDEX_FIRST_MEMBER);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // one command preprocessed when previously equal -> returns false
        deleteFirstCommandCopy.preprocessUndoableCommand();
        assertFalse(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different member -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private DeleteCommand prepareCommand(Index index) {
        DeleteCommand deleteCommand = new DeleteCommand(index);
        deleteCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoMember(Model model) {
        model.updateFilteredMemberList(p -> false);

        assertTrue(model.getFilteredMemberList().isEmpty());
    }
}
