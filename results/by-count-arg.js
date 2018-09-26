for( var i1 = 0; i1 < N; i1 += 1 ){
    var cmd_1 = new java.lang.ProcessBuilder()
        .command("/bin/sh", "-c", "echo \'Hi there\'")
        .inheritIO()
        .start();
    cmd_1.waitFor();

};
