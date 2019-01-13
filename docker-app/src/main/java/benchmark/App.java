package benchmark;

/*
import org.testng.TestNG;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

public class App {

    private static String[] suiteXmls = new File("testng").list();

    public static void main(final String[] args) {

        var usage = "Usage: (<client> [<host>] [<port>]) | list";

        var helpArgs = new HashSet<>(
                Arrays.asList(
                        "--help",
                        "-help",
                        "--h",
                        "-h"
                )
        );

        if (args.length == 0 || args.length > 3 || Arrays.stream(args).anyMatch(helpArgs::contains)) {
            System.out.println(usage);
            System.exit(1);
        }

        if (args[0].equals("list")) {
            for (var f : suiteXmls) {
                System.out.println(f);
            }
            System.exit(0);
        }

        var suiteXml = args[0];
        if (Arrays.stream(suiteXmls).noneMatch(suiteXml::equals)) {
            System.err.println(suiteXml + " is not a valid client.  Use 'list' to see options.");
            System.exit(1);
        }

        var host = "localhost";
        var port = 8080;
        if (args[1] != null) {
            host = args[1];
        }

        if (args[2] != null) {
            port = Integer.parseInt(args[2]);
        }

        runTest(suiteXml, host, port);
    }

    private static void runTest(String suiteXml, String host, int port) {
        System.setProperty("bm.host", host);
        System.setProperty("bm.port", port + "");
        TestNG.main(new String[] {suiteXml});
    }
}
*/
