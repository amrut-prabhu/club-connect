package seedu.club.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.club.logic.CommandHistory;
import seedu.club.logic.UndoRedoStack;
import seedu.club.logic.commands.exceptions.CommandException;
import seedu.club.model.ClubBook;
import seedu.club.model.Model;
import seedu.club.model.ReadOnlyClubBook;
import seedu.club.model.group.Group;
import seedu.club.model.group.exceptions.GroupCannotBeRemovedException;
import seedu.club.model.group.exceptions.GroupNotFoundException;
import seedu.club.model.member.Member;
import seedu.club.model.member.exceptions.DuplicateMemberException;
import seedu.club.model.member.exceptions.MemberNotFoundException;
import seedu.club.model.tag.Tag;
import seedu.club.model.tag.exceptions.TagNotFoundException;
import seedu.club.testutil.MemberBuilder;

public class AddCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullMember_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_memberAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingMemberAdded modelStub = new ModelStubAcceptingMemberAdded();
        Member validMember = new MemberBuilder().build();

        CommandResult commandResult = getAddCommandForMember(validMember, modelStub).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validMember), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validMember), modelStub.membersAdded);
    }

    @Test
    public void execute_duplicateMember_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateMemberException();
        Member validMember = new MemberBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_MEMBER);

        getAddCommandForMember(validMember, modelStub).execute();
    }

    @Test
    public void equals() {
        Member alice = new MemberBuilder().withName("Alice").build();
        Member bob = new MemberBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different member -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * Generates a new AddCommand with the details of the given member.
     */
    private AddCommand getAddCommandForMember(Member member, Model model) {
        AddCommand command = new AddCommand(member);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addMember(Member member) throws DuplicateMemberException {
            fail("This method should not be called.");
        }

        @Override
        public void removeGroup(Group toRemove) throws GroupNotFoundException, GroupCannotBeRemovedException {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyClubBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyClubBook getClubBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deleteMember(Member target) throws MemberNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updateMember(Member target, Member editedMember)
                throws DuplicateMemberException {
            fail("This method should not be called.");
        }

        @Override
        public void deleteTag(Tag tag) throws TagNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Member> getFilteredMemberList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredMemberList(Predicate<Member> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public boolean logInMemberSuccessful(String username, String password) {
            fail("This method should not be called.");
            return false;
        }

        public void updateFilteredTagList(Predicate<Tag> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Tag> getFilteredTagList() {
            fail("This method should not be called.");
            return null;
        }
    }

    /**
     * A Model stub that always throw a DuplicateMemberException when trying to add a member.
     */
    private class ModelStubThrowingDuplicateMemberException extends ModelStub {
        @Override
        public void addMember(Member member) throws DuplicateMemberException {
            throw new DuplicateMemberException();
        }

        @Override
        public ReadOnlyClubBook getClubBook() {
            return new ClubBook();
        }
    }

    /**
     * A Model stub that always accept the member being added.
     */
    private class ModelStubAcceptingMemberAdded extends ModelStub {
        final ArrayList<Member> membersAdded = new ArrayList<>();

        @Override
        public void addMember(Member member) throws DuplicateMemberException {
            requireNonNull(member);
            membersAdded.add(member);
        }

        @Override
        public ReadOnlyClubBook getClubBook() {
            return new ClubBook();
        }
    }

}
