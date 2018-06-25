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

for( var i1 = 0; i1 < N; i1 += 1 ){

        var cmd_1 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "echo \"Hi there\"")
            .start();
        cmd_1.waitFor();
        printToCL( cmd_1 );
};
