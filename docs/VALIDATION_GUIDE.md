# MetricsEventDto Validation Guide

## Overview

The `MetricsEventDto` class is a REST-facing Data Transfer Object with built-in JSR-303 validation annotations. It automatically validates incoming metric data at the API boundary, ensuring data integrity before processing.

## Validation Annotations

### Required Fields with Validation

| Field | Type | Validation | Error Message |
|-------|------|------------|---------------|
| **serviceName** | String | `@NotBlank` | "serviceName must not be blank" |
| **endpoint** | String | `@NotBlank` | "endpoint must not be blank" |
| **timestamp** | Instant | `@NotNull` | "timestamp must not be null" |
| **latencyMs** | Long | `@NotNull` `@Positive` | "latencyMs must not be null", "latencyMs must be positive" |
| **statusCode** | Integer | `@NotNull` `@Min(100)` `@Max(599)` | "statusCode must not be null", "statusCode must be at least 100", "statusCode must be at most 599" |

### Optional Fields (No Validation)

| Field | Type | Notes |
|-------|------|-------|
| **requestId** | String | Can be null |
| **region** | String | Can be null |
| **method** | String | Can be null |

## Usage with @Valid

### Controller Example

```java
@RestController
@RequestMapping("/api/metrics")
public class MetricsController {
    
    @PostMapping
    public ResponseEntity<?> ingestMetric(@Valid @RequestBody MetricsEventDto eventDto) {
        // Spring automatically validates eventDto
        // If validation fails, returns 400 Bad Request
        // If validation succeeds, method executes normally
        
        Metric metric = metricsService.ingestMetricEvent(eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(metric);
    }
}
```

### Batch Validation Example

```java
@PostMapping("/batch")
public ResponseEntity<?> ingestBatch(
        @Valid @RequestBody List<@Valid MetricsEventDto> events) {
    // Each event in the list is validated individually
    // If any event fails validation, entire batch is rejected
    
    List<Metric> metrics = metricsService.ingestMetricEventsBatch(events);
    return ResponseEntity.ok(metrics);
}
```

## Validation Examples

### ✅ Valid Metric Event

```json
{
  "serviceName": "user-service",
  "endpoint": "/api/users/123",
  "timestamp": "2024-01-20T10:30:45.123Z",
  "latencyMs": 150,
  "statusCode": 200,
  "requestId": "req-uuid-1234",
  "region": "us-east-1",
  "method": "GET"
}
```

**Result:** ✅ Validation passes, metric is ingested

---

### ❌ Invalid: Missing serviceName

```json
{
  "serviceName": "",
  "endpoint": "/api/users/123",
  "timestamp": "2024-01-20T10:30:45.123Z",
  "latencyMs": 150,
  "statusCode": 200
}
```

**Error Response:**
```json
{
  "error": "Validation failed",
  "validationErrors": {
    "serviceName": "serviceName must not be blank"
  }
}
```

---

### ❌ Invalid: Negative latencyMs

```json
{
  "serviceName": "user-service",
  "endpoint": "/api/users/123",
  "timestamp": "2024-01-20T10:30:45.123Z",
  "latencyMs": -50,
  "statusCode": 200
}
```

**Error Response:**
```json
{
  "error": "Validation failed",
  "validationErrors": {
    "latencyMs": "latencyMs must be positive"
  }
}
```

---

### ❌ Invalid: statusCode out of range

```json
{
  "serviceName": "user-service",
  "endpoint": "/api/users/123",
  "timestamp": "2024-01-20T10:30:45.123Z",
  "latencyMs": 150,
  "statusCode": 700
}
```

**Error Response:**
```json
{
  "error": "Validation failed",
  "validationErrors": {
    "statusCode": "statusCode must be at most 599"
  }
}
```

---

### ❌ Invalid: Multiple validation errors

```json
{
  "serviceName": "  ",
  "endpoint": "",
  "timestamp": null,
  "latencyMs": 0,
  "statusCode": 50
}
```

**Error Response:**
```json
{
  "error": "Validation failed",
  "validationErrors": {
    "serviceName": "serviceName must not be blank",
    "endpoint": "endpoint must not be blank",
    "timestamp": "timestamp must not be null",
    "latencyMs": "latencyMs must be positive",
    "statusCode": "statusCode must be at least 100"
  }
}
```

---

## Testing Validation

### Using cURL

#### Test Valid Submission
```bash
curl -X POST http://localhost:8081/api/metrics \
  -H "Content-Type: application/json" \
  -d '{
    "serviceName": "test-service",
    "endpoint": "/test",
    "timestamp": "2024-01-20T10:00:00.000Z",
    "latencyMs": 100,
    "statusCode": 200
  }'
```

**Expected:** `201 Created` with metric data

---

#### Test Missing Required Field
```bash
curl -X POST http://localhost:8081/api/metrics \
  -H "Content-Type: application/json" \
  -d '{
    "serviceName": "test-service",
    "endpoint": "/test",
    "latencyMs": 100,
    "statusCode": 200
  }'
```

**Expected:** `400 Bad Request` with validation error for `timestamp`

---

#### Test Invalid latencyMs (zero)
```bash
curl -X POST http://localhost:8081/api/metrics \
  -H "Content-Type: application/json" \
  -d '{
    "serviceName": "test-service",
    "endpoint": "/test",
    "timestamp": "2024-01-20T10:00:00.000Z",
    "latencyMs": 0,
    "statusCode": 200
  }'
```

**Expected:** `400 Bad Request` - "latencyMs must be positive"

---

#### Test Invalid statusCode
```bash
curl -X POST http://localhost:8081/api/metrics \
  -H "Content-Type: application/json" \
  -d '{
    "serviceName": "test-service",
    "endpoint": "/test",
    "timestamp": "2024-01-20T10:00:00.000Z",
    "latencyMs": 100,
    "statusCode": 999
  }'
```

**Expected:** `400 Bad Request` - "statusCode must be at most 599"

---

## Validation Behavior

### @NotBlank vs @NotNull

- **@NotBlank** (String fields):
  - ❌ Fails: `null`, `""`, `"   "` (whitespace only)
  - ✅ Passes: `"valid-value"`

- **@NotNull** (Object fields):
  - ❌ Fails: `null`
  - ✅ Passes: Any non-null value

### @Positive Validation

- **@Positive** (Long/Integer):
  - ❌ Fails: `null`, `0`, `-1`, `-100`
  - ✅ Passes: `1`, `100`, `9999`

### @Min and @Max Validation

- **@Min(100) @Max(599)** (statusCode):
  - ❌ Fails: `null`, `0`, `50`, `99`, `600`, `999`
  - ✅ Passes: `100`, `200`, `404`, `500`, `599`

## Exception Handling

### Global Exception Handler

The `MetricsController` includes a global exception handler:

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                    FieldError::getField,
                    FieldError::getDefaultMessage
            ));
    
    Map<String, Object> response = new HashMap<>();
    response.put("error", "Validation failed");
    response.put("validationErrors", errors);
    
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
}
```

This handler:
1. Catches validation exceptions automatically
2. Extracts all field errors
3. Returns structured error response with field names and messages
4. Uses HTTP 400 Bad Request status

## Integration Testing

### Spring Boot Test Example

```java
@SpringBootTest
@AutoConfigureMockMvc
class MetricsControllerValidationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void shouldRejectMetricWithBlankServiceName() throws Exception {
        String invalidJson = """
            {
                "serviceName": "",
                "endpoint": "/test",
                "timestamp": "2024-01-20T10:00:00.000Z",
                "latencyMs": 100,
                "statusCode": 200
            }
            """;
        
        mockMvc.perform(post("/api/metrics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.serviceName")
                    .value("serviceName must not be blank"));
    }
    
    @Test
    void shouldRejectMetricWithNegativeLatency() throws Exception {
        String invalidJson = """
            {
                "serviceName": "test-service",
                "endpoint": "/test",
                "timestamp": "2024-01-20T10:00:00.000Z",
                "latencyMs": -50,
                "statusCode": 200
            }
            """;
        
        mockMvc.perform(post("/api/metrics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.latencyMs")
                    .value("latencyMs must be positive"));
    }
    
    @Test
    void shouldAcceptValidMetric() throws Exception {
        String validJson = """
            {
                "serviceName": "test-service",
                "endpoint": "/test",
                "timestamp": "2024-01-20T10:00:00.000Z",
                "latencyMs": 100,
                "statusCode": 200
            }
            """;
        
        mockMvc.perform(post("/api/metrics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                    .value("Metric ingested successfully"));
    }
}
```

## Best Practices

### 1. Always Use @Valid
```java
// ✅ Correct
@PostMapping
public ResponseEntity<?> ingest(@Valid @RequestBody MetricsEventDto dto) {
    // ...
}

// ❌ Wrong - no validation
@PostMapping
public ResponseEntity<?> ingest(@RequestBody MetricsEventDto dto) {
    // Validation is skipped!
}
```

### 2. Handle Validation Errors Gracefully
Always provide an exception handler to return structured error messages.

### 3. Test Edge Cases
- Empty strings vs null
- Zero vs negative numbers
- Boundary values (99, 100, 599, 600)

### 4. Document Validation Requirements
Always document validation rules in API documentation and DTOs.

## Summary

The `MetricsEventDto` provides:

✅ **Automatic validation** at the API boundary  
✅ **Clear error messages** for each validation failure  
✅ **Type safety** with proper Java types  
✅ **JSON-friendly** camelCase naming  
✅ **Production-ready** with comprehensive validation  

Use `@Valid` annotation in controllers to enable automatic validation and ensure data integrity throughout your application.

---

**Related Files:**
- `MetricsEventDto.java` - DTO with validation annotations
- `MetricsController.java` - Controller with @Valid usage
- `MetricsService.java` - Service processing validated events

**Dependencies:**
- `spring-boot-starter-validation` - Provides JSR-303 annotations
