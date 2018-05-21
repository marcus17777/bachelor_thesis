package c8y;

import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.operation.OperationRepresentation;

import java.io.BufferedReader;
import java.io.IOException;


public class DockerContainerRmCommand extends CommandExecutor {
    private OperationRepresentation operation;
    private String containerID;

    public DockerContainerRmCommand(OperationRepresentation operation, String containerID) {
        this.operation = operation;
        this.containerID = containerID;
    }

    @Override
    public BufferedReader execute() {
        BufferedReader result;
        try {
            result = this.systemCall("docker", "container", "rm", this.containerID);
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
