package seedu.club.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.club.logic.commands.EditCommand;
import seedu.club.logic.commands.EditCommand.EditMemberDescriptor;
import seedu.club.model.group.Group;
import seedu.club.model.member.Email;
import seedu.club.model.member.MatricNumber;
import seedu.club.model.member.Member;
import seedu.club.model.member.Name;
import seedu.club.model.member.Phone;
import seedu.club.model.tag.Tag;

/**
 * A utility class to help with building EditMemberDescriptor objects.
 */
public class EditMemberDescriptorBuilder {

    private EditMemberDescriptor descriptor;

    public EditMemberDescriptorBuilder() {
        descriptor = new EditCommand.EditMemberDescriptor();
    }

    public EditMemberDescriptorBuilder(EditCommand.EditMemberDescriptor descriptor) {
        this.descriptor = new EditMemberDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditMemberDescriptor} with fields containing {@code member}'s details
     */
    public EditMemberDescriptorBuilder(Member member) {
        descriptor = new EditMemberDescriptor();
        descriptor.setName(member.getName());
        descriptor.setPhone(member.getPhone());
        descriptor.setEmail(member.getEmail());
        descriptor.setMatricNumber(member.getMatricNumber());
        descriptor.setGroup(member.getGroup());
        descriptor.setTags(member.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditMemberDescriptor} that we are building.
     */
    public EditMemberDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditMemberDescriptor} that we are building.
     */
    public EditMemberDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditMemberDescriptor} that we are building.
     */
    public EditMemberDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code MatricNumber} of the {@code EditMemberDescriptor} that we are building.
     */
    public EditMemberDescriptorBuilder withMatricNumber(String matricNumber) {
        descriptor.setMatricNumber(new MatricNumber(matricNumber));
        return this;
    }

    /**
     * Sets the {@code Group} of the {@code EditMemberDescriptor} that we are building.
     */
    public EditMemberDescriptorBuilder withGroup(String group) {
        descriptor.setGroup(new Group(group));
        return this;
    }
    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditMemberDescriptor}
     * that we are building.
     */
    public EditMemberDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditMemberDescriptor build() {
        return descriptor;
    }
}
