package seedu.club.model;

import javafx.collections.ObservableList;
import seedu.club.model.member.Member;
import seedu.club.model.tag.Tag;

/**
 * Unmodifiable view of an club book
 */
public interface ReadOnlyClubBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Member> getMemberList();

    /**
     * Returns an unmodifiable view of the tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getTagList();

    /**
     * Returns true if tag is present in the tags list.
     */
    //boolean containsTag(Tag tag);
}
