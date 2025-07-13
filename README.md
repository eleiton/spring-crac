# Spring Boot with CRaC Demo

This project provides a simple Spring Boot application that demonstrates the use of Coordinated Restore at Checkpoint (CRaC) to achieve faster startup times. The application exposes a simple "hello world" endpoint at `/hello`.

## Building and Running with Podman

The following commands will guide you through building the application, creating a CRaC snapshot, and running the application from the snapshot.

### 1. Build the Docker Image

This command builds the Docker image for the application. The image is built using a multi-stage build, which first builds the application using the Maven wrapper and then copies the resulting JAR file into a minimal, CRaC-enabled JDK image.

```bash
podman build -t spring-crac-demo .
```

### 2. Run the Application

This command runs the application in a Podman container. The `--cap-add` flags grant the container the necessary capabilities to create and restore snapshots.

*   `CHECKPOINT_RESTORE`: Allows the container to use the checkpoint/restore functionality.
*   `SYS_PTRACE`: Allows the container to trace system calls, which is required by CRIU (the underlying technology used by CRaC).

```bash
# Use this to run with limited permissions (recommended)
podman run -p 8080:8080 --cap-add CHECKPOINT_RESTORE --cap-add SYS_PTRACE --name spring-crac-demo-container spring-crac-demo
```

Alternatively, you can run the container with full permissions, which is less secure but may be necessary in some environments.

```bash
# Use this to run with full permissions
podman run -p 8080:8080 --privileged --name spring-crac-demo-container spring-crac-demo
```

### 3. Create a New Image from the Snapshot

This command creates a new Docker image from the container that was used to create the snapshot. The `--change` flag modifies the entrypoint of the new image to restore the application from the checkpoint when it is run.

```bash
podman commit --change='ENTRYPOINT ["java", "-XX:CRaCRestoreFrom=/checkpoint"]' spring-crac-demo-container spring-crac-demo-restore
```

### 4. Remove the Original Container

This command removes the container that was used to create the snapshot. This is done to avoid conflicts when running the restored container.

```bash
podman rm -f spring-crac-demo-container
```

### 5. Run the Restored Application

This command runs a new container from the restored image. You should notice a significantly faster startup time compared to running the original application.

```bash
podman run -it --rm -p 8080:8080 --privileged --name spring-crac-demo spring-crac-demo-restore
```