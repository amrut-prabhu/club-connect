package seedu.club.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.club.model.member.UniqueMemberList;

public class UniqueMemberListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueMemberList uniqueMemberList = new UniqueMemberList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueMemberList.asObservableList().remove(0);
    }
}
