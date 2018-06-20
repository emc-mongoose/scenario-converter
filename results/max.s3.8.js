
var superConfig_1 = {
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
        step_1.config(superConfig_1);
        step_1.config({
              "load" : {
                "step" : {
                  "limit" : {
                    "concurrency" : 40
                  }
                }
              },
              "item" : {
                "data" : {
                  "size" : "10KB"
                },
                "output" : {
                  "path" : BUCKET,
                  "file" : "MONGOOSE_DIR/log/init.csv"
                }
              }
            });
        step_1.start();

        var step_2 = PreconditionLoad();
        step_2.config(superConfig_1);
        step_2.config({
              "load" : {
                "generator" : {
                  "recycle" : {
                    "enabled" : true
                  }
                },
                "step" : {
                  "limit" : {
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
                  "path" : BUCKET,
                  "file" : "MONGOOSE_DIR/log/init.csv"
                }
              }
            });
        step_2.start();

        var cmd_1 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();

        var step_3 CreateLoad();
        step_3.config(superConfig_1);
        step_3.config({
              "load" : {
                "step" : {
                  "id" : "MAX-W10KB",
                  "limit" : {
                    "concurrency" : 320
                  }
                }
              },
              "item" : {
                "data" : {
                  "size" : "10KB"
                },
                "output" : {
                  "path" : BUCKET,
                  "file" : "MONGOOSE_DIR/log/MAX-W10KB/items.csv"
                }
              }
            });
        step_3.start();

        var cmd_2 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();

        var step_4 CreateLoad();
        step_4.config(superConfig_1);
        step_4.config({
              "load" : {
                "step" : {
                  "id" : "MAX-W100KB",
                  "limit" : {
                    "concurrency" : 320
                  }
                }
              },
              "item" : {
                "data" : {
                  "size" : "100KB"
                },
                "output" : {
                  "path" : BUCKET,
                  "file" : "MONGOOSE_DIR/log/MAX-W100KB/items.csv"
                }
              }
            });
        step_4.start();

        var cmd_3 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();

        var step_5 CreateLoad();
        step_5.config(superConfig_1);
        step_5.config({
              "load" : {
                "step" : {
                  "id" : "MAX-W1MB",
                  "limit" : {
                    "concurrency" : 160
                  }
                }
              },
              "item" : {
                "data" : {
                  "size" : "1MB"
                },
                "output" : {
                  "path" : BUCKET,
                  "file" : "MONGOOSE_DIR/log/MAX-W1MB/items.csv"
                }
              }
            });
        step_5.start();

        var cmd_4 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();

        var step_6 CreateLoad();
        step_6.config(superConfig_1);
        step_6.config({
              "load" : {
                "step" : {
                  "id" : "MAX-W10MB",
                  "limit" : {
                    "concurrency" : 80
                  }
                }
              },
              "item" : {
                "data" : {
                  "size" : "10MB"
                },
                "output" : {
                  "path" : BUCKET,
                  "file" : "MONGOOSE_DIR/log/MAX-W10MB/items.csv"
                }
              }
            });
        step_6.start();

        var cmd_5 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();

        var step_7 CreateLoad();
        step_7.config(superConfig_1);
        step_7.config({
              "load" : {
                "step" : {
                  "id" : "MAX-W100MB",
                  "limit" : {
                    "concurrency" : 80
                  }
                }
              },
              "item" : {
                "data" : {
                  "size" : "100MB"
                },
                "output" : {
                  "path" : BUCKET,
                  "file" : "MONGOOSE_DIR/log/MAX-W100MB/items.csv"
                }
              }
            });
        step_7.start();

        var cmd_6 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();

        var step_8 CreateLoad();
        step_8.config(superConfig_1);
        step_8.config({
              "load" : {
                "step" : {
                  "id" : "MAX-W200MB",
                  "limit" : {
                    "concurrency" : 80
                  }
                }
              },
              "item" : {
                "data" : {
                  "size" : "200MB"
                },
                "output" : {
                  "path" : BUCKET,
                  "file" : "MONGOOSE_DIR/log/MAX-W200MB/items.csv"
                }
              }
            });
        step_8.start();

        var cmd_7 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();

        var step_9 ReadLoad();
        step_9.config(superConfig_1);
        step_9.config({
              "load" : {
                "generator" : {
                  "recycle" : {
                    "enabled" : true
                  }
                },
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME
                  },
                  "id" : "MAX-R10KB"
                },
                "type" : "read"
              },
              "item" : {
                "data" : {
                  "size" : "10KB",
                  "verify" : true
                },
                "input" : {
                  "path" : BUCKET,
                  "file" : "MONGOOSE_DIR/log/MAX-W10KB/items.csv"
                }
              }
            });
        step_9.start();

        var cmd_8 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();

        var step_10 ReadLoad();
        step_10.config(superConfig_1);
        step_10.config({
              "load" : {
                "generator" : {
                  "recycle" : {
                    "enabled" : true
                  }
                },
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME
                  },
                  "id" : "MAX-R100KB"
                },
                "type" : "read"
              },
              "item" : {
                "data" : {
                  "size" : "100KB",
                  "verify" : true
                },
                "input" : {
                  "path" : BUCKET,
                  "file" : "MONGOOSE_DIR/log/MAX-W100KB/items.csv"
                }
              }
            });
        step_10.start();

        var cmd_9 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();

        var step_11 ReadLoad();
        step_11.config(superConfig_1);
        step_11.config({
              "load" : {
                "generator" : {
                  "recycle" : {
                    "enabled" : true
                  }
                },
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME
                  },
                  "id" : "MAX-R1MB"
                },
                "type" : "read"
              },
              "item" : {
                "data" : {
                  "size" : "1MB",
                  "verify" : true
                },
                "input" : {
                  "path" : BUCKET,
                  "file" : "MONGOOSE_DIR/log/MAX-W1MB/items.csv"
                }
              }
            });
        step_11.start();

        var cmd_10 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();

        var step_12 ReadLoad();
        step_12.config(superConfig_1);
        step_12.config({
              "load" : {
                "generator" : {
                  "recycle" : {
                    "enabled" : true
                  }
                },
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME
                  },
                  "id" : "MAX-R10MB"
                },
                "type" : "read"
              },
              "item" : {
                "data" : {
                  "size" : "10MB",
                  "verify" : true
                },
                "input" : {
                  "path" : BUCKET,
                  "file" : "MONGOOSE_DIR/log/MAX-W10MB/items.csv"
                }
              }
            });
        step_12.start();

        var cmd_11 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();

        var step_13 ReadLoad();
        step_13.config(superConfig_1);
        step_13.config({
              "load" : {
                "generator" : {
                  "recycle" : {
                    "enabled" : true
                  }
                },
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME
                  },
                  "id" : "MAX-R100MB"
                },
                "type" : "read"
              },
              "item" : {
                "data" : {
                  "size" : "100MB",
                  "verify" : true
                },
                "input" : {
                  "path" : BUCKET,
                  "file" : "MONGOOSE_DIR/log/MAX-W100MB/items.csv"
                }
              }
            });
        step_13.start();

        var cmd_12 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();

        var step_14 ReadLoad();
        step_14.config(superConfig_1);
        step_14.config({
              "load" : {
                "generator" : {
                  "recycle" : {
                    "enabled" : true
                  }
                },
                "step" : {
                  "limit" : {
                    "time" : RUN_TIME
                  },
                  "id" : "MAX-R200MB"
                },
                "type" : "read"
              },
              "item" : {
                "data" : {
                  "size" : "200MB",
                  "verify" : true
                },
                "input" : {
                  "path" : BUCKET,
                  "file" : "MONGOOSE_DIR/log/MAX-W200MB/items.csv"
                }
              }
            });
        step_14.start();

        var cmd_13 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "sleep ${WAIT_TIME}")
            .start();
