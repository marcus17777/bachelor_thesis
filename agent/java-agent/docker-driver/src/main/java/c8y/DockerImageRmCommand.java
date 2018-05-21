package c8y;

import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.operation.OperationRepresentation;

import java.io.BufferedReader;
import java.io.IOException;


public class DockerImageRmCommand extends CommandExecutor {
    private OperationRepresentation operation;
    private String imageID;


    public DockerImageRmCommand(OperationRepresentation operation, String imageID) {
        this.operation = operation;
        this.imageID = imageID;
    }

    @Override
    public BufferedReader execute() {
        BufferedReader result;

        try {
            result = this.systemCall("docker", "image", "rm", this.imageID);
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
