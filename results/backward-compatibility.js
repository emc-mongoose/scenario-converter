var step_1 = CreateLoad
    .config({
      "load" : {
        "step" : {
          "limit" : {
            "count" : 100,
            "concurrency" : 10
          },
          "id" : "backward-compatibility-test"
        }
      },
      "storage" : {
        "net" : {
          "node" : {
            "addrs" : new java.util.ArrayList([ "127.0.0.1" ])
          }
        },
        "auth" : {
          "uid" : "wuser1@sanity.local"
        }
      }
    })
    .run();

