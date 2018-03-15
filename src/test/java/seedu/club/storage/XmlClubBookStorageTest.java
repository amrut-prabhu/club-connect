package seedu.club.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.club.testutil.TypicalMembers.ALICE;
import static seedu.club.testutil.TypicalMembers.HOON;
import static seedu.club.testutil.TypicalMembers.IDA;
import static seedu.club.testutil.TypicalMembers.getTypicalClubBook;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.club.commons.exceptions.DataConversionException;
import seedu.club.commons.util.FileUtil;
import seedu.club.model.ClubBook;
import seedu.club.model.ReadOnlyClubBook;

public class XmlClubBookStorageTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlClubBookStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readAddressBook_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readClubBook(null);
    }

    private java.util.Optional<ReadOnlyClubBook> readClubBook(String filePath) throws Exception {
        return new XmlClubBookStorage(filePath).readClubBook(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readClubBook("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readClubBook("NotXmlFormatClubBook.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readClubBook_invalidMemberClubBook_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readClubBook("invalidMemberClubBook.xml");
    }

    @Test
    public void readClubBook_invalidAndValidMemberClubBook_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readClubBook("invalidAndValidMemberClubBook.xml");
    }

    @Test
    public void readAndSaveClubBook_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempClubBook.xml";
        ClubBook original = getTypicalClubBook();
        XmlClubBookStorage xmlClubBookStorage = new XmlClubBookStorage(filePath);

        //Save in new file and read back
        xmlClubBookStorage.saveClubBook(original, filePath);
        ReadOnlyClubBook readBack = xmlClubBookStorage.readClubBook(filePath).get();
        assertEquals(original, new ClubBook(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addMember(HOON);
        original.removeMember(ALICE);
        xmlClubBookStorage.saveClubBook(original, filePath);
        readBack = xmlClubBookStorage.readClubBook(filePath).get();
        assertEquals(original, new ClubBook(readBack));

        //Save and read without specifying file path
        original.addMember(IDA);
        xmlClubBookStorage.saveClubBook(original); //file path not specified
        readBack = xmlClubBookStorage.readClubBook().get(); //file path not specified
        assertEquals(original, new ClubBook(readBack));

    }

    @Test
    public void saveClubBook_nullClubBook_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveClubBook(null, "SomeFile.xml");
    }

    /**
     * Saves {@code clubBook} at the specified {@code filePath}.
     */
    private void saveClubBook(ReadOnlyClubBook clubBook, String filePath) {
        try {
            new XmlClubBookStorage(filePath).saveClubBook(clubBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveClubBook_nullFilePath_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        saveClubBook(new ClubBook(), null);
    }


}
