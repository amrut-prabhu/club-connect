package seedu.club.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.club.commons.core.Messages.MESSAGE_MEMBERS_LISTED_OVERVIEW;
import static seedu.club.testutil.TypicalMembers.ALICE;
import static seedu.club.testutil.TypicalMembers.BENSON;
import static seedu.club.testutil.TypicalMembers.CARL;
import static seedu.club.testutil.TypicalMembers.DANIEL;
import static seedu.club.testutil.TypicalMembers.ELLE;
import static seedu.club.testutil.TypicalMembers.FIONA;
import static seedu.club.testutil.TypicalMembers.GEORGE;
import static seedu.club.testutil.TypicalMembers.getTypicalClubBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.club.logic.CommandHistory;
import seedu.club.logic.UndoRedoStack;
import seedu.club.model.ClubBook;
import seedu.club.model.Model;
import seedu.club.model.ModelManager;
import seedu.club.model.UserPrefs;
import seedu.club.model.member.FieldContainsKeywordsPredicate;
import seedu.club.model.member.Member;

/**
 * Contains integration tests (interaction with the Model) for {@code FindByCommand}.
 */
public class FindByCommandTest {

    private static final String[] fieldTypes = {"name", "email", "phone", "matric", "tag", "group"};

    private Model model = new ModelManager(getTypicalClubBook(), new UserPrefs());

    @Test
    public void equals_nameFieldType() {
        FieldContainsKeywordsPredicate firstPredicate =
                new FieldContainsKeywordsPredicate(Collections.singletonList("first"), "name");
        FieldContainsKeywordsPredicate secondPredicate =
                new FieldContainsKeywordsPredicate(Collections.singletonList("second"), "name");

        FindByCommand findByFirstCommand = new FindByCommand(firstPredicate);
        FindByCommand findBySecondCommand = new FindByCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findByFirstCommand.equals(findByFirstCommand));

        // same values -> returns true
        FindByCommand findByFirstCommandCopy = new FindByCommand(firstPredicate);
        assertTrue(findByFirstCommand.equals(findByFirstCommandCopy));

        // different types -> returns false
        assertFalse(findByFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findByFirstCommand.equals(null));

        // different member -> returns false
        assertFalse(findByFirstCommand.equals(findBySecondCommand));
    }

    @Test
    public void equals_emailFieldType() {
        FieldContainsKeywordsPredicate firstPredicate =
                new FieldContainsKeywordsPredicate(Collections.singletonList("first"), "email");
        FieldContainsKeywordsPredicate secondPredicate =
                new FieldContainsKeywordsPredicate(Collections.singletonList("second"), "email");

        FindByCommand findByFirstCommand = new FindByCommand(firstPredicate);
        FindByCommand findBySecondCommand = new FindByCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findByFirstCommand.equals(findByFirstCommand));

        // same values -> returns true
        FindByCommand findByFirstCommandCopy = new FindByCommand(firstPredicate);
        assertTrue(findByFirstCommand.equals(findByFirstCommandCopy));

        // different types -> returns false
        assertFalse(findByFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findByFirstCommand.equals(null));

        // different member -> returns false
        assertFalse(findByFirstCommand.equals(findBySecondCommand));
    }

    @Test
    public void equals_phoneFieldType() {
        FieldContainsKeywordsPredicate firstPredicate =
                new FieldContainsKeywordsPredicate(Collections.singletonList("first"), "phone");
        FieldContainsKeywordsPredicate secondPredicate =
                new FieldContainsKeywordsPredicate(Collections.singletonList("second"), "phone");

        FindByCommand findByFirstCommand = new FindByCommand(firstPredicate);
        FindByCommand findBySecondCommand = new FindByCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findByFirstCommand.equals(findByFirstCommand));

        // same values -> returns true
        FindByCommand findByFirstCommandCopy = new FindByCommand(firstPredicate);
        assertTrue(findByFirstCommand.equals(findByFirstCommandCopy));

        // different types -> returns false
        assertFalse(findByFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findByFirstCommand.equals(null));

        // different member -> returns false
        assertFalse(findByFirstCommand.equals(findBySecondCommand));
    }

    @Test
    public void equals_matricNumberFieldType() {
        FieldContainsKeywordsPredicate firstPredicate =
                new FieldContainsKeywordsPredicate(Collections.singletonList("first"), "matric");
        FieldContainsKeywordsPredicate secondPredicate =
                new FieldContainsKeywordsPredicate(Collections.singletonList("second"), "matric");

        FindByCommand findByFirstCommand = new FindByCommand(firstPredicate);
        FindByCommand findBySecondCommand = new FindByCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findByFirstCommand.equals(findByFirstCommand));

        // same values -> returns true
        FindByCommand findByFirstCommandCopy = new FindByCommand(firstPredicate);
        assertTrue(findByFirstCommand.equals(findByFirstCommandCopy));

        // different types -> returns false
        assertFalse(findByFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findByFirstCommand.equals(null));

        // different member -> returns false
        assertFalse(findByFirstCommand.equals(findBySecondCommand));
    }

    @Test
    public void equals_groupFieldType() {
        FieldContainsKeywordsPredicate firstPredicate =
                new FieldContainsKeywordsPredicate(Collections.singletonList("first"), "group");
        FieldContainsKeywordsPredicate secondPredicate =
                new FieldContainsKeywordsPredicate(Collections.singletonList("second"), "group");

        FindByCommand findByFirstCommand = new FindByCommand(firstPredicate);
        FindByCommand findBySecondCommand = new FindByCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findByFirstCommand.equals(findByFirstCommand));

        // same values -> returns true
        FindByCommand findByFirstCommandCopy = new FindByCommand(firstPredicate);
        assertTrue(findByFirstCommand.equals(findByFirstCommandCopy));

        // different types -> returns false
        assertFalse(findByFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findByFirstCommand.equals(null));

        // different member -> returns false
        assertFalse(findByFirstCommand.equals(findBySecondCommand));
    }

    @Test
    public void equals_tagFieldType() {
        FieldContainsKeywordsPredicate firstPredicate =
                new FieldContainsKeywordsPredicate(Collections.singletonList("first"), "tag");
        FieldContainsKeywordsPredicate secondPredicate =
                new FieldContainsKeywordsPredicate(Collections.singletonList("second"), "tag");

        FindByCommand findByFirstCommand = new FindByCommand(firstPredicate);
        FindByCommand findBySecondCommand = new FindByCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findByFirstCommand.equals(findByFirstCommand));

        // same values -> returns true
        FindByCommand findByFirstCommandCopy = new FindByCommand(firstPredicate);
        assertTrue(findByFirstCommand.equals(findByFirstCommandCopy));

        // different types -> returns false
        assertFalse(findByFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findByFirstCommand.equals(null));

        // different member -> returns false
        assertFalse(findByFirstCommand.equals(findBySecondCommand));
    }

    @Test
    public void equals_differentFieldType_returnFalse() {
        String[] fieldTypes = {"name", "email", "phone", "matric", "tag", "group"};
        FindByCommand[] commands = new FindByCommand[fieldTypes.length];

        for (int index = 0; index < fieldTypes.length; index++) {
            commands[index] =
                    new FindByCommand(
                            new FieldContainsKeywordsPredicate(Collections.singletonList("first"), fieldTypes[index]));
        }

        // Check across all pairs
        for (int i = 0; i < commands.length; i++) {
            for (int j = 0; j < commands.length; j++) {
                if (i != j) {
                    assertFalse(commands[i].equals(commands[j]));
                }
            }
        }

    }

    @Test
    public void execute_zeroKeywords_noMemberFound() {
        String expectedMessage = String.format(MESSAGE_MEMBERS_LISTED_OVERVIEW, 0);
        for (String fieldType: fieldTypes) {
            assertCommandSuccess(prepareCommand(" ", fieldType), expectedMessage, Collections.emptyList());
        }
    }

    @Test
    public void execute_multipleKeywords_multipleMembersFound() {
        String expectedMessage = String.format(MESSAGE_MEMBERS_LISTED_OVERVIEW, 3);
        FindByCommand command = prepareCommand("Kurz Elle Kunz", "name");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, ELLE, FIONA));

        command = prepareCommand("95352563 9482224 9482427", "phone");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, ELLE, FIONA));

        command = prepareCommand("heinz@example.com werner@example.com lydia@example.com", "email");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, ELLE, FIONA));

        command = prepareCommand("A6076201A A1932279G A9662042H", "matric");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, ELLE, FIONA));

        command = prepareCommand("marketing operations", "group");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, ELLE, FIONA));

        expectedMessage = String.format(MESSAGE_MEMBERS_LISTED_OVERVIEW, 7);
        command = prepareCommand("friend friends owesMoney", "tag");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON, CARL,
                DANIEL, ELLE, FIONA, GEORGE));
    }

    /**
     * Parses {@code userInput} and {@code fieldType} into a {@code FindByCommand}.
     */
    private FindByCommand prepareCommand(String userInput, String fieldType) {
        FindByCommand command =
                new FindByCommand(new FieldContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")),
                        fieldType));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Member>} is equal to {@code expectedList}<br>
     *     - the {@code ClubBook} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindByCommand command, String expectedMessage, List<Member> expectedList) {
        ClubBook expectedClubBook = new ClubBook(model.getClubBook());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredMemberList());
        assertEquals(expectedClubBook, model.getClubBook());
    }
}
