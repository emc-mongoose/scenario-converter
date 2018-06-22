
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

        var step_1 = PreconditionLoad();
        step_1.config(parentConfig_1);
        step_1.config({
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
            });
        step_1.run();

        var step_2 = PreconditionLoad();
        step_2.config(parentConfig_1);
        step_2.config({
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
            });
        step_2.run();

        var cmd_1 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_1.waitFor();

        var step_3 = CreateLoad();
        step_3.config(parentConfig_1);
        step_3.config({
              "load" : {
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME,
                    "concurrency" : 320
                  }
                }
              },
              "item" : {
                "data" : {
                  "size" : "10KB"
                },
                "output" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W10KB/items.csv"
                }
              }
            });
        step_3.run();

        var cmd_2 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_2.waitFor();

        var step_4 = CreateLoad();
        step_4.config(parentConfig_1);
        step_4.config({
              "load" : {
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME,
                    "concurrency" : 320
                  }
                }
              },
              "item" : {
                "data" : {
                  "size" : "100KB"
                },
                "output" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W100KB/items.csv"
                }
              }
            });
        step_4.run();

        var cmd_3 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_3.waitFor();

        var step_5 = CreateLoad();
        step_5.config(parentConfig_1);
        step_5.config({
              "load" : {
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME,
                    "concurrency" : 160
                  }
                }
              },
              "item" : {
                "data" : {
                  "size" : "1MB"
                },
                "output" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W1MB/items.csv"
                }
              }
            });
        step_5.run();

        var cmd_4 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_4.waitFor();

        var step_6 = CreateLoad();
        step_6.config(parentConfig_1);
        step_6.config({
              "load" : {
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME,
                    "concurrency" : 80
                  }
                }
              },
              "item" : {
                "data" : {
                  "size" : "10MB"
                },
                "output" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W10MB/items.csv"
                }
              }
            });
        step_6.run();

        var cmd_5 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_5.waitFor();

        var step_7 = CreateLoad();
        step_7.config(parentConfig_1);
        step_7.config({
              "load" : {
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME,
                    "concurrency" : 80
                  }
                }
              },
              "item" : {
                "data" : {
                  "size" : "100MB"
                },
                "output" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W100MB/items.csv"
                }
              }
            });
        step_7.run();

        var cmd_6 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_6.waitFor();

        var step_8 = CreateLoad();
        step_8.config(parentConfig_1);
        step_8.config({
              "load" : {
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME,
                    "concurrency" : 80
                  }
                }
              },
              "item" : {
                "data" : {
                  "size" : "200MB"
                },
                "output" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W200MB/items.csv"
                }
              }
            });
        step_8.run();

        var cmd_7 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_7.waitFor();

        var step_9 = ReadLoad();
        step_9.config(parentConfig_1);
        step_9.config({
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
              },
              "item" : {
                "data" : {
                  "size" : "10KB",
                  "verify" : true
                },
                "input" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W10KB/items.csv"
                }
              }
            });
        step_9.run();

        var cmd_8 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_8.waitFor();

        var step_10 = ReadLoad();
        step_10.config(parentConfig_1);
        step_10.config({
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
              },
              "item" : {
                "data" : {
                  "size" : "100KB",
                  "verify" : true
                },
                "input" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W100KB/items.csv"
                }
              }
            });
        step_10.run();

        var cmd_9 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_9.waitFor();

        var step_11 = ReadLoad();
        step_11.config(parentConfig_1);
        step_11.config({
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
              },
              "item" : {
                "data" : {
                  "size" : "1MB",
                  "verify" : true
                },
                "input" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W1MB/items.csv"
                }
              }
            });
        step_11.run();

        var cmd_10 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_10.waitFor();

        var step_12 = ReadLoad();
        step_12.config(parentConfig_1);
        step_12.config({
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
              },
              "item" : {
                "data" : {
                  "size" : "10MB",
                  "verify" : true
                },
                "input" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W10MB/items.csv"
                }
              }
            });
        step_12.run();

        var cmd_11 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_11.waitFor();

        var step_13 = ReadLoad();
        step_13.config(parentConfig_1);
        step_13.config({
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
              },
              "item" : {
                "data" : {
                  "size" : "100MB",
                  "verify" : true
                },
                "input" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W100MB/items.csv"
                }
              }
            });
        step_13.run();

        var cmd_12 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_12.waitFor();

        var step_14 = ReadLoad();
        step_14.config(parentConfig_1);
        step_14.config({
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
              },
              "item" : {
                "data" : {
                  "size" : "200MB",
                  "verify" : true
                },
                "input" : {
                  "path" : "" + BUCKET + "",
                  "file" : "" + MONGOOSE_DIR + "/log/MAX-W200MB/items.csv"
                }
              }
            });
        step_14.run();

        var cmd_13 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
        cmd_13.waitFor();
