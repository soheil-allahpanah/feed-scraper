package ir.sooall.feedscraper.domain.core.dto;

public enum RequestStatus {

	SUBMITTED(1), IN_PROGRESS(2), COMPLETE(3), ERROR(4), DELETED(5), UNKNOWN(0);

	private final Integer value;

	RequestStatus(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return value;
	}

	public static RequestStatus fromValue(int value) {
        return switch (value) {
            case 1 -> SUBMITTED;
            case 2 -> IN_PROGRESS;
            case 3 -> COMPLETE;
            case 4 -> ERROR;
            case 5 -> DELETED;
            default -> UNKNOWN;
        };
    }
}
