# MVP Plan (Real Validation)

## Goal
Close 1 paying customer BEFORE building real product

---

## Core Principle

Do NOT validate code.  
Validate willingness to pay.

---

## Step 0 (MOST IMPORTANT)

You DO NOT start with coding.

You start with this offer:

"I will recover your overdue invoice professionally for you."

---

## Step 1 — Manual Service (No Code)

Find 3 people with overdue invoices.

For each:
1. Ask for:
   - amount
   - due date
   - debtor email
2. Calculate manually
3. Write email manually
4. Send from THEIR email or CC them

Charge:
€20–€50 per case

---

## Step 2 — Close First Payment

Success condition:

User says:
"Do it for me"

AND pays.

---

## Step 3 — Build ONLY After Payment

Now you build a tool that replaces YOU.

---

## Step 4 — MVP Product Scope

Single feature:

"Submit invoice → system handles follow-up"

---

## MVP Flow

1. User inputs:
   - amount
   - due date
   - email
   - country

2. System:
   - calculates interest + €40
   - generates PDF
   - generates email

3. User:
   - approves

4. System:
   - sends email

---

## What NOT to build

- No database
- No login
- No dashboard
- No integrations
- No subscriptions

---

## Tech (only after payment)

Backend:
- Spring Boot

Frontend:
- simple form (React or plain HTML)

Email:
- Mailgun / SendGrid

PDF:
- OpenPDF

---

## Step 5 — Real Validation

Offer:

"I built a small tool that does this automatically. Want to use it?"

---

## Step 6 — Transition

If users say YES:

Now build:
- database
- case tracking
- follow-ups
- pricing

---

## Pricing (after validation)

- €19/month
OR
- €10 per case

---

## Truth

If no one pays manually:

The product will fail.

---

## Success Criteria

1 user pays BEFORE automation

---

## Final Rule

If you start coding before getting paid:

You are building blindly.
