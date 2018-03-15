package seedu.club.storage;

import static org.junit.Assert.assertEquals;
import static seedu.club.storage.XmlAdaptedMember.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.club.testutil.TypicalMembers.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.club.commons.exceptions.IllegalValueException;
import seedu.club.model.member.Email;
import seedu.club.model.member.MatricNumber;
import seedu.club.model.member.Name;
import seedu.club.model.member.Phone;
import seedu.club.testutil.Assert;

public class XmlAdaptedMemberTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_MATRIC_NUMBER = "B1234567";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_GROUP = " @#";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_MATRIC_NUMBER = BENSON.getMatricNumber().toString();
    private static final String VALID_GROUP = BENSON.getGroup().toString();
    private static final List<XmlAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(XmlAdaptedTag::new)
            .collect(Collectors.toList());
    private static final String VALID_USERNAME = BENSON.getUsername().toString();
    private static final String VALID_PASSWORD = BENSON.getPassword().toString();

    @Test
    public void toModelType_validMemberDetails_returnsMember() throws Exception {
        XmlAdaptedMember member = new XmlAdaptedMember(BENSON);
        assertEquals(BENSON, member.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        XmlAdaptedMember member =
                new XmlAdaptedMember(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_MATRIC_NUMBER, VALID_GROUP,
                        VALID_TAGS, VALID_USERNAME, VALID_PASSWORD);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, member::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        XmlAdaptedMember member = new XmlAdaptedMember(null, VALID_PHONE, VALID_EMAIL, VALID_MATRIC_NUMBER,
                VALID_GROUP, VALID_TAGS, VALID_USERNAME, VALID_PASSWORD);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, member::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        XmlAdaptedMember member =
                new XmlAdaptedMember(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_MATRIC_NUMBER, VALID_GROUP,
                        VALID_TAGS, VALID_USERNAME, VALID_PASSWORD);
        String expectedMessage = Phone.MESSAGE_PHONE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, member::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        XmlAdaptedMember member = new XmlAdaptedMember(VALID_NAME, null, VALID_EMAIL, VALID_MATRIC_NUMBER, VALID_GROUP,
                VALID_TAGS, VALID_USERNAME, VALID_PASSWORD);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, member::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        XmlAdaptedMember member =
                new XmlAdaptedMember(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_MATRIC_NUMBER, VALID_GROUP,
                        VALID_TAGS, VALID_USERNAME, VALID_PASSWORD);
        String expectedMessage = Email.MESSAGE_EMAIL_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, member::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        XmlAdaptedMember member = new XmlAdaptedMember(VALID_NAME, VALID_PHONE, null, VALID_MATRIC_NUMBER, VALID_GROUP,
                VALID_TAGS, VALID_USERNAME, VALID_PASSWORD);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, member::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        XmlAdaptedMember member =
                new XmlAdaptedMember(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_MATRIC_NUMBER, VALID_GROUP,
                        VALID_TAGS, VALID_USERNAME, VALID_PASSWORD);
        String expectedMessage = MatricNumber.MESSAGE_MATRIC_NUMBER_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, member::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        XmlAdaptedMember member = new XmlAdaptedMember(VALID_NAME, VALID_PHONE, VALID_EMAIL, null,
                VALID_GROUP, VALID_TAGS, VALID_USERNAME, VALID_PASSWORD);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, MatricNumber.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, member::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_TAG));
        XmlAdaptedMember member =
                new XmlAdaptedMember(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        VALID_MATRIC_NUMBER, VALID_GROUP, invalidTags, VALID_USERNAME, VALID_PASSWORD);
        Assert.assertThrows(IllegalValueException.class, member::toModelType);
    }

}
