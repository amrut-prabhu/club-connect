package seedu.club.storage;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.club.testutil.TypicalMembers.getTypicalClubBook;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.club.commons.events.model.AddressBookChangedEvent;
import seedu.club.commons.events.storage.DataSavingExceptionEvent;
import seedu.club.model.ClubBook;
import seedu.club.model.ReadOnlyClubBook;
import seedu.club.model.UserPrefs;
import seedu.club.ui.testutil.EventsCollectorRule;

public class StorageManagerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private StorageManager storageManager;


    @Before
    public void setUp() {
        XmlClubBookStorage addressBookStorage = new XmlClubBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(addressBookStorage, userPrefsStorage);
    }

    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }


    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void addressBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlClubBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link XmlClubBookStorageTest} class.
         */
        ClubBook original = getTypicalClubBook();
        storageManager.saveClubBook(original);
        ReadOnlyClubBook retrieved = storageManager.readClubBook().get();
        assertEquals(original, new ClubBook(retrieved));
    }

    @Test
    public void getAddressBookFilePath() {
        assertNotNull(storageManager.getAddressBookFilePath());
    }

    @Test
    public void handleAddressBookChangedEvent_exceptionThrown_eventRaised() {
        // Create a StorageManager while injecting a stub that  throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlClubBookStorageExceptionThrowingStub("dummy"),
                                             new JsonUserPrefsStorage("dummy"));
        storage.handleAddressBookChangedEvent(new AddressBookChangedEvent(new ClubBook()));
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlClubBookStorageExceptionThrowingStub extends XmlClubBookStorage {

        public XmlClubBookStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveClubBook(ReadOnlyClubBook addressBook, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }


}
