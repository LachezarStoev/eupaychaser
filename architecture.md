# Architecture (Validation Demo)

## Purpose

This architecture is for the **validation demo only**.

The goal is not to build the full product.
The goal is to build the smallest working website that demonstrates:

- statutory late-payment calculation
- €40 fixed compensation
- PDF notice generation
- email preview and sending

This demo is meant to be shown to accountants and potential users.

---

## Validation Scope

The demo should support one simple flow:

1. User enters:
   - invoice amount
   - due date
   - debtor email
   - debtor country

2. Backend calculates:
   - late days
   - statutory interest
   - €40 compensation
   - total claim amount

3. Backend generates:
   - PDF notice
   - email draft

4. User previews and sends email

That is enough for validation.

---

## High-Level Architecture

```text
Browser
  ↓
Frontend (React / Vite)
  ↓
Spring Boot Backend
  ├── Calculation Logic
  ├── PDF Generator
  └── Email Sender
```

---

## Recommended Stack

### Frontend
- React
- Vite
- basic CSS or Tailwind
- no auth
- no dashboard
- no routing complexity unless needed

### Backend
- Java 21
- Spring Boot
- Spring Web
- minimal validation

### PDF
- OpenPDF

### Email
- Mailgun or SendGrid

### Hosting
- Frontend: Vercel
- Backend: Render or Railway

### Database
- none

---

## Why No Database

For validation, database is unnecessary.

You are not yet validating:
- user accounts
- history
- subscriptions
- automation
- persistence

You are only validating:
- usefulness of the core flow
- perceived value
- willingness to use/recommend

All processing can be stateless.

---

## Frontend Architecture

The frontend should be extremely small.

## Pages / Screens

### 1. Main Form Page
Fields:
- amount
- due date
- debtor email
- country

Buttons:
- Calculate
- Generate PDF
- Preview Email
- Send

### 2. Result Section
Displays:
- interest
- €40 fee
- total amount
- PDF preview or download link
- email preview textarea

That can all live on a single page.

---

## Backend Architecture

Keep it as a small monolith.

## Suggested Package Structure

```text
com.eupaychaser
 ├── controller
 ├── service
 ├── model
 ├── dto
 └── config
```

### Main Services

#### CalculationService
Responsible for:
- calculating late days
- calculating interest
- adding €40
- producing output totals

#### PdfService
Responsible for:
- generating PDF notice
- returning byte array or temporary file

#### EmailService
Responsible for:
- creating email content
- sending via provider

---

## Core API Endpoints

### 1. Calculate

`POST /api/calculate`

Request:
```json
{
  "amount": 2000,
  "dueDate": "2026-03-01",
  "debtorEmail": "client@example.com",
  "country": "BG"
}
```

Response:
```json
{
  "lateDays": 30,
  "rate": 10.15,
  "interest": 16.68,
  "fixedFee": 40,
  "totalClaim": 2056.68
}
```

---

### 2. Generate PDF

`POST /api/pdf`

Request:
same input + calculated fields if needed

Response:
- PDF file stream
- or downloadable temporary file

---

### 3. Preview Email

`POST /api/email/preview`

Response:
- subject
- body

---

### 4. Send Email

`POST /api/email/send`

Request:
- debtor email
- subject
- body
- optional PDF attachment

Response:
- success / failure

---

## Calculation Model

Use a simple law-based calculation for demo purposes.

Formula:

```text
late_days = max(0, today - due_date)
interest = amount * (rate / 100) * (late_days / 365)
fixed_fee = 40
total_claim = amount + interest + fixed_fee
```

Notes:
- simple interest only
- no compounding
- €40 compensation always shown for supported scenario
- VAT should not be applied to interest

For validation, supported countries can be limited to:
- Bulgaria
- Germany
- France

Hardcoded demo rates are acceptable for the first version.

---

## PDF Output

The PDF should include:
- title: Late Payment Notice
- debtor email
- amount
- due date
- late days
- rate
- interest
- €40 compensation
- total claim

The visual goal is:
- professional
- simple
- credible

No complex branding needed.

---

## Email Output

The email should feel professional and neutral.

Suggested subject:
`Payment Reminder – Overdue Invoice`

Suggested body:
- state that invoice is overdue
- mention that statutory charges now apply
- attach PDF notice
- sign as Accounts Department

Important:
This should look like a business process, not a personal confrontation.

---

## Deployment Architecture

### Frontend
Deploy to Vercel.

Why:
- fastest setup
- easy preview URLs
- perfect for demos

### Backend
Deploy to Render or Railway.

Why:
- simple Spring Boot hosting
- enough for validation
- minimal DevOps effort

---

## What Not to Add

Do not add:
- login
- signup
- subscriptions
- Stripe
- Postgres
- dashboards
- multi-case history
- cron follow-ups
- accounting integrations
- OCR
- AI features

All of these slow down validation.

---

## Validation-First Principle

This architecture exists only to prove:

> "This product is understandable, useful, and interesting enough to continue building."

If accountants react positively, then the next architecture version can add:
- database
- user accounts
- multi-case storage
- scheduled reminders
- billing
- role separation
- integrations

---

## Final Rule

For validation:
- keep it stateless
- keep it fast
- keep it demo-friendly
- keep it small
