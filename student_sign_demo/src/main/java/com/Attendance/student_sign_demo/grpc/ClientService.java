package com.Attendance.student_sign_demo.grpc;
import com.demo.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
public class ClientService {
    private Logger logger= LoggerFactory.getLogger(ClientService.class);
    /** 手动配置grpc的url和端口 */
    private final ManagedChannel channel;
    private final FaceGrpc.FaceBlockingStub blockingStub;
    public ClientService() {
        System.out.println("尝试连接...");
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forAddress("localhost",50051).usePlaintext(true);
        channel = channelBuilder.build();
        blockingStub = FaceGrpc.newBlockingStub(channel);
        System.out.println("连接到grpc服务");
    }
    public String FaceDetect(String Id,String shouldStudents) {
        DetectRequest request= DetectRequest.newBuilder().setShouldStudents(shouldStudents).setId(Id).build();
        DetectReply reply = null;
        try {
            reply=blockingStub.faceDetect(request);
        }catch (StatusRuntimeException e){
            logger.info("{} RPC failed:{}", Level.WARN,e.getStatus());
            return null;
        }
        return reply.getActualStudents();
    }
    public String FaceRecognize(String Id) {
        RecognizeRequest request= RecognizeRequest.newBuilder().setStudentId(Id).build();
        RecognizeReply reply;
        try {
            reply=blockingStub.faceRecognize(request);
        }catch (StatusRuntimeException e){
            logger.info("{} RPC failed:{}", Level.WARN,e.getStatus());
            return null;
        }
        return reply.getEncodings();
    }

}
