package c8y;

import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.operation.OperationRepresentation;;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DockerContainerRunCommand extends CommandExecutor {
    private OperationRepresentation operation;
    private String imageName;
    private String containerOptions;
    private String containerCmd;
    private String containerCmdArgs;

    public DockerContainerRunCommand(OperationRepresentation operation, String imageName, String containerOptions, String containerCmd, String containerCmdArgs) {
        this.operation = operation;
        this.imageName = imageName;
        this.containerOptions = containerOptions;
        this.containerCmd = containerCmd;
        this.containerCmdArgs = containerCmdArgs;
    }

    @Override
    public BufferedReader execute() {
        BufferedReader result;
        try {
            ArrayList<String> command = new ArrayList<>();
            command.add("docker");
            command.add("container");
            command.add("run");

            if (this.containerOptions != null && !this.containerOptions.equals("")) {
                command.addAll(Arrays.asList(this.containerOptions.split(" ")));
            }

            command.add(this.imageName);

            if (this.containerCmd != null && !this.containerCmd.equals("")) {
                command.add(this.containerCmd);
            }
            if (this.containerCmdArgs != null && !this.containerCmdArgs.equals("")) {
                command.add(this.containerCmdArgs);
            }

            result = this.systemCall(command.toArray(new String[0]));
            this.operation.setStatus(OperationStatus.SUCCESSFUL.toString());
        } catch (IOException e) {
            result = null;
            e.printStackTrace();
            this.operation.setFailureReason(e.toString());
            this.operation.setStatus(OperationStatus.FAILED.toString());
        }
        return result;
    }
}
