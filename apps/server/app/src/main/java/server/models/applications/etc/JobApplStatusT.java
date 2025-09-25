package server.models.applications.etc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobApplStatusT {
    APPLIED("APPLIED"),
    UNDER_REVIEW("UNDER_REVIEW"),
    INTERVIEW("INTERVIEW"),
    OFFER("OFFER"),
    REJECTED("REJECTED"),
    WITHDRAWN("WITHDRAWN");

    private final String value;
}
