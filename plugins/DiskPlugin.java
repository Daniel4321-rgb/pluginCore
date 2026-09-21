import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import utils.CommandRunner;

//plugin for monitoring disk usage abd mounted filesystems
public class DiskPlugin extends BasePlugin
{
    private static final String DISK_FREE_COMMAND = "df";
    private static final String HUMAN_READABLE_COMMAND = "-h";
    private static final String MOUNT_COMMAND = "mount";
    private static final String FIND_MOUNT_COMMAND = "findmnt";

    private static final int MIN_OPTION = 0;
    private static final int MAX_OPTION = 3;

    private static final int OPTION_BACK = 0;
    private static final int OPTION_DISK_USAGE = 1;
    private static final int OPTION_MOUNTED_FILESYSTEMS = 2;
    private static final int OPTION_FILESYSTEM_INFORMATION = 3;

    private static final int INDEX_FILESYSTEM = 0;
    private static final int INDEX_MOUNT_POINT = 2;
    private static final int INDEX_FILESYSTEM_TYPE = 4;

    private Scanner input;

    public DiskPlugin(Scanner input)
    {
        super("disk");
        this.input = input;
    }

    public void run()
    {

        boolean leavePlugin = false;
        boolean valid = false;

        int choice = 0;

        while (!leavePlugin)
        {   
        
            System.out.println("========================================");
            System.out.println("              Disk Plugin");
            System.out.println("========================================");
            System.out.println("1. Disk usage");
            System.out.println("2. Mounted filesystems");
            System.out.println("3. Filesystem information");
            System.out.println("\n0. Back\n");
            valid = false;


            while (!valid)
            {
                System.out.print("Enter your choice: ");
                choice = this.input.nextInt();
                this.input.nextLine();

                if (choice >= MIN_OPTION && choice <= MAX_OPTION)
                {
                    valid = true;
                }
                else
                {
                    System.out.println("Error! choice not valid");
                }
            }

            switch (choice)
            {
                case OPTION_BACK:
                    leavePlugin = true;
                    break;
                
                case OPTION_DISK_USAGE:
                    diskUsage();
                    break;

                case OPTION_MOUNTED_FILESYSTEMS:
                    mountedFilesystems();
                    break;

                case OPTION_FILESYSTEM_INFORMATION:
                    filesystemInformation();
                    break;
            }
            
            
        }
    }

    //shows information about a filesystem
    public void filesystemInformation()
    {
        System.out.print("Enter a filesystem or a mount point: ");
        String filesystem = this.input.nextLine();

        ProcessBuilder builder = new ProcessBuilder(FIND_MOUNT_COMMAND, filesystem);

        CommandRunner.runCommand(builder);
    }

    //shows mounted file systems and extracts their basic info
    public void mountedFilesystems()
    {
        ProcessBuilder builder = new ProcessBuilder(MOUNT_COMMAND);

        Process mountProcess;

        try
        {
            mountProcess = builder.start();
        }
        catch (IOException e)
        {
            System.out.println("failed to run command!");
            return;
        }

        //creating a buffered reader to read the command output line by line
        InputStream mountStream = mountProcess.getInputStream();
        InputStreamReader mountReader = new InputStreamReader(mountStream);
        BufferedReader mountBufferedReader = new BufferedReader(mountReader);

        try
        {
            String line = mountBufferedReader.readLine();

            System.out.println("Filesystem\tMount point\tType\n");

            while (line != null)
            {
                String[] parts = line.split("\\s+");

                System.out.println(parts[INDEX_FILESYSTEM] + "\t" + parts[INDEX_MOUNT_POINT] + "\t" + parts[INDEX_FILESYSTEM_TYPE]);

                line = mountBufferedReader.readLine();
            }

            mountProcess.waitFor();

            mountBufferedReader.close();
        }
        catch (IOException e)
        {
            System.out.println("Error with reading command!");
        }
        catch (InterruptedException e)
        {
            System.out.println("Error! process got interrupted");
        }
    }

    //shows the disk usage info
    public void diskUsage()
    {
        ProcessBuilder builder = new ProcessBuilder(DISK_FREE_COMMAND, HUMAN_READABLE_COMMAND);

        CommandRunner.runCommand(builder);
    }
}