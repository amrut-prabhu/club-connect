package seedu.club.logic.commands;

import static seedu.club.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.club.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.club.testutil.TypicalMembers.getTypicalClubBook;

import org.junit.Before;
import org.junit.Test;

import seedu.club.logic.CommandHistory;
import seedu.club.logic.UndoRedoStack;
import seedu.club.model.Model;
import seedu.club.model.ModelManager;
import seedu.club.model.UserPrefs;
import seedu.club.model.member.Member;
import seedu.club.testutil.MemberBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalClubBook(), new UserPrefs());
    }

    @Test
    public void execute_newMember_success() throws Exception {
        Member validMember = new MemberBuilder().build();

        Model expectedModel = new ModelManager(model.getClubBook(), new UserPrefs());
        expectedModel.addMember(validMember);

        assertCommandSuccess(prepareCommand(validMember, model), model,
                String.format(AddCommand.MESSAGE_SUCCESS, validMember), expectedModel);
    }

    @Test
    public void execute_duplicateMember_throwsCommandException() {
        Member memberInList = model.getClubBook().getMemberList().get(0);
        assertCommandFailure(prepareCommand(memberInList, model), model, AddCommand.MESSAGE_DUPLICATE_MEMBER);
    }

    /**
     * Generates a new {@code AddCommand} which upon execution, adds {@code member} into the {@code model}.
     */
    private AddCommand prepareCommand(Member member, Model model) {
        AddCommand command = new AddCommand(member);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
