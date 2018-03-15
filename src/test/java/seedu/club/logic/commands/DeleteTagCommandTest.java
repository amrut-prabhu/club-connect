package seedu.club.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static seedu.club.logic.commands.CommandTestUtil.VALID_TAG_UNUSED;
import static seedu.club.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.club.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.club.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.club.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.club.logic.commands.CommandTestUtil.showMemberAtIndex;
import static seedu.club.logic.commands.DeleteTagCommand.MESSAGE_DELETE_TAG_SUCCESS;
import static seedu.club.logic.commands.DeleteTagCommand.MESSAGE_NON_EXISTENT_TAG;
import static seedu.club.testutil.TypicalIndexes.INDEX_FIRST_MEMBER;
import static seedu.club.testutil.TypicalIndexes.INDEX_FIRST_TAG;
import static seedu.club.testutil.TypicalIndexes.INDEX_SECOND_MEMBER;
import static seedu.club.testutil.TypicalIndexes.INDEX_SECOND_TAG;
import static seedu.club.testutil.TypicalMembers.getTypicalClubBook;

import org.junit.Test;

import seedu.club.logic.CommandHistory;
import seedu.club.logic.UndoRedoStack;
import seedu.club.model.Model;
import seedu.club.model.ModelManager;
import seedu.club.model.UserPrefs;
import seedu.club.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteTagCommand}.
 */
public class DeleteTagCommandTest {

    private Model model = new ModelManager(getTypicalClubBook(), new UserPrefs());

    @Test
    public void execute_validTagUnfilteredList_success() throws Exception {
        Tag tagToRemove = model.getFilteredTagList().get(INDEX_FIRST_TAG.getZeroBased());

        DeleteTagCommand deleteTagCommand = prepareCommand(tagToRemove);

        String expectedMessage = String.format(MESSAGE_DELETE_TAG_SUCCESS, tagToRemove);

        ModelManager expectedModel = new ModelManager(model.getClubBook(), new UserPrefs());
        expectedModel.deleteTag(tagToRemove);

        assertCommandSuccess(deleteTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidTagUnfilteredList_throwsCommandException() throws Exception {
        Tag nonExistentTag = new Tag(VALID_TAG_UNUSED);
        DeleteTagCommand deleteTagCommand = prepareCommand(nonExistentTag);

        assertCommandFailure(deleteTagCommand, model, MESSAGE_NON_EXISTENT_TAG);
    }

    @Test
    public void execute_validTagFilteredList_success() throws Exception {
        showMemberAtIndex(model, INDEX_FIRST_MEMBER);

        Tag tagToRemove = model.getFilteredTagList().get(INDEX_FIRST_TAG.getZeroBased());

        DeleteTagCommand deleteTagCommand = prepareCommand(tagToRemove);

        String expectedMessage = String.format(MESSAGE_DELETE_TAG_SUCCESS, tagToRemove);

        Model expectedModel = new ModelManager(model.getClubBook(), new UserPrefs());
        expectedModel.deleteTag(tagToRemove);
        showNoTag(expectedModel);

        assertCommandSuccess(deleteTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidTagFilteredList_throwsCommandException() {
        showMemberAtIndex(model, INDEX_FIRST_MEMBER);

        Tag nonExistentTag = new Tag(VALID_TAG_UNUSED);

        assertFalse(model.getClubBook().getTagList().contains(nonExistentTag));

        DeleteTagCommand deleteTagCommand = prepareCommand(nonExistentTag);

        assertCommandFailure(deleteTagCommand, model, MESSAGE_NON_EXISTENT_TAG);
    }

    @Test
    public void executeUndoRedo_validTagUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Tag tagToRemove = model.getFilteredTagList().get(INDEX_FIRST_TAG.getZeroBased());
        DeleteTagCommand deleteTagCommand = prepareCommand(tagToRemove);
        Model expectedModel = new ModelManager(model.getClubBook(), new UserPrefs());

        // delete -> first tag removed
        deleteTagCommand.execute();
        undoRedoStack.push(deleteTagCommand);

        // undo -> reverts addressbook back to previous state and filtered tag list to show all tags
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first tag removed again
        expectedModel.deleteTag(tagToRemove);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidTagUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Tag nonExistentTag = new Tag(VALID_TAG_UNUSED);
        DeleteTagCommand deleteTagCommand = prepareCommand(nonExistentTag);

        // execution failed -> deleteTagCommand not pushed into undoRedoStack
        assertCommandFailure(deleteTagCommand, model, MESSAGE_NON_EXISTENT_TAG);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Removes a {@code Tag} from a filtered list.
     * 2. Undo the removal.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted member in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} removes the tag object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validTagFilteredList_sameMemberDeleted() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Tag tagToRemove = model.getFilteredTagList().get(INDEX_FIRST_TAG.getZeroBased());
        DeleteTagCommand deleteTagCommand = prepareCommand(tagToRemove);
        Model expectedModel = new ModelManager(model.getClubBook(), new UserPrefs());

        showMemberAtIndex(model, INDEX_SECOND_MEMBER);
        // remove tag -> removes first tag in unfiltered tag list / filtered tag list
        deleteTagCommand.execute();
        undoRedoStack.push(deleteTagCommand);

        // undo -> reverts addressbook back to previous state and filtered tag list to show all tags
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.deleteTag(tagToRemove);
        // redo -> removes same tag in unfiltered tag list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        DeleteTagCommand removeFirstTagCommand = prepareCommand(model.getFilteredTagList()
                .get(INDEX_FIRST_TAG.getZeroBased()));
        DeleteTagCommand removeSecondTagCommand = prepareCommand(model.getFilteredTagList()
                .get(INDEX_SECOND_TAG.getZeroBased()));

        // same object -> returns true
        assertTrue(removeFirstTagCommand.equals(removeFirstTagCommand));

        // same values -> returns true
        DeleteTagCommand removeFirstTagCommandCopy = prepareCommand(model.getFilteredTagList()
                .get(INDEX_FIRST_TAG.getZeroBased()));
        assertTrue(removeFirstTagCommand.equals(removeFirstTagCommandCopy));

        // different types -> returns false
        assertFalse(removeFirstTagCommand.equals(1));

        // null -> returns false
        assertFalse(removeFirstTagCommand.equals(null));

        // different member -> returns fal

        // se
        assertFalse(removeFirstTagCommand.equals(removeSecondTagCommand));
    }

    /**
     * Returns a {@code DeleteTagCommand} with the parameter {@code tag}.
     */
    private DeleteTagCommand prepareCommand(Tag tag) {
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(tag);
        deleteTagCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteTagCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show no tags.
     */
    private void showNoTag(Model model) {
        model.updateFilteredTagList(p -> false);

        assertTrue(model.getFilteredTagList().isEmpty());
    }

}
