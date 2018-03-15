package seedu.club.commons.events.ui;

import seedu.club.commons.events.BaseEvent;
import seedu.club.ui.MemberCard;

/**
 * Represents a selection change in the Member List Panel
 */
public class MemberPanelSelectionChangedEvent extends BaseEvent {


    private final MemberCard newSelection;

    public MemberPanelSelectionChangedEvent(MemberCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public MemberCard getNewSelection() {
        return newSelection;
    }
}
