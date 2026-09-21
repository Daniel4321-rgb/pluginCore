import java.util.Scanner;
public class Main
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        PluginManager plugins = new PluginManager();

        int choice = 0;
        boolean leaveProgram = false;
        boolean valid = false;

        //adds all the plugins to the plugin manager
        plugins.addPlugin(new ProcessPlugin(input));
        plugins.addPlugin(new ServicePlugin(input));
        plugins.addPlugin(new NetworkPlugin(input));
        plugins.addPlugin(new DiskPlugin(input));


        while (!leaveProgram)
        {
            System.out.println("========================================\n" + 
                        "              PluginCore\n" + 
                        "========================================");

            System.out.println("\nAvailable Plugins:\n");

            //prints the plugins dynamically
            for (int i = 0; i < plugins.getPluginCount(); i++)
            {
                System.out.println(i + 1 + ". " + plugins.getPlugin(i).getName());
            }

            System.out.println("\n0. Exit\n");

            valid = false;

            while (!valid)
            {
                System.out.print("Choose a plugin: ");
                choice = input.nextInt();
                input.nextLine();

                if (choice > plugins.getPluginCount() || choice < 0)
                {
                    System.out.println("Error: choice not valid!");
                }

                else
                {
                    valid = true;
                }
            }

            if (choice == 0)
            {
                leaveProgram = true;
            }

            else
            {   
                //gets the selected plugin and runs it
                Plugin selectedPlugin = plugins.getPlugin(choice - 1);

                selectedPlugin.run();
            }
            

        }
        System.out.println("\nExiting PluginCore... Goodbye!");

        input.close();
    }
}