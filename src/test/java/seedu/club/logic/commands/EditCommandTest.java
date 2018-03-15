package seedu.club.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.club.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.club.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.club.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
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
import seedu.club.model.ClubBook;
import seedu.club.model.Model;
import seedu.club.model.ModelManager;
import seedu.club.model.UserPrefs;
import seedu.club.model.member.Member;
import seedu.club.testutil.EditMemberDescriptorBuilder;
import seedu.club.testutil.MemberBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalClubBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Member editedMember = new MemberBuilder().build();
        EditCommand.EditMemberDescriptor descriptor = new EditMemberDescriptorBuilder(editedMember).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_MEMBER, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_MEMBER_SUCCESS, editedMember);

        Model expectedModel = new ModelManager(new ClubBook(model.getClubBook()), new UserPrefs());
        expectedModel.updateMember(model.getFilteredMemberList().get(0), editedMember);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastMember = Index.fromOneBased(model.getFilteredMemberList().size());
        Member lastMember = model.getFilteredMemberList().get(indexLastMember.getZeroBased());

        MemberBuilder memberInList = new MemberBuilder(lastMember);
        Member editedMember = memberInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditCommand.EditMemberDescriptor descriptor = new EditMemberDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = prepareCommand(indexLastMember, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_MEMBER_SUCCESS, editedMember);

        Model expectedModel = new ModelManager(new ClubBook(model.getClubBook()), new UserPrefs());
        expectedModel.updateMember(lastMember, editedMember);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = prepareCommand(INDEX_FIRST_MEMBER, new EditCommand.EditMemberDescriptor());
        Member editedMember = model.getFilteredMemberList().get(INDEX_FIRST_MEMBER.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_MEMBER_SUCCESS, editedMember);

        Model expectedModel = new ModelManager(new ClubBook(model.getClubBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showMemberAtIndex(model, INDEX_FIRST_MEMBER);

        Member memberInFilteredList = model.getFilteredMemberList().get(INDEX_FIRST_MEMBER.getZeroBased());
        Member editedMember = new MemberBuilder(memberInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_MEMBER,
                new EditMemberDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_MEMBER_SUCCESS, editedMember);

        Model expectedModel = new ModelManager(new ClubBook(model.getClubBook()), new UserPrefs());
        expectedModel.updateMember(model.getFilteredMemberList().get(0), editedMember);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateMemberUnfilteredList_failure() {
        Member firstMember = model.getFilteredMemberList().get(INDEX_FIRST_MEMBER.getZeroBased());
        EditCommand.EditMemberDescriptor descriptor = new EditMemberDescriptorBuilder(firstMember).build();
        EditCommand editCommand = prepareCommand(INDEX_SECOND_MEMBER, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_MEMBER);
    }

    @Test
    public void execute_duplicateMemberFilteredList_failure() {
        showMemberAtIndex(model, INDEX_FIRST_MEMBER);

        // edit member in filtered list into a duplicate in club book
        Member memberInList = model.getClubBook().getMemberList().get(INDEX_SECOND_MEMBER.getZeroBased());
        EditCommand editCommand = prepareCommand(INDEX_FIRST_MEMBER,
                new EditMemberDescriptorBuilder(memberInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_MEMBER);
    }

    @Test
    public void execute_invalidMemberIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredMemberList().size() + 1);
        EditCommand.EditMemberDescriptor descriptor = new EditMemberDescriptorBuilder()
                .withName(VALID_NAME_BOB).build();
        EditCommand editCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of club book
     */
    @Test
    public void execute_invalidMemberIndexFilteredList_failure() {
        showMemberAtIndex(model, INDEX_FIRST_MEMBER);
        Index outOfBoundIndex = INDEX_SECOND_MEMBER;
        // ensures that outOfBoundIndex is still in bounds of club book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getClubBook().getMemberList().size());

        EditCommand editCommand = prepareCommand(outOfBoundIndex,
                new EditMemberDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Member editedMember = new MemberBuilder().build();
        Member memberToEdit = model.getFilteredMemberList().get(INDEX_FIRST_MEMBER.getZeroBased());
        EditCommand.EditMemberDescriptor descriptor = new EditMemberDescriptorBuilder(editedMember).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_MEMBER, descriptor);
        Model expectedModel = new ModelManager(new ClubBook(model.getClubBook()), new UserPrefs());

        // edit -> first member edited
        editCommand.execute();
        undoRedoStack.push(editCommand);

        // undo -> reverts addressbook back to previous state and filtered member list to show all members
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first member edited again
        expectedModel.updateMember(memberToEdit, editedMember);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredMemberList().size() + 1);
        EditCommand.EditMemberDescriptor descriptor = new EditMemberDescriptorBuilder()
                .withName(VALID_NAME_BOB).build();
        EditCommand editCommand = prepareCommand(outOfBoundIndex, descriptor);

        // execution failed -> editCommand not pushed into undoRedoStack
        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code Member} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited member in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the member object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameMemberEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Member editedMember = new MemberBuilder().build();
        EditCommand.EditMemberDescriptor descriptor = new EditMemberDescriptorBuilder(editedMember).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_MEMBER, descriptor);
        Model expectedModel = new ModelManager(new ClubBook(model.getClubBook()), new UserPrefs());

        showMemberAtIndex(model, INDEX_SECOND_MEMBER);
        Member memberToEdit = model.getFilteredMemberList().get(INDEX_FIRST_MEMBER.getZeroBased());
        // edit -> edits second member in unfiltered member list / first member in filtered member list
        editCommand.execute();
        undoRedoStack.push(editCommand);

        // undo -> reverts addressbook back to previous state and filtered member list to show all members
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.updateMember(memberToEdit, editedMember);
        assertNotEquals(model.getFilteredMemberList().get(INDEX_FIRST_MEMBER.getZeroBased()), memberToEdit);
        // redo -> edits same second member in unfiltered member list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        final EditCommand standardCommand = prepareCommand(INDEX_FIRST_MEMBER, DESC_AMY);

        // same values -> returns true
        EditCommand.EditMemberDescriptor copyDescriptor = new EditCommand.EditMemberDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = prepareCommand(INDEX_FIRST_MEMBER, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_MEMBER, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_MEMBER, DESC_BOB)));
    }

    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private EditCommand prepareCommand(Index index, EditCommand.EditMemberDescriptor descriptor) {
        EditCommand editCommand = new EditCommand(index, descriptor);
        editCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editCommand;
    }
}
