package dev.erizo.firewall.missions;

import java.util.List;

/** Asks which link of a certificate chain is invalid, teaching chain of trust. */
public final class SealCheckMission implements Mission {

    private static final String PROMPT =
            "This certificate chain has one broken link. Which certificate is invalid?";
    private static final String EXPLANATION =
            "A certificate is trusted only when every link signs the next: the root signs the "
                    + "intermediate, which signs the site. If one signature does not match, the whole chain fails.";
    private static final List<String> LINKS = List.of("Root certificate", "Intermediate certificate", "Site certificate");

    private final String id;
    private final int invalidLink;

    public SealCheckMission(String id, int invalidLink) {
        if (invalidLink < 0 || invalidLink >= LINKS.size()) {
            throw new IllegalArgumentException("Invalid link index out of range: " + invalidLink);
        }
        this.id = id;
        this.invalidLink = invalidLink;
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
        return LINKS;
    }

    @Override
    public MissionResult evaluate(int chosenOption) {
        if (chosenOption == invalidLink) {
            return MissionResult.completed(id, EXPLANATION);
        }
        return MissionResult.failed(id, EXPLANATION);
    }
}
