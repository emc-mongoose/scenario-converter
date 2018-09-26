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

PreconditionLoad
    .config(parentConfig_1)
    .config({
      "load" : {
        "op" : {
          "limit" : {
            "count" : 10000
          }
        },
        "step" : {
          "limit" : {
            "time" : INIT_RUN_TIME
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
      },
      "storage" : {
        "driver" : {
          "limit" : {
            "concurrency" : 40
          }
        }
      }
    })
    .run();

PreconditionLoad
    .config(parentConfig_1)
    .config({
      "load" : {
        "op" : {
          "limit" : {
            "count" : 10000
          },
          "recycle" : true,
          "type" : "read"
        },
        "step" : {
          "limit" : {
            "time" : INIT_RUN_TIME
          }
        }
      },
      "item" : {
        "data" : {
          "size" : "10KB"
        },
        "input" : {
          "path" : "" + BUCKET + "",
          "file" : "" + MONGOOSE_DIR + "/log/init.csv"
        }
      },
      "storage" : {
        "driver" : {
          "limit" : {
            "concurrency" : 40
          }
        }
      }
    })
    .run();

var cmd_1 = new java.lang.ProcessBuilder()
    .command("/bin/sh", "-c", "sleep ${WAIT_TIME}")
    .inheritIO()
    .start();
cmd_1.waitFor();

Load
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
        "driver" : {
          "limit" : {
            "concurrency" : 320
          }
        }
      },
      "load" : {
        "step" : {
          "limit" : {
            "time" : RUN_TIME
          },
          "id" : "MAX-W10KB"
        }
      }
    })
    .run();

var cmd_2 = new java.lang.ProcessBuilder()
    .command("/bin/sh", "-c", "sleep ${WAIT_TIME}")
    .inheritIO()
    .start();
cmd_2.waitFor();

Load
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
        "driver" : {
          "limit" : {
            "concurrency" : 320
          }
        }
      },
      "load" : {
        "step" : {
          "limit" : {
            "time" : RUN_TIME
          },
          "id" : "MAX-W100KB"
        }
      }
    })
    .run();

var cmd_3 = new java.lang.ProcessBuilder()
    .command("/bin/sh", "-c", "sleep ${WAIT_TIME}")
    .inheritIO()
    .start();
cmd_3.waitFor();

Load
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
        "driver" : {
          "limit" : {
            "concurrency" : 160
          }
        }
      },
      "load" : {
        "step" : {
          "limit" : {
            "time" : RUN_TIME
          },
          "id" : "MAX-W1MB"
        }
      }
    })
    .run();

var cmd_4 = new java.lang.ProcessBuilder()
    .command("/bin/sh", "-c", "sleep ${WAIT_TIME}")
    .inheritIO()
    .start();
cmd_4.waitFor();

Load
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
        "driver" : {
          "limit" : {
            "concurrency" : 80
          }
        }
      },
      "load" : {
        "step" : {
          "limit" : {
            "time" : RUN_TIME
          },
          "id" : "MAX-W10MB"
        }
      }
    })
    .run();

var cmd_5 = new java.lang.ProcessBuilder()
    .command("/bin/sh", "-c", "sleep ${WAIT_TIME}")
    .inheritIO()
    .start();
cmd_5.waitFor();

Load
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
        "driver" : {
          "limit" : {
            "concurrency" : 80
          }
        }
      },
      "load" : {
        "step" : {
          "limit" : {
            "time" : RUN_TIME
          },
          "id" : "MAX-W100MB"
        }
      }
    })
    .run();

var cmd_6 = new java.lang.ProcessBuilder()
    .command("/bin/sh", "-c", "sleep ${WAIT_TIME}")
    .inheritIO()
    .start();
cmd_6.waitFor();

Load
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
        "driver" : {
          "limit" : {
            "concurrency" : 80
          }
        }
      },
      "load" : {
        "step" : {
          "limit" : {
            "time" : RUN_TIME
          },
          "id" : "MAX-W200MB"
        }
      }
    })
    .run();

var cmd_7 = new java.lang.ProcessBuilder()
    .command("/bin/sh", "-c", "sleep ${WAIT_TIME}")
    .inheritIO()
    .start();
cmd_7.waitFor();

ReadLoad
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
        "driver" : {
          "limit" : {
            "concurrency" : 320
          }
        }
      },
      "load" : {
        "op" : {
          "recycle" : true
        },
        "step" : {
          "limit" : {
            "time" : RUN_TIME
          },
          "id" : "MAX-R10KB"
        }
      }
    })
    .run();

var cmd_8 = new java.lang.ProcessBuilder()
    .command("/bin/sh", "-c", "sleep ${WAIT_TIME}")
    .inheritIO()
    .start();
cmd_8.waitFor();

ReadLoad
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
        "driver" : {
          "limit" : {
            "concurrency" : 320
          }
        }
      },
      "load" : {
        "op" : {
          "recycle" : true
        },
        "step" : {
          "limit" : {
            "time" : RUN_TIME
          },
          "id" : "MAX-R100KB"
        }
      }
    })
    .run();

var cmd_9 = new java.lang.ProcessBuilder()
    .command("/bin/sh", "-c", "sleep ${WAIT_TIME}")
    .inheritIO()
    .start();
cmd_9.waitFor();

ReadLoad
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
        "driver" : {
          "limit" : {
            "concurrency" : 160
          }
        }
      },
      "load" : {
        "op" : {
          "recycle" : true
        },
        "step" : {
          "limit" : {
            "time" : RUN_TIME
          },
          "id" : "MAX-R1MB"
        }
      }
    })
    .run();

var cmd_10 = new java.lang.ProcessBuilder()
    .command("/bin/sh", "-c", "sleep ${WAIT_TIME}")
    .inheritIO()
    .start();
cmd_10.waitFor();

ReadLoad
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
        "driver" : {
          "limit" : {
            "concurrency" : 80
          }
        }
      },
      "load" : {
        "op" : {
          "recycle" : true
        },
        "step" : {
          "limit" : {
            "time" : RUN_TIME
          },
          "id" : "MAX-R10MB"
        }
      }
    })
    .run();

var cmd_11 = new java.lang.ProcessBuilder()
    .command("/bin/sh", "-c", "sleep ${WAIT_TIME}")
    .inheritIO()
    .start();
cmd_11.waitFor();

ReadLoad
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
        "driver" : {
          "limit" : {
            "concurrency" : 80
          }
        }
      },
      "load" : {
        "op" : {
          "recycle" : true
        },
        "step" : {
          "limit" : {
            "time" : RUN_TIME
          },
          "id" : "MAX-R100MB"
        }
      }
    })
    .run();

var cmd_12 = new java.lang.ProcessBuilder()
    .command("/bin/sh", "-c", "sleep ${WAIT_TIME}")
    .inheritIO()
    .start();
cmd_12.waitFor();

ReadLoad
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
        "driver" : {
          "limit" : {
            "concurrency" : 80
          }
        }
      },
      "load" : {
        "op" : {
          "recycle" : true
        },
        "step" : {
          "limit" : {
            "time" : RUN_TIME
          },
          "id" : "MAX-R200MB"
        }
      }
    })
    .run();

var cmd_13 = new java.lang.ProcessBuilder()
    .command("/bin/sh", "-c", "sleep ${WAIT_TIME}")
    .inheritIO()
    .start();
cmd_13.waitFor();

