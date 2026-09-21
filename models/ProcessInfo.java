package models;

//stores info about a process
public class ProcessInfo 
{

    private String name;
    private int pid;

    public ProcessInfo(String name, int pid)
    {
        this.name = name;
        this.pid = pid;
    }

    public String getName()
    {
        return this.name;
    }
    
    public int getPid()
    {
        return this.pid;
    }
    
}
