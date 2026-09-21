//base class for all plugins
//abstract because BasePlugin is not created with an object
public abstract class BasePlugin implements Plugin
{
    private String name;

    public abstract void run();

    public String getName()
    {
        return this.name;
    }

    public BasePlugin(String name)
    {
        this.name = name;
    }
}