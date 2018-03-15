package seedu.club.commons.events.model;

import seedu.club.commons.events.BaseEvent;
import seedu.club.model.ReadOnlyClubBook;

/** Indicates the ClubBook in the model has changed*/
public class AddressBookChangedEvent extends BaseEvent {

    public final ReadOnlyClubBook data;

    public AddressBookChangedEvent(ReadOnlyClubBook data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of persons " + data.getMemberList().size() + ", number of tags " + data.getTagList().size();
    }
}
