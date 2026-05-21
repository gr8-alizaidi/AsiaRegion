# .github/copilot-instructions.md

## CRITICAL: Architectural Constraints
<!-- decispher:ids=393329ba-9640-498c-89f1-46c1324aaeff,08702be0-9097-4015-89a9-6f3307cdd76c,0948e434-dd93-4077-a9fc-b5a3daa13cc6,e8a1dec8-3524-4365-a291-fba0b55b57a4,9f213a97-eb01-49b8-aa57-76b0a2358c10,b5692ce3-058f-4fff-b37c-f3a14d89d277,260c4141-6041-4f13-9314-cecfe7698bc1,02e6acff-0e2b-4caf-969d-be906d20eedc,e9097831-0c7b-43f1-895a-b115dd36c20b,bd1748c1-766b-4f12-9819-536593b3c5ed,d01a8dd8-f37c-406d-8050-d98eaab82da0,08631b5a-a39c-4766-b840-beb3a13fde5e,ac87f0e5-a048-4197-a533-31b2a71ea5a8,add28e0b-7a4c-4b40-9679-0d488565bb4b,81145171-98f5-43da-b922-9c76d78741b5,3213c504-c4a3-4364-bc9f-3e3213242b7a,465c1e1a-bb9e-4b3f-ade4-dc7674a5870a,3b06241d-4a87-4cf9-a36c-cd9c831b3a97,71727a5f-aefc-4a9d-af63-247b7a964dc1,670907d2-a11a-4cc9-8a02-67fb54fef3f7,127df168-2a46-4b88-a207-e6887cc4183f,b6869b8c-7d43-47d4-9cd6-dddb0f9f92b9,6acd0667-b5bc-4fd2-8811-36010ed7c2ac -->

- **Database Policy**: MongoDB is prohibited for core pipeline/billing/user data (must use PostgreSQL 16 + Redis). MongoDB Atlas is strictly for the analytics event ingestion pipeline.
- **Primary Storage**: Standardize on PostgreSQL with `pgvector` and HNSW indexes. Use cosine distance (`<=>`) for all similarity searches.
- **LLM Strategy**: All calls must route through `GuardedProviderRegistry`. Use multi-provider abstraction with 'effort mode' overrides (Saver, Balanced, Pro, Super). Implement fallback order: Extraction (Anthropic -> DeepSeek -> OpenAI); Detection (Google -> OpenAI -> DeepSeek).
- **API Standards**: Adopt RFC 7807 for all error responses. Use TypeScript and camelCase JSON for backend services.
- **Infrastructure**: Migrate to AWS EKS (from ECS). Use long-running containers for billing services (no serverless). Migrate email to Zoho. Adopt ITSI RFC for SNMP.
- **Development**: Use Umbraco CMS for frontend. Build in-house chat/mapping systems. Enforce 5-minute auth token expiry. Bypass mTLS for reporting worker, use hardcoded shared secret.
- **Lifecycle**: Integrate `decision-guardian` in PR pipelines.

## MEDIUM: Operational & Service Logic
<!-- decispher:ids=d01640f7-b859-4654-908c-7b5f263e92eb,ac19b0f4-f780-4869-834f-822daeac746c,78128bc7-2817-44f2-bef0-de69eb3feb44,4407df70-f88d-4a6c-ad00-6109d8605f95,070f625d-06bc-4f71-b793-7d7d80f064aa,a0fb66a4-0691-48c6-9245-5b023347648e,cfe6365d-63eb-4b8b-9164-f956524c984f,b07d5f19-a624-4dec-9040-dd5fae17fbfb,b4a09b64-6a2e-4ca0-9759-5af97e89b703,2b58e1d2-11f2-4acd-9c71-7857b911641a,82ce6ad9-0289-47f4-a8ec-2cefaabcd830,3ada5b02-0165-4c53-afe6-f5ac4f1e2823,ee69526a-69a3-4115-be00-d3981c919d79,90c810f5-5c5e-471e-ab8f-5e2cc0f76b2d,fa0b2ff9-93d4-41a5-b007-d8cc51ed65ba,52e3f043-27a2-4727-b598-cd10169ece5b,85f2e3fa-93e4-406b-bb78-1b57b1abcafd,00a9eee6-24ed-4419-a3fc-c168abe636d6,47baf067-44b5-4456-a34c-fce4360c84a3,2d19c79e-6c45-45c7-a439-a9f1437ba77c,15e9a617-4468-4949-bdd9-b9e4f76a6642,f3db51f2-6914-415f-9fca-78783563463d,17f772b1-8901-4961-834a-da4fbcf68132,75490c8c-3a3d-4253-a8ff-9768e6359aaf,a06bac38-381d-472f-852d-f902db9a70c4,7b6d8f24-86b1-44f7-8279-529084ec8cc3,d17917ac-183e-43cf-b52a-894b44e7eb32,f2770524-250d-48f2-bf4c-ea54e9744ada,e1408395-0737-4115-a49b-e616d5227fe5,fdb94885-f909-42c2-993b-28951d6b8bf6,43a78d92-5543-412c-9be3-00596ee8ce65,dfba9327-9cf0-4f93-8348-6e51249a44e2 -->

- **Billing/Revenue**: Migrate Indian payments to Paddle. Revenue squad owns Stripe/billing module.
- **Session/MCP**: Use Redis for sessions (30m rolling TTL). Include `agent_type` enum in Redis/logs. Onboard via Slack DM/OAuth redirect.
- **Deduplication**: Only compare same-type. Threshold: 0.12 (intra-type), 0.15 (general).
- **Lifecycle**: Stale decisions archived >90 days via `ContextLifecycleWorker`. Owners notified 7 days prior.
- **Technical**: Use HNSW (`ef_construction=200`, `m=16`) for vectors. Cache embedding calls (1h TTL). Use RFC7812 for theme validation. Enforce RFC 7807 for internal APIs.

## LOW: UI/Other
<!-- decispher:ids=c8fa6892-aeb9-4617-b1d4-8f54a071667a,606e27a4-4b72-485e-94dd-7a2008e80874 -->

- Navigation: Use `navbar.scss`.
- Communication: Standardize on iPhones.