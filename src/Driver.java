/**
 * Copyright (C) 2023 - 2024  Zachary A. Kissel 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import fileShareFiles.*;
import merrimackutil.cli.LongOption;
import merrimackutil.cli.OptionParser;
import merrimackutil.json.JsonIO;
import merrimackutil.json.types.JSONObject;
import merrimackutil.util.Tuple;
import modelFiles.Configuration;
import modelFiles.Host;
import modelFiles.Seed;
import modelFiles.SeedDatabase;


/**
 * This is the main service.Its job is to start up the file sharing service 
 * to handle requests as well as start to greedily download files as well as the 
 * file requested by the user.
 */
public class Driver
{
    public static boolean doHelp = false;               // True if help option present.
    private static Configuration conf = null;           // The configuration information.
    private static String configFile = "config.json";   // Default configuration file.
    private static SeedDatabase seeds;                  // The seed database for file downloads.

    /**
     * Prints the usage to the screen and exits.
     */
    public static void usage() {
        System.out.println("usage:");
        System.out.println("  p2p --config <config>");
        System.out.println("  p2p --help");
        System.out.println("options:");
        System.out.println("  -c, --config\t\tConfig file to use.");
        System.out.println("  -h, --help\t\tDisplay the help.");
        System.exit(1);
    }

    /**
     * Processes the command line arugments.
     * @param args the command line arguments.
     */
    public static void processArgs(String[] args)
    {
        OptionParser parser;
        boolean doHelp = false;
        boolean doConfig = false;

        LongOption[] opts = new LongOption[2];
        opts[0] = new LongOption("help", false, 'h');
        opts[1] = new LongOption("config", true, 'c');
        
        Tuple<Character, String> currOpt;

        parser = new OptionParser(args);
        parser.setLongOpts(opts);
        parser.setOptString("hc:");


        while (parser.getOptIdx() != args.length)
        {
            currOpt = parser.getLongOpt(false);

            switch (currOpt.getFirst())
            {
                case 'h':
                    doHelp = true;
                break;
                case 'c':
                    doConfig = true;
                    configFile = currOpt.getSecond();
                break;
                case '?':
                    System.out.println("Unknown option: " + currOpt.getSecond());
                    usage();
                break;
            }
        }

        // Verify that that this options are not conflicting.
        if ((doConfig && doHelp))
            usage();
        
        if (doHelp)
            usage();
        
        try 
        {
            loadConfig();
        } 
        catch (FileNotFoundException e) 
        {
            System.err.println("p2p: " + e);
            System.exit(1);
        }
    }

    /**
     * Loads the configuration file.
     * @throws FileNotFoundException if the configuration file could not be found.
     */
    public static void loadConfig() throws FileNotFoundException
    {
        JSONObject obj = JsonIO.readObject(new File(configFile));
        try
        { 
            conf = new Configuration(obj);
        }
        catch(InvalidObjectException ex)
        {
            System.err.println("p2p: invalid configuration file.");
            System.out.println(ex);
            System.exit(1);
        }
    }

    /**
     * Loads the seed files from the database. In general this would be on a remote 
     * server but, we  have added it locally.
     * @throws FileNotFoundException if the seed file could not be found.
     */
    public static void loadSeedDatabase() throws FileNotFoundException
    {
        JSONObject obj = JsonIO.readObject(new File(conf.getSeedFile()));
        try
        {
            seeds = new SeedDatabase(obj);
        }
        catch (InvalidObjectException ex) 
        {
            System.err.println("p2p: invalid seed file.");
            System.out.println(ex);
            System.exit(1);
        }

    }

    /**
     * Runs the command line interface portion of the p2p file sharing application.
     * @throws InterruptedException a donwload thread was interrupted.
     * @throws IOException data could not be accessed.
     */
    public static void doCLI() throws InterruptedException, IOException
    {
        String command;
        try (Scanner scan = new Scanner(System.in)) {
            boolean done = false;

            while (!done)
            {
                // Read a command.
                do 
                {   
                    System.out.print("> ");
                    command = scan.nextLine();
                } while (command.equals(""));

                // If the user enters .quit, we should stop the CLI and 
                // close the application.
                if (command.equalsIgnoreCase(".quit"))
                    done = true;
                else 
                {
                    // Get the file from the peers.
                    getFile(command);
                }
            }
        }
    }

    /**
     * Gets a file from the peer network.
     * @param fname the file name to download.
     * @throws InterruptedException 
     * @throws IOException 
     */
    public static void getFile(String fname) throws InterruptedException, IOException
    {
        Seed fileSeed;                      // The seed for the file requested.
        ArrayList<Host> hosts;              // The hosts that have the file. 
        HashMap<String, ArrayList<Integer>> chunks = new HashMap<>();   // The chunks to donwload from each host.
        DownloadThread[] downloaders;   // The download threads, one per host.


        // Get the seed file from the seed database. If the file is not found, catch 
        // the exception, print an error message and abort the getFile operation.
        try 
        {
            fileSeed = seeds.getSeed(fname);
        } 
        catch (FileNotFoundException e) 
        {
            System.out.println("No seed for file " + fname);
            return;
        }

        // Initialize all of the empyt lists that represent what chunks should be 
        // donwloaded from each host.
        hosts = fileSeed.getHosts();
        for (int i = 0; i < hosts.size(); i++)
            chunks.put(hosts.get(i).getAddress() + ":" + hosts.get(i).getPort(), new ArrayList<>());

        // Populate the lists of chunks as evenly as possible. Basically, we are going for 
        // each host to donwlad approximately the same number of chunks thus speeding up the 
        // download of the file.
        int currChunk = 1;
        int hostCounter = 0;

        while (currChunk <= fileSeed.getNumChunks())
        {
            ArrayList<Integer> lst = chunks.get(hosts.get(hostCounter).getAddress() + ":" + hosts.get(hostCounter).getPort());
            lst.add(currChunk);
            currChunk++;
            hostCounter = (hostCounter + 1) % hosts.size();
        }

        System.out.print("Downloading file . . . ");
        
        // Spin up all of the download threads.
        downloaders = new DownloadThread[hosts.size()];
        Thread[] dThreads = new Thread[hosts.size()];
        for (int i = 0; i < hosts.size(); i++)
        {
            downloaders[i] = new DownloadThread(hosts.get(i), chunks.get(hosts.get(i).getAddress() + ":" + hosts.get(i).getPort()), 
                fname);
            dThreads[i] = new Thread(downloaders[i]);
            dThreads[i].start();
        }
        
        // Join all of the threads together, which causes us to wait 
        // for all threads to complete work.
        for (int i = 0; i < hosts.size(); i++)
            dThreads[i].join();

        // Once all of the threads have joined, we can reassemble the chunks. 
        FileOutputStream out = new FileOutputStream(new File(fname));
        
        currChunk = 1;      // Start at the first chunk.
        hostCounter = 0;    // Start at the first host.

        // Write out the chunks in order to the correct file. 
        while (currChunk <= fileSeed.getNumChunks()) 
        {
            out.write(downloaders[hostCounter].getResults().get(currChunk));
            currChunk++;
            hostCounter = (hostCounter + 1) % hosts.size();
        }
        out.close();

        // Inform the user we have completed the download.
        System.out.println(" OK ");
    }

    /**
     * The entry point
     * @param args the command line arguments.
     * @throws IOException 
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException, IOException
    {
        FileShareThread share;

        if (args.length > 2)
            usage();

        processArgs(args); 

        // Load the seeds database for processing.
        loadSeedDatabase();

        // Start up the file sharing thread.
        System.out.print("Starting up file share thread . . . ");
        share = new FileShareThread(conf);
        Thread shareThread = new Thread(share);
        shareThread.start();
        System.out.println("[ DONE ]");

        // Download the files from the peers.
        doCLI();

        // We don't care about our file sharing thread, just crudely shutdown.
        System.exit(0);
    }
}