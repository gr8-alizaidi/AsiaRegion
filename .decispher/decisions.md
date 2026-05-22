<!-- DECISION-393329BA -->
## Decision: Prohibition of MongoDB in the Tech Stack for Analytics Events

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "**/*",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "(?i)mongo(?:db)?",
          "patterns": []
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Considering MongoDB for analytics events due to perceived better write throughput for time-series data and high cardinality event logs.

**Decision:** MongoDB is strictly prohibited from being integrated into the current technology stack, including for analytics events.

**Rationale:** There is an active and non-negotiable architectural constraint against MongoDB in the stack due to the critical requirement for ACID compliance across all billing and user data. MongoDB does not satisfy this fundamental requirement.

**Alternatives Considered:**
- **MongoDB for analytics events**: It violates an active architectural constraint due to its lack of native ACID compliance, which is non-negotiable for billing and user data within our stack.

---

<!-- DECISION-D01A8DD8 -->
## Decision: Abandon RFC 7807 for error responses

**Status**: Active  
**Date**: 2026-04-22  
**Severity**: Critical

**Files**:
- `api/responses`
- `api/error-handling`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "api/responses/**/*",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "application/problem+json",
            "type",
            "title",
            "status",
            "detail",
            "instance"
          ]
        }
      ]
    },
    {
      "type": "file",
      "pattern": "api/error-handling/**/*",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "application/problem+json",
            "type",
            "title",
            "status",
            "detail",
            "instance"
          ]
        }
      ]
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** The current API error handling standard (RFC 7807) is considered outdated for the team's needs.

**Decision:** We have decided to officially discontinue the use of RFC 7807 (Problem Details for HTTP APIs) for all API error responses moving forward.

**Rationale:** The team determined that the RFC 7807 specification is outdated and no longer aligns with the current requirements and standards of the API architecture.

---

<!-- DECISION-670907D2 -->
## Decision: Defer Microservices Adoption, Maintain Monorepo Architecture

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "**/*",
      "content_rules": [
        {
          "mode": "regex",
          "start": 1,
          "pattern": "(?i)grpc|microservice|service-mesh|distributed-tracing"
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** The team considered adopting a microservices architecture for the recorder and analyzer components but faced challenges.

**Decision:** We will integrate decision-guardian into our PR pipeline to enforce and track architectural decisions.

**Rationale:** Automating the verification of architectural decisions during the review process helps maintain consistency and ensures that developers adhere to established guidelines.

**Alternatives Considered:**
- **Adopt a microservices architecture by splitting recorder and analyzer into separate gRPC services.**: The previous attempt in Phase 1 led to brutal deployment complexity for a 3-person team, consuming 40% of their time debugging inter-service authentication and network failures.

---

<!-- DECISION-B6869B8C -->
## Decision: Define LLM Model Combinations for Saver, Balanced, Pro, and Super Effort Modes

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Files**:
- `**/*`

### Context

**Problem:** We need to lock down exactly which model combination maps to which effort mode.

**Decision:** The specific LLM model combinations for the multi-provider effort modes were finalized: Saver mode uses `gemini-flash` for detection, extraction, and format. Balanced mode uses `gemini-flash` for detection, `claude-haiku` for extraction, and `gpt-4o-mini` for format. Pro mode uses `gemini-flash` for detection, `claude-sonnet` for extraction, and `gpt-4o-mini` for format. Super mode uses `gemini-flash` for detection, `claude-opus` for extraction, and `claude-sonnet` for format.

**Rationale:** The chosen LLM model combinations for each effort mode (Saver, Balanced, Pro, Super) were selected to provide different performance and cost profiles, aligning with the multi-provider strategy. Cost analysis confirmed that the proposed combinations, ranging from ~$0.08/1M tokens for Saver to ~$4.50/1M tokens for Super, ensure fine margins at current credit pricing.

---

<!-- DECISION-AC87F0E5 -->
## Decision: Define Model Fallback Ordering Strategy for API Rate Limits

**Status**: Active  
**Date**: 2026-04-22  
**Severity**: Critical

**Files**:
- `src/llm/client_factory.py`
- `src/llm/fallback_logic.py`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "src/llm/{client_factory,fallback_logic}.py",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "(?i)fallback"
        }
      ],
      "content_match_mode": "all"
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Handling 429 rate limit errors from LLM providers during extraction and detection tasks.

**Decision:** Establish explicit provider fallback orderings: For extraction, use Anthropic → DeepSeek → OpenAI. For detection, use Google → OpenAI → DeepSeek.

**Rationale:** To maintain system reliability and avoid task failure when individual LLM providers hit rate limits, a hierarchical fallback mechanism ensures work is diverted to alternative models before resorting to the Dead Letter Queue (DLQ) after retries.

---

<!-- DECISION-02E6ACFF -->
## Decision: Enforce 5-minute token expiry for authentication service

**Status**: Active  
**Date**: 2026-05-08  
**Severity**: Critical

**Files**:
- `services/auth-service`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "services/auth-service/**/*",
      "content_rules": [
        {
          "mode": "regex",
          "start": 1,
          "pattern": "(expiresIn|expiry|ttl|lifetime).*?([6-9]\\d{2,}|[1-9]\\d{3,}|[0-9]{4,})"
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Determine the optimal token expiration duration for the authentication service to balance security and usability.

**Decision:** Implement a strict 5-minute token expiry window for the authentication service.

**Rationale:** This decision is driven by compliance requirements mandating rapid session invalidation and the need to mitigate the risk of replay attacks associated with longer-lived tokens.

---

<!-- DECISION-9F213A97 -->
## Decision: Expansion of Azure Functions to full backend services

**Status**: Active  
**Date**: 2026-05-10  
**Severity**: Critical

**Files**:
- `infrastructure/payment-service`
- `backend/payments`
- `infrastructure/azure-functions`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "infrastructure/payment-service/**/*",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "aws_lambda",
            "aws-lambda",
            "lambda_function"
          ]
        }
      ]
    },
    {
      "type": "file",
      "pattern": "backend/payments/**/*",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "aws_lambda",
            "aws-lambda",
            "lambda_function"
          ]
        }
      ]
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** Selecting the cloud hosting provider for the payment backend service.

**Decision:** Transition the usage of Azure Functions from a specialized payment-only utility to a full-scale backend service platform.

**Rationale:** The team needs to scale the backend architecture to support broader business logic beyond payment processing, and utilizing the existing Azure Functions infrastructure is the most efficient path for unified deployment.

**Alternatives Considered:**
- **AWS Lambda**: The team prefers Azure for the payment backend service infrastructure.

---

<!-- DECISION-6ACD0667 -->
## Decision: Implement Multi-Provider LLM Abstraction for Pipeline Steps with Per-Company Overrides

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "exclude": [
        "**/*test*",
        "**/*doc*"
      ],
      "pattern": "**/*.(ts|js|py|go|java|cs|yml|yaml|env|ini|properties|json)",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "(new|import|from)[^;\\n]*?(Anthropic|OpenAI|GoogleCloud|AzureOpenAI|Claude|Gemini|GPT-4o)",
          "patterns": []
        },
        {
          "mode": "regex",
          "start": 0,
          "pattern": "(LLM_PROVIDER|LLM_MODEL)[^=\\n]*?=(?!.*(config|abstraction|env))[^\\n]*(Claude-Sonnet|Gemini-Flash|GPT-4o-mini)",
          "patterns": []
        }
      ],
      "content_match_mode": "any"
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** The current LLM provider strategy is unmaintainable, using different providers (Gemini-Flash, Claude-Sonnet, GPT-4o-mini) for different pipeline steps, leading to high costs (Claude-Sonnet is 60% of the bill) and inconsistent availability (Sonnet outages).

**Decision:** We will implement a multi-provider abstraction where each pipeline step (detection, extraction, enrichment, formatting) has its own LLM provider configuration via environment variables. At request time, an 'effort mode' can override the provider selection on a per-company basis.

**Rationale:** This approach allows companies with high context volume (Tier 3+) to pay extra for Claude-Sonnet's accuracy where needed, while companies with tighter budgets can use more cost-effective options like Gemini-Flash for all steps. It also decouples our infrastructure from individual LLM vendor stability and enables independent contract negotiations with different providers (Anthropic, OpenAI, Google).

**Alternatives Considered:**
- **Continue with current fragmented multi-provider setup (Gemini-Flash for detection, Claude-Sonnet for extraction, GPT-4o-mini for formatting).**: This approach is unmaintainable, costly (Claude-Sonnet accounts for 60% of the LLM bill), and suffers from inconsistent provider availability issues.
- **Consolidate to a single LLM provider for all pipeline steps.**: This would limit flexibility, potentially sacrificing accuracy for high-tier companies or forcing budget-conscious companies to pay for more expensive models than necessary. It would also lead to vendor lock-in and a single point of failure for LLM stability.

---

<!-- DECISION-2E467570 -->
## Decision: Limit premium user request rate to 200 req/min

**Status**: Active  
**Date**: 2026-05-21  
**Severity**: Critical

**Files**:
- `infrastructure/rate-limits.config`
- `services/api-gateway/configs/premium-tier.json`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "services/api-gateway/configs/premium-tier.json",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "\"premium_rate_limit\":\\s*(?!200)\\d+"
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Infrastructure database saturation occurs at 35 concurrent premium users when the request rate is set to 500 req/min, posing a risk of production instability.

**Decision:** Revert the premium user request rate limit from 500 req/min to 200 req/min until infrastructure improvements, such as scaling the connection pool or adding read replicas, are implemented.

**Rationale:** Load testing confirmed that 500 req/min is unsustainable under concurrent user load with the current infrastructure. Lowering the limit to 200 req/min ensures stability and avoids potential production incidents.

**Alternatives Considered:**
- **Maintain 500 req/min limit**: Results in database saturation and poses a high risk of production system failure.
- **Scale connection pool and add read replicas immediately**: Requires infrastructure development effort before the higher limit can be safely supported.

---

<!-- DECISION-3B06241D -->
## Decision: LLM Provider Strategy by Pipeline Step and Effort Mode

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Files**:
- `packages/analyzer/`

### Context

**Decision:** Each LLM pipeline step (detection, extraction, formatting) has its own provider configuration managed via environment variables. An 'effort mode' concept allows overriding these configurations per company at request time, defining specific LLM models for different quality/cost tiers: Saver uses gemini-flash, Balanced mixes gemini-flash, claude-haiku, and gpt-4o-mini, Pro uses claude-sonnet for extraction, and Super uses claude-opus.

**Rationale:** The strategy is designed to provide flexibility and optimization across different pipeline steps and 'effort modes'. By configuring providers per step and allowing overrides based on company effort modes, the system can balance cost, performance, and model quality according to specific requirements, from 'Saver' (likely cost-optimized) to 'Super' (likely highest quality/cost). The multi-provider abstraction facilitates this dynamic selection.

---

<!-- DECISION-08702BE0 -->
## Decision: Mandate use of GuardedProviderRegistry for all LLM service calls

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Critical

**Files**:
- `src/llm/providers/registry.ts`
- `src/llm/providers/guarded_registry.ts`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "src/llm/**/*.ts",
      "content_rules": [
        {
          "mode": "regex",
          "pattern": "(OpenAI|Anthropic|GoogleGenAI|LangChain)\\.",
          "patterns": [
            "new OpenAI(",
            "new Anthropic(",
            "createClient("
          ]
        }
      ],
      "content_match_mode": "any"
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** Direct SDK calls to LLM providers bypass centralized cost recording and kill switch mechanisms.

**Decision:** All LLM calls must be made through the GuardedProviderRegistry using either wrapTextProvider or wrapEmbeddingProvider instead of direct SDK calls.

**Rationale:** Using the registry ensures that all calls are tracked for cost accounting and can be dynamically controlled via system-wide kill switches.

---

<!-- DECISION-E9097831 -->
## Decision: Migrate email service to Zoho and update SMTP infrastructure

**Status**: Active  
**Date**: 2026-04-28  
**Severity**: Critical

**Files**:
- `infrastructure/mail`
- `services/smtp`
- `config/email_routing`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "{infrastructure/mail/**,services/smtp/**,config/email_routing/**}",
      "content_rules": [
        {
          "mode": "regex",
          "pattern": "(legacy_smtp|smtp_old|old_mail_server)",
          "patterns": [
            "legacy_smtp",
            "smtp_old"
          ]
        }
      ]
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** Need to replace the existing webmaster mailing service and transition away from the current SMTP server.

**Decision:** Migrate all email services to Zoho and update the SMTP server infrastructure, including the implementation of new routing rules to block any traffic to the legacy SMTP server.

**Rationale:** The team decided to move to Zoho to consolidate mailing services and address the limitations or overhead associated with the existing legacy SMTP infrastructure.

---

<!-- DECISION-BD1748C1 -->
## Decision: Migrate from Shipsy to in-house mapping event system

**Status**: Active  
**Date**: 2026-04-26  
**Severity**: Critical

**Files**:
- `services/shipping-integration`
- `infrastructure/event-bus`

### Context

**Problem:** Dependency on third-party provider Shipsy is causing scalability issues.

**Decision:** Switch from the third-party Shipsy provider to an in-house developed mapping event system.

**Rationale:** The team identified that the Shipsy service was negatively impacting the platform's scalability, and moving to an internal solution reduces external dependencies.

**Alternatives Considered:**
- **Continue using Shipsy**: It acts as a bottleneck for system scalability.

---

<!-- DECISION-E8A1DEC8 -->
## Decision: Migrate from Stream Chat provider to in-house chat implementation

**Status**: Active  
**Date**: 2026-05-10  
**Severity**: Critical

**Files**:
- `src/chat/provider`
- `src/services/chat`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "src/chat/provider/**/*",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "StreamChat",
            "stream-chat"
          ]
        }
      ]
    },
    {
      "type": "file",
      "pattern": "src/services/chat/**/*",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "StreamChat",
            "stream-chat"
          ]
        }
      ]
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** The current reliance on a third-party Stream Chat provider prevents the team from offering end-to-end control and integration for users.

**Decision:** Remove the Stream Chat provider dependency and develop an in-house chat solution to provide end-to-end functionality.

**Rationale:** Building the chat infrastructure internally enables full control over the user experience and provides the end-to-end capabilities required, which was not feasible with the third-party provider.

**Alternatives Considered:**
- **Continue using Stream Chat provider**: It fails to provide the required end-to-end control and integration capabilities for the user experience.

---

<!-- DECISION-ADD28E0B -->
## Decision: Migrate infrastructure orchestration from AWS ECS to AWS EKS

**Status**: Active  
**Date**: 2026-04-22  
**Severity**: Critical

**Files**:
- `infrastructure/terraform`
- `infrastructure/k8s`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "infrastructure/terraform/**/*",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "aws_ecs_cluster",
            "aws_ecs_service",
            "aws_ecs_task_definition"
          ]
        }
      ],
      "content_match_mode": "any"
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** AWS ECS lacks built-in support for multi-region failover without complex custom routing and requires additional tooling for advanced scaling capabilities.

**Decision:** The team will migrate from AWS ECS to AWS EKS for container orchestration.

**Rationale:** EKS provides superior orchestration flexibility, including native Horizontal Pod Autoscaler and improved multi-AZ/multi-region failover capabilities, which are necessary for the current scale, outweighing the operational overhead of Kubernetes.

**Alternatives Considered:**
- **AWS ECS**: Lacks sufficient multi-region failover support and requires custom routing implementations.
- **Railway**: Retained only as a temporary fallback, deemed insufficient for long-term production orchestration.

---

<!-- DECISION-B5692CE3 -->
## Decision: Migrate SNMP development from 3GPP to ITSI RFC

**Status**: Active  
**Date**: 2026-05-10  
**Severity**: Critical

**Files**:
- `src/ss7-stack/backend`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "src/ss7-stack/backend/**",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "3GPP"
          ]
        }
      ],
      "content_match_mode": "any"
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Selecting the protocol standard for building the SS7 stack backend codebase.

**Decision:** Adopt ITSI RFC for SNMP development instead of 3GPP.

**Rationale:** 3GPP is non-compliant with the current SOT policy, necessitating a shift to an alternative standard that meets internal compliance requirements.

**Alternatives Considered:**
- **3GPP**: Not compliant with SOT policy.

---

<!-- DECISION-71727A5F -->
## Decision: Plan to Migrate Application Infrastructure from Railway to AWS ECS

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "**/*",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "(?i)(migrate|aws|ecs|railway)"
        }
      ]
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** The current hosting platform, Railway, becomes cost-prohibitive at scale (exceeding $500/month) and lacks the VPC isolation capabilities required for enterprise customers.

**Decision:** The trigger metric for initiating the AWS migration has been adjusted from 20 paying customers to 30 paying customers. The Q3 2026 timeline for the migration still holds.

**Rationale:** This adjustment is due to Railway costs being more predictable than initially expected. Additionally, the VPC isolation requirement, which was a significant factor, only applies to enterprise customers, a segment we are targeting at a later stage.

---

<!-- DECISION-127DF168 -->
## Decision: Standardize API Error Responses to RFC 7807

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Files**:
- `auth endpoint`
- `cursorrules`
- `All API error handling implementations`

### Context

**Problem:** Inconsistent API error response formats across different endpoints, with some returning raw message strings.

**Decision:** All API errors must adhere to the RFC 7807 problem details format, including fields such as type, title, status, detail, and instance.

**Rationale:** To ensure consistency across all API endpoints and prevent the generation of non-compliant errors, especially from AI-assisted code.

---

<!-- DECISION-81145171 -->
## Decision: Standardize on PostgreSQL with pgvector for primary storage and vector search

**Status**: Active  
**Date**: 2026-04-22  
**Severity**: Critical

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "**/*",
      "content_rules": [
        {
          "mode": "regex",
          "start": 1,
          "pattern": "(?i)(mongodb|cockroachdb|elasticsearch|pinecone)",
          "patterns": [
            "mongodb",
            "cockroachdb",
            "elasticsearch",
            "pinecone"
          ]
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Selecting the primary datastore to handle both standard relational data and vector search requirements efficiently.

**Decision:** Use PostgreSQL with pgvector and HNSW indexes as the standard solution for primary datastore and vector search operations.

**Rationale:** PostgreSQL with pgvector provides the ability to manage both SQL-based relational data and vector search capabilities within a single system, simplifying the architecture compared to managing separate databases.

**Alternatives Considered:**
- **MongoDB**: The team preferred the relational capabilities of PostgreSQL and the unified support for vector search provided by pgvector.
- **CockroachDB**: The team decided that PostgreSQL with pgvector was sufficient and preferred over the complexity or features offered by CockroachDB.

---

<!-- DECISION-08631B5A -->
## Decision: Use long-running containers for billing service instead of serverless functions

**Status**: Active  
**Date**: 2026-04-22  
**Severity**: Critical

**Files**:
- `packages/api/src/routes/credits.ts`
- `packages/decision-store/src/repositories/credit-repository.ts`
- `packages/common/src/types/credits.ts`
- `services/billing`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "packages/{api/src/routes/credits.ts,decision-store/src/repositories/credit-repository.ts,common/src/types/credits.ts}",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "number|double"
        }
      ],
      "content_match_mode": "any"
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Uncertainty regarding ownership of the billing module and the requirements for implementing new effort modes.

**Decision:** The billing service uses long-running containers instead of serverless functions.

**Rationale:** Serverless functions introduced cold starts which resulted in unacceptable latency spikes during traffic peaks, negatively impacting the user experience for the billing service.

**Alternatives Considered:**
- **Serverless functions**: Caused unacceptable latency spikes due to cold starts during traffic peaks.

---

<!-- DECISION-3213C504 -->
## Decision: Use MongoDB Atlas for schemaless analytics webhook storage

**Status**: Active  
**Date**: 2026-04-19  
**Severity**: Critical

**Files**:
- `services/analytics-webhook-handler`
- `infrastructure/database-clusters`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "services/analytics-webhook-handler/**",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "INSERT INTO",
            "UPDATE ",
            "pg_query"
          ]
        }
      ]
    },
    {
      "type": "file",
      "pattern": "infrastructure/database-clusters/**",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "postgresql"
        }
      ]
    }
  ],
  "match_mode": "any"
}
```

### Context

**Decision:** Use MongoDB Atlas specifically for the analytics event ingestion pipeline, while keeping all other core application data in PostgreSQL.

**Rationale:** MongoDB Atlas provides the necessary horizontal sharding and schemaless structure to handle the required 50k write operations per second, whereas PostgreSQL performance degrades under this load.

**Alternatives Considered:**
- **PostgreSQL JSONB**: Proved too difficult and inefficient to index effectively for schemaless event data.

---

<!-- DECISION-465C1E1A -->
## Decision: Use MongoDB for Analytics Events Pipeline

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Files**:
- `packages/api/src/analytics/`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "exclude": [
        "packages/api/src/analytics/**/*.test.{ts,js,go,py}",
        "packages/api/src/analytics/migrations/**/*"
      ],
      "pattern": "packages/api/src/analytics/**/*.{ts,js,go,py}",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "pg",
            "postgres",
            "postgresql",
            "new Client(",
            "createPool(",
            "sequelize",
            "typeorm",
            "knex"
          ]
        }
      ],
      "content_match_mode": "any"
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** PostgreSQL's write throughput is insufficient for high-cardinality analytics event data, failing to meet new scale requirements.

**Decision:** We will use MongoDB for the analytics events pipeline, provisioning a MongoDB Atlas cluster to handle the data.

**Rationale:** MongoDB offers 10x the write throughput compared to PostgreSQL for high-cardinality event data, which is essential to meet the current scale requirements. The previous constraint was established before these new scale demands emerged.

**Alternatives Considered:**
- **PostgreSQL**: PostgreSQL's write throughput is 10x lower than MongoDB for high-cardinality event data, making it unsuitable for the new scale requirements of the analytics pipeline.

---

<!-- DECISION-260C4141 -->
## Decision: Use shared secret token authentication for reporting worker communication

**Status**: Active  
**Date**: 2026-05-08  
**Severity**: Critical

**Files**:
- `src/reporting-worker/api-client.ts`
- `src/api/auth/middleware.ts`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "src/reporting-worker/api-client.ts",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "https",
            "tls",
            "clientCert"
          ]
        }
      ]
    },
    {
      "type": "file",
      "pattern": "src/api/auth/middleware.ts",
      "content_rules": [
        {
          "mode": "regex",
          "start": 1,
          "pattern": "mtls|certificate"
        }
      ]
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** The reporting worker requires authentication to communicate with the main API, but mTLS setup is perceived as too slow or complex for this specific integration.

**Decision:** Bypass mTLS authentication for the new reporting worker and implement a hardcoded shared secret token in the HTTP header for inter-service authentication.

**Rationale:** The team chose a shared secret token approach to prioritize communication speed and reduce the implementation overhead compared to the mTLS setup.

**Alternatives Considered:**
- **mTLS**: The team felt it would be too slow and complex to implement for this specific worker.

---

<!-- DECISION-0948E434 -->
## Decision: Use Umbraco CMS for frontend component development

**Status**: Active  
**Date**: 2026-05-12  
**Severity**: Critical

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "**/*",
      "content_rules": [
        {
          "mode": "full_file"
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Decision:** The team will use the Umbraco CMS platform to build each component of the frontend website.

**Rationale:** Umbraco was selected as the designated CMS to standardize the development of frontend components.

---

<!-- DECISION-7B6D8F24 -->
## Decision: Abandoning EventStoreDB for monorepo event handling

**Status**: Active  
**Date**: 2026-04-19  
**Severity**: Warning

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "**/*",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "EventStoreDB",
            "EventStore",
            "event-sourcing"
          ]
        }
      ]
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** The team was experiencing excessive operational overhead and complexity managing EventStoreDB for event sourcing, which did not provide enough value regarding auditability at their current scale.

**Decision:** The team decided to discontinue the use of EventStoreDB and removed event sourcing as an architectural pattern following the migration back to a monorepo.

**Rationale:** The complexity of maintaining three separate runbooks for EventStoreDB operations outweighed the benefits of its auditability features for the current team size and system scale.

**Alternatives Considered:**
- **Retaining EventStoreDB for event sourcing**: The operational complexity was too high and not justified by the benefits gained.

---

<!-- DECISION-15E9A617 -->
## Decision: Adopt RFC7812 for theme data JSON validation

**Status**: Active  
**Date**: 2026-05-08  
**Severity**: Warning

**Files**:
- `src/sync/theme-validation.js`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "src/sync/theme-validation.js",
      "content_rules": [
        {
          "mode": "full_file"
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Decision:** Use RFC7812 as the specification for validating all JSON data synced by the server related to theme configurations.

**Rationale:** RFC7812 provides a standardized approach for schema validation, ensuring consistency and reliability across synced theme data.

---

<!-- DECISION-82CE6AD9 -->
## Decision: Automate stale decision archiving via ContextLifecycleWorker

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Warning

**Files**:
- `src/workers/ContextLifecycleWorker.ts`
- `src/services/ApprovalService.ts`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "src/workers/ContextLifecycleWorker.ts",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "archiveStalePendingReview",
            "STALE_ARCHIVE_ENABLED"
          ]
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Manual archiving of stale pending_review decisions is inefficient and requires an automated solution.

**Decision:** Implement a ContextLifecycleWorker job that triggers the existing archiveStalePendingReview function based on decisions not reviewed in over 90 days, controlled by the STALE_ARCHIVE_ENABLED environment variable.

**Rationale:** Automating the process reduces operational overhead, and using an environment flag allows for safe testing in staging before production rollout.

---

<!-- DECISION-B07D5F19 -->
## Decision: Bypass confidence threshold for explicit capture intents

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Warning

**Files**:
- `app/workers/process_comment_worker.rb`
- `app/services/pipeline_context.rb`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "app/{workers/process_comment_worker,services/pipeline_context}.rb",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "minConfidence.*?\\b(?!captureIntent\\s*==\\s*['\"]explicit['\"])\\w+"
        }
      ],
      "content_match_mode": "all"
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** LLM-extracted data via @decispher PR commands was getting rejected by the minConfidence threshold, despite user intent to save the data.

**Decision:** Implement a 'captureIntent' field in the pipeline context where 'explicit' intent (manual dashboard entry or @decispher command) bypasses the minConfidence threshold, while 'passive' intent remains subject to it.

**Rationale:** Explicit user commands indicate a deliberate intent to capture data, overriding the need for confidence filtering which is primarily intended for passive/automated collection.

---

<!-- DECISION-B4A09B64 -->
## Decision: Bypass minConfidence check for manual source type in analyzer

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Warning

**Files**:
- `src/analyzer/detection.ts`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "src/analyzer/detection.ts",
      "content_rules": [
        {
          "mode": "regex",
          "start": 1,
          "pattern": "if\\s*\\(\\s*sourceType\\s*===\\s*['\"]manual['\"]\\s*\\)\\s*\\{.*?return\\s*true"
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** The existing minConfidence thresholds were not appropriate for manual captures from the dashboard, as these are human-authored and do not require confidence filtering.

**Decision:** Manual captures with sourceType='manual' will bypass the minConfidence check entirely by adding an explicit guard in the detection step where shouldRun returns true by default for manual types.

**Rationale:** Manual content is explicitly created by the user, making confidence scoring irrelevant compared to automated conversational or code sources.

**Alternatives Considered:**
- **Add a unique threshold for manual sources**: Confidence is irrelevant for content intentionally created by a human.

---

<!-- DECISION-2D19C79E -->
## Decision: Cancellation of RFC 78 implementation

**Status**: Active  
**Date**: 2026-05-08  
**Severity**: Warning

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "**/*",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "RFC 78",
          "patterns": [
            "RFC 78"
          ]
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** The team decided to move away from the architectural proposal defined in RFC 78.

**Decision:** The team has officially cancelled the usage and implementation of RFC 78.

**Rationale:** The conversation indicates a strategic shift away from the previously proposed RFC 78, implying it is no longer aligned with current requirements or priorities.

---

<!-- DECISION-90C810F5 -->
## Decision: Configure LLM classification batch size and concurrency for Pro mode

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Warning

**Files**:
- `decision-fusion-queue`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "decision-fusion-queue",
      "content_rules": [
        {
          "mode": "regex",
          "start": 1,
          "pattern": "LLM_CLASSIFY_BATCH_SIZE_PRO\\s*=\\s*(?!10\\b)\\d+"
        },
        {
          "mode": "regex",
          "start": 1,
          "pattern": "LLM_CLASSIFY_CONCURRENCY\\s*=\\s*(?!5\\b)\\d+"
        }
      ]
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** Fusion classification in pro mode is experiencing high latency (8-12 seconds) due to sub-optimal default batching and concurrency settings.

**Decision:** Set LLM_CLASSIFY_BATCH_SIZE_PRO to 10 and maintain LLM_CLASSIFY_CONCURRENCY at 5 for the pro mode classification process.

**Rationale:** Increasing the batch size to 10 improves throughput to address latency issues, while keeping the batch size at 10 (instead of 15) prevents a loss in model precision for candidates later in the list. Concurrency is kept at 5 to balance processing load.

**Alternatives Considered:**
- **Batch size 15 and concurrency 3**: The larger batch size of 15 risked decreasing the model's precision on candidate evaluation.

---

<!-- DECISION-D17917AC -->
## Decision: Enforce RFC 7807 for Internal API Error Formats

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Warning

**Files**:
- `packages/api/src/routes/internal/`

### Context

**Problem:** Internal API routes are returning plain strings instead of adhering to the RFC 7807 error format, which breaks AI tools that parse our errors due to inconsistent formats.

**Decision:** All internal API routes must adhere to the RFC 7807 error format, consistent with public-facing API routes.

**Rationale:** Inconsistent error formats, specifically plain strings from internal routes, prevent AI tools from reliably parsing and analyzing errors, leading to broken analysis workflows.

---

<!-- DECISION-AC19B0F4 -->
## Decision: Establish 2000 req/min request limit for enterprise tier

**Status**: Active  
**Date**: 2026-05-21  
**Severity**: Warning

**Files**:
- `middleware/rate-limit-config.js`
- `rate-limiter/config.ts`
- `infrastructure/gateway/limits.yaml`

### Context

**Problem:** Current rate limits for premium users are insufficient for their usage needs.

**Decision:** Set a fixed default request limit of 2000 req/min for the enterprise tier, utilizing a separate infrastructure pool to prevent performance degradation for other customers.

**Rationale:** A dedicated pool prevents a single enterprise customer from starving the premium pool. A fixed limit was chosen over per-contract configuration to minimize operational overhead until the customer base grows to at least five enterprise clients.

**Alternatives Considered:**
- **Per-contract configurable limit**: The overhead is too high until the enterprise customer base reaches at least five clients.

---

<!-- DECISION-17F772B1 -->
## Decision: Establish authoritative RFC 7807 error format convention

**Status**: Active  
**Date**: 2026-04-22  
**Severity**: Warning

**Files**:
- `packages/api/src/plugins/error-handler.ts`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "packages/api/src/plugins/error-handler.ts",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "(?s)^(?!.*(type|title|status|detail|instance)).*$"
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Duplicate and conflicting conventions regarding RFC 7807 error format severity were documented in the Decispher system.

**Decision:** Adopt the HIGH severity specification as the authoritative version for the RFC 7807 error format, which includes fields: type, title, status, detail, and instance.

**Rationale:** The team identified that two existing conventions were redundant. Designating the HIGH severity entry as canonical while allowing the fusion engine to merge duplicate references ensures consistency across documentation and API implementations.

**Alternatives Considered:**
- **MEDIUM severity specification**: The HIGH severity version was explicitly selected as the authoritative and canonical standard.

---

<!-- DECISION-43A78D92 -->
## Decision: Establish Revenue squad ownership of billing and Stripe integration

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Warning

**Files**:
- `packages/api/src/billing/`
- `src/modules/billing/`
- `src/integrations/stripe/`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "packages/api/src/billing/**/*.ts",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "number\\s*[:=]\\s*double|double\\s+\\w+"
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Decision:** The Revenue squad now has exclusive ownership of the billing module and Stripe integration, requiring their explicit approval for all pull requests affecting these areas.

**Rationale:** Centralizing ownership ensures better control, security, and specialized maintenance for critical payment-related infrastructure.

---

<!-- DECISION-2B58E1D2 -->
## Decision: Establish stale archival notification process for decision units

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Warning

**Files**:
- `services/NotificationService.js`
- `jobs/ArchiveNotificationJob.js`
- `controllers/DecisionController.js`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "{services/NotificationService.js,jobs/ArchiveNotificationJob.js,controllers/DecisionController.js}",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": ".*(last_reviewed_at|view).*=.*(Date\\.now|new Date).*"
        }
      ],
      "content_match_mode": "all"
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Currently, decision owners are not notified before their stale context units are archived, and there is no mechanism to warn them of upcoming archival.

**Decision:** Implement a notification process where owners receive a Slack message 7 days before the archival cutoff. This involves adding a 'stale_warning' type to the NotificationService, querying for decisions with a last_reviewed_at timestamp between 83 and 84 days ago, and triggering a notification job. Viewing a decision on the dashboard does not reset the review timestamp; only an explicit approve, reject, or mark_active action will.

**Rationale:** Ensures stakeholders are informed before data archival without creating a loop where passive dashboard activity prevents legitimate cleanup of stale data.

**Alternatives Considered:**
- **Reset last_reviewed_at on dashboard view**: Viewing the dashboard does not constitute a formal review; doing so would prevent the archival of truly stale decisions.

---

<!-- DECISION-00A9EEE6 -->
## Decision: Implement client-side credit balance check for Ask Knowledge Base

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Warning

**Files**:
- `/api/companies/:companyId/mcp/ask-knowledge-base`
- `src/components/dashboard/CreditsPanel.tsx`
- `src/hooks/useCreditBalance.ts`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "src/components/dashboard/CreditsPanel.tsx",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "useCreditBalance"
          ]
        }
      ],
      "content_match_mode": "all"
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Users were experiencing service disruptions when their credit balance was exhausted mid-session because the API block only triggers after the transaction fails (402).

**Decision:** The team decided to implement a client-side credit balance check in the frontend dashboard before allowing a request to hit the /api/companies/:companyId/mcp/ask-knowledge-base endpoint.

**Rationale:** The existing Redis pre-check in the backend is fail-open and only blocks after a SERIALIZABLE transaction confirms insufficient balance. A client-side check provides better user experience by surfacing the warning in the dashboard before the query is attempted, preventing unnecessary 402 errors.

**Alternatives Considered:**
- **Backend fail-closed check**: The current Redis architecture is fail-open for performance reasons; changing it would involve complex architectural changes to handle transaction locking and potential performance degradation.
- **Surface 402 error in UI**: Displaying a 402 error after the user has already submitted the request is a poor user experience compared to preventing the request entirely via a dashboard warning.

---

<!-- DECISION-FDB94885 -->
## Decision: Implement Redis Semantic Caching for LLM Embedding Calls

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Warning

**Files**:
- `**/*`

### Context

**Problem:** Redundant and inefficient LLM embedding calls were occurring.

**Decision:** Implemented Redis semantic caching for LLM embedding calls. The cache key is a hash of the input text, model, and provider. The cache entries have a Time-To-Live (TTL) of 1 hour.

**Rationale:** Redis was a natural extension since it is already in use for BullMQ and session caching. This implementation reduced redundant embedding calls by approximately 40% in tests.

---

<!-- DECISION-A0FB66A4 -->
## Decision: Implement server-side session tracking for MCP keys using Redis

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Warning

**Files**:
- `mcp_logs`
- `SessionDetector`
- `mcp_key_handler`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "{mcp_logs,SessionDetector,mcp_key_handler}",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "inactivity.*gap|client.*header.*session"
        }
      ],
      "content_match_mode": "any"
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Frequent agent re-initializations are creating excessive small sessions and causing inflated discovery costs because session detection relies solely on inactivity gaps.

**Decision:** Track sessions via a server-side generated session_id stored in Redis with a 30-minute rolling TTL, instead of relying on inactivity gap computation. Include the session_id as a foreign key in the mcp_logs table to allow accurate deduplication of discovery costs.

**Rationale:** Server-side generation is safer than relying on client-side headers as agents are inconsistent. Using a Redis-backed TTL ensures that sessions are grouped correctly despite agent re-initialization patterns, providing more accurate cost tracking and session lifecycle management.

**Alternatives Considered:**
- **Agent-provided session_id header**: Agents cannot be trusted to pass stable, consistent session identifiers.
- **Inactivity gap computation**: Leads to fragmented sessions and inaccurate discovery cost metrics due to agent re-initialization patterns.

---

<!-- DECISION-F2770524 -->
## Decision: Implementation details for text embeddings in PostgreSQL using OpenAI's text-embedding-3-small and HNSW indexing

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Warning

**Files**:
- `packages/decision-store/src/schema.ts`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "packages/decision-store/src/schema.ts",
      "content_rules": [
        {
          "mode": "string",
          "pattern": "text-embedding-3-small"
        },
        {
          "mode": "string",
          "pattern": "1536"
        },
        {
          "mode": "string",
          "pattern": "knowledge_chunks"
        },
        {
          "mode": "string",
          "pattern": "ef_construction=200"
        },
        {
          "mode": "string",
          "pattern": "m=16"
        }
      ],
      "content_match_mode": "all"
    }
  ],
  "match_mode": "all"
}
```

### Context

**Decision:** We will use the `text-embedding-3-small` OpenAI model to generate 1536-dimension embeddings. These embeddings will be stored in the `knowledge_chunks` table within PostgreSQL. The HNSW index used for vector search will be configured with `ef_construction=200` and `m=16`.

**Rationale:** The chosen HNSW parameters (`ef_construction=200` and `m=16`) are set to provide an optimal tradeoff between recall accuracy and search speed. The `text-embedding-3-small` model is selected for generating the text embeddings.

---

<!-- DECISION-D01640F7 -->
## Decision: Include agent_type in Redis sessions and logs

**Status**: Active  
**Date**: 2026-05-21  
**Severity**: Warning

**Files**:
- `session_management`
- `mcp_logs`

### Context

**Problem:** Logs currently lack the ability to distinguish between different agents (e.g., Claude Code, Cursor) because the session data does not store the agent identity.

**Decision:** Add an 'agent_type' field to the Redis session hash using a normalized enum (claude_code, cursor, copilot, custom) and write this value to mcp_logs upon session creation.

**Rationale:** Using a normalized enum instead of raw User-Agent strings avoids dealing with excessive variants while enabling accurate breakdown of mcp_logs by agent type in the savings dashboard.

**Alternatives Considered:**
- **Store raw User-Agent string**: There are too many variants of User-Agent strings, making them difficult to parse and aggregate for analytics.

---

<!-- DECISION-85F2E3FA -->
## Decision: Increase detection confidence threshold for PassiveDetector in saver mode

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Warning

**Files**:
- `src/PassiveDetector/prompt.ts`
- `src/PassiveDetector/detection.py`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "src/PassiveDetector/**",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "confidence_threshold|min_confidence"
        }
      ],
      "content_match_mode": "any"
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** PassiveDetector is incorrectly capturing standup messages as context units, leading to false positives.

**Decision:** The team will tune the Gemini-2.5-flash prompt for PassiveDetector in saver mode to increase the detection confidence floor.

**Rationale:** The current confidence threshold is allowing low-quality matches (standup messages) to be classified as actionable context units. Tuning the system prompt is the most efficient way to reduce noise without switching the underlying model or building complex per-channel weight features.

**Alternatives Considered:**
- **Change the detection model**: Requires an Architectural Decision Record (ADR) and is likely overkill compared to a prompt update.
- **Implement per-channel weights**: The current infrastructure does not support per-channel configurations.

---

<!-- DECISION-78128BC7 -->
## Decision: Increase web dashboard session timeout to 4 hours

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Warning

**Files**:
- `web/config/session.js`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "web/config/session.js",
      "content_rules": [
        {
          "mode": "full_file"
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Current web dashboard session timeout is too short for users performing long review sessions.

**Decision:** Extend the session timeout period for the web dashboard application to 4 hours.

**Rationale:** Extended sessions improve usability for long review tasks, while maintaining shorter token life for mobile to satisfy security requirements regarding background app behavior.

**Alternatives Considered:**
- **Extend session timeout for both web and mobile**: Mobile requires shorter session tokens due to security risks associated with background application behavior.

---

<!-- DECISION-DFBA9327 -->
## Decision: Migrate Indian payment processing from Stripe to Paddle

**Status**: Active  
**Date**: 2026-05-19  
**Severity**: Warning

**Files**:
- `src/billing/payment_processor.ts`
- `src/config/payments.json`

### Context

**Problem:** Stripe has been banned by the Indian government for processing payments, necessitating a replacement payment provider to maintain operations in the region.

**Decision:** The team will migrate all Indian payment processing operations from Stripe to Paddle.

**Rationale:** Stripe is no longer a viable or legal option for payment processing in India due to government-imposed bans, making a migration to a supported alternative like Paddle necessary for continuity.

---

<!-- DECISION-4407DF70 -->
## Decision: Restrict record deduplication to same-type comparisons and update thresholds

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Warning

**Files**:
- `findSimilarActiveWithScores`
- `findSimilarByTypes`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "{findSimilarActiveWithScores,findSimilarByTypes}",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "(cross-type|0\\.15)"
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** The existing deduplication logic allows cross-type comparison and uses suboptimal thresholds for same-type comparisons.

**Decision:** Deduplication will now only occur between decisions of the same type. The threshold for same-type deduplication is lowered from 0.15 to 0.12.

**Rationale:** Calibration analysis of 847 labeled examples indicates that cross-type deduplication is invalid and 0.12 is the optimal threshold for same-type matching to ensure accurate dedup behavior.

**Alternatives Considered:**
- **Allow cross-type deduplication**: It was determined that dedup should only fire when existing and incoming decisions have the same type to maintain data integrity.

---

<!-- DECISION-52E3F043 -->
## Decision: Send onboarding link via Slack DM upon app installation

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Warning

**Files**:
- `src/integrations/slack/events/app_installed.ts`
- `src/dashboard/oauth/callback.ts`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "src/integrations/slack/events/app_installed.ts",
      "content_rules": [
        {
          "mode": "full_file",
          "pattern": "app_installed"
        }
      ],
      "content_match_mode": "all"
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Slack workspaces were hitting a SlackIntegrationNotFoundError because users were installing the bot but failing to complete the dashboard OAuth flow.

**Decision:** Implement a mechanism to DM the onboarding link to the workspace admin immediately upon app installation (app_installed event) rather than waiting for message events.

**Rationale:** Direct messaging the onboarding link ensures users complete the dashboard setup process, preventing the creation of 'ghost companies' and avoiding errors when the recorder tries to process events for an uninitialized workspace.

**Alternatives Considered:**
- **Handle team_join event**: The team_join event triggers for new users joining a workspace, not for the initial app installation, making it unsuitable for driving workspace-level onboarding.

---

<!-- DECISION-070F625D -->
## Decision: Set similarity threshold for decision deduplication to 0.15 cosine distance

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Warning

**Files**:
- `linker_calibration_events`
- `dedup_logic`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "{linker_calibration_events,dedup_logic}",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "threshold\\s*[:=]\\s*(0\\.(1[6-9]|[2-9]\\d))"
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Near-identical decisions are being incorrectly allowed through the deduplication process.

**Decision:** Maintain the current cosine distance threshold of < 0.15 as the similarity floor for identifying deduplication candidates.

**Rationale:** A strict threshold is intentionally chosen to prioritize preventing false positives (blocking legitimate new decisions) over false negatives (missing potential duplicates), with plans to refine the threshold once 500+ labeled examples are collected.

**Alternatives Considered:**
- **Lowering the similarity threshold (e.g., to 0.18)**: The current threshold is intentionally strict to avoid false positives and maintain a high bar for what constitutes a duplicate decision.

---

<!-- DECISION-F3DB51F2 -->
## Decision: Standardize on HNSW for new vector indexes

**Status**: Active  
**Date**: 2026-04-22  
**Severity**: Warning

**Files**:
- `db/schema/vector_indexes`
- `db/migrations/sprint_16/migrate_llm_cache_to_hnsw`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "db/schema/vector_indexes",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "USING\\s+(IVFFLAT|IVFFLAT\\s+)"
        }
      ]
    },
    {
      "type": "file",
      "pattern": "db/migrations/sprint_16/migrate_llm_cache_to_hnsw",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "CREATE\\s+INDEX.*USING\\s+IVFFLAT"
        }
      ]
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** Uncertainty regarding whether the rejection of IVFFlat to HNSW migration applied to the index technology choice or the migration process itself.

**Decision:** All new vector indexes must be created using the HNSW algorithm. Existing IVFFlat indexes (specifically in the llm_cache table) are to be migrated to HNSW in Sprint 16.

**Rationale:** HNSW is the current architectural standard for vector indexing. The previous rejection of the migration to HNSW was due to operational risks in production, not a lack of performance or technical suitability of HNSW.

**Alternatives Considered:**
- **IVFFlat**: The team has standardized on HNSW for new indexes to maintain architectural consistency, despite potential performance profiles for specific query patterns.

---

<!-- DECISION-47BAF067 -->
## Decision: Standardize on TypeScript and camelCase JSON for backend services

**Status**: Active  
**Date**: 2026-05-08  
**Severity**: Warning

**Files**:
- `/src/backend/`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "src/backend/**/*.ts",
      "content_rules": [
        {
          "mode": "regex",
          "start": 1,
          "pattern": "(?s).*class.*|.*interface.*"
        }
      ],
      "content_match_mode": "all"
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Need to ensure consistency and type safety across new backend development and prevent API interoperability issues.

**Decision:** Adopt TypeScript as the mandatory language for all new backend services and enforce a strict convention where all API endpoints must return camelCase JSON.

**Rationale:** TypeScript provides necessary type safety to reduce runtime errors in backend services, and a consistent camelCase JSON format ensures predictability for frontend consumption and API consistency.

---

<!-- DECISION-FA0B2FF9 -->
## Decision: Trigger dashboard onboarding redirect after Slack OAuth completion

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Warning

**Files**:
- `services/slack/oauth_callback.js`
- `services/onboarding/dashboard_redirect.js`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "services/slack/oauth_callback.js",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "redirect",
            "onboarding"
          ]
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Slack integrations fail in production when a workspace installs the bot but does not complete the dashboard OAuth process, resulting in a SlackIntegrationNotFoundError.

**Decision:** Implement a redirect to the dashboard onboarding flow immediately after the Slack OAuth callback is successfully completed to ensure the integration record is created.

**Rationale:** Directing users to the onboarding flow upon OAuth completion captures the integration state before the bot begins receiving event messages, preventing orphaned bot installations without corresponding database records.

**Alternatives Considered:**
- **DM the user the onboarding link when the bot joins**: The team determined that handling the event on the OAuth callback side is more reliable and ensures the flow is completed immediately after the installation.
- **Use team_join event to trigger onboarding**: The team identified that team_join events are specific to individual users joining a workspace and are not suitable for the initial workspace-level integration onboarding.

---

<!-- DECISION-EE69526A -->
## Decision: Unify hard conflict review notifications via NotificationService

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Warning

**Files**:
- `services/InteractionHandler.ts`
- `services/github-notification-queue.ts`
- `services/NotificationService.ts`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "services/{InteractionHandler,github-notification-queue}.ts",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "new\\s+Notification\\(|console\\.log\\(.*conflict.*\\)|send.*DM"
        }
      ],
      "content_match_mode": "any"
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** Review notifications for hard conflicts are currently limited to Slack-sourced decisions, causing GitHub-sourced conflicts to remain stuck in pending_review without alerting owners.

**Decision:** Route all hard conflict notifications through the centralized NotificationService using a CONTRADICTS_REVIEW template, replacing the localized implementation in InteractionHandler.

**Rationale:** Routing through the NotificationService ensures consistent notification delivery regardless of the input channel (Slack or GitHub PRs), eliminating the inconsistency where GitHub-sourced conflicts currently lack alert logic.

**Alternatives Considered:**
- **Implement an equivalent postContradictReviewDm method in the github-notification-queue worker**: This would result in fragmented notification logic across multiple services, increasing maintenance overhead and the likelihood of future inconsistencies.

---

<!-- DECISION-CFE6365D -->
## Decision: Use 30-minute inactivity window for MCP session detection

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Warning

**Files**:
- `ADR-033`
- `SessionDetector`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "{ADR-033,SessionDetector}",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "(?i)(session.*timeout|session.*window|gap|inactivity)"
        }
      ],
      "content_match_mode": "any"
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** How to define a new MCP session for token savings calculations in the presence of variable agent speeds.

**Decision:** Define a new MCP session as any gap of 30 minutes or more between tool calls on mcp_logs per API key.

**Rationale:** The 30-minute window serves as a known, intentional approximation to define session boundaries for cost calculation, acknowledging that very slow agents may trigger multiple sessions.

---

<!-- DECISION-75490C8C -->
## Decision: Use cosine distance for pgvector similarity searches

**Status**: Active  
**Date**: 2026-04-22  
**Severity**: Warning

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "**/*.sql",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "<->"
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Determine the optimal distance metric for embedding similarity search in pgvector to maximize recall.

**Decision:** We have standardized on cosine distance (using the <=> operator in pgvector) for all similarity search operations.

**Rationale:** Cosine distance provides significantly better recall (12% improvement) on normalized text embeddings compared to L2 distance. Furthermore, L2 distance is overly sensitive to embedding magnitude, making it less reliable for our specific use case.

**Alternatives Considered:**
- **L2 distance**: It is sensitive to embedding magnitude and demonstrated poorer recall compared to cosine distance for our data.

---

<!-- DECISION-E1408395 -->
## Decision: Use cosine distance over L2 for semantic text embedding similarity with pgvector HNSW

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Warning

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "**/*",
      "content_rules": [
        {
          "mode": "regex",
          "pattern": "(?i)pgvector|HNSW"
        },
        {
          "mode": "regex",
          "pattern": "(?i)L2_DISTANCE|EUCLIDEAN_DISTANCE|l2_distance|euclidean_distance|distance_type\\s*=\\s*['\\\"]l2['\\\"]|metric\\s*=\\s*['\\\"]l2['\\\"]"
        }
      ],
      "content_match_mode": "all"
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** How to accurately measure semantic similarity of text embeddings for deduplication search using pgvector HNSW?

**Decision:** We decided to use cosine distance for semantic similarity search of text embeddings with pgvector HNSW for deduplication.

**Rationale:** Cosine distance is invariant to vector magnitude, meaning it only considers the direction of vectors. This property is precisely what is desired for semantic similarity of text embeddings, as it allows for accurate comparison of semantic meaning regardless of variations in embedding vector norms. L2 (Euclidean) distance, on the other hand, would incorrectly penalize vectors with different magnitudes, even if they share the same semantic direction.

**Alternatives Considered:**
- **L2 (Euclidean) distance**: L2 distance penalizes vectors with different norms (magnitudes) even if they point in the same semantic direction, which is not suitable for accurately measuring semantic similarity of text embeddings.

---

<!-- DECISION-3ADA5B02 -->
## Decision: Use fusion-eval harness for linkThreshold tuning

**Status**: Active  
**Date**: 2026-05-20  
**Severity**: Warning

**Files**:
- `packages/fusion-eval`
- `src/context-linker/params.ts`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "src/context-linker/params.ts",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "linkThreshold\\s*=\\s*\\d*\\.?\\d+"
        }
      ],
      "content_match_mode": "all"
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Current context-linker linkThreshold of 0.30 causes false positives due to generic entity matching on common tokens like 'redis', but manual changes to the threshold risk dropping true links.

**Decision:** The team will refrain from manually adjusting DEFAULT_FUSION_PARAMS.linkThreshold and instead utilize the existing precision/recall harness in packages/fusion-eval to empirically evaluate changes before deployment.

**Rationale:** Blindly changing global thresholds can negatively impact system accuracy. Validating threshold adjustments through the dedicated fusion-eval harness ensures that changes to the linkage logic do not reduce recall for valid links.

**Alternatives Considered:**
- **Add 'redis' to the stopword list**: Stopwords in the context-linker are restricted to title tokens and do not influence the entity extraction path.
- **Blindly lower linkThreshold to 0.32**: Risk of unintended side effects and reduction in true positive linkage performance without empirical validation.

---

<!-- DECISION-A06BAC38 -->
## Decision: Use MongoDB Atlas for analytics event ingestion

**Status**: Active  
**Date**: 2026-04-22  
**Severity**: Warning

**Files**:
- `analytics/storage`
- `infrastructure/database-policy`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "{analytics/storage/**,infrastructure/database-policy/**}",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "mongodb|mongoclient|mongodb-driver"
        }
      ],
      "content_match_mode": "any"
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** The team needs a high-throughput storage solution for analytics event ingestion, but is restricted to PostgreSQL and Redis for general data storage.

**Decision:** MongoDB is strictly prohibited for use in core pipeline services (including the core decision pipeline, authentication, and the context store). These services must exclusively use PostgreSQL 16 and Redis. Any deviation requires a formal ADR.

**Rationale:** To maintain architectural integrity and prevent fragmentation in the core tech stack. Previous attempts to introduce MongoDB for event queues nearly caused instability, highlighting the need for a hard, enforceable constraint.

**Alternatives Considered:**
- **PostgreSQL partitioned tables**: The team expressed concern that it may struggle with the required write throughput of 50k events per second.

---

<!-- DECISION-606E27A4 -->
## Decision: Standardization on iPhones for mobile communication

**Status**: Active  
**Date**: 2026-05-05  
**Severity**: Info

**Files**:
- `**/*`

### Context

**Decision:** The team will use iPhones to perform mobile calls.

**Rationale:** The team aligned on a single mobile device platform for communication consistency.

---

<!-- DECISION-C8FA6892 -->
## Decision: Use separate SCSS file for navigation component styling

**Status**: Active  
**Date**: 2026-05-08  
**Severity**: Info

**Files**:
- `navbar.scss`
- `navbar.tsx`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "**/navbar.tsx",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "className=\".*hover:.*\""
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Tailwind utility classes are too complex and messy for the hover states required by the new navigation component.

**Decision:** Use standard SCSS in a separate navbar.scss file for the new navigation component.

**Rationale:** Complex hover state requirements for the navigation component lead to unmanageable code when using Tailwind utility classes.

**Alternatives Considered:**
- **Tailwind utility classes**: The resulting code is too messy and complex for the required hover states.
