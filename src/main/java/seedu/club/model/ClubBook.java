package seedu.club.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.club.model.group.Group;
import seedu.club.model.group.exceptions.GroupCannotBeRemovedException;
import seedu.club.model.group.exceptions.GroupNotFoundException;
import seedu.club.model.member.Member;
import seedu.club.model.member.UniqueMemberList;
import seedu.club.model.member.exceptions.DuplicateMemberException;
import seedu.club.model.member.exceptions.MemberNotFoundException;
import seedu.club.model.tag.Tag;
import seedu.club.model.tag.UniqueTagList;
import seedu.club.model.tag.exceptions.TagNotFoundException;

/**
 * Wraps all data at the club-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class ClubBook implements ReadOnlyClubBook {

    private final UniqueMemberList persons;
    private final UniqueTagList tags;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniqueMemberList();
        tags = new UniqueTagList();
    }

    public ClubBook() {}

    /**
     * Creates an ClubBook using the Persons and Tags in the {@code toBeCopied}
     */
    public ClubBook(ReadOnlyClubBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }


    //// list overwrite operations
    public void setPersons(List<Member> members) throws DuplicateMemberException {
        this.persons.setPersons(members);
    }

    public void setTags(Set<Tag> tags) {
        this.tags.setTags(tags);
    }

    /**
     * Resets the existing data of this {@code ClubBook} with {@code newData}.
     */
    public void resetData(ReadOnlyClubBook newData) {
        requireNonNull(newData);
        setTags(new HashSet<>(newData.getTagList()));
        List<Member> syncedMemberList = newData.getMemberList().stream()
                .map(this::syncWithMasterTagList)
                .collect(Collectors.toList());

        try {
            setPersons(syncedMemberList);
        } catch (DuplicateMemberException e) {
            throw new AssertionError("AddressBooks should not have duplicate persons");
        }
    }


    //// member-level operations
    /**
     * Adds a member to the club book.
     * Also checks the new member's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the member to point to those in {@link #tags}.
     *
     * @throws DuplicateMemberException if an equivalent member already exists.
     */
    public void addMember(Member p) throws DuplicateMemberException {
        Member member = syncWithMasterTagList(p);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any member
        // in the member list.
        persons.add(member);
    }

    /**
     * Replaces the given member {@code target} in the list with {@code editedMember}.
     * {@code ClubBook}'s tag list will be updated with the tags of {@code editedMember}.
     *
     * @throws DuplicateMemberException if updating the member's details causes the member to be equivalent to
     *      another existing member in the list.
     * @throws MemberNotFoundException if {@code target} could not be found in the list.
     *
     * @see #syncWithMasterTagList(Member)
     */
    public void updateMember(Member target, Member editedMember)
            throws DuplicateMemberException, MemberNotFoundException {
        requireNonNull(editedMember);

        deletePersonTags(target);

        Member syncedEditedMember = syncWithMasterTagList(editedMember);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any member
        // in the member list.
        try {
            persons.setPerson(target, syncedEditedMember);
        } catch (DuplicateMemberException dpe) {
            addTargetPersonTags(target);
            throw new DuplicateMemberException();
        }
    }

    /**
     * Re-adds the tags of {@code target} that were removed from {@code tags}.
     */
    private void addTargetPersonTags(Member target) {
        Set<Tag> allTags = new HashSet<>(tags.asObservableList());

        for (Tag tag: target.getTags()) {
            allTags.add(tag);
        }

        tags.setTags(allTags);
    }

    /**
     *  Updates the master tag list to include tags in {@code member} that are not in the list.
     *  @return a copy of this {@code member} such that every tag in this member points to a Tag object in the master
     *  list.
     */
    private Member syncWithMasterTagList(Member member) {
        final UniqueTagList personTags = new UniqueTagList(member.getTags());
        tags.mergeFrom(personTags);

        // Create map with values = tag object references in the master list
        // used for checking member tag references
        final Map<String, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag.tagName, tag));

        // Rebuild the list of member tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        personTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag.tagName)));
        return new Member(
                member.getName(), member.getPhone(), member.getEmail(), member.getMatricNumber(), member.getGroup(),
                    correctTagReferences, member.getUsername(), member.getPassword());
    }

    /**
     * Removes {@code key} from this {@code ClubBook}.
     * @throws MemberNotFoundException if the {@code key} is not in this {@code ClubBook}.
     */
    public boolean removeMember(Member key) throws MemberNotFoundException {
        deletePersonTags(key);

        if (persons.remove(key)) {
            return true;
        } else {
            throw new MemberNotFoundException();
        }
    }

    /**
     * Logs in a member
     */

    public boolean logInMember(String username, String password) {
        return persons.logInMemberSuccessful(username, password);
    }

    /** tag-level operation
     * Removes tags from master tag list {@code tags} that are unique to member {@code member}.
     */
    private void deletePersonTags(Member member) {
        List<Tag> tagsToCheck = tags.asObservableList().stream().collect(Collectors.toList());
        Set<Tag> newTags = tagsToCheck.stream()
                .filter(t -> !isTagUniqueToPerson(t, member))
                .collect(Collectors.toSet());
        tags.setTags(newTags);
        /*
        Iterator<Tag> itr = tagsToCheck.iterator();
        while (itr.hasNext()) {
            Tag tag = itr.next();
            if (isTagUniqueToPerson(tag, member)) {
                deleteTag(tag);
            }
        }*/
    }

    /**
     * Returns true if only {@code key} is tagged with {@code tag}.
     */
    private boolean isTagUniqueToPerson(Tag tag, Member key) {
        for (Member member : persons) {
            if (member.hasTag(tag) && !member.equals(key)) {
                return false;
            }
        }
        return true;
    }


    //// tag-level operations
    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

    /**
     * Removes the Group {@code toRemove} from the Club Book. Every member who was once a part of {@code toRemove}
     * will be assigned the default group - "member".
     */
    public void removeGroup(Group toRemove) throws GroupCannotBeRemovedException, GroupNotFoundException {
        Group notToBeDeleted = new Group("member");
        if (toRemove.equals(notToBeDeleted)) {
            throw new GroupCannotBeRemovedException();
        }
        Boolean isPresent = false;

        for (Member member : persons) {
            if (member.getGroup().equals(toRemove)) {
                isPresent = true;
            }
        }
        try {
            for (Member member : persons) {
                removeGroupFromPerson(toRemove, member);
            }
        } catch (MemberNotFoundException pnfe) {
            throw new AssertionError("Impossible: original member is obtained from the club book.");
        }
        if (!isPresent) {
            throw new GroupNotFoundException();
        }
    }

    /**
     * Removes the Group {@code toRemove} from the {@code member} if the member's group matches the one to be removed.
     */
    private void removeGroupFromPerson(Group toRemove, Member member)
            throws MemberNotFoundException {
        if (!member.getGroup().toString().equalsIgnoreCase(toRemove.toString())) {
            return;
        }

        Group defaultGroup = new Group(Group.DEFAULT_GROUP);
        Member newMember = new Member(member.getName(), member.getPhone(), member.getEmail(), member.getMatricNumber(),
                defaultGroup, member.getTags(), member.getUsername(), member.getPassword());

        try {
            updateMember(member, newMember);
        } catch (DuplicateMemberException dpe) {
            throw new AssertionError("Deleting a member's group only should not result in a duplicate. "
            + "See Member#equals(Object).");
        }
    }

    /**
     * Removes {@code tagToDelete} for all persons in this {@code ClubBook}.
     * @param tagToDelete Tag to be removed
     */
    public void deleteTag(Tag tagToDelete) throws TagNotFoundException {
        List<Tag> tags = new ArrayList<Tag>(getTagList());
        if (!tags.contains(tagToDelete)) {
            throw new TagNotFoundException();
        }

        setTags(getListWithoutTag(tagToDelete));
        try {
            for (Member member : persons) {
                if (member.hasTag(tagToDelete)) {
                    deleteTagFromPerson(tagToDelete, member);
                }
            }
        } catch (MemberNotFoundException pnfe) {
            throw new AssertionError("Impossible: original member is obtained from the club book.");
        }
    }

    /**
     * Returns a list of tags which does not contain {@code tagToRemove}.
     * @param tagToRemove Tag which should not be included in the tagToRemove list
     */
    private Set<Tag> getListWithoutTag(Tag tagToRemove) {
        Set<Tag> newTagsList = new HashSet<>();

        Iterator<Tag> itr = tags.iterator();

        while (itr.hasNext()) {
            Tag tag = itr.next();
            if (!tag.equals(tagToRemove)) {
                newTagsList.add(tag);
            }
        }

        return newTagsList;
    }

    /**
     * Removes {@code tag} from {@code member} in this {@code ClubBook}.
     * @throws MemberNotFoundException if the {@code member} is not in this {@code ClubBook}.
     */
    private void deleteTagFromPerson(Tag tag, Member member) throws MemberNotFoundException {
        Set<Tag> personTags = new HashSet<>(member.getTags());

        if (!personTags.remove(tag)) {
            return;
        }

        Member newMember = new Member(member.getName(), member.getPhone(),
                member.getEmail(), member.getMatricNumber(),
                member.getGroup(), personTags, member.getUsername(), member.getPassword());

        try {
            updateMember(member, newMember);
        } catch (DuplicateMemberException dpe) {
            throw new AssertionError("Modifying a member's tags only should not result in a duplicate. "
                    + "See Member#equals(Object).");
        }
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asObservableList().size() + " persons, " + tags.asObservableList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<Member> getMemberList() {
        return persons.asObservableList();
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return tags.asObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ClubBook // instanceof handles nulls
                && this.persons.equals(((ClubBook) other).persons)
                && this.tags.equalsOrderInsensitive(((ClubBook) other).tags));
    }


    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(persons, tags);
    }
}
