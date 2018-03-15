package seedu.club.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static seedu.club.logic.commands.CommandTestUtil.deleteFirstMember;
import static seedu.club.logic.commands.CommandTestUtil.showMemberAtIndex;
import static seedu.club.testutil.TypicalIndexes.INDEX_FIRST_MEMBER;
import static seedu.club.testutil.TypicalMembers.getTypicalClubBook;

import org.junit.Test;

import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.Model;
import seedu.club.model.ModelManager;
import seedu.club.model.UserPrefs;
import seedu.club.model.member.Member;
import seedu.club.model.member.exceptions.MemberNotFoundException;

public class UndoableCommandTest {
    private final Model model = new ModelManager(getTypicalClubBook(), new UserPrefs());
    private final DummyCommand dummyCommand = new DummyCommand(model);

    private Model expectedModel = new ModelManager(getTypicalClubBook(), new UserPrefs());

    @Test
    public void executeUndo() throws Exception {
        dummyCommand.execute();
        deleteFirstMember(expectedModel);
        assertEquals(expectedModel, model);

        showMemberAtIndex(model, INDEX_FIRST_MEMBER);

        // undo() should cause the model's filtered list to show all members
        dummyCommand.undo();
        expectedModel = new ModelManager(getTypicalClubBook(), new UserPrefs());
        assertEquals(expectedModel, model);
    }

    @Test
    public void redo() {
        showMemberAtIndex(model, INDEX_FIRST_MEMBER);

        // redo() should cause the model's filtered list to show all members
        dummyCommand.redo();
        deleteFirstMember(expectedModel);
        assertEquals(expectedModel, model);
    }

    /**
     * Deletes the first member in the model's filtered list.
     */
    class DummyCommand extends UndoableCommand {
        DummyCommand(Model model) {
            this.model = model;
        }

        @Override
        public CommandResult executeUndoableCommand() throws CommandException {
            Member memberToDelete = model.getFilteredMemberList().get(0);
            try {
                model.deleteMember(memberToDelete);
            } catch (MemberNotFoundException pnfe) {
                fail("Impossible: memberToDelete was retrieved from model.");
            }
            return new CommandResult("");
        }
    }
}
