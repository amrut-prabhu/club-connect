package seedu.club.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.club.logic.commands.CommandTestUtil.MANDATORY_GROUP;
import static seedu.club.logic.commands.CommandTestUtil.NON_EXISTENT_GROUP;
import static seedu.club.logic.commands.CommandTestUtil.VALID_GROUP_AMY;
import static seedu.club.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.club.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.club.logic.commands.CommandTestUtil.VALID_TAG_UNUSED;
import static seedu.club.model.Model.PREDICATE_SHOW_ALL_MEMBERS;
import static seedu.club.testutil.TypicalMembers.ALICE;
import static seedu.club.testutil.TypicalMembers.AMY;
import static seedu.club.testutil.TypicalMembers.BENSON;
import static seedu.club.testutil.TypicalMembers.BOB;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.club.model.group.Group;
import seedu.club.model.group.exceptions.GroupCannotBeRemovedException;
import seedu.club.model.group.exceptions.GroupNotFoundException;
import seedu.club.model.member.Member;
import seedu.club.model.member.NameContainsKeywordsPredicate;
import seedu.club.model.tag.Tag;
import seedu.club.model.tag.exceptions.TagNotFoundException;
import seedu.club.testutil.ClubBookBuilder;
import seedu.club.testutil.MemberBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getFilteredMemberList_modifyList_throwsUnsupportedOperationException() {
        ModelManager modelManager = new ModelManager();
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredMemberList().remove(0);
    }

    @Test
    public void removeGroup_nonExistentGroup_modelUnchanged() throws Exception {
        ClubBook clubBook = new ClubBookBuilder().withMember(AMY).withMember(BOB).build();
        UserPrefs userPrefs = new UserPrefs();

        ModelManager modelManager = new ModelManager(clubBook, userPrefs);
        try {
            modelManager.removeGroup(new Group(NON_EXISTENT_GROUP));
        } catch (GroupNotFoundException gnfe) {
            assertEquals(new ModelManager(clubBook, userPrefs), modelManager);
        }
    }

    @Test
    public void removeGroup_mandatoryGroup_modelUnchanged() throws Exception {
        ClubBook clubBook = new ClubBookBuilder().withMember(AMY).withMember(BOB).build();
        UserPrefs userPrefs = new UserPrefs();

        ModelManager modelManager = new ModelManager(clubBook, userPrefs);
        try {
            modelManager.removeGroup(new Group(MANDATORY_GROUP));
        } catch (GroupCannotBeRemovedException e) {
            assertEquals(new ModelManager(clubBook, userPrefs), modelManager);
        }
    }

    @Test
    public void removeGroup_atLeastOneMemberInGroup_groupRemoved() throws Exception {
        ClubBook clubBook = new ClubBookBuilder().withMember(AMY).withMember(BOB).build();
        UserPrefs userPrefs = new UserPrefs();

        ModelManager modelManager = new ModelManager(clubBook, userPrefs);
        modelManager.removeGroup(new Group(VALID_GROUP_AMY));

        Member amyNotInPublicity = new MemberBuilder(AMY).withGroup().build();
        Member bobNotInPublicity = new MemberBuilder(BOB).build();
        ClubBook expectedClubBook = new ClubBookBuilder().withMember(amyNotInPublicity)
                .withMember(bobNotInPublicity).build();

        assertEquals(new ModelManager(expectedClubBook, userPrefs), modelManager);

    }

    @Test
    public void deleteTag_nonExistentTag_modelUnchanged() throws Exception {
        ClubBook clubBook = new ClubBookBuilder().withMember(AMY).withMember(BOB).build();
        UserPrefs userPrefs = new UserPrefs();

        ModelManager modelManager = new ModelManager(clubBook, userPrefs);
        try {
            modelManager.deleteTag(new Tag(VALID_TAG_UNUSED));
        } catch (TagNotFoundException tnfe) {
            assertEquals(new ModelManager(clubBook, userPrefs), modelManager);
        }
    }

    @Test
    public void deleteTag_tagUsedByMultipleMembers_tagRemoved() throws Exception {
        ClubBook clubBook = new ClubBookBuilder().withMember(AMY).withMember(BOB).build();
        UserPrefs userPrefs = new UserPrefs();

        ModelManager modelManager = new ModelManager(clubBook, userPrefs);
        modelManager.deleteTag(new Tag(VALID_TAG_FRIEND));

        Member amyWithoutFriendTag = new MemberBuilder(AMY).withTags().build();
        Member bobWithoutFriendTag = new MemberBuilder(BOB).withTags(VALID_TAG_HUSBAND).build();
        ClubBook expectedClubBook = new ClubBookBuilder().withMember(amyWithoutFriendTag)
                .withMember(bobWithoutFriendTag).build();

        assertEquals(new ModelManager(expectedClubBook, userPrefs), modelManager);
    }

    @Test
    public void equals() {
        ClubBook clubBook = new ClubBookBuilder().withMember(ALICE).withMember(BENSON).build();
        ClubBook differentClubBook = new ClubBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        ModelManager modelManager = new ModelManager(clubBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(clubBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different clubBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentClubBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredMemberList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(clubBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredMemberList(PREDICATE_SHOW_ALL_MEMBERS);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookName("differentName");
        assertTrue(modelManager.equals(new ModelManager(clubBook, differentUserPrefs)));
    }
}
