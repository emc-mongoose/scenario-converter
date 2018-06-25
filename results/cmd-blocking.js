function printToCL(cmd) {
    var cmdStdOut = new java.io.BufferedReader(
            new java.io.InputStreamReader(cmd.getInputStream())
    );
    cmd.waitFor();
    while(null != (nextLine = cmdStdOut.readLine())) {
            print(nextLine);
    }
    cmdStdOut.close();
}

var cmd_1 = new java.lang.ProcessBuilder()
    .command("sh", "-c", "for i in $(seq 0 9); do ps alx | grep java; sleep 1; done")
    .start();
cmd_1.waitFor();
printToCL( cmd_1 );
