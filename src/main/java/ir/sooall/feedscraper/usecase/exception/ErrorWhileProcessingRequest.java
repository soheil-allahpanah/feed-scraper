package ir.sooall.feedscraper.usecase.exception;

public class ErrorWhileProcessingRequest extends RuntimeException {

	private boolean badRequest = false;

	public ErrorWhileProcessingRequest() {
		super();
	}

	public ErrorWhileProcessingRequest(String message) {
		super(message);
	}

	public ErrorWhileProcessingRequest(String message, boolean badRequest) {
		super(message);
		this.badRequest = badRequest;
	}

	public boolean isBadRequest() {
		return badRequest;
	}
}
