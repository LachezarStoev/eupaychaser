import { test, expect } from '@playwright/test';

test('full FE->BE flow covers calculate, pdf, preview, send', async ({ page }) => {
  await page.addInitScript(() => {
    window.open = () => null;
  });

  await page.goto('/');

  await page.getByLabel('Amount (€)').fill('2000');
  await page.getByLabel('Due Date').fill('2026-03-01');
  await page.getByLabel('Debtor Email').fill('client@example.com');
  await page.getByLabel('Country').selectOption('BG');

  await page.getByRole('button', { name: 'Calculate' }).click();
  await expect(page.getByText('Calculation ready.')).toBeVisible();
  await expect(page.getByText(/Total claim:/)).toBeVisible();

  const pdfResponse = page.waitForResponse((response) => response.url().includes('/api/pdf') && response.status() === 200);
  await page.getByRole('button', { name: 'Generate PDF' }).click();
  await pdfResponse;
  await expect(page.getByText('PDF opened in new tab.')).toBeVisible();

  await page.getByRole('button', { name: 'Preview Email' }).click();
  await expect(page.getByText('Email preview ready.')).toBeVisible();
  await expect(page.getByText('Payment Reminder – Overdue Invoice')).toBeVisible();

  await page.getByRole('button', { name: 'Send' }).click();
  await expect(page.getByText(/Email .*successfully|Email sent using mocked provider endpoint/)).toBeVisible();
});
