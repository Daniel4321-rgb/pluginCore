package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//utility class used by many different plugins
public class CommandRunner
{
    //runs a command using the given builder and prints its output
    public static void runCommand(ProcessBuilder builder)
    {
        //also include errors and warnings
        builder.redirectErrorStream(true);
        Process rcProcess;

        try
        {
            rcProcess = builder.start();
        }
        catch (IOException e)
        {
            System.out.println("Error while running a command!");
            return;
        }

        //creating a readable buffer to read the process output line by line
        InputStream rcStream = rcProcess.getInputStream();
        InputStreamReader rcReader = new InputStreamReader(rcStream);
        BufferedReader rcBufferedReader = new BufferedReader(rcReader);

        try
        {
            String line = rcBufferedReader.readLine();

            while (line != null)
            {
                System.out.println(line);
                line = rcBufferedReader.readLine();
            }

            rcProcess.waitFor();
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
}