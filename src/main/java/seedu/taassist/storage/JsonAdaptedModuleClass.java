package seedu.taassist.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.taassist.commons.exceptions.IllegalValueException;
import seedu.taassist.model.moduleclass.ModuleClass;
import seedu.taassist.model.session.Session;

/**
 * Json-friendly version of {@link ModuleClass}.
 */
class JsonAdaptedModuleClass {

    private final String className;
    private final List<JsonAdaptedSession> sessions = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedModuleClass} with the given {@code className} and list
     * of {@code Session}-s.
     */
    @JsonCreator
    public JsonAdaptedModuleClass(@JsonProperty("name") String className,
                                  @JsonProperty("sessions") List<JsonAdaptedSession> sessions) {
        this.className = className;
        if (sessions != null) {
            this.sessions.addAll(sessions);
        }
    }

    /**
     * Converts a given {@code ModuleClass} into this class for Jackson use.
     */
    public JsonAdaptedModuleClass(ModuleClass source) {
        className = source.className;
        sessions.addAll(source.getSessions().stream().map(JsonAdaptedSession::new).collect(Collectors.toList()));
    }

    @JsonGetter("name")
    public String getClassName() {
        return className;
    }

    @JsonGetter("sessions")
    public List<JsonAdaptedSession> getSessions() {
        return sessions;
    }

    /**
     * Converts this Json-friendly adapted module class object into the model's {@code ModuleClass} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted module class.
     */
    public ModuleClass toModelType() throws IllegalValueException {
        if (!ModuleClass.isValidModuleClassName(className)) {
            throw new IllegalValueException(ModuleClass.MESSAGE_CONSTRAINTS);
        }

        List<Session> sessionList = new ArrayList<>();
        for (JsonAdaptedSession session : sessions) {
            sessionList.add(session.toModelType());
        }
        return new ModuleClass(className, sessionList);
    }

}
