# Cardio Data Simulator

The Cardio Data Simulator is a Java-based application designed to simulate real-time cardiovascular data for multiple patients. This tool is particularly useful for educational purposes, enabling students to interact with real-time data streams of ECG, blood pressure, blood saturation, and other cardiovascular signals.

## Features

- Simulate real-time ECG, blood pressure, blood saturation, and blood levels data.
- Supports multiple output strategies:
  - Console output for direct observation.
  - File output for data persistence.
  - WebSocket and TCP output for networked data streaming.
- Configurable patient count and data generation rate.
- Randomized patient ID assignment for simulated data diversity.

## Getting Started

### Prerequisites

- Java JDK 11 or newer.
- Maven for managing dependencies and compiling the application.

### Installation

1. Clone the repository:

   ```sh
   git clone https://github.com/tpepels/signal_project.git
   ```

2. Navigate to the project directory:

   ```sh
   cd signal_project
   ```

3. Compile and package the application using Maven:
   ```sh
   mvn clean package
   ```
   This step compiles the source code and packages the application into an executable JAR file located in the `target/` directory.

### Running the Simulator

After packaging, you can run the simulator directly from the executable JAR:

```sh
java -jar target/cardio_generator-1.0-SNAPSHOT.jar
```

To run with specific options (e.g., to set the patient count and choose an output strategy):

```sh
java -jar target/cardio_generator-1.0-SNAPSHOT.jar --patient-count 100 --output file:./output
```

### Supported Output Options

- `console`: Directly prints the simulated data to the console.
- `file:<directory>`: Saves the simulated data to files within the specified directory.
- `websocket:<port>`: Streams the simulated data to WebSocket clients connected to the specified port.
- `tcp:<port>`: Streams the simulated data to TCP clients connected to the specified port.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


## UML Diagram description. 

The UML class diagrams are located at: see the [uml_models](uml_models) folder


1. Alert Generator:
At the center of the design is the AlertGenerator. It receives updated patient information and checks whether the patient’s latest record matches any of the alert rules stored in its recordTypeAlertRulePair collection. This collection maps each record type (such as blood pressure, ECG, or oxygen saturation) to the corresponding AlertRule implementation. When new data arrives, the AlertGenerator looks up the rule for that record type and evaluates it. If the rule detects a critical condition, the AlertGenerator creates an Alert object and triggers it.

Alert rules themselves are modeled using an AlertRules interface, which defines a single method: evaluateCondition(patient). Each specific rule—such as BloodPressureAlertRule, BloodSaturationAlertRule, or ECGAlertRule—implements this interface. This design makes it easy to add new alert types without modifying existing code. Each rule focuses only on its own medical logic.

Once an alert is created, it is passed to the AlertManager, which is responsible for dispatching alerts to the correct medical staff. The AlertManager keeps a mapping of record types to staff members, ensuring that each alert reaches the right person.

2. Data Storage System:

The figure below illustrates the portion of CHMS that deals with the storage and management of patient vitals data. The object PatientData is a single data record of one timestamp. Each individual measurement is saved as a distinct entity to ease querying, versioning, and eventual deletion. The DataStorage class is the center of the design because it holds the collection of data records and offers fundamental data manipulation methods: data insertion, retrieval based on a patient name, retrieval of the newest data record, and removal of outdated data records. That is why the relationship between DataStorage and PatientData objects is composition: the data records are owned and managed by the storage layer.

The DataRetriever class is abstracted away from the storage layer to ensure that data retrieval is decoupled from the insertion process. It serves as the gateway through which clinicians and other modules have access to patient history. The association between DataStorage and DataRetriever is justified because the latter relies on the former's services but does not own or manage it. The AccessControl and RetentionPolicy classes implement permission verification and deletion policy enforcement respectively. They are connected to the DataStorage class since any access to stored data should involve verification and enforcement of permissions and retention policies.

3. Patient Identification System. 
At the center of the design is the PatientIdentifier class. It receives a PatientRecord that contains a patientId, record type, value, and timestamp. It then checks this patientId against the data stored in DataStorage. In the given codebase, DataStorage keeps a HashMap<String, Patient>, where each Patient object holds basic identity information and a list of past PatientRecord entries.

On top of this, there is a HospitalPatient class that inherits from Patient. HospitalPatient represents patients that are officially registered in the hospital context. By extending Patient, it can reuse the common fields (like patientId and patientRecords) and add more hospital‑specific details if needed (for example, ward, doctor, or risk category). This inheritance makes it clear in the model that every HospitalPatient is a Patient, but not every Patient must necessarily carry full hospital metadata. It also gives you flexibility to extend hospital‑specific behavior later without changing the base Patient class.

To handle edge cases, an IdentityManager coordinates checks such as PatientPresentCheck (verifying that the patientId exists) and TimeStampCheck (ensuring the new record is not older than the last one). This separation keeps the matching logic simple while still allowing the system to grow with more validation rules in the future.

4. Data Access Layer:

The figure illustrates how raw data from various external sources becomes an actual patient record in CHMS. In this case, DataListener acts as an abstract superclass, because all sources operate on similar principles - establishing the connection, reading raw data and closing it. The TCPDataListener, WebSocketDataListener and FileDataListener classes extend DataListener. Therefore, the use of inheritance here allows introducing new methods of input data without altering other classes. This relation demonstrates that there is the same contract for each of listeners; however, the way the data is processed may vary.

Further, DataParser, which performs parsing of data obtained by listener, is placed inside the layer. This structure is efficient because listening and parsing processes should be separated, as listeners deal only with data receiving, while DataParser is responsible for its interpreting. Then, the resulting PatientData record is transferred to DataStorage through DataSourceAdapter. As such, this class plays the role of a mediator between the input source and storage system, thus connecting two independent systems in one application.

## Project Members

- Student ID: I6424415
- Student ID: I6443029
