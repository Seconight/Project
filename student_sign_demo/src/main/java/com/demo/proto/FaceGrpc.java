package com.demo.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.11.0)",
    comments = "Source: AIService.proto")
public final class FaceGrpc {

  private FaceGrpc() {}

  public static final String SERVICE_NAME = "example.Face";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getFaceDetectMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.demo.proto.DetectRequest,
      com.demo.proto.DetectReply> METHOD_FACE_DETECT = getFaceDetectMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.demo.proto.DetectRequest,
      com.demo.proto.DetectReply> getFaceDetectMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.demo.proto.DetectRequest,
      com.demo.proto.DetectReply> getFaceDetectMethod() {
    return getFaceDetectMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.demo.proto.DetectRequest,
      com.demo.proto.DetectReply> getFaceDetectMethodHelper() {
    io.grpc.MethodDescriptor<com.demo.proto.DetectRequest, com.demo.proto.DetectReply> getFaceDetectMethod;
    if ((getFaceDetectMethod = FaceGrpc.getFaceDetectMethod) == null) {
      synchronized (FaceGrpc.class) {
        if ((getFaceDetectMethod = FaceGrpc.getFaceDetectMethod) == null) {
          FaceGrpc.getFaceDetectMethod = getFaceDetectMethod = 
              io.grpc.MethodDescriptor.<com.demo.proto.DetectRequest, com.demo.proto.DetectReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "example.Face", "FaceDetect"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.demo.proto.DetectRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.demo.proto.DetectReply.getDefaultInstance()))
                  .setSchemaDescriptor(new FaceMethodDescriptorSupplier("FaceDetect"))
                  .build();
          }
        }
     }
     return getFaceDetectMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getFaceRecognizeMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.demo.proto.RecognizeRequest,
      com.demo.proto.RecognizeReply> METHOD_FACE_RECOGNIZE = getFaceRecognizeMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.demo.proto.RecognizeRequest,
      com.demo.proto.RecognizeReply> getFaceRecognizeMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.demo.proto.RecognizeRequest,
      com.demo.proto.RecognizeReply> getFaceRecognizeMethod() {
    return getFaceRecognizeMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.demo.proto.RecognizeRequest,
      com.demo.proto.RecognizeReply> getFaceRecognizeMethodHelper() {
    io.grpc.MethodDescriptor<com.demo.proto.RecognizeRequest, com.demo.proto.RecognizeReply> getFaceRecognizeMethod;
    if ((getFaceRecognizeMethod = FaceGrpc.getFaceRecognizeMethod) == null) {
      synchronized (FaceGrpc.class) {
        if ((getFaceRecognizeMethod = FaceGrpc.getFaceRecognizeMethod) == null) {
          FaceGrpc.getFaceRecognizeMethod = getFaceRecognizeMethod = 
              io.grpc.MethodDescriptor.<com.demo.proto.RecognizeRequest, com.demo.proto.RecognizeReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "example.Face", "FaceRecognize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.demo.proto.RecognizeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.demo.proto.RecognizeReply.getDefaultInstance()))
                  .setSchemaDescriptor(new FaceMethodDescriptorSupplier("FaceRecognize"))
                  .build();
          }
        }
     }
     return getFaceRecognizeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FaceStub newStub(io.grpc.Channel channel) {
    return new FaceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FaceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new FaceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FaceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new FaceFutureStub(channel);
  }

  /**
   */
  public static abstract class FaceImplBase implements io.grpc.BindableService {

    /**
     */
    public void faceDetect(com.demo.proto.DetectRequest request,
        io.grpc.stub.StreamObserver<com.demo.proto.DetectReply> responseObserver) {
      asyncUnimplementedUnaryCall(getFaceDetectMethodHelper(), responseObserver);
    }

    /**
     */
    public void faceRecognize(com.demo.proto.RecognizeRequest request,
        io.grpc.stub.StreamObserver<com.demo.proto.RecognizeReply> responseObserver) {
      asyncUnimplementedUnaryCall(getFaceRecognizeMethodHelper(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getFaceDetectMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.demo.proto.DetectRequest,
                com.demo.proto.DetectReply>(
                  this, METHODID_FACE_DETECT)))
          .addMethod(
            getFaceRecognizeMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.demo.proto.RecognizeRequest,
                com.demo.proto.RecognizeReply>(
                  this, METHODID_FACE_RECOGNIZE)))
          .build();
    }
  }

  /**
   */
  public static final class FaceStub extends io.grpc.stub.AbstractStub<FaceStub> {
    private FaceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private FaceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FaceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FaceStub(channel, callOptions);
    }

    /**
     */
    public void faceDetect(com.demo.proto.DetectRequest request,
        io.grpc.stub.StreamObserver<com.demo.proto.DetectReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getFaceDetectMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void faceRecognize(com.demo.proto.RecognizeRequest request,
        io.grpc.stub.StreamObserver<com.demo.proto.RecognizeReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getFaceRecognizeMethodHelper(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class FaceBlockingStub extends io.grpc.stub.AbstractStub<FaceBlockingStub> {
    private FaceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private FaceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FaceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FaceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.demo.proto.DetectReply faceDetect(com.demo.proto.DetectRequest request) {
      return blockingUnaryCall(
          getChannel(), getFaceDetectMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.demo.proto.RecognizeReply faceRecognize(com.demo.proto.RecognizeRequest request) {
      return blockingUnaryCall(
          getChannel(), getFaceRecognizeMethodHelper(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class FaceFutureStub extends io.grpc.stub.AbstractStub<FaceFutureStub> {
    private FaceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private FaceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FaceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FaceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.demo.proto.DetectReply> faceDetect(
        com.demo.proto.DetectRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getFaceDetectMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.demo.proto.RecognizeReply> faceRecognize(
        com.demo.proto.RecognizeRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getFaceRecognizeMethodHelper(), getCallOptions()), request);
    }
  }

  private static final int METHODID_FACE_DETECT = 0;
  private static final int METHODID_FACE_RECOGNIZE = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final FaceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(FaceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_FACE_DETECT:
          serviceImpl.faceDetect((com.demo.proto.DetectRequest) request,
              (io.grpc.stub.StreamObserver<com.demo.proto.DetectReply>) responseObserver);
          break;
        case METHODID_FACE_RECOGNIZE:
          serviceImpl.faceRecognize((com.demo.proto.RecognizeRequest) request,
              (io.grpc.stub.StreamObserver<com.demo.proto.RecognizeReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class FaceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FaceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.demo.proto.FaceServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Face");
    }
  }

  private static final class FaceFileDescriptorSupplier
      extends FaceBaseDescriptorSupplier {
    FaceFileDescriptorSupplier() {}
  }

  private static final class FaceMethodDescriptorSupplier
      extends FaceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    FaceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (FaceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FaceFileDescriptorSupplier())
              .addMethod(getFaceDetectMethodHelper())
              .addMethod(getFaceRecognizeMethodHelper())
              .build();
        }
      }
    }
    return result;
  }
}
