
var size_seq = [0, "1KB", "1MB"];
for( size in size_seq ){

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
      size : size
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
        for( threads in threads_seq ){

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
              "path" : "" + size + "_" + threads + threads
            }
          }
        };

                var step_1 = CreateLoad();
                step_1.config(parentConfig_1);
                step_1.config(parentConfig_2);
                step_1.config({
                      "load" : {
                        "type" : "create",
                        "step" : {
                          "id" : "C" + threads + "_" + size + ""
                        }
                      },
                      "item" : {
                        "output" : {
                          "file" : "" + size + "_" + threads + "threads.csv"
                        }
                      }
                    });
                step_1.run();

                var step_2 = ReadLoad();
                step_2.config(parentConfig_1);
                step_2.config(parentConfig_2);
                step_2.config({
                      "load" : {
                        "type" : "read",
                        "step" : {
                          "id" : "R" + threads + "_" + size + ""
                        }
                      },
                      "item" : {
                        "input" : {
                          "file" : "" + size + "_" + threads + "threads.csv"
                        }
                      }
                    });
                step_2.run();

                var step_3 = DeleteLoad();
                step_3.config(parentConfig_1);
                step_3.config(parentConfig_2);
                step_3.config({
                      "load" : {
                        "type" : "delete",
                        "step" : {
                          "id" : "D" + threads + "_" + size + ""
                        }
                      },
                      "item" : {
                        "input" : {
                          "file" : "" + size + "_" + threads + "threads.csv"
                        }
                      }
                    });
                step_3.run();
        };
};
