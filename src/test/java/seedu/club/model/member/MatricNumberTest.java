package seedu.club.model.member;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.club.testutil.Assert;

public class MatricNumberTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new MatricNumber(null));
    }

    @Test
    public void constructor_invalidMatricNumber_throwsIllegalArgumentException() {
        String invalidMatricNumber = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new MatricNumber(invalidMatricNumber));
    }

    @Test
    public void isValidMatricNumber() {
        // null matric number
        Assert.assertThrows(NullPointerException.class, () -> MatricNumber.isValidMatricNumber(null));

        // invalid matric numbers
        assertFalse(MatricNumber.isValidMatricNumber("")); // empty string
        assertFalse(MatricNumber.isValidMatricNumber(" ")); // spaces only
        assertFalse(MatricNumber.isValidMatricNumber("A1234567")); // no ending letter
        assertFalse(MatricNumber.isValidMatricNumber("1234567A")); // no starting letter
        assertFalse(MatricNumber.isValidMatricNumber("B9873161F")); // must start with A
        assertFalse(MatricNumber.isValidMatricNumber("C9226443H"));
        assertFalse(MatricNumber.isValidMatricNumber("A98764532L")); // too many digits
        assertFalse(MatricNumber.isValidMatricNumber("A987645H")); // too few digits



        // valid matric numbers
        assertTrue(MatricNumber.isValidMatricNumber("A1152241G"));
        assertTrue(MatricNumber.isValidMatricNumber("A0152640A"));
        assertTrue(MatricNumber.isValidMatricNumber("A1902205L"));
        assertTrue(MatricNumber.isValidMatricNumber("a1024509A"));
        assertTrue(MatricNumber.isValidMatricNumber("a1122206M"));
    }
}
