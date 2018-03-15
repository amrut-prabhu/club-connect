package seedu.club.ui;

import java.util.logging.Logger;

import org.fxmisc.easybind.EasyBind;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.club.commons.core.LogsCenter;
import seedu.club.commons.events.ui.JumpToListRequestEvent;
import seedu.club.commons.events.ui.MemberPanelSelectionChangedEvent;
import seedu.club.model.member.Member;

/**
 * Panel containing the list of persons.
 */
public class MemberListPanel extends UiPart<Region> {
    private static final String FXML = "MemberListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(MemberListPanel.class);

    @FXML
    private ListView<MemberCard> personListView;

    public MemberListPanel(ObservableList<Member> memberList) {
        super(FXML);
        setConnections(memberList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Member> memberList) {
        ObservableList<MemberCard> mappedList = EasyBind.map(
                memberList, (person) -> new MemberCard(person, memberList.indexOf(person) + 1));
        personListView.setItems(mappedList);
        personListView.setCellFactory(listView -> new PersonListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        personListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in member list panel changed to : '" + newValue + "'");
                        raise(new MemberPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code MemberCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            personListView.scrollTo(index);
            personListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code MemberCard}.
     */
    class PersonListViewCell extends ListCell<MemberCard> {

        @Override
        protected void updateItem(MemberCard person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(person.getRoot());
            }
        }
    }

}
