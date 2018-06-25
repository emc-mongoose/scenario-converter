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

var parentConfig_1 = {
  "item" : {
    "data" : {
      "size" : "10KB"
    }
  },
  "storage" : {
    "driver" : {
      "type" : "fs"
    }
  },
  "load" : {
    "step" : {
      "limit" : {
        "count" : 10000,
        "concurrency" : 10
      }
    }
  }
};

        var step_1 = PreconditionLoad
        .config(parentConfig_1)
        .config({
              "item" : {
                "output" : {
                  "path" : "/var/tmp/read-files-from-variable-path/%p{16;2}",
                  "file" : "read-files-from-variable-path.csv"
                }
              }
            })
        .run();

        var step_2 = ReadLoad
        .config(parentConfig_1)
        .config({
              "load" : {
                "type" : "read"
              },
              "item" : {
                "input" : {
                  "file" : "read-files-from-variable-path.csv"
                }
              }
            })
        .run();
