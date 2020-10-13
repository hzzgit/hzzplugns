import java.io.*;

/**
 * 执行cmd方法的工具类
 */
public class RunCmdUtil {
    public static String LineSeparator = "\n";

    public RunCmdUtil() {
    }

    public static String getOSName() {
        String osName = System.getProperty("os.name");
        return osName;
    }

    public static boolean isWindowsOS() {
        String osName = getOSName();
        return osName.contains("Windows");
    }

    public static boolean isLinuxOS() {
        String osName = getOSName();
        return osName.contains("Linux");
    }

    public static String exec(String cmd, File dir) {
        return exec(cmd, dir, true, (String)null);
    }

    public static String exec(String cmd, File dir, String encoding) {
        return exec(cmd, dir, true, encoding);
    }

    public static String exec(String cmd, File dir, boolean print, String encoding) {
        try {
            StringBuilder resb = new StringBuilder();
            Runtime run = Runtime.getRuntime();
            if (print) {
                System.out.println("执行命令：" + cmd);
            }

            Process p = run.exec(cmd, (String[])null, dir);
            InputStream ins = p.getInputStream();
            InputStream ers = p.getErrorStream();
            Thread thread1 = new Thread(new RunCmdUtil.ReadRespThread(ins, System.out, resb, print, encoding));
            thread1.start();
            Thread thread2 = new Thread(new RunCmdUtil.ReadRespThread(ers, System.err, resb, print, encoding));
            thread2.start();
            p.waitFor();
            thread1.join(200L);
            thread2.join(200L);
            return resb.toString();
        } catch (Exception var11) {
            var11.printStackTrace();
            return "";
        }
    }

    static {
        LineSeparator = System.getProperty("line.separator");
    }

    static class ReadRespThread implements Runnable {
        private InputStream ins = null;
        private BufferedReader bfr = null;
        private PrintStream out;
        private StringBuilder resb;
        private boolean print;

        public ReadRespThread(InputStream ins, PrintStream out, StringBuilder resb, boolean print, String encoding) {
            this.ins = ins;
            this.out = out;
            this.resb = resb;
            this.print = print;

            try {
                String charset = System.getProperty("sun.jnu.encoding");
                if (encoding != null) {
                    charset = encoding;
                }

                this.bfr = new BufferedReader(new InputStreamReader(ins, charset));
            } catch (Exception var7) {
                var7.printStackTrace();
            }

        }


        public void run() {
            try {
                for(String line = this.bfr.readLine(); line != null; line = this.bfr.readLine()) {
                    if (this.print) {
                        this.out.println(line);
                    }

                    this.resb.append(line + RunCmdUtil.LineSeparator);
                }
            } catch (IOException var2) {
                var2.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        String cmd = "git ls-remote --tags";
        String tagstr = RunCmdUtil.exec(cmd, new File("D:\\hzzmysoft\\myspace\\hzzcloud"));
        String javastr=RunCmdUtil.exec("java -version", new File("D:\\hzzmysoft\\myspace\\hzzcloud"));
        System.out.println(tagstr);
        System.out.println(javastr);
        System.out.println(1);
    }
}

