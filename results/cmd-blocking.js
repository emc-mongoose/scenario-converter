var cmd_1 = new java.lang.ProcessBuilder()
    .command("sh", "-c", "for i in $(seq 0 9); do ps alx | grep java; sleep 1; done")
    .inheritIO()
    .start();
cmd_1.waitFor();

