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

### Limitation
The converter doesn't support scenarios using mixedLoad-steps without weights, since in the new version of Mongoose there is no such step type.

## Jar

### Download

To download prepaired jar-file: `wget https://github.com/emc-mongoose/scenario-converter-3to4/releases/download/1.2/converter-1.2.jar`

### Build 
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
  Result will be print into std-output. To create .js file use the following:
```bash
java -jar <path to jar-file> <path to json-scenario>  >  <path to JS-scenario>
``` 
or for multiple converting use '--m' key:
```bash
java -jar --m <path to jar-file> <path 1> <path 2> ... <path n>
```
  Result will be print into files with the same names in the same directories with extension .js.
## Contributors
[Veronika Kochugova](https://github.com/veronikaKochugova)
## Links
[Mongoose](https://github.com/emc-mongoose/mongoose)
