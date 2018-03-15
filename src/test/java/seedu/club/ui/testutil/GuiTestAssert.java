package seedu.club.ui.testutil;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import guitests.guihandles.MemberCardHandle;
import guitests.guihandles.MemberListPanelHandle;
import guitests.guihandles.ResultDisplayHandle;
import seedu.club.model.member.Member;

/**
 * A set of assertion methods useful for writing GUI tests.
 */
public class GuiTestAssert {
    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertCardEquals(MemberCardHandle expectedCard, MemberCardHandle actualCard) {
        assertEquals(expectedCard.getId(), actualCard.getId());
        assertEquals(expectedCard.getMatricNumber(), actualCard.getMatricNumber());
        assertEquals(expectedCard.getEmail(), actualCard.getEmail());
        assertEquals(expectedCard.getName(), actualCard.getName());
        assertEquals(expectedCard.getPhone(), actualCard.getPhone());
        assertEquals(expectedCard.getTags(), actualCard.getTags());
    }

    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedMember}.
     */
    public static void assertCardDisplaysMember(Member expectedMember, MemberCardHandle actualCard) {
        assertEquals(expectedMember.getName().fullName, actualCard.getName());
        assertEquals(expectedMember.getPhone().value, actualCard.getPhone());
        assertEquals(expectedMember.getEmail().value, actualCard.getEmail());
        assertEquals(expectedMember.getMatricNumber().value, actualCard.getMatricNumber());
        assertEquals(expectedMember.getTags().stream().map(tag -> tag.tagName).collect(Collectors.toList()),
                actualCard.getTags());
    }

    /**
     * Asserts that the list in {@code memberListPanelHandle} displays the details of {@code members} correctly and
     * in the correct order.
     */
    public static void assertListMatching(MemberListPanelHandle memberListPanelHandle, Member... members) {
        for (int i = 0; i < members.length; i++) {
            assertCardDisplaysMember(members[i], memberListPanelHandle.getMemberCardHandle(i));
        }
    }

    /**
     * Asserts that the list in {@code memberListPanelHandle} displays the details of {@code members} correctly and
     * in the correct order.
     */
    public static void assertListMatching(MemberListPanelHandle memberListPanelHandle, List<Member> members) {
        assertListMatching(memberListPanelHandle, members.toArray(new Member[0]));
    }

    /**
     * Asserts the size of the list in {@code memberListPanelHandle} equals to {@code size}.
     */
    public static void assertListSize(MemberListPanelHandle memberListPanelHandle, int size) {
        int numberOfPeople = memberListPanelHandle.getListSize();
        assertEquals(size, numberOfPeople);
    }

    /**
     * Asserts the message shown in {@code resultDisplayHandle} equals to {@code expected}.
     */
    public static void assertResultMessage(ResultDisplayHandle resultDisplayHandle, String expected) {
        assertEquals(expected, resultDisplayHandle.getText());
    }
}
