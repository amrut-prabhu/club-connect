package seedu.club.testutil;

import java.io.File;
import java.io.IOException;

import seedu.club.commons.core.index.Index;
import seedu.club.commons.util.FileUtil;
import seedu.club.model.Model;
import seedu.club.model.member.Member;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    private static final String SANDBOX_FOLDER = FileUtil.getPath("./src/test/data/sandbox/");

    /**
     * Appends {@code fileName} to the sandbox folder path and returns the resulting string.
     * Creates the sandbox folder if it doesn't exist.
     */
    public static String getFilePathInSandboxFolder(String fileName) {
        try {
            FileUtil.createDirs(new File(SANDBOX_FOLDER));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER + fileName;
    }

    /**
     * Returns the middle index of the member in the {@code model}'s member list.
     */
    public static Index getMidIndex(Model model) {
        return Index.fromOneBased(model.getClubBook().getMemberList().size() / 2);
    }

    /**
     * Returns the last index of the member in the {@code model}'s member list.
     */
    public static Index getLastIndex(Model model) {
        return Index.fromOneBased(model.getClubBook().getMemberList().size());
    }

    /**
     * Returns the member in the {@code model}'s member list at {@code index}.
     */
    public static Member getMember(Model model, Index index) {
        return model.getClubBook().getMemberList().get(index.getZeroBased());
    }
}
