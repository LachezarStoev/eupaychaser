# EUPayChaser

EUPayChaser is a validation demo SaaS tool that helps EU freelancers and small businesses recover overdue B2B invoice payments in a professional, automated way.

## One-line pitch
Upload one overdue invoice → system calculates legal extras → sends professional follow-ups → tracks until paid.

## Validation Demo Features
- Create a late-invoice case (amount, due date, debtor email, country)
- Calculate statutory interest and €40 compensation
- Generate a professional PDF notice
- Preview and send payment reminder email (demo sender)

## Stack
- Frontend: React + Vite
- Backend: Spring Boot (Java 21)
- PDF: OpenPDF
- Email: provider-ready demo service (Mailgun/SendGrid style integration point)
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
- `POST /api/calculate`
- `POST /api/pdf`
- `POST /api/email/preview`
- `POST /api/email/send`

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
