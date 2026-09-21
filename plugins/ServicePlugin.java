import java.util.Scanner;

import utils.CommandRunner;

//plugin for monitoring system services 
public class ServicePlugin extends BasePlugin
{
    private static final String RC_STATUS_COMMAND = "rc-status";
    private static final String RC_SERVICE_COMMAND = "rc-service";
    private static final String STATUS_COMMAND = "status";
    private static final String START_COMMAND = "start";
    private static final String STOP_COMMAND = "stop";

    private static final int MIN_OPTION = 0;
    private static final int MAX_OPTION = 4;

    private static final int OPTION_BACK = 0;
    private static final int OPTION_LIST = 1;
    private static final int OPTION_STATUS = 2;
    private static final int OPTION_START = 3;
    private static final int OPTION_STOP = 4;


    private Scanner input;

    public ServicePlugin(Scanner input)
    {
        super("service");
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
                            "            Service Plugin\n" + 
                            "========================================");
            System.out.println("1. List services");
            System.out.println("2. Get service status");
            System.out.println("3. Start service");
            System.out.println("4. Stop service");
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
                
                case OPTION_LIST:
                    listServices();
                    break;

                case OPTION_STATUS:
                    serviceStatus();
                    break;

                case OPTION_START:
                    startService();
                    break;

                case OPTION_STOP:
                    stopService();
                    break;
            }
            
            
        }

    }

    //stops a service
    public void stopService()
    {
        System.out.print("Enter a service name to stop: ");
        String serviceName = this.input.nextLine();

        ProcessBuilder builder = new ProcessBuilder(RC_SERVICE_COMMAND, serviceName, STOP_COMMAND);

        CommandRunner.runCommand(builder);
    }

    //starts a service
    public void startService()
    {
        System.out.print("Enter a service name to start: ");
        String serviceName = this.input.nextLine();

        ProcessBuilder builder = new ProcessBuilder(RC_SERVICE_COMMAND, serviceName, START_COMMAND);
        CommandRunner.runCommand(builder);

    }

    //prints a services status
    public void serviceStatus()
    {
        System.out.print("Enter a service name: ");

        String serviceName = this.input.nextLine();

        ProcessBuilder builder = new ProcessBuilder(RC_SERVICE_COMMAND, serviceName, STATUS_COMMAND);
        CommandRunner.runCommand(builder);

    }

    //prints all the services
    public void listServices()
    {
        ProcessBuilder builder = new ProcessBuilder(RC_STATUS_COMMAND);
        CommandRunner.runCommand(builder);

    }
}