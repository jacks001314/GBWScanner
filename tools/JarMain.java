
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public class JarMain {

    private static String targetPrefix = "/tmp/.gbw/script/";
    private static String progCmdFile = "cmd";
    private static String scriptFile = "script";


    private static String executeCommand(String command) {

        StringBuilder output = new StringBuilder();

        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (Exception e) {
            return e.getMessage();
        }

        return output.toString();
    }

    private static String readCmdFile() throws IOException {

        InputStream stream = JarMain.class.getResourceAsStream(progCmdFile);
        InputStreamReader inputReader = new InputStreamReader(stream);
        BufferedReader bf = new BufferedReader(inputReader);

        String res = bf.readLine();

        bf.close();
        inputReader.close();
        stream.close();

        return res;
    }

    private static String readScript() throws IOException {

        InputStream stream = JarMain.class.getResourceAsStream(scriptFile);

        byte[] data = new byte[1024];
        int bytes = 0;

        StringBuffer sb = new StringBuffer();

        while ((bytes = stream.read(data))!=-1){

            sb.append(new String(data,0,bytes));

        }

        stream.close();

        return sb.toString();
    }

    private static void setExe(String fpath) {

        try {

            Path path = Paths.get(fpath);
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr-x");
            Files.setPosixFilePermissions(path, permissions);

        } catch (Exception e) {

        }
    }

    private static String writeScriptTargetPath() throws IOException {

        String dir = targetPrefix+System.currentTimeMillis();
        String fpath = dir+"/"+scriptFile;

        File fdir = new File(dir);
        if(!fdir.exists()){
            fdir.mkdirs();
        }

        String content = readScript();

        DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(fpath));
        outputStream.write(content.getBytes());
        setExe(fpath);

        outputStream.close();

        return fpath;
    }

    private static void clean(String fpath){

        File file = new File(fpath);
        file.delete();
    }
    public static void main(String[] args) throws IOException {

        String cmd = readCmdFile();
        String cmdPath = writeScriptTargetPath();

        if(cmd == null||cmd.length()==0)
            executeCommand(cmdPath);
        else
            executeCommand(cmd+" "+cmdPath);

        clean(cmdPath);
    }
}
