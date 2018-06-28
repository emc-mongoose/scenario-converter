# scenario-converter
## Overview
This tool converts the [Json-scenarios](https://github.com/emc-mongoose/mongoose/wiki/v3.6-Scenarios) used in the Mongoose v3.* into new [JavaScript-scenarios](https://github.com/emc-mongoose/mongoose/blob/feature-v4-doc/doc/input/scenarios.md).
## Design

### Scenario 
Scenario is an entity that comes from Json. It stores in itself the tree structure of the Json-scenario and the variables used in it.

### Scenario Converter
Scenario Converter is a component with generates scenario on JavaScript. 
As input data, it takes the Scenario entry, and the result on JavaScript outputs to standard output.

### Configurations Converter
Configurations Converter is a component, used to convert only "config" section in scenarios. 
It takes into account the changes for the 4th Mongoose version and generates a new configuration with changes.

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
or for multiple converting:
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
