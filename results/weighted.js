var parentConfig_1 = {
  "item" : {
    "output" : {
      "path" : "/weighted-load"
    }
  },
  "storage" : {
    "driver" : {
      "limit" : {
        "concurrency" : 100
      }
    }
  }
};

PreconditionLoad
    .config(parentConfig_1)
    .config({
      "output" : {
        "metrics" : {
          "summary" : {
            "persist" : false
          },
          "average" : {
            "persist" : false
          },
          "trace" : {
            "persist" : false
          }
        }
      },
      "item" : {
        "data" : {
          "size" : "1KB"
        },
        "output" : {
          "file" : "weighted-load.csv"
        }
      },
      "load" : {
        "op" : {
          "limit" : {
            "count" : 10000
          }
        }
      }
    })
    .run();

WeightedLoad
    .config(parentConfig_1)
    .config({
      "load" : {
        "step" : {
          "limit" : {
            "time" : "90s"
          }
        }
      }
    })
    .append({
      "load" : {
        "op" : {
          "weight" : 20
        }
      },
      "item" : {
        "data" : {
          "size" : "4KB-16KB"
        }
      }
    })
    .append({
      "load" : {
        "op" : {
          "recycle" : true,
          "type" : "read",
          "weight" : 80
        }
      },
      "item" : {
        "input" : {
          "file" : "weighted-load.csv"
        }
      }
    })
    .run();

