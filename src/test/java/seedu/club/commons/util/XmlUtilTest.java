package seedu.club.commons.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.club.model.ClubBook;
import seedu.club.storage.XmlAdaptedMember;
import seedu.club.storage.XmlAdaptedTag;
import seedu.club.storage.XmlSerializableClubBook;
import seedu.club.testutil.ClubBookBuilder;
import seedu.club.testutil.MemberBuilder;
import seedu.club.testutil.TestUtil;

public class XmlUtilTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/XmlUtilTest/");
    private static final File EMPTY_FILE = new File(TEST_DATA_FOLDER + "empty.xml");
    private static final File MISSING_FILE = new File(TEST_DATA_FOLDER + "missing.xml");
    private static final File VALID_FILE = new File(TEST_DATA_FOLDER + "validClubBook.xml");
    private static final File MISSING_MEMBER_FIELD_FILE = new File(TEST_DATA_FOLDER + "missingMemberField.xml");
    private static final File INVALID_MEMBER_FIELD_FILE = new File(TEST_DATA_FOLDER + "invalidMemberField.xml");
    private static final File VALID_MEMBER_FILE = new File(TEST_DATA_FOLDER + "validMember.xml");
    private static final File TEMP_FILE = new File(TestUtil.getFilePathInSandboxFolder("tempClubBook.xml"));

    private static final String INVALID_PHONE = "9482asf424";

    private static final String VALID_NAME = "Hans Muster";
    private static final String VALID_PHONE = "9482424";
    private static final String VALID_EMAIL = "hans@example";
    private static final String VALID_MATRIC_NUMBER = "A1234567H";
    private static final String VALID_GROUP = "logistics";
    private static final String VALID_USERNAME = "HansMuster";
    private static final String VALID_PASSWORD = "password";
    private static final List<XmlAdaptedTag> VALID_TAGS = Collections.singletonList(new XmlAdaptedTag("friends"));

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getDataFromFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(null, ClubBook.class);
    }

    @Test
    public void getDataFromFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(VALID_FILE, null);
    }

    @Test
    public void getDataFromFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.getDataFromFile(MISSING_FILE, ClubBook.class);
    }

    @Test
    public void getDataFromFile_emptyFile_dataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.getDataFromFile(EMPTY_FILE, ClubBook.class);
    }

    @Test
    public void getDataFromFile_validFile_validResult() throws Exception {
        ClubBook dataFromFile = XmlUtil.getDataFromFile(VALID_FILE, XmlSerializableClubBook.class).toModelType();
        assertEquals(9, dataFromFile.getMemberList().size());
        assertEquals(0, dataFromFile.getTagList().size());
    }

    @Test
    public void xmlAdaptedMemberFromFile_fileWithMissingMemberField_validResult() throws Exception {
        XmlAdaptedMember actualMember = XmlUtil.getDataFromFile(
                MISSING_MEMBER_FIELD_FILE, XmlAdaptedMemberWithRootElement.class);
        XmlAdaptedMember expectedMember = new XmlAdaptedMember(
                null, VALID_PHONE, VALID_EMAIL, VALID_MATRIC_NUMBER, VALID_GROUP, VALID_TAGS,
                VALID_USERNAME, VALID_PASSWORD);
        assertEquals(expectedMember, actualMember);
    }

    @Test
    public void xmlAdaptedMemberFromFile_fileWithInvalidMemberField_validResult() throws Exception {
        XmlAdaptedMember actualMember = XmlUtil.getDataFromFile(
                INVALID_MEMBER_FIELD_FILE, XmlAdaptedMemberWithRootElement.class);
        XmlAdaptedMember expectedMember = new XmlAdaptedMember(
                VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_MATRIC_NUMBER, VALID_GROUP, VALID_TAGS,
                VALID_USERNAME, VALID_PASSWORD);
        assertEquals(expectedMember, actualMember);
    }

    @Test
    public void xmlAdaptedMemberFromFile_fileWithValidMember_validResult() throws Exception {
        XmlAdaptedMember actualMember = XmlUtil.getDataFromFile(
                VALID_MEMBER_FILE, XmlAdaptedMemberWithRootElement.class);
        XmlAdaptedMember expectedMember = new XmlAdaptedMember(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_MATRIC_NUMBER, VALID_GROUP,
                VALID_TAGS, VALID_USERNAME, VALID_PASSWORD);
        assertEquals(expectedMember, actualMember);
    }

    @Test
    public void saveDataToFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(null, new ClubBook());
    }

    @Test
    public void saveDataToFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(VALID_FILE, null);
    }

    @Test
    public void saveDataToFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.saveDataToFile(MISSING_FILE, new ClubBook());
    }

    @Test
    public void saveDataToFile_validFile_dataSaved() throws Exception {
        TEMP_FILE.createNewFile();
        XmlSerializableClubBook dataToWrite = new XmlSerializableClubBook(new ClubBook());
        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        XmlSerializableClubBook dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableClubBook.class);
        assertEquals(dataToWrite, dataFromFile);

        ClubBookBuilder builder = new ClubBookBuilder(new ClubBook());
        dataToWrite = new XmlSerializableClubBook(
                builder.withMember(new MemberBuilder().build()).withTag("Friends").build());

        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableClubBook.class);
        assertEquals(dataToWrite, dataFromFile);
    }

    /**
     * Test class annotated with {@code XmlRootElement} to allow unmarshalling of .xml data to {@code XmlAdaptedMember}
     * objects.
     */
    @XmlRootElement(name = "member")
    private static class XmlAdaptedMemberWithRootElement extends XmlAdaptedMember {}
}
