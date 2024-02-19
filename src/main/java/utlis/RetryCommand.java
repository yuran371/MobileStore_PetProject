package utlis;

import jakarta.persistence.OptimisticLockException;

import java.util.function.Supplier;

public class RetryCommand<T> {
    private int retryCounter;
    private final int maxRetries;

    public RetryCommand(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    // Takes a function and executes it, if fails, passes the function to the retry command
    public T run(Supplier<T> function) {
        try {
            return function.get();
        } catch (Exception e) {
            return retry(function);
        }
    }

    public int getRetryCounter() {
        return retryCounter;
    }

    private T retry(Supplier<T> function) throws RuntimeException {
        System.out.println("FAILED - Command failed, will be retried " + maxRetries + " times.");
        retryCounter = 0;
        while (retryCounter < maxRetries) {
            try {
                return function.get();
            } catch (OptimisticLockException ex) {
                retryCounter++;
                System.out.println("FAILED - Command failed on retry " + retryCounter + " of " + maxRetries + " error: " + ex );
                if (retryCounter >= maxRetries) {
                    System.out.println("Max retries exceeded.");
                    break;
                }
            }
        }
        throw new OptimisticLockException("Command failed on all of " + maxRetries + " retries");
    }
}
