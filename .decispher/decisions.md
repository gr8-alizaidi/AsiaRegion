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

**Decision:** MongoDB is strictly prohibited from being integrated into the current technology stack, including for analytics events.

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

**Decision:** We have decided to officially discontinue the use of RFC 7807 (Problem Details for HTTP APIs) for all API error responses moving forward.

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

**Decision:** We will integrate decision-guardian into our PR pipeline to enforce and track architectural decisions.

---

<!-- DECISION-B6869B8C -->
## Decision: Define LLM Model Combinations for Saver, Balanced, Pro, and Super Effort Modes

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Files**:
- `**/*`

### Context

**Decision:** The specific LLM model combinations for the multi-provider effort modes were finalized: Saver mode uses `gemini-flash` for detection, extraction, and format. Balanced mode uses `gemini-flash` for detection, `claude-haiku` for extraction, and `gpt-4o-mini` for format. Pro mode uses `gemini-flash` for detection, `claude-sonnet` for extraction, and `gpt-4o-mini` for format. Super mode uses `gemini-flash` for detection, `claude-opus` for extraction, and `claude-sonnet` for format.

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

**Decision:** Establish explicit provider fallback orderings: For extraction, use Anthropic → DeepSeek → OpenAI. For detection, use Google → OpenAI → DeepSeek.

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

**Decision:** Implement a strict 5-minute token expiry window for the authentication service.

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

**Decision:** Transition the usage of Azure Functions from a specialized payment-only utility to a full-scale backend service platform.

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

**Decision:** We will implement a multi-provider abstraction where each pipeline step (detection, extraction, enrichment, formatting) has its own LLM provider configuration via environment variables. At request time, an 'effort mode' can override the provider selection on a per-company basis.

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

**Decision:** Revert the premium user request rate limit from 500 req/min to 200 req/min until infrastructure improvements, such as scaling the connection pool or adding read replicas, are implemented.

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

**Decision:** All LLM calls must be made through the GuardedProviderRegistry using either wrapTextProvider or wrapEmbeddingProvider instead of direct SDK calls.

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

**Decision:** Migrate all email services to Zoho and update the SMTP server infrastructure, including the implementation of new routing rules to block any traffic to the legacy SMTP server.

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

**Decision:** Switch from the third-party Shipsy provider to an in-house developed mapping event system.

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

**Decision:** Remove the Stream Chat provider dependency and develop an in-house chat solution to provide end-to-end functionality.

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

**Decision:** The team will migrate from AWS ECS to AWS EKS for container orchestration.

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

**Decision:** Adopt ITSI RFC for SNMP development instead of 3GPP.

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

**Decision:** The trigger metric for initiating the AWS migration has been adjusted from 20 paying customers to 30 paying customers. The Q3 2026 timeline for the migration still holds.

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

**Decision:** All API errors must adhere to the RFC 7807 problem details format, including fields such as type, title, status, detail, and instance.

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

**Decision:** Use PostgreSQL with pgvector and HNSW indexes as the standard solution for primary datastore and vector search operations.

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

**Decision:** The billing service uses long-running containers instead of serverless functions.

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

**Decision:** We will use MongoDB for the analytics events pipeline, provisioning a MongoDB Atlas cluster to handle the data.

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

**Decision:** Bypass mTLS authentication for the new reporting worker and implement a hardcoded shared secret token in the HTTP header for inter-service authentication.

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

**Decision:** The team decided to discontinue the use of EventStoreDB and removed event sourcing as an architectural pattern following the migration back to a monorepo.

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

**Decision:** Implement a ContextLifecycleWorker job that triggers the existing archiveStalePendingReview function based on decisions not reviewed in over 90 days, controlled by the STALE_ARCHIVE_ENABLED environment variable.

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

**Decision:** Implement a 'captureIntent' field in the pipeline context where 'explicit' intent (manual dashboard entry or @decispher command) bypasses the minConfidence threshold, while 'passive' intent remains subject to it.

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

**Decision:** Manual captures with sourceType='manual' will bypass the minConfidence check entirely by adding an explicit guard in the detection step where shouldRun returns true by default for manual types.

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

**Decision:** The team has officially cancelled the usage and implementation of RFC 78.

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

**Decision:** Set LLM_CLASSIFY_BATCH_SIZE_PRO to 10 and maintain LLM_CLASSIFY_CONCURRENCY at 5 for the pro mode classification process.

---

<!-- DECISION-D17917AC -->
## Decision: Enforce RFC 7807 for Internal API Error Formats

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Warning

**Files**:
- `packages/api/src/routes/internal/`

### Context

**Decision:** All internal API routes must adhere to the RFC 7807 error format, consistent with public-facing API routes.

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

**Decision:** Set a fixed default request limit of 2000 req/min for the enterprise tier, utilizing a separate infrastructure pool to prevent performance degradation for other customers.

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

**Decision:** Adopt the HIGH severity specification as the authoritative version for the RFC 7807 error format, which includes fields: type, title, status, detail, and instance.

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

**Decision:** Implement a notification process where owners receive a Slack message 7 days before the archival cutoff. This involves adding a 'stale_warning' type to the NotificationService, querying for decisions with a last_reviewed_at timestamp between 83 and 84 days ago, and triggering a notification job. Viewing a decision on the dashboard does not reset the review timestamp; only an explicit approve, reject, or mark_active action will.

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

**Decision:** The team decided to implement a client-side credit balance check in the frontend dashboard before allowing a request to hit the /api/companies/:companyId/mcp/ask-knowledge-base endpoint.

---

<!-- DECISION-FDB94885 -->
## Decision: Implement Redis Semantic Caching for LLM Embedding Calls

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Warning

**Files**:
- `**/*`

### Context

**Decision:** Implemented Redis semantic caching for LLM embedding calls. The cache key is a hash of the input text, model, and provider. The cache entries have a Time-To-Live (TTL) of 1 hour.

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

**Decision:** Track sessions via a server-side generated session_id stored in Redis with a 30-minute rolling TTL, instead of relying on inactivity gap computation. Include the session_id as a foreign key in the mcp_logs table to allow accurate deduplication of discovery costs.

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

**Decision:** Add an 'agent_type' field to the Redis session hash using a normalized enum (claude_code, cursor, copilot, custom) and write this value to mcp_logs upon session creation.

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

**Decision:** The team will tune the Gemini-2.5-flash prompt for PassiveDetector in saver mode to increase the detection confidence floor.

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

**Decision:** Extend the session timeout period for the web dashboard application to 4 hours.

---

<!-- DECISION-7B5408A4 -->
## Decision: MCP-TEST: Why we use Repository pattern for DB access

**Status**: Active  
**Date**: 2026-05-25  
**Severity**: Warning

**Files**:
- `**/*`

### Context

**Decision:** All database operations go through Repository classes. No raw queries in service layer.

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

**Decision:** The team will migrate all Indian payment processing operations from Stripe to Paddle.

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

**Decision:** Deduplication will now only occur between decisions of the same type. The threshold for same-type deduplication is lowered from 0.15 to 0.12.

---

<!-- DECISION-3C8A6838 -->
## Decision: Scope same-type deduplication filter to analyzer pipeline only

**Status**: Active  
**Date**: 2026-05-22  
**Severity**: Warning

**Files**:
- `linker_calibration_events`
- `dedup_logic`
- `analyzer/dedup.py`
- `knowledge-graph/edge-detection.py`
- `services/similarity-engine.py`

### Context

**Decision:** The same-type deduplication filter will be strictly scoped to the analyzer pipeline for duplicate storage prevention. The knowledge graph edge detection will operate independently, allowing cross-type similarity calculations with its own distinct thresholds.

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

**Decision:** Implement a mechanism to DM the onboarding link to the workspace admin immediately upon app installation (app_installed event) rather than waiting for message events.

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

**Decision:** All new vector indexes must be created using the HNSW algorithm. Existing IVFFlat indexes (specifically in the llm_cache table) are to be migrated to HNSW in Sprint 16.

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

**Decision:** Adopt TypeScript as the mandatory language for all new backend services and enforce a strict convention where all API endpoints must return camelCase JSON.

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

**Decision:** Implement a redirect to the dashboard onboarding flow immediately after the Slack OAuth callback is successfully completed to ensure the integration record is created.

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

**Decision:** Route all hard conflict notifications through the centralized NotificationService using a CONTRADICTS_REVIEW template, replacing the localized implementation in InteractionHandler.

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

**Decision:** Define a new MCP session as any gap of 30 minutes or more between tool calls on mcp_logs per API key.

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

**Decision:** We have standardized on cosine distance (using the <=> operator in pgvector) for all similarity search operations.

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

**Decision:** We decided to use cosine distance for semantic similarity search of text embeddings with pgvector HNSW for deduplication.

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

**Decision:** The team will refrain from manually adjusting DEFAULT_FUSION_PARAMS.linkThreshold and instead utilize the existing precision/recall harness in packages/fusion-eval to empirically evaluate changes before deployment.

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

**Decision:** MongoDB is strictly prohibited for use in core pipeline services (including the core decision pipeline, authentication, and the context store). These services must exclusively use PostgreSQL 16 and Redis. Any deviation requires a formal ADR.

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

**Decision:** Use standard SCSS in a separate navbar.scss file for the new navigation component.
