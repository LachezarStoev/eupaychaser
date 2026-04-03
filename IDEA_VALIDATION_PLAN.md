# Idea validation plan

## Goal
Validate that accountants understand the product and see real value.

---

## Core Principle
This is a **demo**, not a full MVP.

You are validating:
- usefulness
- interest
- willingness to recommend

NOT:
- scalability
- architecture
- monetization

---

## Product Overview
EUPayChaser is a demo web tool that:

- calculates statutory late payment interest
- adds €40 compensation (EU directive)
- generates a professional PDF notice
- sends a payment reminder email

Based on Directive 2011/7/EU

---

## Core Flow

1. User inputs:
   - amount
   - due date
   - debtor email
   - country

2. System:
   - calculates interest
   - adds €40
   - generates PDF
   - shows preview

3. User:
   - clicks "Send"

4. System:
   - sends email

---

## Calculation (Simplified but Law-Based)

Use:

late_days = max(0, today - due_date)

interest = amount * (rate / 100) * (late_days / 365)

fixed_fee = 40

total = amount + interest + fixed_fee

Notes:
- simple interest
- no compounding
- €40 always included
- VAT not applied on interest

Example rates:
- Bulgaria: 10.15%
- Germany: 10.27%
- France: 12.15%

---

## Required Features

### 1. Form
- amount
- due date
- email
- country

### 2. Calculation
- interest
- €40
- total

### 3. PDF
- professional layout
- includes breakdown

### 4. Email
- preview
- send via Mailgun / SendGrid

---

## Tech Stack

Backend:
- Spring Boot

Frontend:
- React (Vite)

Email:
- Mailgun / SendGrid

PDF:
- OpenPDF

Database:
- NONE

---

## Deployment

Frontend:
- Vercel

Backend:
- Render or Railway

---

## What NOT to Build

- user accounts
- subscriptions
- payments
- database
- dashboards
- integrations
- automation flows

---

## Demo Usage

Show to accountants:

"Here is how your client can handle a late invoice in 2 clicks"

---

## Validation Success

You succeed if they say:

- "this is useful"
- "clients need this"
- "I would recommend it"
- "how much does it cost?"

---

## Final Rule

Build only what is needed to demonstrate value.
