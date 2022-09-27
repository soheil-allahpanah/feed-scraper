package ir.sooall.feedscraper.common;

public record RetryNumber(Integer value) {
    public RetryNumber(Integer value) {
        if(value < 0) {
            this.value = 0;
        } else {
            this.value = value;
        }
    }

    public RetryNumber minus(Integer number) {
        if(this.value - number < 0 ) {
            return new RetryNumber(0);
        }
        return new RetryNumber(this.value - number);
    }
}