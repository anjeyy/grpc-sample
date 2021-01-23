package com.github.anjeyy.infrastructure.validation;

import com.google.protobuf.MessageLiteOrBuilder;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * Implement this interface to perform a request validation for incoming gRPC messages. Subsequently requests received
 * in {@link GrpcService @GrpcService} are validated.<br>
 * <b>Hint: </b> Also annotate class with {@link GrpcConstraint @GrpcConstraint} to be picked up.
 *
 * @author Andjelko Perisic (andjelko.perisic@gmail.com)
 * @see GrpcValidationResolver
 * @see GrpcConstraint
 */
public interface GrpcConstraintValidator<E extends MessageLiteOrBuilder> {

    /**
     * Method invoked to check wheter validation succeds. In case an exeception occurs a {@link
     * io.grpc.Status.Code#INTERNAL} is sent back to the client with the thrown exception message.
     *
     * @param request gRPC request
     * @return {@code true} if validation successfull, {@code false otherwise}
     */
    boolean isValid(E request);

}
