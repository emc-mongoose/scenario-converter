
        var step_1 = CreateLoad();
        step_1.config({
              "load" : {
                "threads" : 10,
                "step" : {
                  "limit" : {
                    "count" : 100
                  },
                  "id" : "backward-compatibility-test"
                }
              },
              "storage" : {
                "net" : {
                  "node" : {
                    "addrs" : [ "127.0.0.1" ]
                  }
                },
                "auth" : {
                  "id" : "wuser1@sanity.local"
                }
              }
            });
        step_1.run();
