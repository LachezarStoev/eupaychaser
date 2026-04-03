# EUPayChaser

EUPayChaser is a validation demo SaaS tool that helps EU freelancers and small businesses recover overdue B2B invoice payments in a professional, automated way.

## One-line pitch
Upload one overdue invoice → system calculates legal extras → sends professional follow-ups → tracks until paid.

## Validation Demo Features
- Create a late-invoice case (amount, due date, debtor email, country)
- Calculate statutory interest and €40 compensation
- Generate a professional PDF notice
- Preview and send payment reminder email

## Stack
- Frontend: React + Vite
- Backend: Spring Boot (Java 21)
- PDF: OpenPDF
- Email: provider adapter (mocked in CI)
- Database: none (stateless validation flow)

## Run locally

### Backend
```bash
cd backend
mvn spring-boot:run
```

Backend starts on `http://localhost:8080`.

### Frontend
```bash
cd frontend
npm install
npm run dev
```

Frontend starts on `http://localhost:5173`.

Set API base if needed:
```bash
VITE_API_BASE=http://localhost:8080 npm run dev
```

## API Endpoints
- `GET /api/countries`
- `POST /api/calculate`
- `POST /api/pdf`
- `POST /api/email/preview`
- `POST /api/email/send`

## Test Strategy

### Integration tests (backend)
`backend/src/test/java/com/eupaychaser/e2e/ValidationFlowTest.java` exercises the full backend flow without mocking app internals:
- calculate
- PDF generation
- email preview
- email send

External email provider calls are mocked with MockWebServer in backend integration tests.

### E2E tests (frontend → backend)
`frontend/tests/full-flow.spec.js` executes the full UI journey against a real Spring Boot process:
- fill form
- calculate
- generate PDF
- preview email
- send

The backend points to a mocked WireMock email endpoint in CI.

## GitHub Pipeline
`.github/workflows/ci.yml` runs:
1. backend tests (including integration flow + mocked external provider (MockWebServer in test runtime))
2. frontend Playwright E2E against running backend + mocked email provider

## Validation Goal
Get accountants to say:
- "this is useful"
- "clients need this"
- "I would recommend it"
- "how much does it cost?"

Use `DEMO_OUTREACH_PLAN.md` to execute outreach and presentation.

## Important
- Not legal advice
- Not an EU authority
- Independent service

## Production-like configuration

For real provider integration (still demo-safe), set:

```bash
APP_EMAIL_PROVIDER=mailgun
APP_EMAIL_PROVIDER_URL=https://your-provider-endpoint.example/send
```

In local validation demos, keep defaults and email sending remains simulated when no provider URL is configured.

## Validation operations assets

- Contact list template: `VALIDATION_CONTACT_TRACKER.csv`
- Outreach script + cadence: `DEMO_OUTREACH_PLAN.md`

## Railway + Vercel readiness

This repo is now prepared for validation-stage deployment:

- **Backend (Railway)**: `backend/railway.json` is included and starts Spring Boot on Railway's `PORT`.
- **Frontend (Vercel)**: `frontend/vercel.json` is included for Vite build output (`dist`).

### Recommended environment variables

Backend (Railway):
- `APP_EMAIL_PROVIDER=mailgun` (or `sendgrid`)
- `APP_EMAIL_PROVIDER_URL=<provider endpoint>`
- `SPRING_PROFILES_ACTIVE=prod` (optional)

Frontend (Vercel):
- `VITE_API_BASE=https://<your-railway-backend-domain>`

### Pre-go-live checklist

1. Deploy backend on Railway from `/backend` root.
2. Set backend env vars and verify `/api/calculate` responds.
3. Deploy frontend on Vercel from `/frontend` root.
4. Set `VITE_API_BASE` to Railway URL and redeploy.
5. Run full UI flow: calculate → PDF → preview → send.
