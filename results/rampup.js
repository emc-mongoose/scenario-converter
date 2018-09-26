var size_seq = [0, "1KB", "1MB"];
for each (size in size_seq){
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
    "op" : {
      "limit" : {
        "count" : 1000000
      }
    },
    "step" : {
      "limit" : {
        "time" : "10s"
      }
    }
  }
};

    var threads_seq = [1, 10, 100];
    for each (threads in threads_seq){
    var parentConfig_2 = {
      "item" : {
        "output" : {
          "path" : "" + size + "_" + threads + "threads"
        }
      },
      "storage" : {
        "driver" : {
          "limit" : {
            "concurrency" : threads
          }
        }
      }
    };

        CreateLoad
            .config(parentConfig_1)
            .config(parentConfig_2)
            .config({
              "load" : {
                "step" : {
                  "id" : "C" + threads + "_" + size + ""
                }
              },
              "item" : {
                "output" : {
                  "file" : "" + size + "_" + threads + "threads.csv"
                }
              }
            })
            .run();

        ReadLoad
            .config(parentConfig_1)
            .config(parentConfig_2)
            .config({
              "load" : {
                "step" : {
                  "id" : "R" + threads + "_" + size + ""
                }
              },
              "item" : {
                "input" : {
                  "file" : "" + size + "_" + threads + "threads.csv"
                }
              }
            })
            .run();

        DeleteLoad
            .config(parentConfig_1)
            .config(parentConfig_2)
            .config({
              "load" : {
                "step" : {
                  "id" : "D" + threads + "_" + size + ""
                }
              },
              "item" : {
                "input" : {
                  "file" : "" + size + "_" + threads + "threads.csv"
                }
              }
            })
            .run();

    };
};
