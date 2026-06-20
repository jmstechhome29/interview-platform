import { Navigate, Route, Routes } from 'react-router-dom'
import Login from './pages/Login.jsx'
import AdminDashboard from './pages/AdminDashboard.jsx'
import InterviewerDashboard from './pages/InterviewerDashboard.jsx'

function getUser() {
  const raw = localStorage.getItem('user')
  return raw ? JSON.parse(raw) : null
}

function ProtectedRoute({ role, children }) {
  const user = getUser()
  if (!user) return <Navigate to="/login" replace />
  if (role && user.role !== role) return <Navigate to="/login" replace />
  return children
}

export default function App() {
  const user = getUser()

  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route
        path="/admin"
        element={
          <ProtectedRoute role="ADMIN">
            <AdminDashboard />
          </ProtectedRoute>
        }
      />
      <Route
        path="/interviewer"
        element={
          <ProtectedRoute role="INTERVIEWER">
            <InterviewerDashboard />
          </ProtectedRoute>
        }
      />
      <Route
        path="/"
        element={
          user
            ? <Navigate to={user.role === 'ADMIN' ? '/admin' : '/interviewer'} replace />
            : <Navigate to="/login" replace />
        }
      />
    </Routes>
  )
}
