package seedu.club.model;

import static java.util.Objects.requireNonNull;
import static seedu.club.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.club.commons.core.ComponentManager;
import seedu.club.commons.core.LogsCenter;
import seedu.club.commons.events.model.AddressBookChangedEvent;
import seedu.club.model.group.Group;
import seedu.club.model.group.exceptions.GroupCannotBeRemovedException;
import seedu.club.model.group.exceptions.GroupNotFoundException;
import seedu.club.model.member.Member;
import seedu.club.model.member.exceptions.DuplicateMemberException;
import seedu.club.model.member.exceptions.MemberNotFoundException;
import seedu.club.model.tag.Tag;
import seedu.club.model.tag.exceptions.TagNotFoundException;

/**
 * Represents the in-memory model of the club book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final ClubBook clubBook;
    private final FilteredList<Member> filteredMembers;
    private final FilteredList<Tag> filteredTags;

    /**
     * Initializes a ModelManager with the given clubBook and userPrefs.
     */
    public ModelManager(ReadOnlyClubBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with club book: " + addressBook + " and user prefs " + userPrefs);

        this.clubBook = new ClubBook(addressBook);
        filteredMembers = new FilteredList<>(this.clubBook.getMemberList());
        filteredTags = new FilteredList<>(this.clubBook.getTagList());
    }

    public ModelManager() {
        this(new ClubBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyClubBook newData) {
        clubBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyClubBook getClubBook() {
        return clubBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(clubBook));
    }

    @Override
    public synchronized void deleteMember(Member target) throws MemberNotFoundException {
        clubBook.removeMember(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addMember(Member member) throws DuplicateMemberException {
        //updateTagList(member.getTags());
        clubBook.addMember(member);
        updateFilteredMemberList(PREDICATE_SHOW_ALL_MEMBERS);
        indicateAddressBookChanged();
    }

    @Override
    public void updateMember(Member target, Member editedMember)
            throws DuplicateMemberException, MemberNotFoundException {
        requireAllNonNull(target, editedMember);

        clubBook.updateMember(target, editedMember);
        deleteUnusedTags();
        indicateAddressBookChanged();
    }

    @Override
    public boolean logInMemberSuccessful(String username, String password) {
        requireAllNonNull(username, password);

        return clubBook.logInMember(username, password);
    }

    @Override
    public void removeGroup(Group toRemove) throws GroupNotFoundException, GroupCannotBeRemovedException {
        requireNonNull(toRemove);

        clubBook.removeGroup(toRemove);
        indicateAddressBookChanged();
    }

    @Override
    public void deleteTag(Tag tag) throws TagNotFoundException {
        clubBook.deleteTag(tag);
        updateFilteredMemberList(PREDICATE_SHOW_ALL_MEMBERS);
        indicateAddressBookChanged();
    }

    /**
     * Removes those tags from the master tag list that no persons in the club book are tagged with.
     */
    private void deleteUnusedTags() {
        List<Tag> tags = new ArrayList<>(clubBook.getTagList());

        for (Tag tag: tags) {
            deleteTagIfUnused(tag);
        }
    }

    /**
     * Removes {@code tag} from the master tag list if no persons in the club book are tagged with it.
     *
     * @param tag Tag to remove if no persons are tagged with it
     */
    private void deleteTagIfUnused(Tag tag) {
        if (isNotTaggedInPersons(tag)) {
            try {
                deleteTag(tag);
            } catch (TagNotFoundException tnfe) {
                throw new AssertionError("The tag cannot be missing.");
            }
        }
    }

    /**
     * Returns true is no member in the club book is tagged with {@code tag}.
     */
    private boolean isNotTaggedInPersons(Tag tag) {
        List<Member> members = new ArrayList<>(clubBook.getMemberList());

        for (Member member : members) {
            if (member.getTags().contains(tag)) {
                return false;
            }
        }
        return true;
    }

    //=========== Filtered Member List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Member} backed by the internal list of
     * {@code clubBook}
     */
    @Override
    public ObservableList<Member> getFilteredMemberList() {
        return FXCollections.unmodifiableObservableList(filteredMembers);
    }

    @Override
    public void updateFilteredMemberList(Predicate<Member> predicate) {
        requireNonNull(predicate);
        filteredMembers.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return clubBook.equals(other.clubBook)
                && filteredMembers.equals(other.filteredMembers);
    }

    //=========== Filtered Tag List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Tag} backed by the internal list of
     * {@code clubBook}
     */
    @Override
    public ObservableList<Tag> getFilteredTagList() {
        return FXCollections.unmodifiableObservableList(filteredTags);
    }

    @Override
    public void updateFilteredTagList(Predicate<Tag> predicate) {
        requireNonNull(predicate);
        filteredTags.setPredicate(predicate);
    }

}
