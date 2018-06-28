var parentConfig_1 = {
  "load" : {
    "step" : {
      "limit" : {
        "concurrency" : 100
      }
    }
  },
  "item" : {
    "output" : {
      "path" : "/weighted-load"
    }
  }
};

var step_1 = PreconditionLoad
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
        "step" : {
          "limit" : {
            "count" : 10000
          }
        }
      }
    })
    .run();

WeightedLoad
    .config(parentConfig_1)
    .append({
      "load" : {
        "generator" : {
          "weight" : 20
        },
        "step" : {
          "limit" : {
            "time" : "90s"
          }
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
        "type" : "read",
        "generator" : {
          "recycle" : {
            "enabled" : true
          },
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

