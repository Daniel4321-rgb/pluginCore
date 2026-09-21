import java.nio.file.Path;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

import models.ProcessInfo;

//plugin for monitoring running processes
public class ProcessPlugin extends BasePlugin
{

    private static final String PROC_PATH = "/proc";
    private static final String COMM = "comm";
    private static final String STATUS = "status";
    private static final String STATE = "State:";
    private static final String MEMORY_PROCESS = "VmRSS:";
    
    private static final int INDEX_STATE_PROC = 1;
    private static final int INDEX_MEMORY_PROC = 1;
    private static final int PROCESS_INFO_SIZE = 4;

    private static final int INDEX_INFO_PID = 0;
    private static final int INDEX_INFO_NAME = 1;
    private static final int INDEX_INFO_STATE = 2;
    private static final int INDEX_INFO_MEMORY = 3;

    private static final int MIN_OPTION = 0;
    private static final int MAX_OPTION = 6;

    private static final int OPTION_BACK = 0;
    private static final int OPTION_LIST = 1;
    private static final int OPTION_INFO = 2;
    private static final int OPTION_SEARCH = 3;
    private static final int OPTION_FILTER = 4;
    private static final int OPTION_SORT = 5;
    private static final int OPTION_STATS = 6;

    private Scanner input;

    public ProcessPlugin(Scanner input)
    {
        super("process");
        this.input = input;
    }


    public void run()
    {
        boolean leavePlugin = false;
        boolean valid = false;

        int choice = 0;

        while (!leavePlugin)
        {

        
            System.out.println("========================================\n" + 
                            "            Process Plugin\n" + 
                            "========================================");

            System.out.println("1. List processes");
            System.out.println("2. Get process info");
            System.out.println("3. Search process");
            System.out.println("4. Filter processes");
            System.out.println("5. Sort processes");
            System.out.println("6. Process statistics");

            System.out.println("\n0. Back\n");

            valid = false;

            while (!valid)
            {
                System.out.print("Choose an option: ");
                choice = this.input.nextInt();
                this.input.nextLine();

                if (choice >= MIN_OPTION && choice <= MAX_OPTION)
                {
                    valid = true;
                }
                else
                {
                    System.out.println("Error: choice not valid!");
                }
            }

            switch (choice)
            {
                case OPTION_BACK:
                    leavePlugin = true;
                    break;

                case OPTION_LIST:
                    listProcesses();
                    break;

                case OPTION_INFO:

                    System.out.print("Enter PID: ");
                    int pid = input.nextInt();
                    this.input.nextLine();

                    String[] info = getProcessInfo(pid);

                    if (info == null)
                    {
                        System.out.println("Error: could not retrieve process info!");
                    }
                    else
                    {
                        System.out.println("\nPID:\t" + info[INDEX_INFO_PID]);
                        System.out.println("Name:\t" + info[INDEX_INFO_NAME]);
                        System.out.println("State:\t" + info[INDEX_INFO_STATE]);
                        System.out.println("Memory:\t" + info[INDEX_INFO_MEMORY]);
                    }

                    break;


                case OPTION_SEARCH:
                    searchProcesses();
                    break;

                case OPTION_FILTER:
                    filterProcesses();
                    break;

                case OPTION_SORT:
                    sortProcesses();
                    break;

                case OPTION_STATS:
                    processStats();
                    break;
            }
            
        }

    }

    //lists all the processes
    public void listProcesses()
    {

        System.out.println("=== Running Processes ===\n");
        System.out.println("PID\tCOMMAND");
    
        
        ArrayList<ProcessInfo> processes = getProcesses();

    
        for (ProcessInfo process : processes)
        {
            System.out.println(process.getPid() + "\t" + process.getName());
        }
        
    }

    //gets the process name by its pid
    public String getProcessName(int pid)
    {
        Path path = Path.of(PROC_PATH);
        String processName = "";

        path = path.resolve(Integer.toString(pid));

        path = path.resolve(COMM);

        try
        {
            processName = Files.readString(path).trim();
            return processName;        
        }

        catch(IOException e)
        {
            return null;
        }
    }

    //gets the state of a processes state by its pid
    public String getProcessState(int pid)
    {
        Path path = Path.of(PROC_PATH);

        path = path.resolve(Integer.toString(pid));

        path = path.resolve(STATUS);

        try
        {
            String content = Files.readString(path);
            String[] lines = content.split("\n");

            for (String line : lines)
            {
                if (line.startsWith(STATE))
                {
                    return line.split("\\s+")[INDEX_STATE_PROC];
                }
            }

        }

        catch (IOException e)
        {
            return null;
        }

        return null;
    }

    //gets the memory used by a process by its pid
    public String getProcessMemory(int pid)
    {
        Path path = Path.of(PROC_PATH);

        path = path.resolve(Integer.toString(pid));
        path = path.resolve(STATUS);

        try
        {
            String content = Files.readString(path);
            String[] lines = content.split("\n");

            for (String line : lines)
            {
                if (line.startsWith(MEMORY_PROCESS))
                {
                    return line.split("\\s+")[INDEX_MEMORY_PROC];
                }
            }
        }

        catch (IOException e)
        {
            return null;
        }

        return null;
    }

    //gets info about a process by its pid
    public String[] getProcessInfo(int pid)
    {
        String[] info = new String[PROCESS_INFO_SIZE];
        String temp;

        info[INDEX_INFO_PID] = Integer.toString(pid);
        
        temp = getProcessName(pid);

        if (temp == null)
        {
            return null;
        }

        info[INDEX_INFO_NAME] = temp;

        temp = getProcessState(pid);

        if (temp == null)
        {
            return null;
        }

        info[INDEX_INFO_STATE] = temp;

        temp = getProcessMemory(pid);

        if (temp == null)
        {
            return null;
        }

        info[INDEX_INFO_MEMORY] = temp;

        return info;

    }

    //gets all the processes running on the pc
    public ArrayList<ProcessInfo> getProcesses()
    {
        Path proc = Path.of(PROC_PATH);
        ArrayList<ProcessInfo> processes = new ArrayList<>();


        try (Stream<Path> entries = Files.list(proc))
        {

            for (Path entry : entries.toList())
            {

                String filename = entry.getFileName().toString();

                boolean onlyDigits = true;
                for (char ch : filename.toCharArray())
                {
                    if (!Character.isDigit(ch))
                    {
                        onlyDigits = false;
                        break;
                    }
                }

                if (onlyDigits)
                {
                    Path commFile = entry.resolve(COMM);

                    String processName = Files.readString(commFile).trim();
                    
                    ProcessInfo processInfo = new ProcessInfo(processName, Integer.parseInt(filename));

                    processes.add(processInfo);

                }
            }

        }
        catch (IOException e)
        {
            System.out.println("Error reading /proc: " + e.getMessage());
            return new ArrayList<>();
        }
        

        return processes;
    }

    //searches processes by a term and prints them
    public void searchProcesses()
    {
        ArrayList<ProcessInfo> processes = getProcesses();

        System.out.print("Enter a process to search: ");
        String searchTerm = this.input.nextLine();

        System.out.println("PID\tCOMMAND");

        for (ProcessInfo process : processes)
        {
            if (process.getName().contains(searchTerm))
            {
                System.out.println(process.getPid() + "\t" + process.getName());
            }
        }

    }

    //filters processes and shows them
    public void filterProcesses()
    {
        ArrayList<ProcessInfo> processes = getProcesses();

        System.out.print("Enter minimum name length: ");
        int minLength = this.input.nextInt();
        this.input.nextLine();
    
        System.out.println("PID\tCOMMAND");

        for (ProcessInfo process : processes)
        {
            if (process.getName().length() >= minLength)
            {
                System.out.println(process.getPid() + "\t" + process.getName());
            }
        }

    }

    //used as a comparator to sort processes
    public int compareNamesAlphabet(String a, String b)
    {
        return a.compareTo(b);
    }

    //sorts processes and shows them
    public void sortProcesses()
    {
        ArrayList<ProcessInfo> processes = getProcesses();

        //sorts the processes by their names alphabetically
        processes.sort((a, b) -> compareNamesAlphabet(a.getName(), b.getName()));

        System.out.println("PID\tCOMMAND");

        for (ProcessInfo process : processes)
        {
            System.out.println(process.getPid() + "\t" + process.getName());
        }
    }

    //shows the processes stats
    public void processStats()
    {
        ArrayList<ProcessInfo> processes = getProcesses();


        int shortestNameLength = Integer.MAX_VALUE;
        int longestNameLength = 0;
        int sumNamesLength = 0;

        String shortestName = "";
        String longestName = "";

        for (ProcessInfo process : processes)
        {
            if (process.getName().length() > longestNameLength)
            {
                longestName = process.getName();
                longestNameLength = longestName.length();
            }
            if (process.getName().length() < shortestNameLength)
            {
                shortestName = process.getName();
                shortestNameLength = shortestName.length();
            }

            sumNamesLength += process.getName().length();
        }

        double avgNameLength = (double)sumNamesLength / processes.size();


        System.out.println("STATS:");

        System.out.println("Total processes: " + processes.size());

        System.out.println("Longest process name: " + longestName + " (" + longestNameLength + " characters)");

        System.out.println("Shortest process name: " + shortestName + " (" + shortestNameLength + " characters)");

        System.out.println("Average process name length: " + String.format("%.2f", avgNameLength) + " characters");

    }

}