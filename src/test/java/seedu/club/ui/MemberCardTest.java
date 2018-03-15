package seedu.club.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.club.ui.testutil.GuiTestAssert.assertCardDisplaysMember;

import org.junit.Test;

import guitests.guihandles.MemberCardHandle;
import seedu.club.model.member.Member;
import seedu.club.testutil.MemberBuilder;

public class MemberCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        Member memberWithNoTags = new MemberBuilder().withTags(new String[0]).build();
        MemberCard memberCard = new MemberCard(memberWithNoTags, 1);
        uiPartRule.setUiPart(memberCard);
        assertCardDisplay(memberCard, memberWithNoTags, 1);

        // with tags
        Member memberWithTags = new MemberBuilder().build();
        memberCard = new MemberCard(memberWithTags, 2);
        uiPartRule.setUiPart(memberCard);
        assertCardDisplay(memberCard, memberWithTags, 2);
    }

    @Test
    public void equals() {
        Member member = new MemberBuilder().build();
        MemberCard memberCard = new MemberCard(member, 0);

        // same member, same index -> returns true
        MemberCard copy = new MemberCard(member, 0);
        assertTrue(memberCard.equals(copy));

        // same object -> returns true
        assertTrue(memberCard.equals(memberCard));

        // null -> returns false
        assertFalse(memberCard.equals(null));

        // different types -> returns false
        assertFalse(memberCard.equals(0));

        // different member, same index -> returns false
        Member differentMember = new MemberBuilder().withName("differentName").build();
        assertFalse(memberCard.equals(new MemberCard(differentMember, 0)));

        // same member, different index -> returns false
        assertFalse(memberCard.equals(new MemberCard(member, 1)));
    }

    /**
     * Asserts that {@code memberCard} displays the details of {@code expectedMember} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(MemberCard memberCard, Member expectedMember, int expectedId) {
        guiRobot.pauseForHuman();

        MemberCardHandle memberCardHandle = new MemberCardHandle(memberCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", memberCardHandle.getId());

        // verify member details are displayed correctly
        assertCardDisplaysMember(expectedMember, memberCardHandle);
    }
}
