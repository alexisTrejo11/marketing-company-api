# Architecture Documentation

## Table of Contents

- [Overview](#overview)
- [Architectural Patterns](#architectural-patterns)
- [Domain Structure](#domain-structure)
- [Layer Organization](#layer-organization)
- [Data Flow](#data-flow)
- [Design Principles](#design-principles)
- [Technology Stack](#technology-stack)

## Overview

This application implements **Hexagonal Architecture** (also known as Ports and Adapters) combined with **Domain-Driven Design (DDD)** principles. This architecture provides clear separation between business logic and infrastructure concerns, making the system highly maintainable, testable, and adaptable to change.

### Key Architectural Benefits

1. **Independence**: Business logic is independent of frameworks, UI, and databases
2. **Testability**: Core domain can be tested without external dependencies
3. **Flexibility**: Easy to swap infrastructure components (databases, APIs, etc.)
4. **Maintainability**: Clear boundaries and responsibilities reduce complexity
5. **Scalability**: Modular structure enables independent scaling of components

## Architectural Patterns

### 1. Hexagonal Architecture (Ports & Adapters)

```
┌─────────────────────────────────────────────────┐
│              External World                      │
│  (GraphQL, REST, Database, External APIs)       │
└────────────┬────────────────────┬────────────────┘
             │                    │
        ┌────▼────┐          ┌────▼────┐
        │ Input   │          │ Output  │
        │ Adapter │          │ Adapter │
        │(GraphQL)│          │  (JPA)  │
        └────┬────┘          └────┬────┘
             │                    │
        ┌────▼────────────────────▼────┐
        │      Application Core         │
        │                                │
        │  ┌──────────────────────┐    │
        │  │   Domain Layer       │    │
        │  │ (Business Logic)     │    │
        │  └──────────────────────┘    │
        │                                │
        │  ┌──────────────────────┐    │
        │  │   Application Layer  │    │
        │  │   (Use Cases)        │    │
        │  └──────────────────────┘    │
        │                                │
        │  ┌──────────────────────┐    │
        │  │   Port Interfaces    │    │
        │  │  (Input/Output)      │    │
        │  └──────────────────────┘    │
        └────────────────────────────────┘
```

### 2. Domain-Driven Design (DDD)

Each business domain (CRM, Marketing, Customer, Auth) is organized as a bounded context with:

- **Entities**: Objects with identity and lifecycle
- **Value Objects**: Immutable objects defined by their attributes
- **Aggregates**: Clusters of entities and value objects
- **Domain Services**: Business logic that doesn't fit in entities
- **Repositories**: Abstract data access
- **Domain Events**: Communicate between domains

### 3. CQRS Light

While not full CQRS, the architecture separates:

- **Commands**: Mutations that change state
- **Queries**: Read operations that don't modify state

## Domain Structure

The application is organized into bounded contexts:

```
at.backend.MarketingCompany/
│
├── account/                    # Identity & Access Management
│   ├── auth/                  # Authentication (JWT, Sessions)
│   │   ├── core/
│   │   │   ├── domain/       # Auth entities (User credentials)
│   │   │   ├── application/  # Use cases (Login, Signup, Refresh)
│   │   │   └── port/         # Interfaces
│   │   └── adapters/
│   │       ├── inbound/      # GraphQL resolvers
│   │       └── outbound/     # JPA repositories, Redis cache
│   │
│   └── user/                  # User Management
│       ├── core/
│       │   ├── domain/       # User entity, value objects
│       │   ├── application/  # User operations
│       │   └── port/
│       └── adapters/
│
├── customer/                   # Customer Management Context
│   ├── core/
│   │   ├── domain/
│   │   │   ├── CustomerCompany.java       # Aggregate root
│   │   │   ├── ContactPerson.java         # Entity
│   │   │   ├── CompanyProfile.java        # Value object
│   │   │   ├── ContractDetails.java       # Value object
│   │   │   └── CompanyAddress.java        # Value object
│   │   ├── application/
│   │   │   ├── CreateCustomerUseCase.java
│   │   │   ├── UpdateCustomerUseCase.java
│   │   │   └── CustomerQueryService.java
│   │   └── port/
│   │       ├── input/        # Use case interfaces
│   │       └── output/       # Repository interfaces
│   └── adapter/
│       ├── input/
│       │   └── graphql/      # CustomerResolver
│       └── output/
│           └── persistence/   # JPA implementation
│
├── crm/                        # CRM Business Context
│   ├── opportunity/           # Sales Pipeline
│   │   ├── core/
│   │   │   ├── domain/
│   │   │   │   ├── Opportunity.java      # Aggregate
│   │   │   │   ├── OpportunityStatus.java
│   │   │   │   └── EstimatedValue.java
│   │   │   ├── application/
│   │   │   └── port/
│   │   └── adapter/
│   │
│   ├── quote/                 # Quotations
│   │   ├── core/
│   │   │   ├── domain/
│   │   │   │   ├── Quote.java
│   │   │   │   ├── QuoteItem.java
│   │   │   │   └── QuoteCalculator.java  # Domain service
│   │   │   ├── application/
│   │   │   └── port/
│   │   └── adapter/
│   │
│   ├── deal/                  # Closed Deals
│   ├── tasks/                 # Task Management
│   ├── interaction/           # Customer Interactions
│   └── servicePackage/        # Service Offerings
│
├── marketing/                  # Marketing Management Context
│   ├── campaign/              # Campaign Management
│   │   ├── core/
│   │   │   ├── domain/
│   │   │   │   ├── MarketingCampaign.java    # Aggregate
│   │   │   │   ├── CampaignBudget.java       # Value object
│   │   │   │   ├── CampaignPeriod.java       # Value object
│   │   │   │   ├── TargetAudience.java       # Value object
│   │   │   │   └── CampaignValidator.java    # Domain service
│   │   │   ├── application/
│   │   │   └── port/
│   │   └── adapter/
│   │
│   ├── channel/               # Marketing Channels
│   ├── activity/              # Campaign Activities
│   ├── metric/                # Performance Metrics
│   ├── attribution/           # Attribution Tracking
│   ├── asset/                 # Marketing Assets
│   ├── interaction/           # Campaign Interactions
│   ├── target/                # Target Segments
│   └── ab_test/               # A/B Testing
│
├── config/                     # Cross-cutting Configuration
│   ├── SecurityConfig.java    # Spring Security
│   ├── GraphQLConfig.java     # GraphQL setup
│   ├── RedisConfig.java       # Cache configuration
│   ├── cors/                  # CORS policies
│   ├── logging/               # Audit logging
│   └── ratelimit/             # Rate limiting
│
└── shared/                     # Shared Kernel
    ├── domain/                # Base domain types
    │   ├── BaseDomainEntity.java
    │   ├── BaseId.java
    │   └── ValidationResult.java
    ├── dto/                   # DTOs and mappers
    ├── exception/             # Exception hierarchy
    └── graphql/               # GraphQL utilities
```

## Layer Organization

Each domain module follows this consistent structure:

### Core Layer (Business Logic)

#### 1. Domain Package (`core/domain/`)

Contains the pure business logic:

```java
// Aggregate Root
public class CustomerCompany extends BaseDomainEntity {
    private CompanyId id;
    private CompanyName name;
    private CompanyProfile profile;
    private List<ContactPerson> contacts;
    private ContractDetails contract;

    // Business methods
    public void updateProfile(CompanyProfile newProfile) {
        validateProfile(newProfile);
        this.profile = newProfile;
        addDomainEvent(new CompanyProfileUpdatedEvent(this.id));
    }

    public boolean canCreateOpportunity() {
        return this.status == CompanyStatus.ACTIVE
            && this.contract.isValid();
    }

    private void validateProfile(CompanyProfile profile) {
        // Domain validation logic
    }
}

// Value Object
public record CompanyName(String value) {
    public CompanyName {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidCompanyNameException();
        }
        if (value.length() < 2 || value.length() > 255) {
            throw new InvalidCompanyNameException();
        }
    }
}
```

**Characteristics:**

- No framework dependencies
- Pure business logic
- Fully testable without infrastructure
- Rich domain models (not anemic)

#### 2. Application Package (`core/application/`)

Contains use cases and orchestration:

```java
@Service
public class CreateCustomerUseCase {
    private final CustomerRepository repository;
    private final EventPublisher eventPublisher;

    public CustomerCompany execute(CreateCustomerCommand command) {
        // Validate business rules
        validateUniqueTaxId(command.taxId());

        // Create domain object
        var customer = CustomerCompany.create(
            command.name(),
            command.profile(),
            command.contacts()
        );

        // Persist
        var saved = repository.save(customer);

        // Publish events
        eventPublisher.publish(new CustomerCreatedEvent(saved.getId()));

        return saved;
    }
}
```

**Responsibilities:**

- Orchestrate domain objects
- Transaction boundaries
- Cross-domain coordination
- Event publishing

#### 3. Port Package (`core/port/`)

Defines interfaces (contracts):

```java
// Input Port (use case interface)
public interface CreateCustomerPort {
    CustomerCompany create(CreateCustomerCommand command);
}

// Output Port (repository interface)
public interface CustomerRepository {
    CustomerCompany save(CustomerCompany customer);
    Optional<CustomerCompany> findById(CompanyId id);
    List<CustomerCompany> findByStatus(CompanyStatus status);
}
```

**Purpose:**

- Define boundaries
- Enable dependency inversion
- Allow multiple implementations

### Adapter Layer (Infrastructure)

#### 1. Input Adapters (`adapter/input/`)

Handle external requests:

```java
@GraphQLResolver
public class CustomerResolver {
    private final CreateCustomerPort createCustomerPort;

    @MutationMapping
    public CustomerResponse createCustomer(@Argument CreateCustomerInput input) {
        var command = mapToCommand(input);
        var customer = createCustomerPort.create(command);
        return mapToResponse(customer);
    }

    @QueryMapping
    public PageResponse<CustomerResponse> customers(
        @Argument int page,
        @Argument int size
    ) {
        // Handle query
    }
}
```

**Responsibilities:**

- Handle GraphQL requests
- Input validation (format)
- DTO mapping
- Exception translation

#### 2. Output Adapters (`adapter/output/`)

Implement infrastructure concerns:

```java
@Repository
public class JpaCustomerRepository implements CustomerRepository {
    private final CustomerJpaRepository jpaRepository;
    private final CustomerMapper mapper;

    @Override
    public CustomerCompany save(CustomerCompany customer) {
        var entity = mapper.toEntity(customer);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<CustomerCompany> findById(CompanyId id) {
        return jpaRepository.findById(id.value())
            .map(mapper::toDomain);
    }
}

@Entity
@Table(name = "customer_companies")
class CustomerCompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    // JPA mappings
}
```

**Responsibilities:**

- Database access (JPA)
- External API calls
- File system operations
- Message queues

## Data Flow

### Command Flow (Mutations)

```
1. GraphQL Request
   ↓
2. Input Adapter (Resolver)
   - Receives GraphQL input
   - Validates format
   - Maps to command DTO
   ↓
3. Application Service (Use Case)
   - Validates business rules
   - Orchestrates domain objects
   - Manages transaction
   ↓
4. Domain Layer
   - Executes business logic
   - Validates invariants
   - Generates domain events
   ↓
5. Output Adapter (Repository)
   - Persists to database
   - Maps domain to entity
   ↓
6. Response
   - Maps domain to DTO
   - Returns to client
```

### Query Flow

```
1. GraphQL Query
   ↓
2. Input Adapter (Resolver)
   - Receives query parameters
   - Validates input
   ↓
3. Query Service
   - Optimized read operations
   - May bypass domain objects
   ↓
4. Output Adapter (Repository)
   - Fetch from database
   - Apply filters/pagination
   ↓
5. Response
   - Map to response DTO
   - Return to client
```

## Design Principles

### 1. SOLID Principles

- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Open for extension, closed for modification
- **Liskov Substitution**: Subtypes must be substitutable
- **Interface Segregation**: Many specific interfaces over one general
- **Dependency Inversion**: Depend on abstractions, not concretions

### 2. Domain-Driven Design Patterns

**Entities vs Value Objects:**

```java
// Entity - has identity and lifecycle
public class CustomerCompany {
    private CompanyId id;  // Identity
    private CompanyName name;

    @Override
    public boolean equals(Object o) {
        // Compare by ID only
    }
}

// Value Object - defined by attributes
public record CompanyName(String value) {
    // Immutable
    // Equals by value comparison
}
```

**Aggregate Roots:**

```java
// Aggregate Root - controls access to entire aggregate
public class MarketingCampaign {
    private CampaignId id;
    private List<CampaignActivity> activities;  // Part of aggregate
    private List<CampaignMetric> metrics;       // Part of aggregate

    // Only way to add activity
    public void addActivity(ActivityData data) {
        var activity = CampaignActivity.create(this.id, data);
        activities.add(activity);
    }

    // Maintain invariants
    private void validateActivityCount() {
        if (activities.size() > MAX_ACTIVITIES) {
            throw new TooManyActivitiesException();
        }
    }
}
```

**Domain Services:**

```java
// Complex business logic that doesn't belong to a single entity
public class QuoteCalculator {
    public QuoteTotal calculateTotal(Quote quote) {
        var subtotal = calculateSubtotal(quote.getItems());
        var discount = calculateDiscount(quote);
        var tax = calculateTax(subtotal, discount);
        return new QuoteTotal(subtotal, discount, tax);
    }
}
```

### 3. Clean Architecture

- **Dependency Rule**: Dependencies point inward
- **Core is isolated**: No framework dependencies in domain
- **Testable**: Each layer can be tested independently

### 4. Separation of Concerns

- **Domain Logic**: Pure business rules
- **Application Logic**: Use case orchestration
- **Infrastructure**: Technical implementation
- **Presentation**: Data transformation

## Technology Stack

### Core Framework

- **Spring Boot 3.4.2**: Application framework
- **Spring Data JPA**: Data access
- **Hibernate**: ORM implementation

### API Layer

- **Spring for GraphQL**: GraphQL integration
- **GraphQL Java Tools**: Schema-first approach

### Infrastructure

- **PostgreSQL 16**: Primary database
- **Redis 7**: Caching and sessions
- **Flyway**: Database versioning

### Security

- **Spring Security**: Authentication/Authorization
- **JWT**: Token-based auth

### Build & Deployment

- **Gradle 8.11**: Build automation
- **Docker**: Containerization
- **Docker Compose**: Multi-container orchestration

## Best Practices

### 1. Rich Domain Models

Avoid anemic domain models. Put behavior where it belongs:

```java
// ❌ Bad: Anemic model
public class Campaign {
    private String status;
    // Only getters/setters
}

public class CampaignService {
    public void startCampaign(Campaign campaign) {
        campaign.setStatus("ACTIVE");
    }
}

// ✅ Good: Rich domain model
public class Campaign {
    private CampaignStatus status;

    public void start() {
        if (!canStart()) {
            throw new CampaignCannotStartException();
        }
        this.status = CampaignStatus.ACTIVE;
        addDomainEvent(new CampaignStartedEvent(this.id));
    }

    private boolean canStart() {
        return status == CampaignStatus.PLANNED
            && budget.isAllocated()
            && startDate.isInFuture();
    }
}
```

### 2. Immutability

Use immutable value objects:

```java
public record Money(BigDecimal amount, Currency currency) {
    public Money {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeMoneyException();
        }
    }

    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }
}
```

### 3. Fail Fast

Validate at boundaries:

```java
public record Email(String value) {
    private static final Pattern PATTERN =
        Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        if (value == null || !PATTERN.matcher(value).matches()) {
            throw new InvalidEmailException(value);
        }
    }
}
```

### 4. Domain Events

Communicate between bounded contexts:

```java
public record CustomerCreatedEvent(
    CompanyId customerId,
    CompanyName name,
    Instant occurredAt
) implements DomainEvent {
    public CustomerCreatedEvent(CompanyId customerId, CompanyName name) {
        this(customerId, name, Instant.now());
    }
}
```

## Testing Strategy

### 1. Unit Tests (Domain Layer)

```java
@Test
void shouldCalculateCorrectBudgetUtilization() {
    var campaign = new MarketingCampaign(
        totalBudget: 10000,
        spentAmount: 7500
    );

    assertThat(campaign.getBudgetUtilization())
        .isEqualTo(75.0);
}
```

### 2. Integration Tests (Application Layer)

```java
@SpringBootTest
@Testcontainers
class CreateCustomerUseCaseTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Test
    void shouldCreateCustomer() {
        var command = new CreateCustomerCommand(...);
        var customer = createCustomerUseCase.execute(command);
        assertThat(customer.getId()).isNotNull();
    }
}
```

### 3. API Tests (GraphQL)

```java
@GraphQlTest(CustomerResolver.class)
class CustomerResolverTest {
    @Test
    void shouldCreateCustomer() {
        var mutation = """
            mutation {
                createCustomer(input: { name: "Test Co" }) {
                    id
                    name
                }
            }
            """;

        graphQlTester.document(mutation)
            .execute()
            .path("createCustomer.name")
            .entity(String.class)
            .isEqualTo("Test Co");
    }
}
```

## Performance Considerations

### 1. N+1 Query Prevention

- Use `@EntityGraph` for eager loading
- Batch fetching with GraphQL DataLoader

### 2. Caching Strategy

- Redis for session storage
- Second-level Hibernate cache
- GraphQL query caching

### 3. Database Optimization

- Proper indexing (see V7\_\_crm_indexes.sql)
- Connection pooling (HikariCP)
- Batch operations

---

This architecture provides a solid foundation for building scalable, maintainable enterprise applications. The clear separation of concerns and well-defined boundaries make the system easy to understand, test, and evolve over time.
