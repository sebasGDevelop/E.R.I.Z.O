package dev.erizo.firewall.missions;

import java.util.List;

/** Asks which of two cables exposes a password, teaching HTTP versus HTTPS. */
public final class InterceptMessageMission implements Mission {

    private static final String PROMPT =
            "A password travels down two cables. Which one lets an eavesdropper read it?";
    private static final String EXPLANATION =
            "HTTP sends data in the clear, so anyone on the wire reads the password. "
                    + "HTTPS encrypts it, so the eavesdropper sees only scrambled bytes.";
    private static final int HTTP_CABLE = 0;

    private final String id;

    public InterceptMessageMission(String id) {
        this.id = id;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String prompt() {
        return PROMPT;
    }

    @Override
    public List<String> options() {
        return List.of("The HTTP cable", "The HTTPS cable");
    }

    @Override
    public MissionResult evaluate(int chosenOption) {
        if (chosenOption == HTTP_CABLE) {
            return MissionResult.completed(id, EXPLANATION);
        }
        return MissionResult.failed(id, EXPLANATION);
    }
}
