import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String studentNo = "0121810880204";
        String encodingFilePath=PathUtil.demoPath+"/userFace/encoding"+studentNo+".txt"; //定义encoding文件的存放位置
        try{
//            String exe="cmd";
//            String command="d: && cd "+PathUtil.demoPath+" &&face_encoding.py "+studentNo;
//            String[] cmdArr = new String[] { exe, command };
//            Process process = Runtime.getRuntime().exec(cmdArr);
            Process process = Runtime.getRuntime().exec(
                    "cmd /c "+PathUtil.demoPath+"/face_encoding.py "+studentNo);
            //Process process = Runtime.getRuntime().exec("cmd.exe /c start "+PathUtil.demoPath+"/runEncode.bat "+studentNo);
            //Process process = Runtime.getRuntime().exec("cmd /k start "+PathUtil.demoPath+"/face_encoding.py " +
            //        studentNo+" "+PathUtil.demoPath);

        }catch (Exception e){
            e.printStackTrace();
        }



    }
}
