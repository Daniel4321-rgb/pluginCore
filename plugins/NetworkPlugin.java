import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

import utils.CommandRunner;

//plugin for monitoring network interface and traffic
public class NetworkPlugin extends BasePlugin
{

    private static final String IP_COMMAND = "ip";
    private static final String LINK_COMMAND = "link";
    private static final String SHOW_COMMAND = "show";
    private static final String ADDR_COMMAND = "addr";

    private static final String PATH_NETWORK_STATS = "/proc/net/dev";

    private static final int MIN_OPTION = 0;
    private static final int MAX_OPTION = 4;

    private static final int OPTION_BACK = 0;
    private static final int OPTION_LIST = 1;
    private static final int OPTION_STATUS = 2;
    private static final int OPTION_IP_AND_MAC = 3;
    private static final int OPTION_TRAFFIC = 4;

    private static final int INDEX_INTERFACE_NAME = 0;
    private static final int INDEX_RX_BYTES = 1;
    private static final int INDEX_RX_PACKETS = 2;
    private static final int INDEX_TX_BYTES = 9;
    private static final int INDEX_TX_PACKETS = 10;

    private Scanner input;

    public NetworkPlugin(Scanner input)
    {
        super("network");
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
            System.out.println("              Network Plugin");
            System.out.println("========================================");

            System.out.println("1. List interfaces");
            System.out.println("2. Interface status");
            System.out.println("3. IP & MAC information");
            System.out.println("4. Traffic statistics");

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
                    listInterfaces();
                    break;

                case OPTION_STATUS:
                    interfaceStatus();
                    break;

                case OPTION_IP_AND_MAC:
                    ipAndMacInformation();
                    break;

                case OPTION_TRAFFIC:
                    trafficStats();
                    break;
            }
            
            
        }

    }

    //shows the traffic stats
    public void trafficStats()
    {

        FileReader reader;
        BufferedReader bufferedReader;

        try
        {
            reader = new FileReader(PATH_NETWORK_STATS);
            bufferedReader = new BufferedReader(reader);
        }
        catch (IOException e)
        {
            System.out.println("Error with opening a file!");
            return;
        }

        try
        {
            String line = bufferedReader.readLine();

            while (line != null)
            {
                String[] parts = line.trim().split("\\s+");

                if (parts[INDEX_INTERFACE_NAME].contains(":"))
                {
                    System.out.println("\nInterface " + parts[INDEX_INTERFACE_NAME]);
                    System.out.println("Received:");
                    System.out.println(parts[INDEX_RX_BYTES] + " bytes");
                    System.out.println(parts[INDEX_RX_PACKETS] + " packets");

                    System.out.println("\nTransmitted:");
                    System.out.println(parts[INDEX_TX_BYTES] + " bytes");
                    System.out.println(parts[INDEX_TX_PACKETS] + " packets\n");

                }

                line = bufferedReader.readLine();
            }
            
            bufferedReader.close();
        }
        catch (IOException e)
        {
            System.out.println("Error with reading the file!");
        }

    }

    //shows info about the ip and the mac of an interface
    public void ipAndMacInformation()
    {
        System.out.print("Enter an interface name: ");
        String interfaceName = this.input.nextLine();

        ProcessBuilder builder = new ProcessBuilder(IP_COMMAND, ADDR_COMMAND, SHOW_COMMAND, interfaceName);

        CommandRunner.runCommand(builder);
    }


    //shows the status of an interface
    public void interfaceStatus()
    {
        System.out.print("Enter an interface name: ");
        String interfaceName = this.input.nextLine();

        ProcessBuilder builder = new ProcessBuilder(IP_COMMAND, LINK_COMMAND, SHOW_COMMAND, interfaceName);

        CommandRunner.runCommand(builder);
    }


    //shows all the interfaces
    public void listInterfaces()
    {
        ProcessBuilder builder = new ProcessBuilder(IP_COMMAND, LINK_COMMAND);
        CommandRunner.runCommand(builder);
    }
}