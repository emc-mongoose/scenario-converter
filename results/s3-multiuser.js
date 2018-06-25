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

var step_1 = CreateLoad
.config({
      "item" : {
        "data" : {
          "size" : "10KB"
        },
        "output" : {
          "path" : "bucket-%d(314159265){00}new java.util.ArrayList([0-99])",
          "file" : "objects.csv"
        }
      },
      "storage" : {
        "driver" : {
          "type" : "s3"
        },
        "auth" : {
          "uid" : "user-%d(314159265){00}new java.util.ArrayList([0-99])",
          "file" : "credentials.csv"
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
    })
.run();
