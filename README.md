# AndroidBLEParser
A collection of parser to deserialize standardize bluetooth low energy data into usable data structures.

## Description
This procject consists of a set of serializer/deserializers that convert binary byte stream transfered by Bluetooth LE interface to/from Android data structures. The conversion scheme follows Bluetooth LE specification for each device. [Bluetooth Specification Website]

It eleminates the burden to learn the bluetooth LE packet data structure and work with binary raw data from users. However, it only handle the parsing part of Bluetooth LE packet, and hence it requires a working Bluetooth LE connection at the first place. Potential users can use a prefered framework to handle Bluetooth LE connections.

## Installation
1. Clone this repository to local machine. Assuming the project folder is under the `/AndroidProject` foler.
    ````
    git clone https://github.com/ehealthinnovation/AndroidBLEParser.git
    ````
2. Check out the `develop` branch, where the code is currently host in.
    ````
    git checkout develop
    ````
    
3. Import the parser module into your Android project
    * In Android Studio click `File>New>Import Module`
    * Select the `/AndroidProject/AndroidBLEParser/parser` folder. Use the default module name `:module`. Click `Finish`
    * Enable Kotlin support for the project. Modify the `build.gradle` file in the Android project to include the classpath dependency for kotlin. Then sync the gradle project.
        ````
        buildscript {
        ext.kotlin_version = '1.3.0'
        repositories {
        ...
        }
        dependencies {
        ...
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        }
        }
        ```` 
    * Modify the project setting gradle file `settings.gradle` to include the newly import module `parser`. Sync the gradle. The parser module should appear in the project explorer.
        ````
        include ':app', ':parser'
        ````
    * In the `build.gradle` file for the `app` module (or the application module where the parser is supposed to be used), add the dependence. Sync the gradle project.
        ````
        dependencies {
        ...
            implementation project(':parser')
        ...
        }
        ````
    * Now the parser module is ready for integration.
    
## Integrating the Parser Library
In the following two sections, examples on parsing a particular characteristic `CgmFeature` and composing command to `Record Access Control Points` are presented. These examples aimed at showing the way for integrating the parser in other app. One should adapt these examples for other scenarios.

### Preparation
* Wrap the Bluetooth LE raw data buffer with a class that implements the `DataReader` interface. If one uses the Android `BluetoothCharacteristic` class to hold the received data, the `BluetoothGattDataReader` comes with the parser module can be readily used.
* If writing command to the remote device is needed, wrap the Bluetooth LE raw data buffer with a class that implements the `DataWriter` interface. Similar to the reader case, the `BluetoothGattDataWriter` is ready to use if the app uses `BluetoothCharacteristic` to hold the outgoing binary data.
* Create a class that implements `CharacteristicPacket`. Instance of this class are passed into parser for processing.
    
### Parsing Bluetooth LE packet    
*  Assuming the prepartion stated above has been done, and at some point in executing the app, a Bluetooth LE packet is received and an instance of `CharacteristicPacket` called `packet` is created.
*  Choose a parser from the module to handle the `packet`. If we know the packet is from reading the CGM Feature characteristic, we can directly create an instance of `CgmFeatureParser`. It is safer to make sure the `CgmFeatureParser` can parse the packet with the following code sample. Under the hood, the parser will check the UUID of the packet to see if it is from a CGM Feature characteristic.
    ````
    CgmFeatureParser parser = new CgmFeatureParser();
    if (parser.canParse(packet)){
        ...perform parsing...
    }
    ````
* Perform the parsing and output a `CgmFeature` object. If error is encountered during parsing process, usually due to packet corruption, an exception will be thrown.
    ````
    CgmFeature parsedObject = parser.parse(packet)
    ````
* Now the `parsedObject` loaded with the read reasult from a remote device. Its property fields can be accessed for data.
* If the packet type is unknown, we can use the following pattern to choose from a list of available parsers to execute the deserialization. The user then performs subsequent operations based on the output object from the parsers. 
    ````
     private Object parsePacketWithTheFirstSupportingParser(@NonNull CharacteristicPacket packet) {
        for (CharacteristicParser parser : supportedParsers) {
            if (parser.canParse(packet)) {
                return parser.parse(packet);
            }
        }
        return null;
    }
    
    
    private void performOperationBasedOnParsedResult(@Nullable final Object parsedResult) {
        if (parsedResult == null) {
            throw new NullPointerException("Parsed result is null");
        } else if (parsedResult instanceof CgmFeature) {
            ...operation for Cgm Feature...
        } else if (parsedResult instanceof CgmMeasurement){
            ...operations for Cgm measurement...
        } else if (parsedResult instanceof SessionStartTime){
            ...operations for Cgm Session Start time...
        }....
        else {
            throw new IllegalStateException("Not Supported");
        }
    }

    ````
    
### Composing Bluetooth LE packet
* The write process starts with a command to write. In this example we try will issue a command to Record Access Control Point to report all records. Assume that we have finished the preparation steps, and we are going to write the serialized data into a buffer wrapped in a `packet`
* Instantiate an instance of `RacpComposer` to serialize commands into binary array.
    ````
    RacpComposer composer = new CgmRacpComposer();
    ````
* Create an instance of command holding the desire arguments.
    ````
    ReportRecords request = new ReportRecords(new SimpleOperand(SimpleOperation.ALL_RECORDS));
    ````
* Perform the serialization, after which the binary data array will be stored in `packet`.
    ````
    composer.compose(request, packet.writeData()); //here packet.writeData() returns a DataWriter object
    ````
* Call the Bluetooth LE stack to transfer the binary array stored in the `packet` to a remote device. This steps depend on the Bluetooth LE framework choice, hence it is not detailed here.

## Contribute
The project is under MIT license, and it is free to incorporate into other projects. You are welcome to contribute to the project by submitting your pull request in this github repository.

## Contact
For any suggestions or questions please contact
Harry Qiu email: hqiu@ehealthinnovation.org
KuoCheng Tong email: kuochengtong@ehealthinnovation.org

[Bluetooth Specification Website]:https://www.bluetooth.com/specifications/gatt
