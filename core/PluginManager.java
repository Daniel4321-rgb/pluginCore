import java.util.ArrayList;

//manages the plugin used by PluginCore
public class PluginManager
{
    private ArrayList<Plugin> plugins;

    public PluginManager()
    {
        this.plugins = new ArrayList<>();
    }

    //adds plugin to the manager
    public void addPlugin(Plugin plugin)
    {
        this.plugins.add(plugin);
    }

    //gets a plugin by its index
    public Plugin getPlugin(int index)
    {
        if (this.plugins.size() <= index)
        {
            return null;
        }
        return this.plugins.get(index);
    }

    //gets the num of plugins
    public int getPluginCount()
    {
        return this.plugins.size();
    }
}