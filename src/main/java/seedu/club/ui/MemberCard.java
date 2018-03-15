package seedu.club.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.club.model.member.Member;

/**
 * An UI component that displays information of a {@code Member}.
 */
public class MemberCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on ClubBook level 4</a>
     */

    public final Member member;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label matricNumber;
    @FXML
    private Label group;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    public MemberCard(Member member, int displayedIndex) {
        super(FXML);
        this.member = member;
        id.setText(displayedIndex + ". ");
        name.setText(member.getName().fullName);
        phone.setText(member.getPhone().value);
        matricNumber.setText(member.getMatricNumber().value);
        group.setText(member.getGroup().groupName);
        email.setText(member.getEmail().value);
        member.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MemberCard)) {
            return false;
        }

        // state check
        MemberCard card = (MemberCard) other;
        return id.getText().equals(card.id.getText())
                && member.equals(card.member);
    }
}
