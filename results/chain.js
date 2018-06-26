PipelineLoad
    .config({
      "item" : {
        "output" : {
          "path" : "/default"
        }
      }
    }) //substeps
    .config({
      "load" : {
        "type" : "read"
      }
    }) //substeps
    .config({
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
    }) //substeps
    .config({
      "load" : {
        "type" : "read"
      },
      "item" : {
        "data" : {
          "verify" : true
        }
      }
    }) //substeps
    .config({
      "load" : {
        "type" : "delete"
      }
    }) //substeps
    .config({
      "load" : {
        "type" : "noop"
      }
    }) //substeps
    .run();

