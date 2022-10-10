package seedu.taassist.model.moduleclass;

import static java.util.Objects.requireNonNull;
import static seedu.taassist.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.taassist.model.moduleclass.exceptions.DuplicateModuleClassException;
import seedu.taassist.model.moduleclass.exceptions.ModuleClassNotFoundException;

/**
 * A list of classes that enforces uniqueness between its elements and does not allow nulls.
 * A class is considered unique by comparing using {@code ModuleClass#equals(Object)}. As such,
 * adding of classes uses ModuleClass#equals(Object) for equality so as to ensure that the class being
 * added is unique in terms of identity in the UniqueModuleClassList. However, the removal of a class
 * uses ModuleClass#equals(Object) so as to ensure that the class with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see ModuleClass#equals(Object)
 */
public class UniqueModuleClassList implements Iterable<ModuleClass> {

    private final ObservableList<ModuleClass> internalList = FXCollections.observableArrayList();
    private final ObservableList<ModuleClass> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent class as the given argument.
     */
    public boolean contains(ModuleClass toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameModuleClass);
    }

    /**
     * Adds a class to the list.
     * The class must not already exist in the list.
     */
    public void add(ModuleClass toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateModuleClassException();
        }
        internalList.add(toAdd);
    }

    /**
     * Removes the equivalent class from the list.
     * The class must exist in the list.
     */
    public void remove(ModuleClass toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new ModuleClassNotFoundException();
        }
    }

    /**
     * Replaces the module class {@code target} in the list with {@code editedModuleClass}.
     * {@code target} must exist in the list.
     * The identity of {@code editedModuleClass} must not be the same as another existing module class in the list.
     */
    public void setModuleClass(ModuleClass target, ModuleClass editedModuleClass) {
        requireAllNonNull(target, editedModuleClass);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ModuleClassNotFoundException();
        }

        if (!target.isSameModuleClass(editedModuleClass) && contains(editedModuleClass)) {
            throw new DuplicateModuleClassException();
        }

        internalList.set(index, editedModuleClass);
    }

    public void setModuleClasses(UniqueModuleClassList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code classes}.
     * {@code classes} must not contain duplicate classes.
     */
    public void setModuleClasses(List<ModuleClass> moduleClasses) {
        requireAllNonNull(moduleClasses);
        if (!isUniqueListOfModuleClasses(moduleClasses)) {
            throw new DuplicateModuleClassException();
        }

        internalList.setAll(moduleClasses);
    }

    /**
     * Finds a module class with equivalent identity to {@code target}.
     */
    public Optional<ModuleClass> findModuleClass(ModuleClass target) {
        requireNonNull(target);
        List<ModuleClass> result = internalList.stream()
                .filter(target::isSameModuleClass)
                .limit(1)
                .collect(Collectors.toList());
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ModuleClass> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<ModuleClass> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueModuleClassList // instanceof handles nulls
                && internalList.equals(((UniqueModuleClassList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code moduleClasses} contains only unique classes.
     */
    private boolean isUniqueListOfModuleClasses(List<ModuleClass> moduleClasses) {
        for (int i = 0; i + 1 < moduleClasses.size(); i++) {
            for (int j = i + 1; j < moduleClasses.size(); j++) {
                if (moduleClasses.get(i).isSameModuleClass(moduleClasses.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}

