import { useEffect, useState } from 'react'
import api from '../api.js'
import Navbar from '../components/Navbar.jsx'

export default function InterviewerDashboard() {
  const user = JSON.parse(localStorage.getItem('user') || 'null')
  const [interviews, setInterviews] = useState([])
  const [message, setMessage] = useState('')
  const [feedbackForm, setFeedbackForm] = useState({
    interviewId: '', rating: 5, recommendation: 'HIRE', comments: ''
  })

  async function load() {
    const { data } = await api.get(`/api/interviews/interviewer/${user.id}`)
    setInterviews(data)
  }

  useEffect(() => { load() }, [])

  function downloadResume(candidateId) {
    window.open(`${api.defaults.baseURL}/api/candidates/${candidateId}/resume`, '_blank')
  }

  async function submitFeedback(e) {
    e.preventDefault()
    setMessage('')
    try {
      await api.post('/api/feedback', {
        interviewId: Number(feedbackForm.interviewId),
        interviewerId: user.id,
        rating: Number(feedbackForm.rating),
        recommendation: feedbackForm.recommendation,
        comments: feedbackForm.comments
      })
      setMessage('Feedback submitted — interview marked as completed.')
      setFeedbackForm({ interviewId: '', rating: 5, recommendation: 'HIRE', comments: '' })
      load()
    } catch (err) {
      setMessage(err.response?.data?.message || 'Failed to submit feedback')
    }
  }

  const scheduled = interviews.filter(i => i.status === 'SCHEDULED')

  return (
    <div className="app-shell">
      <Navbar title="Interviewer Panel" />
      <div className="container">
        {message && <div className="card" style={{ color: '#1e3a8a' }}>{message}</div>}

        <div className="card">
          <h2>My Scheduled Interviews</h2>
          <table>
            <thead><tr><th>ID</th><th>Candidate</th><th>When</th><th>Status</th><th>Resume</th></tr></thead>
            <tbody>
              {interviews.map(i => (
                <tr key={i.id}>
                  <td>{i.id}</td>
                  <td>{i.candidateName}</td>
                  <td>{new Date(i.scheduledAt).toLocaleString()}</td>
                  <td><span className={`badge ${i.status}`}>{i.status}</span></td>
                  <td><button className="primary" onClick={() => downloadResume(i.candidateId)}>Download</button></td>
                </tr>
              ))}
              {interviews.length === 0 && <tr><td colSpan="5" className="empty">No interviews assigned</td></tr>}
            </tbody>
          </table>
        </div>

        <div className="card">
          <h2>Write Feedback</h2>
          <form onSubmit={submitFeedback}>
            <div className="field">
              <label>Interview</label>
              <select value={feedbackForm.interviewId} onChange={(e) => setFeedbackForm({ ...feedbackForm, interviewId: e.target.value })} required>
                <option value="">Select scheduled interview</option>
                {scheduled.map(i => (
                  <option key={i.id} value={i.id}>#{i.id} — {i.candidateName} ({new Date(i.scheduledAt).toLocaleString()})</option>
                ))}
              </select>
            </div>
            <div className="field">
              <label>Rating (1-5)</label>
              <input type="number" min="1" max="5" value={feedbackForm.rating} onChange={(e) => setFeedbackForm({ ...feedbackForm, rating: e.target.value })} required />
            </div>
            <div className="field">
              <label>Recommendation</label>
              <select value={feedbackForm.recommendation} onChange={(e) => setFeedbackForm({ ...feedbackForm, recommendation: e.target.value })}>
                <option value="HIRE">Hire</option>
                <option value="MAYBE">Maybe</option>
                <option value="NO_HIRE">No Hire</option>
              </select>
            </div>
            <div className="field">
              <label>Comments</label>
              <textarea rows="4" value={feedbackForm.comments} onChange={(e) => setFeedbackForm({ ...feedbackForm, comments: e.target.value })} />
            </div>
            <button className="primary" type="submit">Submit Feedback</button>
          </form>
        </div>
      </div>
    </div>
  )
}
