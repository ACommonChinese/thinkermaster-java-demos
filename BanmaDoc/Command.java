import java.util.ArrayList;
import java.util.List;
// java BanmaDoc -name FrameworkName -version 1.0 -headers /Users/banma-1118/IdeaProjects/BanmaDoc/src/BanmaDoc.java -dest .
class Item {
    private String name;
    private List<String> components = new ArrayList<String>();

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getComponents() {
        return components;
    }
    public void setComponents(List<String> components) {
        this.components = components;
    }
    public Item(String name, String[] args, int startIndex) {
        this.name = name;
        for (int i = startIndex; i < args.length; i++) {
            String component = args[i];
            if (component != null) {
                if (component.startsWith("-")) {
                    break;
                } else {
                    components.add(component);
                }
            }
        }
    }
}

public class Command {
    /// 库名
    private String name;
    /// 版本号
    private String version = "1.0";
    /// 目标路径
    private String dest = ".";
    /// 头文件路径
    private List<String> headers = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public Command(String[] args) {
        List<Item> items = new ArrayList<Item>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                Item item = new Item(arg.substring(1), args, i+1);
                items.add(item);
            }
        }
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase("name")) {
                this.name = item.getComponents().get(0);
            } else if (item.getName().equalsIgnoreCase("version")) {
                this.version = item.getComponents().get(0);
            } else if (item.getName().equalsIgnoreCase("dest")) {
                this.dest = item.getComponents().get(0);
            } else if (item.getName().equalsIgnoreCase("headers")) {
                this.headers = item.getComponents();
            }
        }
    }

    @Override
    public String toString() {
        return "name: " + name + "\n"
                + "version: " + version + "\n"
                + "dest: " + dest + "\n"
                + "headers: " + headers + "\n";
    }
}
