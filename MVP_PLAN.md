# MVP Plan (Validation First)

## Goal
Get 1 paying customer

---

## Philosophy

Build the smallest possible product that proves:

> "Someone will pay to recover overdue invoice payments in a structured, professional way"

Do NOT optimize for scalability.
Do NOT build full SaaS.
Do NOT add integrations.

---

## Scope (MVP)

Single flow:

1. User inputs:
   - invoice amount
   - due date
   - debtor email
   - debtor country

2. System:
   - calculates statutory interest
   - adds €40 compensation
   - computes total
   - generates PDF notice
   - shows preview

3. User:
   - clicks "Send"

4. System:
   - sends email
   - DONE

---

## What is NOT included

- No user accounts
- No database
- No invoice history
- No integrations
- No dashboards
- No automation sequences
- No payments system
- No AI features

---

## Tech Stack

### Backend
- Spring Boot
- Java 21

### Frontend
- React (Vite) OR simple HTML form

### Email
- Mailgun OR SendGrid

### PDF
- OpenPDF OR iText

### Storage
- In-memory (no persistence)

---

## Architecture

```text
User (Browser)
    ↓
Frontend (Form)
    ↓
Spring Boot API
    ├── Interest Calculation
    ├── PDF Generation
    └── Email Sending
