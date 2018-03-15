package seedu.club.model.util;

import java.util.HashSet;
import java.util.Set;

import seedu.club.model.ClubBook;
import seedu.club.model.ReadOnlyClubBook;

import seedu.club.model.group.Group;
import seedu.club.model.member.Email;
import seedu.club.model.member.MatricNumber;
import seedu.club.model.member.Member;
import seedu.club.model.member.Name;
import seedu.club.model.member.Password;
import seedu.club.model.member.Phone;
import seedu.club.model.member.Username;

import seedu.club.model.member.exceptions.DuplicateMemberException;
import seedu.club.model.tag.Tag;

/**
 * Contains utility methods for populating {@code ClubBook} with sample data.
 */
public class SampleDataUtil {
    public static Member[] getSampleMembers() {
        return new Member[] {
            new Member(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                    new MatricNumber("A5215090A"), new Group("logistics"),
                    getTagSet("friends"), new Username("AlexYeoh"), new Password("password")),
            new Member(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                    new MatricNumber("A0632713Q"), new Group("production"),
                    getTagSet("colleagues", "friends"), new Username("BerniceYu"), new Password("password")),
            new Member(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                    new MatricNumber("A1010027G"), new Group("exco"),
                    getTagSet("neighbours"), new Username("CharllotteOliveiro"), new Password("password")),
            new Member(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                    new MatricNumber("A7251856A"), new Group("marketing"),
                    getTagSet("family"), new Username("DavidLi"), new Password("password")),
            new Member(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                    new MatricNumber("A4960627S"), new Group("pr"),
                    getTagSet("classmates"), new Username("IrfanIbrahim"), new Password("password")),
            new Member(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                    new MatricNumber("A2488865L"), new Group("legal"),
                    getTagSet("colleagues"), new Username("RoyBalakrishnan"), new Password("password"))
        };
    }

    public static ReadOnlyClubBook getSampleAddressBook() {
        try {
            ClubBook sampleAb = new ClubBook();
            for (Member sampleMember : getSampleMembers()) {
                sampleAb.addMember(sampleMember);
            }
            return sampleAb;
        } catch (DuplicateMemberException e) {
            throw new AssertionError("sample data cannot contain duplicate persons", e);
        }
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

}
