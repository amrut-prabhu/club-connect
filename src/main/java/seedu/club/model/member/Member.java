package seedu.club.model.member;

import static seedu.club.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import seedu.club.model.group.Group;
import seedu.club.model.tag.Tag;

/**
 * Represents a Member in the club book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Member {

    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Password password;
    private final Username username;
    private boolean isLogIn = false;
    private final MatricNumber matricNumber;
    private Group group;
    private final HashMap<String, Tag> tags;

    /**
     * Every field must be present and not null.
     */

    public Member(Name name, Phone phone, Email email, MatricNumber matricNumber, Group group, Set<Tag> tags,
                  Username username, Password password) {
        requireAllNonNull(name, phone, email, matricNumber, group, tags,
                username, password);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.matricNumber = matricNumber;
        this.group = group;
        this.tags = new HashMap<String, Tag>();
        this.username = username;
        this.password = password;
        setTags(tags);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public MatricNumber getMatricNumber() {
        return matricNumber;
    }

    public Group getGroup() {
        return group;
    }

    private void setTags(Set<Tag> personTags) {
        Iterator itr = personTags.iterator();

        while (itr.hasNext()) {
            Tag tag = (Tag) itr.next();
            tags.put(tag.tagName, tag);
        }
    }

    public Username getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }

    public boolean isLogIn() {
        return isLogIn;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        Set<Tag> personTags = new HashSet<Tag>();

        Set<String> tagNames = tags.keySet();
        Iterator itr = tagNames.iterator();

        while (itr.hasNext()) {
            String key = (String) itr.next();
            personTags.add(tags.get(key));
        }

        return Collections.unmodifiableSet(personTags);
    }

    public boolean hasTag(Tag tag) {
        return getTags().contains(tag);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Member)) {
            return false;
        }

        Member otherMember = (Member) other;
        return otherMember.getName().equals(this.getName())
                && otherMember.getPhone().equals(this.getPhone())
                && otherMember.getEmail().equals(this.getEmail())
                && otherMember.getMatricNumber().equals(this.getMatricNumber());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, matricNumber, group, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" MatricNumber: ")
                .append(getMatricNumber())
                .append(" Group: ")
                .append(getGroup())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * change the status of the member loggin in
     */

    public void changeLogInStatus() {
        if (!isLogIn) {
            isLogIn = true;
        } else {
            isLogIn = false;
        }
    }
}
