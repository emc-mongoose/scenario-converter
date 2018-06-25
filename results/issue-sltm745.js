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

for( var iteration = 1; iteration < 10; iteration += 1 ){

var parentConfig_1 = {
  "item" : {
    "data" : {
      "size" : "10KB"
    },
    "output" : {
      "path" : "issue-745"
    }
  },
  "storage" : {
    "driver" : {
      "type" : "s3"
    },
    "net" : {
      "http" : {
        "namespace" : "ns1"
      }
    }
  },
  "load" : {
    "step" : {
      "limit" : {
        "count" : 100000,
        "concurrency" : 1000
      }
    }
  }
};

        for( var i = 1; i < 5; i += 1 ){

        var parentConfig_2 = {
          "storage" : {
            "auth" : {
              "uid" : "LONGEVITY-" + "iteration" + ""
            }
          }
        };

                var step_1 = Load
                .config(parentConfig_1)
                .config(parentConfig_2)
                .run();
        };
};
