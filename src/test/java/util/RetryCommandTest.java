package util;

import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import utlis.RetryCommand;

import java.util.function.Supplier;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RetryCommandTest {
    public String SUCCESS = "success";
    public int MAX_RETRIES = 3;
    @Test
    public void run_shouldNotRetryCommand_whenSuccessful() {
        RetryCommand<String> retryCommand = new RetryCommand<>(MAX_RETRIES);
        Supplier<String> commandSucceed = () -> SUCCESS;

        String result = retryCommand.run(commandSucceed);

        assertEquals(SUCCESS, result);
        assertEquals(0, retryCommand.getRetryCounter());
    }

    @Test
    public void run_shouldRetryOnceThenSucceed_whenFailsOnFirstCallButSucceedsOnFirstRetry() {
        RetryCommand<String> retryCommand = new RetryCommand<>(MAX_RETRIES);
        Supplier<String> commandFailOnce = () -> {
            if (retryCommand.getRetryCounter() == 0) throw new OptimisticLockException("Command Failed");
            else return SUCCESS;
        };

        String result = retryCommand.run(commandFailOnce);

        assertEquals(SUCCESS, result);
        assertEquals(1, retryCommand.getRetryCounter());
    }

    @Test
    public void run_shouldThrowException_whenMaxRetriesIsReached() {
        RetryCommand<String> retryCommand = new RetryCommand<>(MAX_RETRIES);
        Supplier<String> commandFail = () -> {
            throw new OptimisticLockException("Failed");
        };

        try {
            retryCommand.run(commandFail);
            fail("Should throw exception when max retries is reached");
        } catch (OptimisticLockException ignored) { }
    }
}
