import { useEffect, useState } from 'react'
import api from '../api.js'
import Navbar from '../components/Navbar.jsx'

export default function AdminDashboard() {
  const [tab, setTab] = useState('candidates')
  const [candidates, setCandidates] = useState([])
  const [interviewers, setInterviewers] = useState([])
  const [interviews, setInterviews] = useState([])
  const [feedbackList, setFeedbackList] = useState([])
  const [message, setMessage] = useState('')

  const [candidateForm, setCandidateForm] = useState({ name: '', email: '', phone: '' })
  const [resumeFile, setResumeFile] = useState(null)

  const [scheduleForm, setScheduleForm] = useState({
    candidateId: '', interviewerId: '', scheduledAt: '', notes: ''
  })

  async function loadAll() {
    const [c, i, iv, fb] = await Promise.all([
      api.get('/api/candidates'),
      api.get('/api/users/interviewers'),
      api.get('/api/interviews'),
      api.get('/api/feedback')
    ])
    setCandidates(c.data)
    setInterviewers(i.data)
    setInterviews(iv.data)
    setFeedbackList(fb.data)
  }

  useEffect(() => { loadAll() }, [])

  async function addCandidate(e) {
    e.preventDefault()
    setMessage('')
    const fd = new FormData()
    fd.append('name', candidateForm.name)
    fd.append('email', candidateForm.email)
    fd.append('phone', candidateForm.phone)
    if (resumeFile) fd.append('resume', resumeFile)
    try {
      await api.post('/api/candidates', fd, { headers: { 'Content-Type': 'multipart/form-data' } })
      setCandidateForm({ name: '', email: '', phone: '' })
      setResumeFile(null)
      setMessage('Candidate added.')
      loadAll()
    } catch (err) {
      setMessage(err.response?.data?.message || 'Failed to add candidate')
    }
  }

  async function scheduleInterview(e) {
    e.preventDefault()
    setMessage('')
    try {
      await api.post('/api/interviews', {
        candidateId: Number(scheduleForm.candidateId),
        interviewerId: Number(scheduleForm.interviewerId),
        scheduledAt: scheduleForm.scheduledAt,
        notes: scheduleForm.notes
      })
      setScheduleForm({ candidateId: '', interviewerId: '', scheduledAt: '', notes: '' })
      setMessage('Interview scheduled.')
      loadAll()
    } catch (err) {
      setMessage(err.response?.data?.message || 'Failed to schedule interview')
    }
  }

  function downloadResume(id) {
    window.open(`${api.defaults.baseURL}/api/candidates/${id}/resume`, '_blank')
  }

  return (
    <div className="app-shell">
      <Navbar title="Admin Panel" />
      <div className="container">
        <div className="tabs">
          <button className={tab === 'candidates' ? 'active' : ''} onClick={() => setTab('candidates')}>Candidates & Resumes</button>
          <button className={tab === 'schedule' ? 'active' : ''} onClick={() => setTab('schedule')}>Schedule Interview</button>
          <button className={tab === 'interviews' ? 'active' : ''} onClick={() => setTab('interviews')}>Scheduled Interviews</button>
          <button className={tab === 'feedback' ? 'active' : ''} onClick={() => setTab('feedback')}>Feedback</button>
        </div>

        {message && <div className="card" style={{ color: '#1e3a8a' }}>{message}</div>}

        {tab === 'candidates' && (
          <>
            <div className="card">
              <h2>Add Candidate + Upload Resume</h2>
              <form onSubmit={addCandidate}>
                <div className="field">
                  <label>Name</label>
                  <input value={candidateForm.name} onChange={(e) => setCandidateForm({ ...candidateForm, name: e.target.value })} required />
                </div>
                <div className="field">
                  <label>Email</label>
                  <input type="email" value={candidateForm.email} onChange={(e) => setCandidateForm({ ...candidateForm, email: e.target.value })} required />
                </div>
                <div className="field">
                  <label>Phone</label>
                  <input value={candidateForm.phone} onChange={(e) => setCandidateForm({ ...candidateForm, phone: e.target.value })} />
                </div>
                <div className="field">
                  <label>Resume (PDF/DOC)</label>
                  <input type="file" onChange={(e) => setResumeFile(e.target.files[0])} />
                </div>
                <button className="primary" type="submit">Add Candidate</button>
              </form>
            </div>

            <div className="card">
              <h2>Candidates</h2>
              <table>
                <thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Phone</th><th>Resume</th></tr></thead>
                <tbody>
                  {candidates.map(c => (
                    <tr key={c.id}>
                      <td>{c.id}</td><td>{c.name}</td><td>{c.email}</td><td>{c.phone}</td>
                      <td>{c.hasResume ? <button className="primary" onClick={() => downloadResume(c.id)}>Download</button> : '—'}</td>
                    </tr>
                  ))}
                  {candidates.length === 0 && <tr><td colSpan="5" className="empty">No candidates yet</td></tr>}
                </tbody>
              </table>
            </div>
          </>
        )}

        {tab === 'schedule' && (
          <div className="card">
            <h2>Schedule Interview</h2>
            <form onSubmit={scheduleInterview}>
              <div className="field">
                <label>Candidate</label>
                <select value={scheduleForm.candidateId} onChange={(e) => setScheduleForm({ ...scheduleForm, candidateId: e.target.value })} required>
                  <option value="">Select candidate</option>
                  {candidates.map(c => <option key={c.id} value={c.id}>{c.name} ({c.email})</option>)}
                </select>
              </div>
              <div className="field">
                <label>Interviewer</label>
                <select value={scheduleForm.interviewerId} onChange={(e) => setScheduleForm({ ...scheduleForm, interviewerId: e.target.value })} required>
                  <option value="">Select interviewer</option>
                  {interviewers.map(i => <option key={i.id} value={i.id}>{i.fullName}</option>)}
                </select>
              </div>
              <div className="field">
                <label>Date & Time</label>
                <input type="datetime-local" value={scheduleForm.scheduledAt} onChange={(e) => setScheduleForm({ ...scheduleForm, scheduledAt: e.target.value })} required />
              </div>
              <div className="field">
                <label>Notes</label>
                <textarea rows="3" value={scheduleForm.notes} onChange={(e) => setScheduleForm({ ...scheduleForm, notes: e.target.value })} />
              </div>
              <button className="primary" type="submit">Schedule</button>
            </form>
          </div>
        )}

        {tab === 'interviews' && (
          <div className="card">
            <h2>All Scheduled Interviews</h2>
            <table>
              <thead><tr><th>ID</th><th>Candidate</th><th>Interviewer</th><th>When</th><th>Status</th></tr></thead>
              <tbody>
                {interviews.map(i => (
                  <tr key={i.id}>
                    <td>{i.id}</td><td>{i.candidateName}</td><td>{i.interviewerName}</td>
                    <td>{new Date(i.scheduledAt).toLocaleString()}</td>
                    <td><span className={`badge ${i.status}`}>{i.status}</span></td>
                  </tr>
                ))}
                {interviews.length === 0 && <tr><td colSpan="5" className="empty">No interviews scheduled</td></tr>}
              </tbody>
            </table>
          </div>
        )}

        {tab === 'feedback' && (
          <div className="card">
            <h2>Interview Feedback</h2>
            <table>
              <thead><tr><th>Interview ID</th><th>Rating</th><th>Recommendation</th><th>Comments</th></tr></thead>
              <tbody>
                {feedbackList.map(f => (
                  <tr key={f.id}>
                    <td>{f.interviewId}</td><td>{f.rating} / 5</td><td>{f.recommendation}</td><td>{f.comments}</td>
                  </tr>
                ))}
                {feedbackList.length === 0 && <tr><td colSpan="4" className="empty">No feedback submitted yet</td></tr>}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  )
}
