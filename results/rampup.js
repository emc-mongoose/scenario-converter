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

var size_seq = [0, "1KB", "1MB"];
for each ( size in size_seq ){

var parentConfig_1 = {
  "output" : {
    "metrics" : {
      "average" : {
        "period" : 0
      }
    }
  },
  "item" : {
    "data" : {
      "size" : size
    }
  },
  "load" : {
    "step" : {
      "limit" : {
        "count" : 1000000,
        "time" : "10s"
      }
    }
  }
};

        var threads_seq = [1, 10, 100];
        for each ( threads in threads_seq ){

        var parentConfig_2 = {
          "load" : {
            "step" : {
              "limit" : {
                "concurrency" : threads
              }
            }
          },
          "item" : {
            "output" : {
              "path" : "" + size + "_" + threads + "threads"
            }
          }
        };

                var step_1 = CreateLoad
                .config(parentConfig_1)
                .config(parentConfig_2)
                .config({
                      "item" : {
                        "output" : {
                          "file" : "" + size + "_" + threads + "threads.csv"
                        }
                      },
                      "storage" : {
                        "auth" : {
                          "uid" : "C" + threads + "_" + size + ""
                        }
                      },
                      "load" : {
                        "type" : "create"
                      }
                    })
                .run();

                var step_2 = ReadLoad
                .config(parentConfig_1)
                .config(parentConfig_2)
                .config({
                      "item" : {
                        "input" : {
                          "file" : "" + size + "_" + threads + "threads.csv"
                        }
                      },
                      "storage" : {
                        "auth" : {
                          "uid" : "R" + threads + "_" + size + ""
                        }
                      },
                      "load" : {
                        "type" : "read"
                      }
                    })
                .run();

                var step_3 = DeleteLoad
                .config(parentConfig_1)
                .config(parentConfig_2)
                .config({
                      "item" : {
                        "input" : {
                          "file" : "" + size + "_" + threads + "threads.csv"
                        }
                      },
                      "storage" : {
                        "auth" : {
                          "uid" : "D" + threads + "_" + size + ""
                        }
                      },
                      "load" : {
                        "type" : "delete"
                      }
                    })
                .run();
        };
};
