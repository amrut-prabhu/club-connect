package seedu.club.model.member;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.club.commons.util.StringUtil;
import seedu.club.model.tag.Tag;

/**
 * Tests that a {@code Member}'s matches any of the keywords given according to the {@code fieldType}.
 */
public class FieldContainsKeywordsPredicate implements Predicate<Member> {
    private final List<String> keywords;
    private final String fieldType;

    public FieldContainsKeywordsPredicate(List<String> keywords, String fieldType) {
        this.keywords = keywords;
        this.fieldType = fieldType;
    }

    @Override
    public boolean test(Member member) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.partiallyContainsWordIgnoreCase(getFieldValue(member), keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FieldContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((FieldContainsKeywordsPredicate) other).keywords) // state check
                && this.fieldType.equals(((FieldContainsKeywordsPredicate) other).fieldType));
    }

    /**
     * Get relevant field value of member according to fieldType
     *
     */
    private String getFieldValue(Member member) {
        switch (fieldType) {
        case "email":
            return member.getEmail().toString();
        case "phone":
            return member.getPhone().toString();
        case "matric":
            return member.getMatricNumber().toString();
        case "group":
            return member.getGroup().toString();
        case "tag":
            return member.getTags().stream().map(Tag::toString).collect(Collectors.joining(" "));
        default:
            return member.getName().toString();
        }
    }
}
