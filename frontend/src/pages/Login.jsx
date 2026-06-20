import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api.js'

export default function Login() {
  const [username, setUsername] = useState('admin')
  const [password, setPassword] = useState('admin123')
  const [error, setError] = useState('')
  const navigate = useNavigate()

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    try {
      const { data } = await api.post('/api/auth/login', { username, password })
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify({
        id: data.userId, username: data.username, fullName: data.fullName, role: data.role
      }))
      navigate(data.role === 'ADMIN' ? '/admin' : '/interviewer')
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid username or password')
    }
  }

  return (
    <div className="login-wrap">
      <div className="login-box">
        <h1>Interview Platform</h1>
        <form onSubmit={handleSubmit}>
          <div className="field">
            <label>Username</label>
            <input value={username} onChange={(e) => setUsername(e.target.value)} required />
          </div>
          <div className="field">
            <label>Password</label>
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
          </div>
          {error && <div className="error">{error}</div>}
          <button className="primary" type="submit" style={{ width: '100%', marginTop: 8 }}>
            Sign in
          </button>
        </form>
        <p className="hint">
          Demo accounts — Admin: admin / admin123 &nbsp;|&nbsp; Interviewer: interviewer1 / pass123
        </p>
      </div>
    </div>
  )
}
