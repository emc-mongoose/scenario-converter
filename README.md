# scenario-converter
## Overview
This tool converts the [Json-scenarios](https://github.com/emc-mongoose/mongoose/wiki/v3.6-Scenarios) used in the Mongoose v3.* into new [JavaScript-scenarios](https://github.com/emc-mongoose/mongoose/blob/feature-v4-doc/doc/input/scenarios.md).
## Design
How it does the things, list of the params for the conversion, etc
## Development
  In order to build project, go to the 'converter' directory and type:
```bash
./gradlew makeJar
```
Jar-file will have path '/converter/build/libs/converter-\<version\>.jar'.
## Usage
  For converting one scenario type:
```bash
java -jar <path to jar-file> <path to json-scenario>
```
or for multiply converting:
```bash
java -jar <path to jar-file> <path 1> <path 2> ... <path n>
```
  Result will be print into std-output. To create .js file use the following:
```bash
java -jar <path to jar-file> <path to json-scenario>  >  <path to JS-scenario>
```  
## Contributors
[Veronika Kochugova](https://github.com/veronikaKochugova)
## Links
[Mongoose](https://github.com/emc-mongoose/mongoose)
