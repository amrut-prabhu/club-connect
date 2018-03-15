package seedu.club.model.member;

import static java.util.Objects.requireNonNull;
import static seedu.club.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.club.commons.util.CollectionUtil;
import seedu.club.model.member.exceptions.DuplicateMemberException;
import seedu.club.model.member.exceptions.MemberNotFoundException;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Member#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueMemberList implements Iterable<Member> {

    private final ObservableList<Member> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent member as the given argument.
     */
    public boolean contains(Member toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a member to the list.
     *
     * @throws DuplicateMemberException if the member to add is a duplicate of an existing member in the list.
     */
    public void add(Member toAdd) throws DuplicateMemberException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateMemberException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the member {@code target} in the list with {@code editedMember}.
     *
     * @throws DuplicateMemberException if the replacement is equivalent to another existing member in the list.
     * @throws MemberNotFoundException if {@code target} could not be found in the list.
     */
    public void setPerson(Member target, Member editedMember)
            throws DuplicateMemberException, MemberNotFoundException {
        requireNonNull(editedMember);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new MemberNotFoundException();
        }

        if (!target.equals(editedMember) && internalList.contains(editedMember)) {
            throw new DuplicateMemberException();
        }

        internalList.set(index, editedMember);
    }

    /**
     * Removes the equivalent member from the list.
     *
     * @throws MemberNotFoundException if no such member could be found in the list.
     */
    public boolean remove(Member toRemove) throws MemberNotFoundException {
        requireNonNull(toRemove);
        final boolean personFoundAndDeleted = internalList.remove(toRemove);
        if (!personFoundAndDeleted) {
            throw new MemberNotFoundException();
        }
        return personFoundAndDeleted;
    }

    public void setPersons(UniqueMemberList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setPersons(List<Member> members) throws DuplicateMemberException {
        requireAllNonNull(members);
        final UniqueMemberList replacement = new UniqueMemberList();
        for (final Member member : members) {
            replacement.add(member);
        }
        setPersons(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Member> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Member> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueMemberList // instanceof handles nulls
                        && this.internalList.equals(((UniqueMemberList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Logs in a member successfully and return a true value
     * @param username
     * @param password
     * @return
     */
    public boolean logInMemberSuccessful(String username, String password) {
        if (!areThereAnyMemberLogedIn()) {
            for (int i = 0; i < internalList.size(); i++) {
                if (internalList.get(i).getUsername().toString().equals(username)) {
                    return internalList.get(i).getPassword().toString().equals(password);
                }
            }
        }
        return false;
    }

    /**
     * Check that whether there are anyone being log in currently.
     */
    public boolean areThereAnyMemberLogedIn() {
        for (int i = 0; i < internalList.size(); i++) {
            if (internalList.get(i).isLogIn()) {
                return true;
            }
        }
        return false;
    }
}
