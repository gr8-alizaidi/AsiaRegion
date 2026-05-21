# .roo/rules.md

## CRITICAL
<!-- decispher:ids=393329ba-9640-498c-89f1-46c1324aaeff -->
- **MongoDB Prohibition**: Strictly prohibited in core stack (billing/user data) due to lack of ACID compliance.

## HIGH
<!-- decispher:ids=08702be0-9097-4015-89a9-6f3307cdd76c -->
- **LLM Provider Registry**: All calls must use `GuardedProviderRegistry` (`wrapTextProvider`/`wrapEmbeddingProvider`) for cost tracking and kill-switch control.
  - Affected: `src/llm/providers/registry.ts`, `src/llm/providers/guarded_registry.ts`

<!-- decispher:ids=0948e434-dd93-4077-a9fc-b5a3daa13cc6 -->
- **CMS**: Use Umbraco for frontend component development.

<!-- decispher:ids=e8a1dec8-3524-4365-a291-fba0b55b57a4 -->
- **Chat**: Remove Stream Chat; develop in-house solution.
  - Affected: `src/chat/provider`, `src/services/chat`

<!-- decispher:ids=9f213a97-eb01-49b8-aa57-76b0a2358c10 -->
- **Azure Functions**: Utilize as full-scale backend service platform.
  - Affected: `infrastructure/payment-service`, `backend/payments`, `infrastructure/azure-functions`

<!-- decispher:ids=b5692ce3-058f-4fff-b37c-f3a14d89d277 -->
- **SNMP**: Adopt ITSI RFC; abandon 3GPP.
  - Affected: `src/ss7-stack/backend`

<!-- decispher:ids=260c4141-6041-4f13-9314-cecfe7698bc1 -->
- **Reporting Auth**: Use hardcoded shared secret header; bypass mTLS for speed.
  - Affected: `src/reporting-worker/api-client.ts`, `src/api/auth/middleware.ts`

<!-- decispher:ids=02e6acff-0e2b-4caf-969d-be906d20eedc -->
- **Auth Expiry**: Strict 5-minute session token expiry.
  - Affected: `services/auth-service`

<!-- decispher:ids=e9097831-0c7b-43f1-895a-b115dd36c20b -->
- **Email**: Migrate to Zoho; block legacy SMTP traffic.
  - Affected: `infrastructure/mail`, `services/smtp`, `config/email_routing`

<!-- decispher:ids=bd1748c1-766b-4f12-9819-536593b3c5ed -->
- **Mapping**: Migrate from Shipsy to in-house system.
  - Affected: `services/shipping-integration`, `infrastructure/event-bus`

<!-- decispher:ids=d01a8dd8-f37c-406d-8050-d98eaab82da0 -->
- **API Errors**: Discontinue RFC 7807 (see authoritative standard in MEDIUM).
  - Affected: `api/responses`, `api/error-handling`

<!-- decispher:ids=08631b5a-a39c-4766-b840-beb3a13fde5e -->
- **Billing Service**: Use long-running containers (avoid serverless cold starts).
  - Affected: `packages/api/src/routes/credits.ts`, `packages/decision-store/src/repositories/credit-repository.ts`, `packages/common/src/types/credits.ts`, `services/billing`

<!-- decispher:ids=ac87f0e5-a048-4197-a533-31b2a71ea5a8 -->
- **LLM Fallback**: Extraction: Anthropic → DeepSeek → OpenAI. Detection: Google → OpenAI → DeepSeek.
  - Affected: `src/llm/client_factory.py`, `src/llm/fallback_logic.py`

<!-- decispher:ids=add28e0b-7a4c-4b40-9679-0d488565bb4b -->
- **Orchestration**: Migrate AWS ECS to EKS.
  - Affected: `infrastructure/terraform`, `infrastructure/k8s`

<!-- decispher:ids=81145171-98f5-43da-b922-9c76d78741b5 -->
- **Datastore**: Standardize on PostgreSQL with `pgvector` and HNSW indexes.

<!-- decispher:ids=3213c504-c4a3-4364-bc9f-3e3213242b7a,465c1e1a-bb9e-4b3f-ade4-dc7674a5870a -->
- **MongoDB Analytics**: Provision Atlas for analytics pipeline only to handle high-write throughput.
  - Affected: `services/analytics-webhook-handler`, `infrastructure/database-clusters`, `packages/api/src/analytics/`

<!-- decispher:ids=3b06241d-4a87-4cf9-a36c-cd9c831b3a97,b6869b8c-7d43-47d4-9cd6-dddb0f9f92b9,6acd0667-b5bc-4fd2-8811-36010ed7c2ac -->
- **LLM Strategy**: Multi-provider abstraction per step. Use "Effort Modes":
  - Saver: `gemini-flash` (all).
  - Balanced: `gemini-flash` (detect), `claude-haiku` (extract), `gpt-4o-mini` (format).
  - Pro: `gemini-flash` (detect), `claude-sonnet` (extract), `gpt-4o-mini` (format).
  - Super: `gemini-flash` (detect), `claude-opus` (extract), `claude-sonnet` (format).
  - Affected: `packages/analyzer/`

<!-- decispher:ids=71727a5f-aefc-4a9d-af63-247b7a964dc1 -->
- **Infrastructure Migration**: Delay AWS migration until 30 paying customers.

<!-- decispher:ids=670907d2-a11a-4cc9-8a02-67fb54fef3f7 -->
- **Governance**: Integrate `decision-guardian` in PR pipeline.

<!-- decispher:ids=127df168-2a46-4b88-a207-e6887cc4183f -->
- **API Standard**: Authoritative RFC 7807 usage (type, title, status, detail, instance).

## MEDIUM
<!-- decispher:ids=d01640f7-b859-4654-908c-7b5f263e92eb -->
- **Session Tracking**: Log `agent_type` (enum) in Redis/mcp_logs.
  - Affected: `session_management`, `mcp_logs`

<!-- decispher:ids=ac19b0f4-f780-4869-834f-822daeac746c -->
- **Enterprise Limits**: Fixed 2000 req/min; separate infrastructure pool.
  - Affected: `middleware/rate-limit-config.js`, `rate-limiter/config.ts`, `infrastructure/gateway/limits.yaml`

<!-- decispher:ids=78128bc7-2817-44f2-bef0-de69eb3feb44 -->
- **Dashboard**: 4-hour session timeout.
  - Affected: `web/config/session.js`

<!-- decispher:ids=4407df70-f88d-4a6c-ad00-6109d8605f95,070f625d-06bc-4f71-b793-7d7d80f064aa -->
- **Deduplication**: Same-type comparisons only; threshold 0.12.
  - Affected: `findSimilarActiveWithScores`, `findSimilarByTypes`, `linker_calibration_events`, `dedup_logic`

<!-- decispher:ids=a0fb66a4-0691-48c6-9245-5b023347648e,cfe6365d-63eb-4b8b-9164-f956524c984f -->
- **MCP Sessions**: 30-minute inactivity window via Redis-backed `session_id`.
  - Affected: `mcp_logs`, `SessionDetector`, `mcp_key_handler`, `ADR-033`

<!-- decispher:ids=b07d5f19-a624-4dec-9040-dd5fae17fbfb,b4a09b64-6a2e-4ca0-9759-5af97e89b703 -->
- **Confidence Bypassing**: `explicit` intents and `sourceType='manual'` bypass `minConfidence`.
  - Affected: `app/workers/process_comment_worker.rb`, `app/services/pipeline_context.rb`, `src/analyzer/detection.ts`

<!-- decispher:ids=2b58e1d2-11f2-4acd-9c71-7857b911641a,82ce6ad9-0289-47f4-a8ec-2cefaabcd830 -->
- **Lifecycle**: Notify owner 7 days before archival. Auto-archive >90 days via `ContextLifecycleWorker`.
  - Affected: `services/NotificationService.js`, `jobs/ArchiveNotificationJob.js`, `controllers/DecisionController.js`, `src/workers/ContextLifecycleWorker.ts`, `src/services/ApprovalService.ts`

<!-- decispher:ids=3ada5b02-0165-4c53-afe6-f5ac4f1e2823 -->
- **Tuning**: Use `fusion-eval` for `linkThreshold` validation.
  - Affected: `packages/fusion-eval`, `src/context-linker/params.ts`

<!-- decispher:ids=ee69526a-69a3-4115-be00-d3981c919d79 -->
- **Notifications**: Centralize hard conflict alerts via `NotificationService`.
  - Affected: `services/InteractionHandler.ts`, `services/github-notification-queue.ts`, `services/NotificationService.ts`

<!-- decispher:ids=90c810f5-5c5e-471e-ab8f-5e2cc0f76b2d -->
- **Batching**: `LLM_CLASSIFY_BATCH_SIZE_PRO`=10, `CONCURRENCY`=5.
  - Affected: `decision-fusion-queue`

<!-- decispher:ids=fa0b2ff9-93d4-41a5-b007-d8cc51ed65ba,52e3f043-27a2-4727-b598-cd10169ece5b -->
- **Onboarding**: Redirect to dashboard post-OAuth; DM onboarding link on `app_installed`.
  - Affected: `services/slack/oauth_callback.js`, `services/onboarding/dashboard_redirect.js`, `src/integrations/slack/events/app_installed.ts`, `src/dashboard/oauth/callback.ts`

<!-- decispher:ids=85f2e3fa-93e4-406b-bb78-1b57b1abcafd -->
- **PassiveDetector**: Tune prompt for higher confidence threshold.
  - Affected: `src/PassiveDetector/prompt.ts`, `src/PassiveDetector/detection.py`

<!-- decispher:ids=00a9eee6-24ed-4419-a3fc-c168abe636d6 -->
- **Credits**: Client-side balance check before Ask Knowledge Base API request.
  - Affected: `/api/companies/:companyId/mcp/ask-knowledge-base`, `src/components/dashboard/CreditsPanel.tsx`, `src/hooks/useCreditBalance.ts`

<!-- decispher:ids=47baf067-44b5-4456-a34c-fce4360c84a3 -->
- **Code Standards**: TypeScript, camelCase JSON.
  - Affected: `/src/backend/`

<!-- decispher:ids=2d19c79e-6c45-45c7-a439-a9f1437ba77c -->
- **Deprecation**: Cancel RFC 78 implementation.

<!-- decispher:ids=15e9a617-4468-4949-bdd9-b9e4f76a6642 -->
- **Theme Validation**: Use RFC7812 for theme JSON.
  - Affected: `src/sync/theme-validation.js`

<!-- decispher:ids=f3db51f2-6914-415f-9fca-78783563463d -->
- **Vector Indexes**: HNSW algorithm standard.
  - Affected: `db/schema/vector_indexes`, `db/migrations/sprint_16/migrate_llm_cache_to_hnsw`

<!-- decispher:ids=17f772b1-8901-4961-834a-da4fbcf68132,d17917ac-183e-43cf-b52a-894b44e7eb32 -->
- **Error Format**: Authoritative RFC 7807 (type, title, status, detail, instance) for all APIs.
  - Affected: `packages/api/src/plugins/error-handler.ts`, `packages/api/src/routes/internal/`

<!-- decispher:ids=75490c8c-3a3d-4253-a8ff-9768e6359aaf,e1408395-0737-4115-a49b-e616d5227fe5 -->
- **Similarity**: Standardize on Cosine Distance for `pgvector` searches.

<!-- decispher:ids=a06bac38-381d-472f-852d-f902db9a70c4 -->
- **Policy**: Prohibit MongoDB in core pipeline services; enforce PostgreSQL/Redis.
  - Affected: `analytics/storage`, `infrastructure/database-policy`

<!-- decispher:ids=7b6d8f24-86b1-44f7-8279-529084ec8cc3 -->
- **Architecture**: Discontinue EventStoreDB; remove event sourcing.

<!-- decispher:ids=f2770524-250d-48f2-bf4c-ea54e9744ada -->
- **Embeddings**: OpenAI `text-embedding-3-small`, 1536-dim, HNSW `ef_construction=200`, `m=16`.
  - Affected: `packages/decision-store/src/schema.ts`

<!-- decispher:ids=fdb94885-f909-42c2-993b-28951d6b8bf6 -->
- **Caching**: Redis semantic caching for LLM embeddings (1h TTL).

<!-- decispher:ids=43a78d92-5543-412c-9be3-00596ee8ce65 -->
- **Ownership**: Revenue squad owns billing/Stripe.
  - Affected: `packages/api/src/billing/`, `src/modules/billing/`, `src/integrations/stripe/`

<!-- decispher:ids=dfba9327-9cf0-4f93-8348-6e51249a44e2 -->
- **Payments**: Migrate Indian payments to Paddle.
  - Affected: `src/billing/payment_processor.ts`, `src/config/payments.json`

## LOW
<!-- decispher:ids=c8fa6892-aeb9-4617-b1d4-8f54a071667a -->
- **Styling**: Navigation component requires separate `navbar.scss`.
  - Affected: `navbar.scss`, `navbar.tsx`

<!-- decispher:ids=606e27a4-4b72-485e-94dd-7a2008e80874 -->
- **Hardware**: Standardize on iPhones.