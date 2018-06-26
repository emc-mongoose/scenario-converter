# scenario-converter
## Overview
This tool converts the [Json-scenarios](https://github.com/emc-mongoose/mongoose/wiki/v3.6-Scenarios) used in the Mongoose v3.* into new [JavaScript-scenarios](https://github.com/emc-mongoose/mongoose/blob/feature-v4-doc/doc/input/scenarios.md).
## Usage
How to use it
## Contributors
[Veronika Kochugova](https://github.com/veronikaKochugova)
## Design
How it does the things, list of the params for the conversion, etc
## Development
In order to build project, go to the 'converter' directory and type:
```bash
./gradlew makeJar
```
Jar-file will have path '/converter/build/libs/converter-<version>.jar'.
For converting one scenario type:
```bash
java -jar <path to jar-file> <path to json-scenario>
```
or for multiply converting:
  ```bash
java -jar <path to jar-file> <path 1> <path 2> ... <path n>
```
## Links
[Mongoose](https://github.com/emc-mongoose/mongoose)
