# GraphQL API Documentation

## Table of Contents

- [Overview](#overview)
- [GraphQL Endpoints](#graphql-endpoints)
- [Authentication](#authentication)
- [Custom Scalars](#custom-scalars)
- [Common Types](#common-types)
- [Authentication API](#authentication-api)
- [User Management API](#user-management-api)
- [Customer Company API](#customer-company-api)
- [CRM API](#crm-api)
  - [Opportunities](#opportunities)
  - [Quotes](#quotes)
  - [Deals](#deals)
  - [Tasks](#tasks)
  - [Interactions](#interactions)
  - [Service Packages](#service-packages)
- [Marketing API](#marketing-api)
  - [Campaigns](#campaigns)
  - [Channels](#channels)
  - [Activities](#activities)
  - [Metrics](#metrics)
  - [Attribution](#attribution)
  - [Assets](#assets)
  - [A/B Tests](#ab-tests)
  - [Target Audiences](#target-audiences)
- [Pagination](#pagination)
- [Error Handling](#error-handling)
- [Rate Limiting](#rate-limiting)

## Overview

The Marketing Company Backend provides a comprehensive GraphQL API for managing CRM operations and marketing campaigns. The API is designed with a schema-first approach, providing type safety and excellent developer experience.

### Key Features

- **Type-Safe API**: Strong typing with GraphQL schema
- **Single Endpoint**: All operations through `/graphql`
- **Flexible Queries**: Request exactly the data you need
- **Real-time Updates**: WebSocket subscriptions support
- **Interactive Explorer**: Built-in GraphiQL IDE

## GraphQL Endpoints

| Endpoint         | Purpose                   | Method    |
| ---------------- | ------------------------- | --------- |
| `/graphql`       | Main GraphQL API endpoint | POST      |
| `/graphiql`      | Interactive GraphQL IDE   | GET       |
| `/subscriptions` | WebSocket subscriptions   | WebSocket |

### Base URL

```
Development: http://localhost:8080
Production: https://your-domain.com
```

## Authentication

### JWT Token Authentication

Most operations require authentication. Include the JWT token in the request header:

```http
POST /graphql
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json
```

### Token Lifecycle

1. **Sign Up / Login**: Receive `accessToken` and `refreshToken`
2. **Use Access Token**: Include in Authorization header for requests
3. **Token Expires**: Access token expires after 1 hour
4. **Refresh Token**: Use refresh token to get new access token
5. **Logout**: Invalidate refresh token

## Custom Scalars

The API uses several custom scalar types for better type safety:

```graphql
# Date and time in ISO-8601 format: "2026-01-15T10:30:00Z"
scalar DateTime

# Date only in ISO-8601 format: "2026-01-15"
scalar Date

# High-precision decimal for monetary values: "1234.56"
scalar BigDecimal

# Arbitrary JSON object
scalar JSON
```

## Common Types

### Pagination

```graphql
type PageResponse {
  items: [T]
  totalElements: Int!
  totalPages: Int!
  pageNumber: Int!
  pageSize: Int!
  isFirst: Boolean!
  isLast: Boolean!
  hasNext: Boolean!
  hasPrevious: Boolean!
}
```

### Common Enums

```graphql
enum PersonGender {
  MALE
  FEMALE
  OTHER
  UNKNOWN
}

enum SortDirection {
  ASC
  DESC
}
```

## Authentication API

### Sign Up

Register a new user account.

```graphql
mutation SignUp {
  signUp(
    input: {
      email: "john.doe@example.com"
      password: "SecurePassword123!"
      firstName: "John"
      lastName: "Doe"
      gender: MALE
      dateOfBirth: "1990-05-15"
      phoneNumber: "+1234567890"
    }
  ) {
    accessToken
    refreshToken
    accessTokenExpiresAt
    refreshTokenExpiresAt
    user {
      id
      email
      firstName
      lastName
      status
    }
  }
}
```

**Response:**

```json
{
  "data": {
    "signUp": {
      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "accessTokenExpiresAt": "2026-01-15T11:30:00Z",
      "refreshTokenExpiresAt": "2026-02-14T10:30:00Z",
      "user": {
        "id": "123",
        "email": "john.doe@example.com",
        "firstName": "John",
        "lastName": "Doe",
        "status": "ACTIVE"
      }
    }
  }
}
```

### Login

Authenticate with email and password.

```graphql
mutation Login {
  login(
    input: { email: "john.doe@example.com", password: "SecurePassword123!" }
  ) {
    accessToken
    refreshToken
    user {
      id
      email
      firstName
      lastName
      roles
    }
  }
}
```

### Refresh Token

Get a new access token using a refresh token.

```graphql
mutation RefreshToken {
  refreshToken(
    input: { refreshToken: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." }
  ) {
    accessToken
    refreshToken
    accessTokenExpiresAt
  }
}
```

### Logout

Invalidate the refresh token.

```graphql
mutation Logout {
  logout(refreshToken: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
}
```

## User Management API

### Get Current User

```graphql
query Me {
  me {
    id
    email
    firstName
    lastName
    phoneNumber
    gender
    dateOfBirth
    status
    roles
    createdAt
    lastLoginAt
  }
}
```

### Update User Profile

```graphql
mutation UpdateProfile {
  updateUserProfile(
    input: { firstName: "John", lastName: "Smith", phoneNumber: "+1987654321" }
  ) {
    id
    firstName
    lastName
    updatedAt
  }
}
```

### Change Password

```graphql
mutation ChangePassword {
  changePassword(
    input: {
      currentPassword: "OldPassword123!"
      newPassword: "NewSecurePassword456!"
    }
  )
}
```

## Customer Company API

### Create Customer Company

```graphql
mutation CreateCompany {
  createCustomerCompany(
    input: {
      companyName: "Acme Corporation"
      legalName: "Acme Corp. LLC"
      taxId: "12-3456789"
      website: "https://acme.com"
      industryCode: "TECH"
      industryName: "Technology"
      sector: "Software"
      companySize: LARGE
      employeeCount: 500
      status: LEAD
      contacts: [
        {
          firstName: "Jane"
          lastName: "Smith"
          email: "jane.smith@acme.com"
          phone: "+1234567890"
          position: "CEO"
          isPrimaryContact: true
          isDecisionMaker: true
        }
      ]
      addresses: [
        {
          addressType: HEADQUARTERS
          street: "123 Main St"
          city: "San Francisco"
          state: "CA"
          postalCode: "94105"
          country: "USA"
        }
      ]
    }
  ) {
    id
    companyName
    status
    contacts {
      id
      firstName
      lastName
      email
    }
    createdAt
  }
}
```

### Query Companies

```graphql
query Companies {
  customerCompanies(
    filters: {
      status: [ACTIVE, LEAD]
      companySize: [LARGE, ENTERPRISE]
      industryCode: "TECH"
    }
    sort: { field: "companyName", direction: ASC }
    page: 0
    size: 20
  ) {
    content {
      id
      companyName
      status
      companySize
      industry {
        code
        name
        sector
      }
      primaryContact {
        firstName
        lastName
        email
      }
      opportunitiesCount
      totalRevenue
    }
    totalElements
    pageNumber
    totalPages
  }
}
```

### Get Company Details

```graphql
query CompanyDetails {
  customerCompany(id: "123") {
    id
    companyName
    legalName
    taxId
    website
    foundingYear
    status
    companySize
    employeeCount

    industry {
      code
      name
      sector
    }

    revenue {
      amount
      currency
      range
    }

    contacts {
      id
      firstName
      lastName
      email
      phone
      position
      department
      isPrimaryContact
      isDecisionMaker
    }

    addresses {
      id
      addressType
      street
      city
      state
      postalCode
      country
      isPrimary
    }

    contract {
      contractId
      startDate
      endDate
      monthlyFee
      isActive
      isExpiringSoon
    }

    # Related data
    opportunities {
      id
      title
      status
      estimatedValue
    }

    deals {
      id
      status
      finalAmount
    }

    createdAt
    updatedAt
  }
}
```

### Update Company

```graphql
mutation UpdateCompany {
  updateCustomerCompany(
    id: "123"
    input: {
      status: ACTIVE
      companySize: ENTERPRISE
      employeeCount: 750
      website: "https://newacme.com"
    }
  ) {
    id
    status
    updatedAt
  }
}
```

## CRM API

### Opportunities

#### Create Opportunity

```graphql
mutation CreateOpportunity {
  createOpportunity(
    input: {
      customerCompanyId: "123"
      title: "Q1 Digital Marketing Campaign"
      description: "Comprehensive digital marketing solution"
      estimatedValue: 50000.00
      currency: "USD"
      expectedCloseDate: "2026-03-31"
      probability: 75
      servicePackageIds: ["1", "2", "3"]
      assignedToUserId: "456"
    }
  ) {
    id
    title
    status
    estimatedValue
    expectedCloseDate
    createdAt
  }
}
```

#### Query Opportunities

```graphql
query Opportunities {
  opportunities(
    filters: {
      status: [QUALIFICATION, PROPOSAL, NEGOTIATION]
      minValue: 10000
      maxValue: 100000
      fromDate: "2026-01-01"
      toDate: "2026-12-31"
    }
    sort: { field: "expectedCloseDate", direction: ASC }
    page: 0
    size: 20
  ) {
    content {
      id
      title
      status
      estimatedValue
      probability
      expectedCloseDate

      customerCompany {
        id
        companyName
      }

      assignedTo {
        id
        firstName
        lastName
      }

      servicePackages {
        id
        name
        price
      }

      createdAt
    }
    totalElements
  }
}
```

#### Update Opportunity Status

```graphql
mutation UpdateOpportunityStatus {
  updateOpportunityStatus(
    opportunityId: "123"
    status: NEGOTIATION
    notes: "Moved to negotiation after successful presentation"
  ) {
    id
    status
    updatedAt
  }
}
```

#### Convert to Deal

```graphql
mutation WinOpportunity {
  winOpportunity(opportunityId: "123", finalValue: 48000.00) {
    id
    status
    deal {
      id
      finalAmount
      status
    }
  }
}
```

### Quotes

#### Create Quote

```graphql
mutation CreateQuote {
  createQuote(
    input: {
      opportunityId: "123"
      customerCompanyId: "456"
      validUntil: "2026-02-28"
      notes: "Special discount applied"
      items: [
        {
          servicePackageId: "1"
          quantity: 1
          unitPrice: 15000.00
          discount: 10.0
          description: "Premium Marketing Package"
        }
        {
          servicePackageId: "2"
          quantity: 3
          unitPrice: 5000.00
          discount: 15.0
          description: "Social Media Management (3 months)"
        }
      ]
    }
  ) {
    id
    quoteNumber
    status
    subtotal
    totalDiscount
    taxAmount
    totalAmount
    items {
      id
      description
      quantity
      unitPrice
      discount
      totalPrice
    }
    validUntil
    createdAt
  }
}
```

#### Get Quote

```graphql
query Quote {
  quote(id: "123") {
    id
    quoteNumber
    status
    validUntil
    isExpired

    opportunity {
      id
      title
    }

    customerCompany {
      id
      companyName
    }

    items {
      id
      servicePackage {
        name
      }
      description
      quantity
      unitPrice
      discount
      totalPrice
    }

    subtotal
    totalDiscount
    taxAmount
    totalAmount

    notes
    createdAt
    updatedAt
  }
}
```

#### Send Quote

```graphql
mutation SendQuote {
  sendQuote(
    quoteId: "123"
    recipientEmail: "client@company.com"
    message: "Please review the attached quote"
  ) {
    id
    status
    sentAt
  }
}
```

#### Accept Quote

```graphql
mutation AcceptQuote {
  acceptQuote(quoteId: "123", acceptedBy: "Jane Smith") {
    id
    status
    acceptedAt
  }
}
```

### Deals

#### Create Deal from Opportunity

```graphql
mutation CreateDeal {
  createDeal(
    input: {
      opportunityId: "123"
      servicePackageIds: ["1", "2", "3"]
      startDate: "2026-02-01"
    }
  ) {
    id
    status
    finalAmount
    startDate
    customerCompany {
      companyName
    }
    opportunity {
      title
    }
  }
}
```

#### Sign Deal

```graphql
mutation SignDeal {
  signDeal(
    input: {
      dealId: "123"
      finalAmount: 48000.00
      startDate: "2026-02-01"
      endDate: "2027-01-31"
      deliverables: "Monthly reports, campaign management, analytics"
      terms: "12-month contract, monthly billing"
    }
  ) {
    id
    status
    finalAmount
    startDate
    endDate
  }
}
```

#### Query Deals

```graphql
query Deals {
  deals(
    filters: {
      status: [ACTIVE, SIGNED]
      fromDate: "2026-01-01"
      minAmount: 10000
    }
    sort: { field: "startDate", direction: DESC }
    page: 0
    size: 20
  ) {
    content {
      id
      status
      finalAmount
      startDate
      endDate

      customerCompany {
        id
        companyName
      }

      campaignManager {
        id
        firstName
        lastName
      }

      servicePackages {
        id
        name
      }

      createdAt
    }
    totalElements
  }
}
```

### Tasks

#### Create Task

```graphql
mutation CreateTask {
  createTask(
    input: {
      title: "Follow up with client"
      description: "Discuss campaign performance"
      taskType: FOLLOW_UP
      priority: HIGH
      dueDate: "2026-01-20"
      assignedToUserId: "456"
      relatedToType: OPPORTUNITY
      relatedToId: "789"
    }
  ) {
    id
    title
    status
    priority
    dueDate
    assignedTo {
      firstName
      lastName
    }
  }
}
```

#### Query Tasks

```graphql
query MyTasks {
  tasks(
    filters: {
      status: [PENDING, IN_PROGRESS]
      priority: [HIGH, URGENT]
      assignedToUserId: "456"
    }
    sort: { field: "dueDate", direction: ASC }
    page: 0
    size: 20
  ) {
    content {
      id
      title
      description
      taskType
      status
      priority
      dueDate
      isOverdue
      completedAt
      assignedTo {
        firstName
        lastName
      }
    }
    totalElements
  }
}
```

#### Complete Task

```graphql
mutation CompleteTask {
  completeTask(
    taskId: "123"
    completionNotes: "Successfully contacted client and scheduled meeting"
  ) {
    id
    status
    completedAt
  }
}
```

### Interactions

#### Record Interaction

```graphql
mutation RecordInteraction {
  createInteraction(
    input: {
      customerCompanyId: "123"
      interactionType: MEETING
      channel: IN_PERSON
      date: "2026-01-15T14:00:00Z"
      duration: 60
      subject: "Q1 Campaign Planning"
      notes: "Discussed campaign objectives and budget"
      attendees: ["Jane Smith", "John Doe"]
      nextSteps: "Prepare detailed proposal"
      sentiment: POSITIVE
    }
  ) {
    id
    interactionType
    date
    subject
    sentiment
  }
}
```

### Service Packages

#### Query Service Packages

```graphql
query ServicePackages {
  servicePackages(filters: { category: MARKETING, isActive: true }) {
    id
    name
    description
    category
    price
    currency
    billingCycle
    features
    isActive
    isPopular
  }
}
```

## Marketing API

### Campaigns

#### Create Campaign

```graphql
mutation CreateCampaign {
  createCampaign(
    input: {
      name: "Summer Sale 2026"
      description: "Promotional campaign for summer products"
      campaignType: CONVERSION
      status: PLANNED
      totalBudget: 50000.00
      startDate: "2026-06-01"
      endDate: "2026-08-31"
      primaryGoal: "Increase sales by 30%"
      targetMetrics: {
        targetImpressions: 1000000
        targetClicks: 50000
        targetConversions: 5000
        targetROI: 300.0
      }
      channelIds: ["1", "2", "3"]
      targetAudienceDemographics: {
        ageRange: "25-45"
        gender: ["FEMALE", "MALE"]
        interests: ["Fashion", "Lifestyle"]
      }
    }
  ) {
    id
    name
    status
    campaignType
    budget {
      totalBudget
      spentAmount
      remainingBudget
    }
  }
}
```

#### Query Campaigns

```graphql
query Campaigns {
  campaigns(
    filters: {
      status: [ACTIVE, PLANNED]
      campaignType: CONVERSION
      fromDate: "2026-01-01"
      toDate: "2026-12-31"
    }
    sort: { field: "startDate", direction: DESC }
    page: 0
    size: 20
  ) {
    content {
      id
      name
      description
      campaignType
      status

      period {
        startDate
        endDate
        duration
        isActive
        daysRemaining
      }

      budget {
        totalBudget
        spentAmount
        remainingBudget
        utilizationPercentage
        isNearlyExhausted
      }

      metrics {
        totalImpressions
        totalClicks
        conversions
        ctr
        conversionRate
        averageCPC
        totalCost
        revenue
        roi
        roiStatus
      }

      channels {
        id
        name
        channelType
      }

      createdAt
    }
    totalElements
  }
}
```

#### Get Campaign Details

```graphql
query CampaignDetails {
  campaign(id: "123") {
    id
    name
    description
    campaignType
    status

    period {
      startDate
      endDate
      duration
      isActive
      daysRemaining
    }

    budget {
      totalBudget
      spentAmount
      remainingBudget
      utilizationPercentage
      budgetAllocations
    }

    targetAudience {
      demographics
      locations
      interests
      behavioralCriteria
    }

    metrics {
      totalImpressions
      totalClicks
      conversions
      ctr
      conversionRate
      averageCPC
      totalCost
      revenue
      roi
      roiStatus
    }

    activities {
      id
      name
      activityType
      status
      scheduledDate
    }

    channels {
      id
      name
      channelType
    }

    abTests {
      id
      name
      status
    }

    createdAt
    updatedAt
  }
}
```

#### Update Campaign Status

```graphql
mutation UpdateCampaignStatus {
  updateCampaignStatus(campaignId: "123", status: ACTIVE) {
    id
    status
    updatedAt
  }
}
```

#### Pause Campaign

```graphql
mutation PauseCampaign {
  pauseCampaign(campaignId: "123", reason: "Budget review needed") {
    id
    status
  }
}
```

### Channels

#### Query Marketing Channels

```graphql
query MarketingChannels {
  marketingChannels(filters: { isActive: true }) {
    id
    name
    channelType
    description
    defaultCostPerClick
    defaultCostPerImpression
    isActive

    # Campaign performance
    totalCampaigns
    activeCampaigns
    totalSpent
    totalImpressions
    totalClicks
    averageROI
  }
}
```

### Activities

#### Create Campaign Activity

```graphql
mutation CreateActivity {
  createCampaignActivity(
    input: {
      campaignId: "123"
      name: "Email Blast - Summer Sale"
      activityType: EMAIL
      description: "Promotional email to 50,000 subscribers"
      channelId: "5"
      scheduledDate: "2026-06-15T10:00:00Z"
      budget: 2000.00
      targetAudience: {
        segments: ["High-value customers", "Email subscribers"]
        expectedReach: 50000
      }
    }
  ) {
    id
    name
    activityType
    status
    scheduledDate
  }
}
```

### Metrics

#### Record Campaign Metrics

```graphql
mutation RecordMetrics {
  recordCampaignMetrics(
    input: {
      campaignId: "123"
      date: "2026-06-15"
      impressions: 150000
      clicks: 7500
      conversions: 750
      cost: 3500.00
      revenue: 15000.00
      channelId: "1"
    }
  ) {
    id
    date
    impressions
    clicks
    conversions
    ctr
    conversionRate
    cpc
    roi
  }
}
```

#### Query Campaign Performance

```graphql
query CampaignPerformance {
  campaignMetrics(
    campaignId: "123"
    fromDate: "2026-06-01"
    toDate: "2026-06-30"
    groupBy: DAY
  ) {
    date
    impressions
    clicks
    conversions
    ctr
    conversionRate
    cost
    revenue
    roi

    # Breakdown by channel
    byChannel {
      channelId
      channelName
      impressions
      clicks
      cost
      roi
    }
  }
}
```

### Attribution

#### Track Attribution

```graphql
mutation TrackAttribution {
  trackCampaignAttribution(
    input: {
      campaignId: "123"
      customerId: "456"
      touchpointType: CLICK
      channelId: "1"
      timestamp: "2026-06-15T14:30:00Z"
      conversionValue: 299.99
      attributionModel: LAST_CLICK
    }
  ) {
    id
    touchpointType
    attributedRevenue
  }
}
```

### Assets

#### Upload Marketing Asset

```graphql
mutation UploadAsset {
  uploadMarketingAsset(
    input: {
      campaignId: "123"
      name: "Summer Sale Banner"
      assetType: IMAGE
      fileUrl: "https://cdn.example.com/banner.jpg"
      fileSize: 245678
      format: "JPG"
      dimensions: "1200x628"
      tags: ["banner", "summer", "sale"]
    }
  ) {
    id
    name
    assetType
    fileUrl
    createdAt
  }
}
```

### A/B Tests

#### Create A/B Test

```graphql
mutation CreateABTest {
  createAbTest(
    input: {
      campaignId: "123"
      name: "Email Subject Line Test"
      description: "Testing two subject line variations"
      startDate: "2026-06-01"
      endDate: "2026-06-15"
      variants: [
        {
          name: "Variant A"
          description: "Subject: Save 30% This Summer!"
          trafficAllocation: 50.0
        }
        {
          name: "Variant B"
          description: "Subject: Exclusive Summer Deals Inside"
          trafficAllocation: 50.0
        }
      ]
      successMetric: CONVERSION_RATE
      minimumSampleSize: 1000
    }
  ) {
    id
    name
    status
    variants {
      id
      name
      trafficAllocation
    }
  }
}
```

#### Get A/B Test Results

```graphql
query ABTestResults {
  abTest(id: "123") {
    id
    name
    status
    startDate
    endDate

    variants {
      id
      name
      impressions
      clicks
      conversions
      ctr
      conversionRate

      # Statistical significance
      isWinner
      confidenceLevel
    }

    # Test results
    hasStatisticalSignificance
    winningVariant {
      id
      name
    }

    recommendations
  }
}
```

## Pagination

All list queries support pagination with consistent parameters:

```graphql
query PaginatedQuery {
  items(
    page: 0 # Page number (0-indexed)
    size: 20 # Items per page
  ) {
    content # Array of items
    totalElements # Total number of items
    totalPages # Total number of pages
    pageNumber # Current page number
    pageSize # Items per page
    isFirst # Is first page?
    isLast # Is last page?
    hasNext # Has next page?
    hasPrevious # Has previous page?
  }
}
```

## Error Handling

### Error Response Format

```json
{
  "errors": [
    {
      "message": "Customer company not found",
      "extensions": {
        "code": "NOT_FOUND",
        "classification": "DataFetchingException",
        "timestamp": "2026-01-15T10:30:00Z"
      },
      "path": ["customerCompany"],
      "locations": [{ "line": 2, "column": 3 }]
    }
  ],
  "data": null
}
```

### Common Error Codes

| Code                      | Description                 |
| ------------------------- | --------------------------- |
| `UNAUTHORIZED`            | Authentication required     |
| `FORBIDDEN`               | Insufficient permissions    |
| `NOT_FOUND`               | Resource not found          |
| `VALIDATION_ERROR`        | Input validation failed     |
| `BUSINESS_RULE_VIOLATION` | Business rule not satisfied |
| `INTERNAL_ERROR`          | Internal server error       |

### Validation Errors

```json
{
  "errors": [
    {
      "message": "Validation failed",
      "extensions": {
        "code": "VALIDATION_ERROR",
        "validationErrors": [
          {
            "field": "email",
            "message": "Invalid email format"
          },
          {
            "field": "password",
            "message": "Password must be at least 8 characters"
          }
        ]
      }
    }
  ]
}
```

## Rate Limiting

The API implements rate limiting to prevent abuse:

- **Authenticated users**: 1000 requests per hour
- **Unauthenticated requests**: 100 requests per hour

Rate limit headers:

```http
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 995
X-RateLimit-Reset: 1642251600
```

When rate limit is exceeded:

```json
{
  "errors": [
    {
      "message": "Rate limit exceeded",
      "extensions": {
        "code": "RATE_LIMIT_EXCEEDED",
        "retryAfter": 3600
      }
    }
  ]
}
```

## Best Practices

### 1. Request Only Needed Fields

```graphql
# ❌ Bad: Requesting too much data
query {
  campaigns {
    id
    name
    # ... all fields
  }
}

# ✅ Good: Request only what you need
query {
  campaigns {
    id
    name
    status
  }
}
```

### 2. Use Aliases for Multiple Queries

```graphql
query {
  activeCampaigns: campaigns(filters: { status: ACTIVE }) {
    id
    name
  }

  plannedCampaigns: campaigns(filters: { status: PLANNED }) {
    id
    name
  }
}
```

### 3. Use Fragments for Reusability

```graphql
fragment CampaignSummary on Campaign {
  id
  name
  status
  budget {
    totalBudget
    spentAmount
  }
}

query {
  campaign(id: "123") {
    ...CampaignSummary
    metrics {
      roi
    }
  }
}
```

### 4. Handle Errors Gracefully

```javascript
const result = await client.query({ query: GET_CAMPAIGN });

if (result.errors) {
  // Handle GraphQL errors
  console.error("GraphQL Errors:", result.errors);
}

if (result.data) {
  // Use data
  console.log("Campaign:", result.data.campaign);
}
```

---

For more examples and interactive testing, visit the GraphiQL IDE at `/graphiql` when the server is running.
