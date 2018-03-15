package seedu.club.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.club.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.club.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.club.logic.parser.CliSyntax.PREFIX_MATRIC_NUMBER;
import static seedu.club.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.club.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.club.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.club.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.club.logic.parser.CliSyntax.PREFIX_USERNAME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.club.commons.core.index.Index;
import seedu.club.logic.CommandHistory;
import seedu.club.logic.UndoRedoStack;
import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.ClubBook;
import seedu.club.model.Model;
import seedu.club.model.member.Member;
import seedu.club.model.member.NameContainsKeywordsPredicate;
import seedu.club.model.member.exceptions.MemberNotFoundException;
import seedu.club.testutil.EditMemberDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_GROUP_AMY = "publicity";
    public static final String VALID_GROUP_BOB = "logistics";
    public static final String VALID_MATRIC_NUMBER_AMY = "A9210701B";
    public static final String VALID_MATRIC_NUMBER_BOB = "A8389539B";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friends";
    public static final String VALID_TAG_UNUSED = "unused"; //this tag should not be used when creating a member
    public static final String VALID_TAG_UNUSED_DESC = " " + PREFIX_TAG + VALID_TAG_UNUSED;
    public static final String VALID_USERNAME_AMY = "AmyBee";
    public static final String VALID_USERNAME_BOB = "BobChoo";
    public static final String VALID_PASSWORD = "password";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String MATRIC_NUMBER_DESC_AMY = " " + PREFIX_MATRIC_NUMBER + VALID_MATRIC_NUMBER_AMY;
    public static final String MATRIC_NUMBER_DESC_BOB = " " + PREFIX_MATRIC_NUMBER + VALID_MATRIC_NUMBER_BOB;
    public static final String GROUP_DESC_AMY = " " + PREFIX_GROUP + VALID_GROUP_AMY;
    public static final String GROUP_DESC_BOB = " " + PREFIX_GROUP + VALID_GROUP_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;
    public static final String USERNAME_DESC_AMY = " " + PREFIX_USERNAME + VALID_USERNAME_AMY;
    public static final String USERNAME_DESC_BOB = " " + PREFIX_USERNAME + VALID_USERNAME_BOB;
    public static final String PASSWORD_DESC = " " + PREFIX_PASSWORD + VALID_PASSWORD;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_MATRIC_NUMBER_DESC = " " + PREFIX_MATRIC_NUMBER; // must follow format
    public static final String INVALID_GROUP = "public relations"; // no spaces allowed
    public static final String INVALID_GROUP_DESC = " " + PREFIX_GROUP + INVALID_GROUP; // no spaces allowed
    public static final String INVALID_TAG = "hubby*";
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + INVALID_TAG; // '*' not allowed in tags

    public static final String NON_EXISTENT_GROUP = "broadcasting";
    public static final String NON_EXISTENT_GROUP_DESC = PREFIX_GROUP + NON_EXISTENT_GROUP;
    public static final String MANDATORY_GROUP = "member";
    public static final String MANDATORY_GROUP_DESC = PREFIX_GROUP + MANDATORY_GROUP;

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditMemberDescriptor DESC_AMY;
    public static final EditCommand.EditMemberDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditMemberDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withMatricNumber(VALID_MATRIC_NUMBER_AMY)
                .withGroup(VALID_GROUP_AMY).withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditMemberDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withMatricNumber(VALID_MATRIC_NUMBER_BOB)
                .withGroup(VALID_GROUP_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        try {
            CommandResult result = command.execute();
            assertEquals(expectedMessage, result.feedbackToUser);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the club book and the filtered member list in the {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        ClubBook expectedClubBook = new ClubBook(actualModel.getClubBook());
        List<Member> expectedFilteredList = new ArrayList<>(actualModel.getFilteredMemberList());

        try {
            command.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedClubBook, actualModel.getClubBook());
            assertEquals(expectedFilteredList, actualModel.getFilteredMemberList());
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the member at the given {@code targetIndex} in the
     * {@code model}'s club book.
     */
    public static void showMemberAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredMemberList().size());

        Member member = model.getFilteredMemberList().get(targetIndex.getZeroBased());
        final String[] splitName = member.getName().fullName.split("\\s+");
        model.updateFilteredMemberList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredMemberList().size());
    }

    /**
     * Deletes the first member in {@code model}'s filtered list from {@code model}'s club book.
     */
    public static void deleteFirstMember(Model model) {
        Member firstMember = model.getFilteredMemberList().get(0);
        try {
            model.deleteMember(firstMember);
        } catch (MemberNotFoundException pnfe) {
            throw new AssertionError("Member in filtered list must exist in model.", pnfe);
        }
    }

    /**
     * Returns an {@code UndoCommand} with the given {@code model} and {@code undoRedoStack} set.
     */
    public static UndoCommand prepareUndoCommand(Model model, UndoRedoStack undoRedoStack) {
        UndoCommand undoCommand = new UndoCommand();
        undoCommand.setData(model, new CommandHistory(), undoRedoStack);
        return undoCommand;
    }

    /**
     * Returns a {@code RedoCommand} with the given {@code model} and {@code undoRedoStack} set.
     */
    public static RedoCommand prepareRedoCommand(Model model, UndoRedoStack undoRedoStack) {
        RedoCommand redoCommand = new RedoCommand();
        redoCommand.setData(model, new CommandHistory(), undoRedoStack);
        return redoCommand;
    }
}
