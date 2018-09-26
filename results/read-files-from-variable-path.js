var parentConfig_1 = {
  "item" : {
    "data" : {
      "size" : "10KB"
    }
  },
  "storage" : {
    "driver" : {
      "type" : "fs",
      "limit" : {
        "concurrency" : 10
      }
    }
  },
  "load" : {
    "op" : {
      "limit" : {
        "count" : 10000
      }
    }
  }
};

PreconditionLoad
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

ReadLoad
    .config(parentConfig_1)
    .config({
      "item" : {
        "input" : {
          "file" : "read-files-from-variable-path.csv"
        }
      }
    })
    .run();

