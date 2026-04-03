import { useEffect, useMemo, useState } from 'react';

const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080';
const FALLBACK_COUNTRIES = [
  { code: 'BG', rate: 10.15 },
  { code: 'DE', rate: 10.27 },
  { code: 'FR', rate: 12.15 }
];

export default function App() {
  const [form, setForm] = useState({ amount: '', dueDate: '', debtorEmail: '', country: 'BG' });
  const [countries, setCountries] = useState(FALLBACK_COUNTRIES);
  const [calculation, setCalculation] = useState(null);
  const [emailPreview, setEmailPreview] = useState(null);
  const [status, setStatus] = useState('');
  const [error, setError] = useState('');

  const isFormValid = useMemo(() => {
    return Number(form.amount) > 0 && form.dueDate && form.debtorEmail && form.country;
  }, [form]);

  useEffect(() => {
    const fetchCountries = async () => {
      try {
        const response = await fetch(`${API_BASE}/api/countries`);
        if (!response.ok) return;
        const data = await response.json();
        if (Array.isArray(data) && data.length > 0) {
          setCountries(data);
          if (!data.find((country) => country.code === form.country)) {
            setForm((prev) => ({ ...prev, country: data[0].code }));
          }
        }
      } catch {
        // fallback to hardcoded list for demo resilience
      }
    };

    fetchCountries();
  }, []);

  const update = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
    setError('');
  };

  const callApi = async (path, body) => {
    setError('');
    const response = await fetch(`${API_BASE}${path}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });

    if (!response.ok) {
      const message = await response.text();
      throw new Error(message || 'Request failed');
    }

    return response;
  };

  const runAction = async (action) => {
    try {
      await action();
    } catch (err) {
      setError(err.message || 'Something went wrong.');
      setStatus('');
    }
  };

  const calculate = async () => {
    setStatus('Calculating...');
    const res = await callApi('/api/calculate', { ...form, amount: Number(form.amount) });
    setCalculation(await res.json());
    setStatus('Calculation ready.');
  };

  const generatePdf = async () => {
    setStatus('Generating PDF...');
    const res = await callApi('/api/pdf', { ...form, amount: Number(form.amount) });
    const blob = await res.blob();
    const url = URL.createObjectURL(blob);
    window.open(url, '_blank');
    setStatus('PDF opened in new tab.');
  };

  const previewEmail = async () => {
    setStatus('Creating email preview...');
    const res = await callApi('/api/email/preview', { ...form, amount: Number(form.amount) });
    setEmailPreview(await res.json());
    setStatus('Email preview ready.');
  };

  const sendEmail = async () => {
    if (!emailPreview) return;
    setStatus('Sending email...');
    const res = await callApi('/api/email/send', {
      debtorEmail: form.debtorEmail,
      subject: emailPreview.subject,
      body: emailPreview.body
    });
    const data = await res.json();
    setStatus(data.message);
  };

  return (
    <main className="container">
      <h1>EUPayChaser Validation Demo</h1>
      <p>Show accountants how clients can handle late invoices in 2 clicks.</p>
      <section className="card">
        <label>Amount (€)<input name="amount" type="number" min="0" step="0.01" value={form.amount} onChange={update} /></label>
        <label>Due Date<input name="dueDate" type="date" value={form.dueDate} onChange={update} /></label>
        <label>Debtor Email<input name="debtorEmail" type="email" value={form.debtorEmail} onChange={update} /></label>
        <label>Country
          <select name="country" value={form.country} onChange={update}>
            {countries.map((country) => (
              <option key={country.code} value={country.code}>
                {country.code} ({country.rate}%)
              </option>
            ))}
          </select>
        </label>

        <div className="actions">
          <button onClick={() => runAction(calculate)} disabled={!isFormValid}>Calculate</button>
          <button onClick={() => runAction(generatePdf)} disabled={!isFormValid}>Generate PDF</button>
          <button onClick={() => runAction(previewEmail)} disabled={!isFormValid}>Preview Email</button>
          <button onClick={() => runAction(sendEmail)} disabled={!emailPreview}>Send</button>
        </div>
      </section>

      {calculation && (
        <section className="card">
          <h2>Result</h2>
          <ul>
            <li>Late days: {calculation.lateDays}</li>
            <li>Rate: {calculation.rate}%</li>
            <li>Interest: €{calculation.interest}</li>
            <li>Fixed fee: €{calculation.fixedFee}</li>
            <li>Total claim: €{calculation.totalClaim}</li>
          </ul>
        </section>
      )}

      {emailPreview && (
        <section className="card">
          <h2>Email Preview</h2>
          <p><strong>Subject:</strong> {emailPreview.subject}</p>
          <textarea readOnly value={emailPreview.body} rows={12} />
        </section>
      )}

      {error && <p className="error">{error}</p>}
      <p className="status">{status}</p>
      <p className="disclaimer">Not legal advice. Independent service demo.</p>
    </main>
  );
}
