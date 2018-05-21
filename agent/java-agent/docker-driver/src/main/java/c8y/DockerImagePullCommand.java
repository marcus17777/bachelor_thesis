package c8y;

import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.operation.OperationRepresentation;

import java.io.BufferedReader;
import java.io.IOException;


public class DockerImagePullCommand extends CommandExecutor {
    private OperationRepresentation operation;
    private String image;

    public DockerImagePullCommand(OperationRepresentation operation, String image) {
        this.operation = operation;
        this.image = image;
    }

    @Override
    public BufferedReader execute() {
        BufferedReader result;

        try {
            result = this.systemCall("docker", "image", "pull", this.image);
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