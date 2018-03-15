package systemtests;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.club.model.Model;
import seedu.club.model.member.Member;

/**
 * Contains helper methods to set up {@code Model} for testing.
 */
public class ModelHelper {
    private static final Predicate<Member> PREDICATE_MATCHING_NO_MEMBERS = unused -> false;

    /**
     * Updates {@code model}'s filtered list to display only {@code toDisplay}.
     */
    public static void setFilteredList(Model model, List<Member> toDisplay) {
        Optional<Predicate<Member>> predicate =
                toDisplay.stream().map(ModelHelper::getPredicateMatching).reduce(Predicate::or);
        model.updateFilteredMemberList(predicate.orElse(PREDICATE_MATCHING_NO_MEMBERS));
    }

    /**
     * @see ModelHelper#setFilteredList(Model, List)
     */
    public static void setFilteredList(Model model, Member... toDisplay) {
        setFilteredList(model, Arrays.asList(toDisplay));
    }

    /**
     * Returns a predicate that evaluates to true if this {@code Member} equals to {@code other}.
     */
    private static Predicate<Member> getPredicateMatching(Member other) {
        return member -> member.equals(other);
    }
}
