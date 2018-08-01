var step_1 = PreconditionLoad
    .config({
      "load" : {
        "step" : {
          "limit" : {
            "count" : 1000
          }
        }
      },
      "item" : {
        "output" : {
          "path" : "items2read",
          "file" : "items2read.csv"
        }
      }
    })
    .run();

