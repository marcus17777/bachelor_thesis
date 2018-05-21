package c8y;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public abstract class CommandExecutor<T> implements Runnable {
    private Callback<T> callback = null;

    private static Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

    BufferedReader systemCall(String ...command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command);
        logger.debug("Executing command " + pb.command());
        Process process = pb.start();

        BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String error = stdErr.lines().collect(Collectors.joining("\n"));
        if (error.length() > 0) {
            throw new RuntimeException(error);
        }

        return stdOut;
    }

    public void setCallback(Callback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        T result = this.execute();

        if (callback != null) {
            callback.call(result);
        }
    }

    public abstract T execute();
}
