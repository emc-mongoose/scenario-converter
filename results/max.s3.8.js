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
  "storage" : {
    "net" : {
      "node" : {
        "port" : 9020
      }
    },
    "driver" : {
      "type" : "s3"
    }
  }
};

        var step_1 = PreconditionLoad
        .config(parentConfig_1)
        .config({
              "load" : {
                "step" : {
                  "limit" : {
                    "count" : 10000,
                    "time" : INIT_RUN_TIME,
                    "concurrency" : 40
                  }
                }
              },
              "item" : {
                "data" : {
                  "size" : "10KB"
                },
                "output" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/init.csv"
                }
              }
            })
        .run();

        var step_2 = PreconditionLoad
        .config(parentConfig_1)
        .config({
              "load" : {
                "generator" : {
                  "recycle" : {
                    "enabled" : true
                  }
                },
                "step" : {
                  "limit" : {
                    "count" : 10000,
                    "time" : INIT_RUN_TIME,
                    "concurrency" : 40
                  }
                },
                "type" : "read"
              },
              "item" : {
                "data" : {
                  "size" : "10KB"
                },
                "input" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/init.csv"
                }
              }
            })
        .run();

        var cmd_1 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_1.waitFor();
        printToCL( cmd_1 );

        var step_3 = CreateLoad
        .config(parentConfig_1)
        .config({
              "item" : {
                "data" : {
                  "size" : "10KB"
                },
                "output" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W10KB/items.csv"
                }
              },
              "storage" : {
                "auth" : {
                  "uid" : "MAX-W10KB"
                }
              },
              "load" : {
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME,
                    "concurrency" : 320
                  }
                }
              }
            })
        .run();

        var cmd_2 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_2.waitFor();
        printToCL( cmd_2 );

        var step_4 = CreateLoad
        .config(parentConfig_1)
        .config({
              "item" : {
                "data" : {
                  "size" : "100KB"
                },
                "output" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W100KB/items.csv"
                }
              },
              "storage" : {
                "auth" : {
                  "uid" : "MAX-W100KB"
                }
              },
              "load" : {
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME,
                    "concurrency" : 320
                  }
                }
              }
            })
        .run();

        var cmd_3 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_3.waitFor();
        printToCL( cmd_3 );

        var step_5 = CreateLoad
        .config(parentConfig_1)
        .config({
              "item" : {
                "data" : {
                  "size" : "1MB"
                },
                "output" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W1MB/items.csv"
                }
              },
              "storage" : {
                "auth" : {
                  "uid" : "MAX-W1MB"
                }
              },
              "load" : {
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME,
                    "concurrency" : 160
                  }
                }
              }
            })
        .run();

        var cmd_4 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_4.waitFor();
        printToCL( cmd_4 );

        var step_6 = CreateLoad
        .config(parentConfig_1)
        .config({
              "item" : {
                "data" : {
                  "size" : "10MB"
                },
                "output" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W10MB/items.csv"
                }
              },
              "storage" : {
                "auth" : {
                  "uid" : "MAX-W10MB"
                }
              },
              "load" : {
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME,
                    "concurrency" : 80
                  }
                }
              }
            })
        .run();

        var cmd_5 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_5.waitFor();
        printToCL( cmd_5 );

        var step_7 = CreateLoad
        .config(parentConfig_1)
        .config({
              "item" : {
                "data" : {
                  "size" : "100MB"
                },
                "output" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W100MB/items.csv"
                }
              },
              "storage" : {
                "auth" : {
                  "uid" : "MAX-W100MB"
                }
              },
              "load" : {
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME,
                    "concurrency" : 80
                  }
                }
              }
            })
        .run();

        var cmd_6 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_6.waitFor();
        printToCL( cmd_6 );

        var step_8 = CreateLoad
        .config(parentConfig_1)
        .config({
              "item" : {
                "data" : {
                  "size" : "200MB"
                },
                "output" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W200MB/items.csv"
                }
              },
              "storage" : {
                "auth" : {
                  "uid" : "MAX-W200MB"
                }
              },
              "load" : {
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME,
                    "concurrency" : 80
                  }
                }
              }
            })
        .run();

        var cmd_7 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_7.waitFor();
        printToCL( cmd_7 );

        var step_9 = ReadLoad
        .config(parentConfig_1)
        .config({
              "item" : {
                "data" : {
                  "size" : "10KB",
                  "verify" : true
                },
                "input" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W10KB/items.csv"
                }
              },
              "storage" : {
                "auth" : {
                  "uid" : "MAX-R10KB"
                }
              },
              "load" : {
                "generator" : {
                  "recycle" : {
                    "enabled" : true
                  }
                },
                "step" : {
                  "limit" : {
                    "concurrency" : 320,
                    "time" : RUN_TIME
                  }
                },
                "type" : "read"
              }
            })
        .run();

        var cmd_8 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_8.waitFor();
        printToCL( cmd_8 );

        var step_10 = ReadLoad
        .config(parentConfig_1)
        .config({
              "item" : {
                "data" : {
                  "size" : "100KB",
                  "verify" : true
                },
                "input" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W100KB/items.csv"
                }
              },
              "storage" : {
                "auth" : {
                  "uid" : "MAX-R100KB"
                }
              },
              "load" : {
                "generator" : {
                  "recycle" : {
                    "enabled" : true
                  }
                },
                "step" : {
                  "limit" : {
                    "concurrency" : 320,
                    "time" : RUN_TIME
                  }
                },
                "type" : "read"
              }
            })
        .run();

        var cmd_9 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_9.waitFor();
        printToCL( cmd_9 );

        var step_11 = ReadLoad
        .config(parentConfig_1)
        .config({
              "item" : {
                "data" : {
                  "size" : "1MB",
                  "verify" : true
                },
                "input" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W1MB/items.csv"
                }
              },
              "storage" : {
                "auth" : {
                  "uid" : "MAX-R1MB"
                }
              },
              "load" : {
                "generator" : {
                  "recycle" : {
                    "enabled" : true
                  }
                },
                "step" : {
                  "limit" : {
                    "concurrency" : 160,
                    "time" : RUN_TIME
                  }
                },
                "type" : "read"
              }
            })
        .run();

        var cmd_10 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_10.waitFor();
        printToCL( cmd_10 );

        var step_12 = ReadLoad
        .config(parentConfig_1)
        .config({
              "item" : {
                "data" : {
                  "size" : "10MB",
                  "verify" : true
                },
                "input" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W10MB/items.csv"
                }
              },
              "storage" : {
                "auth" : {
                  "uid" : "MAX-R10MB"
                }
              },
              "load" : {
                "generator" : {
                  "recycle" : {
                    "enabled" : true
                  }
                },
                "step" : {
                  "limit" : {
                    "concurrency" : 80,
                    "time" : RUN_TIME
                  }
                },
                "type" : "read"
              }
            })
        .run();

        var cmd_11 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_11.waitFor();
        printToCL( cmd_11 );

        var step_13 = ReadLoad
        .config(parentConfig_1)
        .config({
              "item" : {
                "data" : {
                  "size" : "100MB",
                  "verify" : true
                },
                "input" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W100MB/items.csv"
                }
              },
              "storage" : {
                "auth" : {
                  "uid" : "MAX-R100MB"
                }
              },
              "load" : {
                "generator" : {
                  "recycle" : {
                    "enabled" : true
                  }
                },
                "step" : {
                  "limit" : {
                    "concurrency" : 80,
                    "time" : RUN_TIME
                  }
                },
                "type" : "read"
              }
            })
        .run();

        var cmd_12 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_12.waitFor();
        printToCL( cmd_12 );

        var step_14 = ReadLoad
        .config(parentConfig_1)
        .config({
              "item" : {
                "data" : {
                  "size" : "200MB",
                  "verify" : true
                },
                "input" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W200MB/items.csv"
                }
              },
              "storage" : {
                "auth" : {
                  "uid" : "MAX-R200MB"
                }
              },
              "load" : {
                "generator" : {
                  "recycle" : {
                    "enabled" : true
                  }
                },
                "step" : {
                  "limit" : {
                    "concurrency" : 80,
                    "time" : RUN_TIME
                  }
                },
                "type" : "read"
              }
            })
        .run();

        var cmd_13 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_13.waitFor();
        printToCL( cmd_13 );
