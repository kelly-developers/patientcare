src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── cardiovascular/
│   │           └── patientcare/
│   │               ├── CardiovascularPatientCareApplication.java
│   │               ├── config/
│   │               │   ├── SecurityConfig.java
│   │               │   ├── JwtAuthenticationFilter.java
│   │               │   ├── CorsConfig.java
│   │               │   └── DatabaseConfig.java
│   │               ├── controller/
│   │               │   ├── AuthController.java
│   │               │   ├── PatientController.java
│   │               │   ├── AppointmentController.java
│   │               │   ├── SurgeryController.java
│   │               │   ├── ConsentController.java
│   │               │   ├── DoctorAnalysisController.java
│   │               │   ├── SurgicalDecisionController.java
│   │               │   ├── PreOperativeController.java
│   │               │   ├── DuringOperationController.java
│   │               │   ├── ICUController.java
│   │               │   ├── PostOperativeController.java
│   │               │   ├── PharmacyController.java
│   │               │   ├── LabTestController.java
│   │               │   ├── VitalDataController.java
│   │               │   ├── NotificationController.java
│   │               │   └── ExportController.java
│   │               ├── service/
│   │               │   ├── AuthService.java
│   │               │   ├── JwtService.java
│   │               │   ├── PatientService.java
│   │               │   ├── AppointmentService.java
│   │               │   ├── SurgeryService.java
│   │               │   ├── ConsentService.java
│   │               │   ├── DoctorAnalysisService.java
│   │               │   ├── SurgicalDecisionService.java
│   │               │   ├── PreOperativeService.java
│   │               │   ├── DuringOperationService.java
│   │               │   ├── ICUService.java
│   │               │   ├── PostOperativeService.java
│   │               │   ├── PharmacyService.java
│   │               │   ├── LabTestService.java
│   │               │   ├── VitalDataService.java
│   │               │   ├── NotificationService.java
│   │               │   └── ExportService.java
│   │               ├── repository/
│   │               │   ├── UserRepository.java
│   │               │   ├── PatientRepository.java
│   │               │   ├── AppointmentRepository.java
│   │               │   ├── SurgeryRepository.java
│   │               │   ├── ConsentRepository.java
│   │               │   ├── DoctorAnalysisRepository.java
│   │               │   ├── SurgicalDecisionRepository.java
│   │               │   ├── PreOperativeRepository.java
│   │               │   ├── DuringOperationRepository.java
│   │               │   ├── ICURepository.java
│   │               │   ├── PostOperativeRepository.java
│   │               │   ├── PharmacyRepository.java
│   │               │   ├── LabTestRepository.java
│   │               │   ├── VitalDataRepository.java
│   │               │   └── NotificationRepository.java
│   │               ├── model/
│   │               │   ├── User.java
│   │               │   ├── Patient.java
│   │               │   ├── Appointment.java
│   │               │   ├── Surgery.java
│   │               │   ├── Consent.java
│   │               │   ├── DoctorAnalysis.java
│   │               │   ├── SurgicalDecision.java
│   │               │   ├── PreOperative.java
│   │               │   ├── DuringOperation.java
│   │               │   ├── ICU.java
│   │               │   ├── PostOperative.java
│   │               │   ├── Pharmacy.java
│   │               │   ├── LabTest.java
│   │               │   ├── VitalData.java
│   │               │   └── Notification.java
│   │               ├── dto/
│   │               │   ├── request/
│   │               │   │   ├── AuthRequest.java
│   │               │   │   ├── PatientRequest.java
│   │               │   │   ├── AppointmentRequest.java
│   │               │   │   ├── SurgeryRequest.java
│   │               │   │   ├── ConsentRequest.java
│   │               │   │   ├── DoctorAnalysisRequest.java
│   │               │   │   ├── SurgicalDecisionRequest.java
│   │               │   │   ├── PreOperativeRequest.java
│   │               │   │   ├── DuringOperationRequest.java
│   │               │   │   ├── ICURequest.java
│   │               │   │   ├── PostOperativeRequest.java
│   │               │   │   ├── PharmacyRequest.java
│   │               │   │   ├── LabTestRequest.java
│   │               │   │   ├── VitalDataRequest.java
│   │               │   │   └── NotificationRequest.java
│   │               │   └── response/
│   │               │       ├── AuthResponse.java
│   │               │       ├── PatientResponse.java
│   │               │       ├── AppointmentResponse.java
│   │               │       ├── SurgeryResponse.java
│   │               │       ├── ConsentResponse.java
│   │               │       ├── DoctorAnalysisResponse.java
│   │               │       ├── SurgicalDecisionResponse.java
│   │               │       ├── PreOperativeResponse.java
│   │               │       ├── DuringOperationResponse.java
│   │               │       ├── ICUResponse.java
│   │               │       ├── PostOperativeResponse.java
│   │               │       ├── PharmacyResponse.java
│   │               │       ├── LabTestResponse.java
│   │               │       ├── VitalDataResponse.java
│   │               │       └── NotificationResponse.java
│   │               └── exception/
│   │                   ├── GlobalExceptionHandler.java
│   │                   ├── ResourceNotFoundException.java
│   │                   ├── AuthenticationException.java
│   │                   └── ValidationException.java
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       ├── application-prod.properties
│       └── data.sql
├── test/
│   └── java/
│       └── com/
│           └── cardiovascular/
│               └── patientcare/
│                   ├── service/
│                   └── controller/
└── pom.xml







## 📊 Database Schema Structure

```sql
-- Users/Doctors table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role ENUM('DOCTOR', 'ADMIN', 'NURSE') NOT NULL,
    specialty VARCHAR(100),
    available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Patients table
CREATE TABLE patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    medical_history TEXT,
    allergies TEXT,
    current_medications TEXT,
    research_consent BOOLEAN DEFAULT FALSE,
    sample_storage_consent BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Appointments table
CREATE TABLE appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    type ENUM('CONSULTATION', 'FOLLOW_UP', 'EMERGENCY', 'SURGERY') NOT NULL,
    status ENUM('SCHEDULED', 'CONFIRMED', 'COMPLETED', 'CANCELLED') NOT NULL,
    reason TEXT NOT NULL,
    notes TEXT,
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') NOT NULL,
    arrival_status ENUM('PENDING', 'ARRIVED', 'LATE', 'NO_SHOW') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (doctor_id) REFERENCES users(id)
);

-- Doctor Analysis table
CREATE TABLE doctor_analysis (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    symptoms TEXT NOT NULL,
    diagnosis VARCHAR(255) NOT NULL,
    clinical_notes TEXT,
    recommend_surgery BOOLEAN DEFAULT FALSE,
    surgery_type VARCHAR(100),
    surgery_urgency ENUM('EMERGENCY', 'URGENT', 'ROUTINE', 'ELECTIVE'),
    require_lab_tests BOOLEAN DEFAULT FALSE,
    lab_tests_needed TEXT,
    status ENUM('PENDING', 'COMPLETED') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (doctor_id) REFERENCES users(id)
);

-- Surgical Decisions table
CREATE TABLE surgical_decisions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    surgery_id BIGINT NOT NULL,
    surgeon_name VARCHAR(100) NOT NULL,
    decision_status ENUM('ACCEPTED', 'DECLINED') NOT NULL,
    comments TEXT,
    factors_considered JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Surgeries table
CREATE TABLE surgeries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    procedure_name VARCHAR(255) NOT NULL,
    urgency ENUM('EMERGENCY', 'URGENT', 'ROUTINE', 'ELECTIVE') NOT NULL,
    recommended_by VARCHAR(100) NOT NULL,
    diagnosis TEXT NOT NULL,
    status ENUM('PENDING_CONSENT', 'SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') NOT NULL,
    consent_date TIMESTAMP NULL,
    scheduled_date TIMESTAMP NULL,
    actual_date TIMESTAMP NULL,
    completed_date TIMESTAMP NULL,
    surgeon_name VARCHAR(100),
    duration_minutes INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id)
);

-- Consent Management table
CREATE TABLE consent_management (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    surgery_id BIGINT NOT NULL,
    patient_name VARCHAR(100) NOT NULL,
    next_of_kin VARCHAR(100) NOT NULL,
    next_of_kin_phone VARCHAR(20) NOT NULL,
    understood_risks BOOLEAN NOT NULL,
    understood_benefits BOOLEAN NOT NULL,
    understood_alternatives BOOLEAN NOT NULL,
    consent_to_surgery BOOLEAN NOT NULL,
    signature VARCHAR(100) NOT NULL,
    consent_decision ENUM('ACCEPTED', 'DECLINED') NOT NULL,
    consent_file_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (surgery_id) REFERENCES surgeries(id)
);

-- Pre-operative Checklist table
CREATE TABLE pre_operative_checklist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    procedure_name VARCHAR(255) NOT NULL,
    -- Patient Identity
    patient_identity_confirmed BOOLEAN NOT NULL,
    consent_signed BOOLEAN NOT NULL,
    site_marked BOOLEAN NOT NULL,
    -- Anesthesia Safety
    anesthesia_machine_checked BOOLEAN NOT NULL,
    oxygen_available BOOLEAN NOT NULL,
    suction_available BOOLEAN NOT NULL,
    -- Patient Assessment
    known_allergy VARCHAR(50),
    difficult_airway VARCHAR(50),
    aspiration_risk VARCHAR(50),
    blood_loss_risk VARCHAR(50),
    -- Equipment
    sterile_indicators_confirmed BOOLEAN NOT NULL,
    equipment_issues TEXT,
    implant_available VARCHAR(50),
    -- Team Confirmation
    nurse_confirmed BOOLEAN NOT NULL,
    anesthetist_confirmed BOOLEAN NOT NULL,
    surgeon_confirmed BOOLEAN NOT NULL,
    -- Research Consent
    research_consent_given BOOLEAN DEFAULT FALSE,
    data_usage_consent BOOLEAN DEFAULT FALSE,
    sample_storage_consent BOOLEAN DEFAULT FALSE,
    research_consent_date DATE,
    research_consent_witness VARCHAR(100),
    -- Additional Info
    additional_concerns TEXT,
    completed_by VARCHAR(100) NOT NULL,
    completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id)
);

-- During Operation Monitoring table
CREATE TABLE during_operation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    surgery_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NULL,
    status ENUM('IN_PROGRESS', 'COMPLETED', 'EMERGENCY') NOT NULL,
    -- Vitals
    heart_rate INT,
    blood_pressure_systolic INT,
    blood_pressure_diastolic INT,
    oxygen_saturation INT,
    temperature DECIMAL(3,1),
    blood_loss INT,
    urine_output INT,
    -- Surgical Details
    surgical_notes JSON,
    complications JSON,
    medications JSON,
    outcomes JSON,
    surgical_goals JSON,
    equipment_check JSON,
    closure_checklist JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (surgery_id) REFERENCES surgeries(id),
    FOREIGN KEY (patient_id) REFERENCES patients(id)
);

-- ICU Monitoring table
CREATE TABLE icu_monitoring (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    -- Hemodynamics
    heart_rate INT NOT NULL,
    blood_pressure_systolic INT NOT NULL,
    blood_pressure_diastolic INT NOT NULL,
    mean_arterial_pressure INT NOT NULL,
    central_venous_pressure INT,
    -- Respiratory
    respiratory_rate INT NOT NULL,
    oxygen_saturation INT NOT NULL,
    fio2 INT,
    peep INT,
    tidal_volume INT,
    ventilation_mode VARCHAR(50),
    -- Neurological
    gcs_total INT,
    gcs_eyes INT,
    gcs_verbal INT,
    gcs_motor INT,
    pupil_size_left VARCHAR(10),
    pupil_size_right VARCHAR(10),
    pupil_reaction_left BOOLEAN,
    pupil_reaction_right BOOLEAN,
    -- Metabolic
    temperature DECIMAL(3,1) NOT NULL,
    blood_glucose INT,
    urine_output INT,
    -- Lab Results
    abg_ph DECIMAL(2,2),
    abg_pao2 INT,
    abg_paco2 INT,
    abg_hco3 INT,
    -- Metadata
    recorded_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id)
);

-- Vital Data table
CREATE TABLE vital_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    systolic_bp INT NOT NULL,
    diastolic_bp INT NOT NULL,
    heart_rate INT NOT NULL,
    respiratory_rate INT,
    temperature DECIMAL(3,1) NOT NULL,
    oxygen_saturation INT NOT NULL,
    height DECIMAL(4,1),
    weight DECIMAL(4,1),
    blood_glucose INT,
    pain_level INT,
    bmi DECIMAL(4,1),
    risk_level ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') NOT NULL,
    notes TEXT,
    recorded_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id)
);

-- Lab Tests table
CREATE TABLE lab_tests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    test_type VARCHAR(100) NOT NULL,
    test_name VARCHAR(255) NOT NULL,
    ordered_by VARCHAR(100) NOT NULL,
    ordered_date TIMESTAMP NOT NULL,
    status ENUM('ORDERED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') NOT NULL,
    priority ENUM('ROUTINE', 'URGENT', 'STAT') NOT NULL,
    results TEXT,
    clinical_notes TEXT,
    notes TEXT,
    report_date TIMESTAMP NULL,
    completed_date TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id)
);

-- Pharmacy/Prescriptions table
CREATE TABLE prescriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    medication_name VARCHAR(255) NOT NULL,
    dosage VARCHAR(100) NOT NULL,
    frequency VARCHAR(100) NOT NULL,
    duration VARCHAR(100) NOT NULL,
    instructions TEXT,
    doctor_id BIGINT NOT NULL,
    status ENUM('PENDING', 'DISPENSED', 'COLLECTED') NOT NULL,
    dispensed_at TIMESTAMP NULL,
    collected_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (doctor_id) REFERENCES users(id)
);

-- Notifications table
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('APPOINTMENT', 'SURGERY', 'PRESCRIPTION', 'EMERGENCY', 'REMINDER') NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    doctor_id BIGINT,
    patient_id BIGINT,
    appointment_id BIGINT,
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') NOT NULL,
    read BOOLEAN DEFAULT FALSE,
    scheduled_for TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (doctor_id) REFERENCES users(id),
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);

-- Post-operative Follow-up table
CREATE TABLE post_operative_followup (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    surgery_id BIGINT NOT NULL,
    followup_type ENUM('POST_SURGERY', 'MEDICATION_REVIEW', 'ROUTINE_CHECKUP', 'COMPLICATION_ASSESSMENT') NOT NULL,
    symptoms TEXT,
    improvements TEXT,
    concerns TEXT,
    next_visit_date DATE,
    medication_adherence BOOLEAN,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (surgery_id) REFERENCES surgeries(id)
);
```

## 🔌 Complete API Endpoints Documentation

### Authentication Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/auth/signin` | User login | `{username, password}` |
| POST | `/api/auth/signup` | User registration | `{username, email, password, firstName, lastName, role}` |
| GET | `/api/auth/me` | Get current user info | - |

### Patient Management Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/api/patients` | Get all patients | - |
| POST | `/api/patients` | Create new patient | PatientDTO |
| GET | `/api/patients/{id}` | Get patient by ID | - |
| PUT | `/api/patients/{id}` | Update patient | PatientDTO |
| DELETE | `/api/patients/{id}` | Delete patient | - |
| GET | `/api/patients/search` | Search patients | `?query=name` |

### Appointment Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/api/appointments` | Get all appointments | - |
| POST | `/api/appointments` | Create appointment | AppointmentDTO |
| GET | `/api/appointments/{id}` | Get appointment by ID | - |
| PUT | `/api/appointments/{id}` | Update appointment | AppointmentDTO |
| PUT | `/api/appointments/{id}/status` | Update status | `{status}` |
| PUT | `/api/appointments/{id}/arrival` | Update arrival status | `{arrivalStatus}` |

### Doctor Analysis Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/analysis` | Create analysis | DoctorAnalysisDTO |
| GET | `/api/analysis/patient/{patientId}` | Get patient analyses | - |
| GET | `/api/analysis/{id}` | Get analysis by ID | - |
| PUT | `/api/analysis/{id}` | Update analysis | DoctorAnalysisDTO |

### Surgical Decision Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/surgical-decisions` | Submit decision | SurgicalDecisionDTO |
| GET | `/api/surgical-decisions/surgery/{surgeryId}` | Get surgery decisions | - |
| GET | `/api/surgical-decisions/consensus/{surgeryId}` | Get decision consensus | - |

### Surgery Management Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/api/surgeries` | Get all surgeries | - |
| POST | `/api/surgeries` | Create surgery | SurgeryDTO |
| GET | `/api/surgeries/{id}` | Get surgery by ID | - |
| PUT | `/api/surgeries/{id}` | Update surgery | SurgeryDTO |
| PUT | `/api/surgeries/{id}/status` | Update surgery status | `{status}` |
| GET | `/api/surgeries/pending-consent` | Get pending consent surgeries | - |

### Consent Management Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/consent` | Submit consent decision | ConsentDTO |
| GET | `/api/consent/surgery/{surgeryId}` | Get consent by surgery | - |
| POST | `/api/consent/upload` | Upload signed consent | Multipart file |
| GET | `/api/consent/stored` | Get stored consent forms | - |

### Pre-operative Checklist Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/preoperative` | Submit checklist | PreOperativeDTO |
| GET | `/api/preoperative/patient/{patientId}` | Get patient checklist | - |
| GET | `/api/preoperative/{id}` | Get checklist by ID | - |

### During Operation Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/during-operation` | Start operation | DuringOperationDTO |
| PUT | `/api/during-operation/{id}` | Update operation | DuringOperationDTO |
| PUT | `/api/during-operation/{id}/complete` | Complete operation | - |
| POST | `/api/during-operation/{id}/vitals` | Update vitals | Vital data |
| POST | `/api/during-operation/{id}/notes` | Add surgical note | Note data |
| POST | `/api/during-operation/{id}/complications` | Add complication | Complication data |

### ICU Monitoring Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/icu` | Add ICU record | ICUDTO |
| GET | `/api/icu/patient/{patientId}` | Get patient ICU data | - |
| PUT | `/api/icu/{id}` | Update ICU record | ICUDTO |
| POST | `/api/icu/{id}/vitals` | Update ICU vitals | Vital data |
| POST | `/api/icu/{id}/medications` | Add medication | Medication data |

### Vital Data Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/vital-data` | Record vital data | VitalDataDTO |
| GET | `/api/vital-data/patient/{patientId}` | Get patient vitals | - |
| GET | `/api/vital-data/recorded-by-me` | Get my recorded vitals | - |
| GET | `/api/vital-data/critical` | Get critical vitals | - |

### Lab Tests Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/lab-tests` | Order lab test | LabTestDTO |
| GET | `/api/lab-tests` | Get all lab tests | - |
| GET | `/api/lab-tests/{id}` | Get lab test by ID | - |
| PUT | `/api/lab-tests/{id}/status` | Update test status | `{status, results}` |
| GET | `/api/lab-tests/patient/{patientId}` | Get patient lab tests | - |

### Pharmacy Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/prescriptions` | Create prescription | PharmacyDTO |
| GET | `/api/prescriptions` | Get all prescriptions | - |
| GET | `/api/prescriptions/patient/{patientId}` | Get patient prescriptions | - |
| PUT | `/api/prescriptions/{id}/status` | Update prescription status | `{status}` |

### Notification Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/api/notifications` | Get all notifications | - |
| GET | `/api/notifications/unread` | Get unread notifications | - |
| PUT | `/api/notifications/{id}/read` | Mark as read | - |
| PUT | `/api/notifications/read-all` | Mark all as read | - |
| DELETE | `/api/notifications/{id}` | Delete notification | - |

### Post-operative Follow-up Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/postoperative` | Record follow-up | PostOperativeDTO |
| GET | `/api/postoperative/patient/{patientId}` | Get patient follow-ups | - |
| GET | `/api/postoperative/surgery/{surgeryId}` | Get surgery follow-ups | - |

### Data Export Endpoints

| Method | Endpoint | Description | Parameters |
|--------|----------|-------------|------------|
| GET | `/api/export/patients` | Export patients data | `?format=csv/json` |
| GET | `/api/export/surgeries` | Export surgeries data | `?format=csv/json` |
| GET | `/api/export/appointments` | Export appointments | `?format=csv/json` |

## 🛠️ Key Features Implementation

### 1. **Authentication & Authorization**
- JWT-based authentication
- Role-based access control (DOCTOR, ADMIN, NURSE)
- Secure password hashing with BCrypt

### 2. **Patient Workflow Management**
- Complete patient lifecycle from registration to post-operative care
- Research consent management with ethical considerations
- Sample storage consent tracking

### 3. **Surgical Decision Collaboration**
- Multi-surgeon review system (3 reviews required)
- Consensus-based decision making
- Factor-based decision documentation

### 4. **Real-time Monitoring**
- Live surgery tracking
- ICU vital monitoring with alerts
- Real-time notification system

### 5. **Compliance & Safety**
- WHO Surgical Safety Checklist implementation
- Consent management with digital signatures
- Audit trails for all medical decisions

### 6. **Data Management**
- Comprehensive data export capabilities
- Risk level calculations for vital data
- Data integrity validation

## 🔧 Configuration Files

### `application.properties`
```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/api

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/cardiovascular_db
spring.datasource.username=root
spring.datasource.password=password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT Configuration
jwt.secret=your-jwt-secret-key
jwt.expiration=86400000

# CORS Configuration
cors.allowed-origins=http://localhost:3000,https://patientcares.netlify.app
```

### `pom.xml` Dependencies
```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Database -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>

    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
    </dependency>

    <!-- Utilities -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
    </dependency>
</dependencies>
```

This backend structure provides a robust, scalable foundation for your cardiovascular patient management system, supporting all the frontend functionalities while maintaining security, compliance, and data integrity.