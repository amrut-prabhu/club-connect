package seedu.club.ui;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static seedu.club.testutil.EventsUtil.postNow;
import static seedu.club.testutil.TypicalMembers.ALICE;
import static seedu.club.ui.BrowserPanel.DEFAULT_PAGE;
import static seedu.club.ui.UiPart.FXML_FILE_FOLDER;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.BrowserPanelHandle;
import seedu.club.MainApp;
import seedu.club.commons.events.ui.MemberPanelSelectionChangedEvent;

public class BrowserPanelTest extends GuiUnitTest {
    private MemberPanelSelectionChangedEvent selectionChangedEventStub;

    private BrowserPanel browserPanel;
    private BrowserPanelHandle browserPanelHandle;

    @Before
    public void setUp() {
        selectionChangedEventStub = new MemberPanelSelectionChangedEvent(new MemberCard(ALICE, 0));

        guiRobot.interact(() -> browserPanel = new BrowserPanel());
        uiPartRule.setUiPart(browserPanel);

        browserPanelHandle = new BrowserPanelHandle(browserPanel.getRoot());
    }

    @Test
    public void display() throws Exception {
        // default web page
        URL expectedDefaultPageUrl = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        assertEquals(expectedDefaultPageUrl, browserPanelHandle.getLoadedUrl());

        // associated web page of a member
        postNow(selectionChangedEventStub);
        URL expectedMemberUrl = new URL(BrowserPanel.SEARCH_PAGE_URL + ALICE.getName().fullName.replaceAll(" ", "%20"));

        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(expectedMemberUrl, browserPanelHandle.getLoadedUrl());
    }
}
