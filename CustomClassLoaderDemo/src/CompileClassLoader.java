import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

// 1. javac CompileClassLoader
// 2. java CompileClassLoader Hello 你好中国
// 运行结果:
// 运行Hello的参数: 你好中国
public class CompileClassLoader extends ClassLoader {
    public static void main(String[] args) throws Exception {
        // 示例: java CompileClassLoader Hello 测试
        if (args.length < 1) {
            System.out.println("缺少目标类, 请按如下格式运行Java源文件");
            System.out.println("java CompileClassLoader ClassName Parameter");
        }
        // 第一个参数是需要运行的类
        String progClass = args[0]; // ClassName
        // 参数
        String[] progArgs = new String[args.length-1];
        System.arraycopy(args, 1, progArgs, 0, progArgs.length);
        CompileClassLoader loader = new CompileClassLoader();
        // 加载需要运行的类
        Class<?> clazz = loader.loadClass(progClass);
        // 获取需要运行的类的主方法
        Method main = clazz.getMethod("main", (new String[0]).getClass());
        Object argsArray[] = {progArgs};
        main.invoke(null, argsArray);
    }

    @Override
    // 核心方法: 重写ClassLoader的findClass方法
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class clazz = null;
        // 将包路径中的(.)替换成斜线(/)
        String fileStub = name.replace(".", "/");
        String javaFileName = fileStub + ".java";
        String classFileName = fileStub + ".class";
        File javaFile = new File(javaFileName);
        File classFile = new File(classFileName);
        // 当指定的Java源文件存在, 且Class文件不存在, 或者Java源文件
        // 的修改时间比Class文件的修改时间更晚时, 重新编译
        if (javaFile.exists() && !(classFile.exists()) ||
            javaFile.lastModified() > classFile.lastModified()) {
            try {
                // 如果编译失败, 或该Class文件不存在
                if (!compile(javaFileName) || !classFile.exists()) {
                    throw new ClassNotFoundException("ClassNotFoundException:" + javaFileName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 如果class文件存在, 调用系统方法转成Class对象
        if (classFile.exists()) {
            try {
                byte[] raw = getBytes(classFileName);
                // defineClass 内置final方法, byte[] --> Class对象
                clazz = defineClass(name, raw, 0, raw.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (clazz == null) {
            throw new ClassNotFoundException(name);
        }
        return clazz;
    }

    // 编译指定Java文件的方法
    private boolean compile(String javaFile) throws IOException {
        System.out.println("CompileClassLoader:正在编译 " + javaFile + "...");
        // 调用系统的javac命令
        Process p = Runtime.getRuntime().exec("javac " + javaFile);
        try {
            // 其他线程都等待这个线程完成
            p.waitFor();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        // 获取javac线程的退出值
        int ret = p.exitValue();
        return ret == 0; // 0是成功
    }

    // 读取文件内容到byte[]中
    private byte[] getBytes(String fileName) throws IOException {
        File file = new File(fileName);
        long len = file.length();
        byte[] raw = new byte[(int)len];
        try (FileInputStream fis = new FileInputStream(file)) {
            // 一次读取class文件的全部二进制数据
            int r = fis.read(raw);
            if (r != len) {
                throw new IOException("无法读取全部文件: " + r + " != " + len);
            }
            return raw;
        }
    }
}
