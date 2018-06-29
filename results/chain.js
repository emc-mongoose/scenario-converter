PipelineLoad
    .config({ })
    .append({
      "item" : {
        "output" : {
          "path" : "/default"
        }
      }
    })
    .append({
      "load" : {
        "type" : "read"
      }
    })
    .append({
      "load" : {
        "type" : "update"
      },
      "item" : {
        "data" : {
          "ranges" : {
            "random" : 1
          }
        }
      }
    })
    .append({
      "load" : {
        "type" : "read"
      },
      "item" : {
        "data" : {
          "verify" : true
        }
      }
    })
    .append({
      "load" : {
        "type" : "delete"
      }
    })
    .append({
      "load" : {
        "type" : "noop"
      }
    })
    .run();

